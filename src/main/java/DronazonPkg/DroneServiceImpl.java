package DronazonPkg;

import SensorPkg.MeasurementAverage;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.Context;
import project.drone.grpc.DroneServiceGrpc;
import project.drone.grpc.DroneServiceGrpc.DroneServiceImplBase;
import project.drone.grpc.DroneServiceOuterClass.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class DroneServiceImpl extends DroneServiceImplBase{
    public Drone drone;
    public LogicNetwork network;

    //notAlive è usato per identificare i droni che non sono nella rete (ma sono in memoria) alla fine di un'elezione
    public ArrayList<Integer> notAlive;

    public DeliveryStatsQueue deliveryStatsQueue;

    public final Object consumerLock;

    public DroneServiceImpl(Drone drone, LogicNetwork network, DeliveryStatsQueue deliveryStatsQueue, Object consumerLock) {
        this.drone = drone;
        this.network = network;
        this.deliveryStatsQueue = deliveryStatsQueue;
        notAlive =new ArrayList<Integer>();
        this.consumerLock=consumerLock;
    }

    @Override
    public void join(JoinNotification request, StreamObserver<JoinResponse> responseObserver){
        System.out.println("\nSERVICE IMPL - Si presenta il drone con id: " + request.getId());

        Drone droneToAdd=new Drone(request.getId(), request.getPort());
        droneToAdd.setPosition(new Position(request.getPosX(), request.getPosY()));

        network.add(droneToAdd);

        network.printDroneList("SERVICE IMPL");

        JoinResponse response = JoinResponse.newBuilder()
                .setMaster(drone.isMaster())
                .setPort(drone.getDronePort())
                .setPosX(drone.getPosition().getX())
                .setPosY(drone.getPosition().getY())
                .build();

//        questo è giusto per verificare la sync
        try { Thread.sleep(1500); } catch (Exception e) { }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateInfo(InfoMessage request, StreamObserver<AckResponse> responseObserver){
        System.out.println("\nSERVICE IMPL - Ho ricevuto le info del drone con id: " + request.getId());

        Drone droneInfo = new Drone();
        droneInfo.setId(request.getId());
        droneInfo.setEnergy(request.getEnergy());
        droneInfo.setPosition(new Position(request.getPosX(), request.getPosY()));

        network.updateEnergy(droneInfo);
        network.updatePosition(droneInfo);

        network.printDroneList("SERVICE IMPL");

        //questo è usato solo durante la fase finale dell'elezione
        if(notAlive.contains(droneInfo.getId())){
            notAlive.remove((Object)droneInfo.getId());
        }

        AckResponse response = AckResponse.newBuilder().setAck("OK").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void election(ElectionMessage request, StreamObserver<AckResponse> responseObserver){

        int myEnergy=drone.getEnergy();
        if(drone.isDelivering()){
            myEnergy -= 10;
        }

        AckResponse response = AckResponse.newBuilder()
                .setAck("OK")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        if(request.getHigherId() > -1 ){
            System.out.println("\nSERVICE IMPL - Mess di elezione ricevuto: " + request.getElected() + " " + request.getHigherId() + " " + request.getHigherEnergy() + " - partecipant: " + drone.isPartecipant());
        }
        else{
            if(drone.isPartecipant()){
                System.out.println("\nSERVICE IMPL - C'è già un'elezione in corso" + " - partecipant: " + drone.isPartecipant());
                return;
            }
            System.out.println("\nSERVICE IMPL - Inizio elezione" + " - partecipant: " + drone.isPartecipant());

            drone.setPartecipant(true);
            forwardElection(false, drone.getId(), myEnergy);
            return;
        }


        if(request.getElected()){
            //a questo punto il master è stato deciso quindi confronto solo l'id
            if(request.getHigherId() == drone.getId()){
                System.out.println("SERVICE IMPL - Fine elezione, a breve sarò ufficialmente master, aspetto le info da tutti" + " - partecipant: " + drone.isPartecipant());
                try { Thread.sleep(3000); } catch (Exception e) { }

                //mi imposto come master
                drone.setRankingUp(true);
                drone.setDroneMasterPort(-1);

                for (int id : notAlive){
                    System.out.println("SERVICE IMPL - Drone " + id + " non ha inviato le info, lo considero off" + " - partecipant: " + drone.isPartecipant());
                    network.removeById(id);
                }
                notAlive.clear();
                network.printDroneList("SERVICE IMPL");

                return;
            }
            //master eletto
            Drone master = network.getById(request.getHigherId());
            drone.setDroneMasterPort(master.getDronePort());

            System.out.println("SERVICE IMPL - Il master è il drone con id: " + master.getId());
            System.out.println("SERVICE IMPL - Porta verso il master: " + drone.getDroneMasterPort());

            drone.setPartecipant(false);

            forwardElection(true, request.getHigherId(), request.getHigherEnergy());
            sendInfoToMaster();

            //per evitare più master se parte un'elezione mentre viene iniziata un'elezione proprio mentre i droni stanno finendo la precedente l'elezione e hanno partecipant a false
            if(drone.getId() != master.getId() && drone.isMaster()){
                drone.setMaster(false);
            }

        }else{
            if(request.getHigherEnergy() > myEnergy || (request.getHigherEnergy() == myEnergy && request.getHigherId() > drone.getId())){
                drone.setPartecipant(true);
                try { Thread.sleep(1000); } catch (Exception e) { } //questo è giusto per verificare la sync
                forwardElection(false, request.getHigherId(), request.getHigherEnergy());
            }
            else if(myEnergy > request.getHigherEnergy() || (myEnergy == request.getHigherEnergy()  && drone.getId() > request.getHigherId())){
                if(!drone.isPartecipant()){
                    drone.setPartecipant(true);
                    try { Thread.sleep(1000); } catch (Exception e) { } //questo è giusto per verificare la sync
                    forwardElection(false, drone.getId(), myEnergy);
                }
                else{
                    System.out.println("\nSERVICE IMPL - Elezione multipla in corso, la fermo" + " - partecipant: " + drone.isPartecipant());
                    return;
                }
            }
            else if(request.getHigherEnergy() == myEnergy && request.getHigherId() == drone.getId()){
                //master eletto
                System.out.println("\nSERVICE IMPL - Sono appena stato eletto master " + drone.toString() + " - partecipant: " + drone.isPartecipant());

                ArrayList<Drone> copyList= new ArrayList<>(network.getDroneList());
                for (Drone d : copyList){
                    if(drone.getId() != d.getId()){
                        notAlive.add(d.getId());
                    }
                }
                drone.setPartecipant(false);

                forwardElection(true, request.getHigherId(), request.getHigherEnergy());
            }

        }

    }

    public void forwardElection(boolean electedReceived, int idToForward, int energyToForward) {
        Drone nextDrone=network.nextDrone(drone);
        System.out.println("\nSERVICE IMPL - Forward election al drone " + nextDrone.getId() + " sulla porta " + nextDrone.getDronePort() + " - partecipant: " + drone.isPartecipant());

        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:"+nextDrone.getDronePort()).usePlaintext().build();
        DroneServiceGrpc.DroneServiceStub stub = DroneServiceGrpc.newStub(channel);

        ElectionMessage request = ElectionMessage.newBuilder()
                .setElected(electedReceived)
                .setHigherId(idToForward)
                .setHigherEnergy(energyToForward)
                .build();
        System.out.println("SERVICE IMPL - Mess per il drone " + nextDrone.getId() + ": " + request.getElected() + " " + request.getHigherId() + " " + request.getHigherEnergy() + " - partecipant: " + drone.isPartecipant());

        Context newContext = Context.current().fork();
        Context origContext = newContext.attach();
        try{
            stub.election(request, new StreamObserver<AckResponse>() {
                public void onNext(AckResponse response) {
//                    System.out.println("SERVICE IMPL - Ricevuto ack (election)");
                }
                public void onError(Throwable throwable) {
                    System.out.println("SERVICE IMPL - onError forward election: " + throwable.getMessage());
                    System.out.println("SERVICE IMPL - Drone " + nextDrone.getId() + " non raggiungibile, lo elimino e riprovo");
                    network.removeById(nextDrone.getId());
                    network.printDroneList("SERVICE IMPL");
                    forwardElection(electedReceived, idToForward, energyToForward);
                    channel.shutdown();
                }
                public void onCompleted() {
//                    System.out.println("onCompleted, chiudo il canale verso il drone " + nextDrone.getId());
                    channel.shutdownNow();
                }
            });
            channel.awaitTermination(3, TimeUnit.SECONDS);
        }
        catch (Exception e){
            System.out.println("SERVICE IMPL - " + e.toString());
        }
        finally {
            newContext.detach(origContext);
        }

    }

    public void sendInfoToMaster() {
        System.out.println("SERVICE IMPL - Mando info al master sulla porta " + drone.getDroneMasterPort());

        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:"+drone.getDroneMasterPort()).usePlaintext().build();
        DroneServiceGrpc.DroneServiceStub stub = DroneServiceGrpc.newStub(channel);

        InfoMessage request = InfoMessage.newBuilder()
                .setId(drone.getId())
                .setEnergy(drone.getEnergy())
                .setPosX(drone.getPosition().getX())
                .setPosY(drone.getPosition().getY())
                .build();

        try{
            stub.updateInfo(request, new StreamObserver<AckResponse>() {
                public void onNext(AckResponse response) {

                }
                public void onError(Throwable throwable) {
                    System.out.println("SERVICE IMPL - onError send info to master: " + throwable.getMessage());
                    System.out.println("SERVICE IMPL - Master non raggiungibile?! WTF?!");
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

    @Override
    public void sendOrder(OrderMessage request, StreamObserver<AckResponse> responseObserver){
        AckResponse response = AckResponse.newBuilder()
                .setAck("NO")
                .build();

        String esito = "Rifiutato ordine";

        if(!drone.isDelivering()){
            Order order=new Order(request.getId(), new Position(request.getPickUpX(), request.getPickUpY()), new Position(request.getDeliveryX(), request.getDeliveryY()));
            esito="Accettato ordine";
            drone.setOrder(order);
            response = AckResponse.newBuilder()
                    .setAck("OK")
                    .build();
        }

        System.out.println("\nSERVICE IMPL - "+ esito + " " + request.getId());

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateDelivery(DeliveryMessage request, StreamObserver<AckResponse> responseObserver){
        long time=request.getTime();
        Position position = new Position(request.getPosX(), request.getPosY());
        float km = request.getKm();
        int energy = request.getEnergy();
        ArrayList<MeasurementAverage> aveList = new ArrayList<MeasurementAverage>();

        for(int i=0; i<request.getAveCount(); i++){
            MeasurementAverage ma = new MeasurementAverage(request.getAve(i), request.getAveTime(i));
            aveList.add(ma);
        }

        DeliveryStats deliveryStats = new DeliveryStats(time, position, km, energy, aveList);
        System.out.println("\nSERVICE IMPL - Ho ricevuto le stats di consegna dal drone " + request.getId() + ": " + deliveryStats.toString());

        AckResponse response = AckResponse.newBuilder()
                .setAck("OK")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        deliveryStatsQueue.add(deliveryStats);

        Drone droneInfo = new Drone();
        droneInfo.setId(request.getId());
        droneInfo.setEnergy(request.getEnergy());
        droneInfo.setPosition(position);
        droneInfo.setDelivering(false);

        network.updateEnergy(droneInfo);
        network.updatePosition(droneInfo);
        network.updateDeliveryStatus(droneInfo);

        System.out.println("SERVICE IMPL - Stats aggiunte in coda, status del drone aggiornato");
        network.printDroneList("SERVICE IMPL");
        deliveryStatsQueue.printAll("SERVICE IMPL");

        synchronized (consumerLock){
            consumerLock.notify();
        }
    }

    @Override
    public void pingPong(AckResponse request, StreamObserver<AckResponse> responseObserver){
        AckResponse response = AckResponse.newBuilder()
                .setAck("YO")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
