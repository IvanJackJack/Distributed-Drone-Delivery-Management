package ClientPkg;

import ServerPkg.StatsServiceResponse;
import DronazonPkg.Drone;
import ServerPkg.GlobalStats;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClientAdmin {
    private static final String listPath="http://localhost:8080/StatsService/list";
    private static final String lastPath="http://localhost:8080/StatsService/last";
    private static final String aveDelPath="http://localhost:8080/StatsService/averageDel";
    private static final String aveKmPath="http://localhost:8080/StatsService/averageKm";

    private static final Gson gson = new Gson();
    private static final Client restClient = Client.create();
    private static final BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        String input;

        while(true){
            System.out.println("\nInserisci un comando tra: list - last - aveDel - aveKm - quit");
            input = inFromUser.readLine();

            if(input.equalsIgnoreCase("quit")){
                break;
            }

            switch (input){
                case "list":
                    listRequest();
                    break;

                case "last":
                    lastRequest();
                    break;

                case "aveDel":
                    aveDelRequest();
                    break;

                case "aveKm":
                    aveKmRequest();
                    break;

                default:
                    System.out.println("Comando non riconosciuto");

            }

        }
        System.out.println("\nI'm outta here, see you later");
    }

    public static void listRequest(){
        StatsServiceResponse ssr = new StatsServiceResponse();

        try{
            WebResource webResource = restClient.resource(listPath);
            ClientResponse response = webResource
                    .get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus() + " - " + response.getStatusInfo());
            }

            String resFromServer = response.getEntity(String.class);
            ssr = gson.fromJson(resFromServer, StatsServiceResponse.class);

        }catch (Exception e){
            System.out.println(e.toString());
            return;
        }

        if(ssr.getDroneList().size()==0){
            System.out.println("\nNon ci sono droni");
            return;
        }
        System.out.println("\nEcco l'elenco dei droni:");
        for (Drone d : ssr.getDroneList()) {
            System.out.println(d.toString());
        }
    }

    public static void lastRequest() throws Exception {
        String input;
        StatsServiceResponse ssr = new StatsServiceResponse();

        while(true){
            System.out.println("Inserisci il numero di stats che vuoi vedere");
            input = inFromUser.readLine();

            if(Integer.valueOf(input) > 0){
                break;
            }

            System.out.println("Numero non valido");
        }


        try{
            WebResource webResource = restClient.resource(lastPath + "/" + input);
            ClientResponse response = webResource
                    .get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus() + " - " + response.getStatusInfo());
            }

            String resFromServer = response.getEntity(String.class);
            ssr = gson.fromJson(resFromServer, StatsServiceResponse.class);

        }catch (Exception e){
            System.out.println(e.toString());
            return;
        }

        if(ssr.getLastGlobalStats().size()==0){
            System.out.println("\nNon ci sono stats valide");
            return;
        }
        System.out.println("\nEcco l'elenco delle ultime stats:");
        for (GlobalStats gs : ssr.getLastGlobalStats()){
            System.out.println(gs.toString());
        }

    }

    public static void aveDelRequest() throws Exception {
        String input1, input2;
        StatsServiceResponse ssr = new StatsServiceResponse();

        while(true){
            System.out.println("Inserisci il tempo di inizio");
            input1 = inFromUser.readLine();

            if(Long.valueOf(input1) > 0){
                break;
            }

            System.out.println("Tempo di inizio non valido");
        }

        while(true){
            System.out.println("Inserisci il tempo di fine");
            input2 = inFromUser.readLine();

            if(Long.valueOf(input2) > Long.valueOf(input1)){
                break;
            }

            System.out.println("Tempo di fine non valido");
        }

        try{
            WebResource webResource = restClient.resource(aveDelPath + "/" + input1 + "/" + input2);
            ClientResponse response = webResource
                    .get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus() + " - " + response.getStatusInfo());
            }

            String resFromServer = response.getEntity(String.class);
            ssr = gson.fromJson(resFromServer, StatsServiceResponse.class);

        }catch (Exception e){
            System.out.println(e.toString());
            return;
        }

        System.out.println("\nLa media del num di consegne in quel timespan è: " + ssr.getAverage());

    }

    public static void aveKmRequest() throws Exception {
        String input1, input2;
        StatsServiceResponse ssr = new StatsServiceResponse();

        while(true){
            System.out.println("Inserisci il tempo di inizio");
            input1 = inFromUser.readLine();

            if(Long.valueOf(input1) > 0){
                break;
            }

            System.out.println("Tempo di inizio non valido");
        }

        while(true){
            System.out.println("Inserisci il tempo di fine");
            input2 = inFromUser.readLine();

            if(Long.valueOf(input2) > Long.valueOf(input1)){
                break;
            }

            System.out.println("Tempo di fine non valido");
        }

        try{
            WebResource webResource = restClient.resource(aveKmPath + "/" + input1 + "/" + input2);
            ClientResponse response = webResource
                    .get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus() + " - " + response.getStatusInfo());
            }

            String resFromServer = response.getEntity(String.class);
            ssr = gson.fromJson(resFromServer, StatsServiceResponse.class);

        }catch (Exception e){
            System.out.println(e.toString());
            return;
        }

        System.out.println("\nLa media dei km in quel timespan è: " + ssr.getAverage());

    }

}
