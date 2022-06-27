package DronazonPkg;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import project.drone.grpc.DroneServiceGrpc;
import project.drone.grpc.DroneServiceOuterClass.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MasterPingThread implements Runnable{
    private int counter;
    private Drone drone;
    private boolean ending;
    private int maxCount;
    private int waitTime;
    public int oldMasterPort;

    public MasterPingThread(Drone drone) {
        this.drone = drone;
        counter=0;
        ending=false;
        maxCount= new Random().nextInt(8) + 8; //range 8 - 15
        waitTime = new Random().nextInt(2001) + 2000; //range 2000-4000
        oldMasterPort = 0;
    }

    public void start(){    }

    public void run(){
        while(!ending){
            if(drone.isMaster()){
                break;
            }

            try { Thread.sleep(waitTime); } catch (Exception e) { }

            if(drone.isDelivering()){
                counter=0;
                continue;
            }

            if(counter>=maxCount){
                counter=0;
                oldMasterPort=drone.getDroneMasterPort();
                PingMaster();
            }

            counter++;
        }
    }

    public void PingMaster(){
        if(drone.isMaster()){
            return;
        }

        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:"+drone.getDroneMasterPort()).usePlaintext().build();
        DroneServiceGrpc.DroneServiceStub stub = DroneServiceGrpc.newStub(channel);

        AckResponse request = AckResponse.newBuilder()
                .setAck("HI")
                .build();

        System.out.println("\nPING THREAD - Invio ping al master, non ricevo ordini da troppo tempo");

        try{
            stub.pingPong(request, new StreamObserver<AckResponse>() {
                public void onNext(AckResponse response) {
                    System.out.println("PING THREAD - Ricevuto pong dal master");
                }
                public void onError(Throwable throwable) {
                    System.out.println("PING THREAD - onError ping to master: " + throwable.getMessage());

                    if(oldMasterPort == drone.getDroneMasterPort()){
                        System.out.println("PING THREAD - Master non raggiungibile, inizio elezione");
                        StartElection();
                    }else{
                        System.out.println("PING THREAD - Nuovo master eletto già eletto");
                    }
                    channel.shutdown();
                }
                public void onCompleted() {
//                    System.out.println("onCompleted, chiudo il canale verso il drone master");
                    channel.shutdownNow();
                }
            });
            channel.awaitTermination(3, TimeUnit.SECONDS);
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

    }

    public void StartElection(){
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:"+drone.getDronePort()).usePlaintext().build();
        DroneServiceGrpc.DroneServiceStub stub = DroneServiceGrpc.newStub(channel);

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
                    System.out.println("PING THREAD - onError StartElection: " + throwable.getMessage());
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

}
