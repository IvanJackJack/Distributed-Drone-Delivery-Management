package DronazonPkg;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import ServerPkg.DroneServiceResponse;
import ServerPkg.GlobalStats;
import SensorPkg.*;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.google.gson.Gson;
import io.grpc.*;
import org.eclipse.paho.client.mqttv3.*;
import project.drone.grpc.DroneServiceGrpc;
import project.drone.grpc.DroneServiceGrpc.DroneServiceStub;
import project.drone.grpc.DroneServiceOuterClass.*;
import io.grpc.stub.StreamObserver;

public class DroneMain {
    private static final String addPath="http://localhost:8080/DroneService/add";
    private static final String removePath="http://localhost:8080/DroneService/remove";
    private static final String broker = "tcp://localhost:1883";
    private static final String topic = "dronazon/smartcity/orders/.";
    private static final String receivePath="http://localhost:8080/DroneService/receive";
    private static final int qos = 2;

    private static final Gson gson = new Gson();

    private static final Object consumerLock = new Object();

    public static void main(String[] args) throws InterruptedException {
        //info generali drone
        Drone drone = new Drone();
        TotalDroneDeliveryStats totalStats=new TotalDroneDeliveryStats();
        DeliveryStats deliveryStats;

        //queste le usa solo il master
        DeliveryStatsQueue deliveryStatsQueue = new DeliveryStatsQueue();


        //IMPORTANTE - rete logica che è gestita come un anello, difatti ogni drone comunica sempre con il master o con il suo successivo, e nessun altro
        //IMPORTANTE - tutti i droni mantengono in memoria i droni con cui comunicano per poter formare l'anello
        //IMPORTANTE - solo il drone master avrà le informazioni aggiornate di tutti i droni, relative a: posizione, energia e stato di consegna
        //IMPORTANTE - tutte le altre info non saranno aggiornate in quanto non necessarie
        LogicNetwork network=new LogicNetwork();

        //gestione ordini
        OrderQueue orderQueue=new OrderQueue();

        //sensore e misurazioni
        BufferQueue bufferQueue = new BufferQueue();
        PM10Simulator simulator = new PM10Simulator(bufferQueue);
        AverageBufferQueue averageBufferQueue = new AverageBufferQueue();

        //info iniziali ricevute dal rest server
        DroneServiceResponse droneServiceResponse=new DroneServiceResponse();

        //server grpc
        Server server = ServerBuilder.forPort(drone.getDronePort()).addService(new DroneServiceImpl(drone, network, deliveryStatsQueue, consumerLock)).build();

        //mqtt
        String clientId = MqttClient.generateClientId();
        MqttClient mqttClient;
        try {
            mqttClient = new MqttClient(broker, clientId);
        }catch(Exception e){
            mqttClient=null;
        }
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);

        //client rest
        Client restClient = Client.create();

        //chiude se inserisco un input
        Thread endingTh = new Thread(new DroneEndingThread(drone, restClient, mqttClient, server, orderQueue, network, deliveryStatsQueue));

        //prendo gli ordini e li assegno ad un drone, lo fa iniziare solo il master
        Thread consumerTh = new Thread(new DroneOrderConsumerThread(orderQueue, network, consumerLock));

        //stampa a video ogni 10 sec le stats del drone
        Thread statsTh = new Thread(new DroneStatsThread(drone, network, totalStats, deliveryStatsQueue, restClient));

        //esegue la media quando ci sono 8 misurazioni nel buffer del sensore e mette la media appena calcolata nell'altro buffer
        Thread measConsumerTh=new Thread(new MeasurementConsumerThread(bufferQueue, averageBufferQueue));

        //pingo il master se non ricevo ordini da troppo tempo, per capire se è vivo
        Thread pingTh=new Thread(new MasterPingThread(drone));

        //gestisce le interfacce per la comunicazione grpc con i droni
        Thread rpcServerTh=new Thread(new DroneRpcServerThread(server));

        //---------------------------- OPERAZIONI PRELIMINARI ------------------------------------------

        String droneJsonString = gson.toJson(drone);

        try{
            WebResource webResource = restClient.resource(addPath);
            ClientResponse response = webResource
                    .accept("application/json")
                    .type("application/json")
                    .post(ClientResponse.class, droneJsonString);

            if (response.getStatus() != 200) {
                throw new RuntimeException("MAIN - Failed : HTTP error code : " + response.getStatus() + " - " + response.getStatusInfo());
            }

            String resFromServer = response.getEntity(String.class);
            droneServiceResponse = gson.fromJson(resFromServer, DroneServiceResponse.class);
        }catch (Exception e){
            //Se la risposta non è OK, allora chiudo
            System.out.println(e.toString());
            System.out.println("MAIN - Nella rete c'è già un drone col mio ID");
            System.exit(0);
        }

        drone.setPosition(droneServiceResponse.getPosition());
        network.setDroneList(droneServiceResponse.getDroneSetCopy());
        network.updatePosition(drone);

        System.out.println("MAIN - Io sono: " + drone.toString());

        rpcServerTh.start();
        endingTh.start();
        measConsumerTh.start();

        //ci vuole un pò di tempo per far partire il server grpc
        try { Thread.sleep(3000); } catch (Exception e) { }

        statsTh.start();
        simulator.start();

        //se sei l'unico, diventa master, altrimenti comunico agli altri che sono entrato
        if(network.getDroneListSize() == 1){
            System.out.println("MAIN - Non ci sono altri droni nella rete");
            drone.setRankingUp(true);
        } else {
            System.out.println("MAIN - Notifico la mia entrata nella rete");
            NotifyNetwork(drone, network);
        }

        network.printDroneList("MAIN");

        //se ho contattato tutti gli altri e non c'è un master inizio l'elezione
        if(!drone.isMaster() && drone.getDroneMasterPort() == -1 && !drone.isRankingUp()){
            System.out.println("MAIN - Nella rete non c'è un master, iniziare elezione");
            StartElection(drone);
        }

        pingTh.start();

        //---------------------------- MAIN CYCLE ------------------------------------------

        System.out.println("\nMAIN - Inizio main cycle");
        while (true){
            //se devo diventare il master
            if(drone.isRankingUp()){
                becomeMaster(drone, mqttClient, connOpts, orderQueue, consumerTh);
            }

            //eseguo l'ordine se ne ho uno assegnato
            if(drone.getOrder() != null){

                drone.setDelivering(true);
                network.updateDeliveryStatus(drone);

                deliveryStats = drone.Delivery(averageBufferQueue);

                //update my network stats
                drone.setDelivering(false);
                network.updateDeliveryStatus(drone);
                network.updateEnergy(drone);
                network.updatePosition(drone);

                //update my total delivery stats
                totalStats.update(deliveryStats.getKm(), drone.getEnergy());

                System.out.println("\nMAIN - Le stat di consegna sono: " + deliveryStats.toString());
                if(!drone.isMaster()) {
                    sendStatsToMaster(drone, deliveryStats);
                }else{
                    deliveryStatsQueue.add(deliveryStats);
                    System.out.println("SERVICE IMPL - Stats aggiunte in coda, status del drone aggiornato");
                    deliveryStatsQueue.printAll("MAIN");
                    network.printDroneList("MAIN");

                    synchronized (consumerLock){
                        consumerLock.notify();
                    }
                }
            }

            // chiudi se energy sotto 15
            if(drone.getEnergy() < 15){
                deleteMe(drone, restClient, mqttClient, server, orderQueue, network, deliveryStatsQueue);
            }

            try { Thread.sleep(1000); } catch (Exception e) { }
        }
    }

    public static void NotifyNetwork(Drone myDrone, LogicNetwork network) throws InterruptedException{
        ArrayList<Thread> thList=new ArrayList<Thread>();

        ArrayList<Drone> droneListCopy= new ArrayList<>(network.getDroneList());
        for (Drone d : droneListCopy){
            if(d.getId() != myDrone.getId()){
                Thread notifyTh = new Thread(new DroneJoinNotifyThread(myDrone, d, network));
                thList.add(notifyTh);
            }
        }

        for(Thread t:thList){
            t.start();
        }

        for(Thread t:thList){
            t.join();
        }

    }

    public static void becomeMaster(Drone drone, MqttClient mqttClient, MqttConnectOptions connOpts, OrderQueue queue, Thread consumer){
        drone.setRankingUp(false);

        if(drone.isMaster()){
            return;
        }

        drone.setMaster(true);
        System.out.println("\nMAIN - Sono diventato il master");

        consumer.start();

        try{
            mqttClient.connect(connOpts);
            System.out.println("MAIN - Connected to broker " + broker);

            //un thread solo gestisce tutte le callback dei mess ricevuti
            mqttClient.setCallback(new MqttCallback() {
                public void messageArrived(String topic, MqttMessage message) {
                    String receivedMessage = new String(message.getPayload());
                    Order order = gson.fromJson(receivedMessage, Order.class);
                    System.out.println("\nMAIN - Received order: " + order);
                    queue.put(order);
                }

                public void connectionLost(Throwable cause) {
                    System.out.println("MAIN - mqtt connection lost");
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                     System.out.println("MAIN - mqtt delivery complete");
                }
            });
            mqttClient.subscribe(topic,qos);

        }catch (Exception e){
            System.out.println("MAIN - " + e.getMessage());
        }
    }

    public static void StartElection(Drone myDrone){
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:"+myDrone.getDronePort()).usePlaintext().build();
        DroneServiceStub stub = DroneServiceGrpc.newStub(channel);

        ElectionMessage request = ElectionMessage.newBuilder()
                .setElected(false)
                .setHigherId(-1)
                .setHigherEnergy(-1)
                .build();

        try {
            stub.election(request, new StreamObserver<AckResponse>() {
                public void onNext(AckResponse response) {

                }
                public void onError(Throwable throwable) {
                    System.out.println("MAIN - onError StartElection a me stesso: " + throwable.getMessage());
                    channel.shutdown();
                }
                public void onCompleted() {
                    channel.shutdownNow();
                }
            });
            channel.awaitTermination(3, TimeUnit.SECONDS);
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

    }

    public static void deleteMe(Drone drone, Client restClient, MqttClient mqttClient, Server server, OrderQueue orderQueue, LogicNetwork network, DeliveryStatsQueue dsQueue){
        System.out.println("----- DELETE FROM MAIN -----");

        if(drone.isMaster()){
            try {
                if(mqttClient.isConnected()){
                    mqttClient.disconnect();
                    System.out.println("MAIN - Disconnected from broker -----");
                }

                //non sarò mai in delivering qui, è più un controllo per capire se tutto funziona come dovrebbe
                if(drone.isDelivering()){
                    System.out.println("MAIN - Aspetto fine consegna -----");
                    while (drone.isDelivering()){

                    }
                }

                if(!orderQueue.isEmpty()){
                    System.out.println("MAIN - Aspetto svuotamento coda -----");
                    while (!orderQueue.isEmpty()){

                        if(!drone.isDelivering()){
                            //metto il drone in perenne delivering cosi non esegue più ordini visto che non ha energia
                            drone.setDelivering(true);
                            network.updateDeliveryStatus(drone);
                            System.out.println("MAIN - Non dovrei più eseguire ordini -----");
                        }

                        if(network.getDroneListSize()==1){
                            System.out.println("MAIN - Chiudo lo stesso, non ci sono altri droni e io non ho energia, restanti ordini persi -----");
                            break;
                        }
                    }

                    if(orderQueue.getCurrent() != null && network.getDroneListSize() > 1){
                        System.out.println("MAIN - Aspetto assegnamento ultimo ordine");
                        while(orderQueue.getCurrent() != null){
                            if(network.getDroneListSize()==1){
                                System.out.println("MAIN - Chiudo lo stesso, non ci sono altri droni e io non ho energia, restanti ordini persi -----");
                                break;
                            }
                        }
                    }
                }

                server.shutdownNow();
                server.awaitTermination();

                SendGlobalsToServer(network, restClient, dsQueue);

            }catch (Exception e){
                System.out.println("MAIN - Exception disconnect" + e.toString());
            }
        }
        else{
            //non sarò mai in delivering qui, è più un controllo per capire se tutto funziona come dovrebbe
            if(drone.isDelivering()){
                System.out.println("MAIN - Aspetto fine consegna -----");
                while (drone.isDelivering()){

                }
            }

            try{
                server.shutdownNow();
                server.awaitTermination();
            }catch (Exception e){
                System.out.println("MAIN - Exception disconnect" + e.toString());
            }

        }

        System.out.println("MAIN - Delete request for drone with id: " + drone.getId());
        String droneJsonString = gson.toJson(drone);
        try {
            WebResource webResource = restClient.resource(removePath);
            ClientResponse response = webResource
                    .type("application/json")
                    .delete(ClientResponse.class, droneJsonString);

            if (response.getStatus() != 200) {
                throw new RuntimeException("MAIN - Failed : HTTP error code : " + response.getStatus());
            }

            System.out.println("MAIN - Drone with id: " + drone.getId() + " deleted");

        }catch (Exception e){
            System.out.println(e.toString());
        }

        System.exit(0);
    }

    public static void sendStatsToMaster(Drone myDrone, DeliveryStats stats){
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:"+myDrone.getDroneMasterPort()).usePlaintext().build();
        DroneServiceGrpc.DroneServiceStub stub = DroneServiceGrpc.newStub(channel);

        List<Double> aveValueList = new ArrayList<Double>();
        List<Long> aveTimeList = new ArrayList<Long>();

        for(MeasurementAverage ma : stats.getAveList()){
            aveValueList.add(ma.getAverage());
            aveTimeList.add(ma.getTimestamp());
        }

        DeliveryMessage request = DeliveryMessage.newBuilder()
                .setId(myDrone.getId())
                .setEnergy(myDrone.getEnergy())
                .setPosX(myDrone.getPosition().getX())
                .setPosY(myDrone.getPosition().getY())
                .setKm(stats.getKm())
                .addAllAve(aveValueList)
                .setTime(stats.getTimestamp())
                .addAllAveTime(aveTimeList)
                .build();

        System.out.println("MAIN - Invio stats al master");

        Context newContext = Context.current().fork();
        Context origContext = newContext.attach();
        try{
            stub.updateDelivery(request, new StreamObserver<AckResponse>() {
                public void onNext(AckResponse response) {

                }
                public void onError(Throwable throwable) {
                    System.out.println("MAIN - onError send stats: " + throwable.getMessage());
                    System.out.println("MAIN - Master non raggiungibile, inizio elezione");
                    StartElection(myDrone);
                    channel.shutdown();
                }
                public void onCompleted() {
                    channel.shutdownNow();
                }
            });
            channel.awaitTermination(3, TimeUnit.SECONDS);
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
        finally {
            newContext.detach(origContext);
        }

    }

    public static void SendGlobalsToServer(LogicNetwork network, Client restClient, DeliveryStatsQueue dsQueue){
        System.out.println("MAIN - Mando stats globali al server");
        GlobalStats globalStats = new GlobalStats();

        ArrayList<DeliveryStats> copy = dsQueue.readAllAndClear();

        if(copy.size()==0){
            System.out.println("MAIN - Non si sono stats in coda per calcolare quelle globali");
            return;
        }

        System.out.println("MAIN - Nella coda di stats ci sono: ");
        for(DeliveryStats ds : copy){
            System.out.println("   " + ds.toString());
        }
        globalStats.calculate(copy, network.getDroneListSize());
        System.out.println("MAIN - Le stat globali sono: " + globalStats.toString());

        String statsJsonString = gson.toJson(globalStats);

        try{
            WebResource webResource = restClient.resource(receivePath);
            ClientResponse response = webResource
                    .accept("application/json")
                    .type("application/json")
                    .post(ClientResponse.class, statsJsonString);

            if (response.getStatus() != 200) {
                throw new RuntimeException("MAIN - Failed : HTTP error code : " + response.getStatus() + " - " + response.getStatusInfo());
            }

            System.out.println("MAIN - Stats globali inviate");

        }catch (Exception e){
            System.out.println(e.toString());
            System.out.println("MAIN - Invio non riuscito");
        }
    }
}