package DronazonPkg;

import ServerPkg.GlobalStats;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.eclipse.paho.client.mqttv3.MqttClient;
import io.grpc.*;
import java.util.ArrayList;

public class DroneEndingThread implements Runnable{
    private Drone drone;
    private String removePath;
    private Client restClient;
    private MqttClient mqttClient;
    private Server server;
    private OrderQueue queue;
    private LogicNetwork network;
    private DeliveryStatsQueue dsQueue;
    public String receivePath="http://localhost:8080/DroneService/receive";
    public Gson gson = new Gson();

    public DroneEndingThread(Drone drone, Client restClient, MqttClient mqttClient, Server server, OrderQueue queue, LogicNetwork network, DeliveryStatsQueue dsQueue) {
        this.drone = drone;
        this.restClient =restClient;
        this.mqttClient = mqttClient;
        this.server=server;
        this.queue=queue;
        this.network=network;
        this.dsQueue=dsQueue;
        removePath="http://localhost:8080/DroneService/remove";
    }

    public void start(){    }

    public void run(){
        try {
            System.out.println("ENDING THREAD - Press a key to stop---");
            System.in.read();

            if(drone.getEnergy() > 25 || (!drone.isDelivering() && drone.getEnergy() > 15)){
                DeleteDrone();
            }
            else{
                System.out.println("ENDING THREAD - L'energia è, o sarà, sotto 15 quindi chiude il main");
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }
        System.exit(0);

    }

    public void DeleteDrone(){
        System.out.println("ENDING THREAD - DELETE FROM THREAD -----");

        if(drone.isMaster()){
            try {
                if(mqttClient.isConnected()){
                    mqttClient.disconnect();
                    System.out.println("\nENDING THREAD - Disconnected from broker");
                }

                if(drone.isDelivering()){
                    System.out.println("ENDING THREAD - Aspetto fine consegna -----");
                    while (drone.isDelivering()){

                    }
                }

                if(!queue.isEmpty()){
                    System.out.println("ENDING THREAD - Aspetto svuotamento coda -----");
                    while (!queue.isEmpty()){

                        if(!drone.isDelivering()){
                            //metto il drone in perenne delivering cosi non esegue più ordini visto che sta uscendo dalla rete
                            drone.setDelivering(true);
                            network.updateDeliveryStatus(drone);
                            System.out.println("ENDING THREAD - Non dovrei più eseguire ordini -----");
                        }

                        if(network.getDroneListSize()==1){
                            System.out.println("ENDING THREAD - Chiudo lo stesso, non ci sono altri droni per finire le consegne, restanti ordini persi -----");
                            break;
                        }
                    }

                    if(queue.getCurrent() != null && network.getDroneListSize() > 1){
                        System.out.println("ENDING THREAD - Aspetto assegnamento ultimo ordine");
                        while(queue.getCurrent() != null){
                            if(network.getDroneListSize()==1){
                                System.out.println("ENDING THREAD - Chiudo lo stesso, non ci sono altri droni e io non ho energia, restanti ordini persi -----");
                                break;
                            }
                        }
                    }

                }

                server.shutdownNow();
                server.awaitTermination();

                SendGlobalsToServer();

            }catch (Exception e){
                System.out.println("ENDING THREAD - Exception disconnect" + e.toString());
            }
        }
        else{
            if(drone.isDelivering()){
                System.out.println("ENDING THREAD - Aspetto fine consegna -----");
                while (drone.isDelivering()){

                }
            }

            try{
                server.shutdownNow();
                server.awaitTermination();
            }catch (Exception e){
                System.out.println("ENDING THREAD - Exception disconnect" + e.toString());
            }
        }

        System.out.println("\nENDING THREAD - Delete request for drone with id: " + drone.getId());
        String droneJsonString = gson.toJson(drone);

        WebResource webResource = restClient.resource(removePath);
        ClientResponse response = webResource
                .type("application/json")
                .delete(ClientResponse.class, droneJsonString);

        if (response.getStatus() != 200) {
            throw new RuntimeException("ENDING THREAD - Failed : HTTP error code : " + response.getStatus());
        }

        System.out.println("ENDING THREAD - Drone with id: " + drone.getId() + " deleted");
    }

    public void SendGlobalsToServer(){
        System.out.println("ENDING THREAD - Mando stats globali al server");
        GlobalStats globalStats = new GlobalStats();

        ArrayList<DeliveryStats> copy = dsQueue.readAllAndClear();

        if(copy.size()==0){
            System.out.println("ENDING THREAD - Non si sono stats in coda per calcolare quelle globali");
            return;
        }

        System.out.println("ENDING THREAD - Nella coda di stats ci sono: ");
        for(DeliveryStats ds : copy){
            System.out.println("   " + ds.toString());
        }
        globalStats.calculate(copy, network.getDroneListSize());
        System.out.println("ENDING THREAD - Le stat globali sono: " + globalStats.toString());

        String statsJsonString = gson.toJson(globalStats);

        try{
            WebResource webResource = restClient.resource(receivePath);
            ClientResponse response = webResource
                    .accept("application/json")
                    .type("application/json")
                    .post(ClientResponse.class, statsJsonString);

            if (response.getStatus() != 200) {
                throw new RuntimeException("ENDING THREAD - Failed : HTTP error code : " + response.getStatus() + " - " + response.getStatusInfo());
            }

            System.out.println("ENDING THREAD - Stats globali inviate");

        }catch (Exception e){
            System.out.println(e.toString());
            System.out.println("ENDING THREAD - Invio non riuscito");
        }
    }

}