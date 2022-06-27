package DronazonPkg;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import project.drone.grpc.DroneServiceGrpc;
import project.drone.grpc.DroneServiceOuterClass.*;
import java.util.concurrent.TimeUnit;

public class DroneJoinNotifyThread implements Runnable{
    private Drone droneToNotify;
    private Drone myDrone;
    private LogicNetwork network;

    public DroneJoinNotifyThread(Drone myDrone, Drone droneToNotify, LogicNetwork network) {
        this.droneToNotify = droneToNotify;
        this.myDrone = myDrone;
        this.network = network;
    }

    public void start(){    }

    public void run(){
        try{
            final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:"+droneToNotify.getDronePort()).usePlaintext().build();
            DroneServiceGrpc.DroneServiceStub stub = DroneServiceGrpc.newStub(channel);

            JoinNotification request = JoinNotification.newBuilder()
                    .setId(myDrone.getId())
                    .setPort(myDrone.getDronePort())
                    .setPosX(myDrone.getPosition().getX())
                    .setPosY(myDrone.getPosition().getY())
                    .build();

            System.out.println("JOIN THREAD - Join notify verso il drone " + droneToNotify.getId());

            stub.join(request, new StreamObserver<JoinResponse>() {
                public void onNext(JoinResponse response) {
                    System.out.println("JOIN THREAD - Ricevuta risposta di join dal drone " + droneToNotify.getId());
                    droneToNotify.setPosition(new Position(response.getPosX(), response.getPosY()));
                    if(response.getMaster()){
                        System.out.println("JOIN THREAD - Il drone master è: " + droneToNotify.getId());
                        myDrone.setDroneMasterPort(response.getPort());
                        System.out.println("JOIN THREAD - Porta verso il drone master: " + myDrone.getDroneMasterPort());
                    }
                }
                public void onError(Throwable throwable) {
                    System.out.println("JOIN THREAD - onError join notify: " + throwable.getMessage());
                    System.out.println("JOIN THREAD - Drone " + droneToNotify.getId() + " non raggiungibile, va eliminato");
                    network.removeById(droneToNotify.getId());
                    channel.shutdown();
                }
                public void onCompleted() {
                    channel.shutdownNow();
                }
            });
            channel.awaitTermination(3, TimeUnit.SECONDS);
            while(!channel.isTerminated()){

            }

        }catch (Exception e){
            System.out.println("JOIN THREAD - " + e.getMessage());
        }

    }
}
