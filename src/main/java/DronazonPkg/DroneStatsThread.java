package DronazonPkg;

import ServerPkg.GlobalStats;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.util.ArrayList;

public class DroneStatsThread  implements Runnable{
    public Drone drone;
    public LogicNetwork network;
    public TotalDroneDeliveryStats tdds;
    public DeliveryStatsQueue dsQueue;
    public Client restClient;
    public String receivePath="http://localhost:8080/DroneService/receive";
    public GlobalStats globalStats;
    public Gson gson = new Gson();

    public DroneStatsThread(Drone drone, LogicNetwork network, TotalDroneDeliveryStats tdds, DeliveryStatsQueue deliveryStatsQueue, Client restClient) {
        this.drone=drone;
        this.network=network;
        this.tdds = tdds;
        this.dsQueue=deliveryStatsQueue;
        this.restClient=restClient;
        globalStats=new GlobalStats();
    }

    public void start(){    }

    public void run(){
        while (true){
            try { Thread.sleep(10000); } catch (Exception e) { }

            System.out.println("\nSTATS THREAD - Drone total stats: "
                                + "id - " + drone.getId()
                                + ", master - " + drone.isMaster()
                                + ", energy - " + tdds.getEnergy()
                                + ", total deliveries - " + tdds.getDeliveriesTotal()
                                + ", total km - " + tdds.getKmTotal());

            if(drone.isMaster()){
                System.out.println("\nSTATS THREAD - Calcolo stats globali");
                ArrayList<DeliveryStats> copy = dsQueue.readAllAndClear();

                if(copy.size()==0){
                    System.out.println("STATS THREAD - Non si sono stats in coda per calcolare quelle globali");
                    continue;
                }

                System.out.println("STATS THREAD - Calcolo stats globali sulle seguenti stats: ");
                for(DeliveryStats ds : copy){
                    System.out.println("   " + ds.toString());
                }

                globalStats.calculate(copy, network.getDroneListSize());
                System.out.println("STATS THREAD - Le stat globali sono: " + globalStats.toString());

                SendGlobalsToServer();
                System.out.println("STATS THREAD - Stats mandate");

                globalStats.reset();
            }
        }
    }

    public void SendGlobalsToServer(){
        String statsJsonString = gson.toJson(globalStats);

        try{
            WebResource webResource = restClient.resource(receivePath);
            ClientResponse response = webResource
                    .accept("application/json")
                    .type("application/json")
                    .post(ClientResponse.class, statsJsonString);

            if (response.getStatus() != 200) {
                throw new RuntimeException("STATS THREAD - Failed : HTTP error code : " + response.getStatus() + " - " + response.getStatusInfo());
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }

    }

}
