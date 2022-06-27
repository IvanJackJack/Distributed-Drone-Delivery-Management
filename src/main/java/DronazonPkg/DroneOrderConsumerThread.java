package DronazonPkg;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import project.drone.grpc.DroneServiceGrpc;
import project.drone.grpc.DroneServiceOuterClass;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class DroneOrderConsumerThread implements Runnable{
    public OrderQueue queue;
    public LogicNetwork network;
    public Drone executor;
    public float minDist;
    public String responseString;
    public final Object consumerLock;

    public DroneOrderConsumerThread(OrderQueue queue, LogicNetwork network, Object consumerLock) {
        this.queue = queue;
        this.network = network;
        this.consumerLock=consumerLock;
    }

    public void start(){    }

    public void run(){
        while(true){
            Order order = queue.take();
            System.out.println("CONSUMER THREAD - Ho preso dalla coda l'ordine " + order.getId());
            assign(order);
        }
    }

    public void assign(Order order){
        while(true){
            executor=null;
            minDist=9999;
            responseString="";

            network.printDroneList("CONSUMER THREAD");
            ArrayList<Drone> copyList= new ArrayList<>(network.getDroneList());
            for (Drone d: copyList){
                if(!d.isDelivering()){
                    float dist=Distance(d.position, order.pickUpPoint);
                    System.out.println("CONSUMER THREAD - Distanza del drone " + d.getId() + " in pos " + d.getPosition() + " : " + dist);
                    if (dist < minDist){
                        executor=d;
                        minDist=dist;
                    }
                    else if(dist == minDist && d.getEnergy() > executor.getEnergy() ){
                        executor=d;
                        minDist=dist;
                    }
                    else if(dist == minDist && d.getEnergy() == executor.getEnergy() && d.getId() > executor.getId() ){
                        executor=d;
                        minDist=dist;
                    }
                }
            }

            if(executor==null){
                System.out.println("\nCONSUMER THREAD - Non ci sono droni validi per la consegna, attendo...");

                try {
                    synchronized (consumerLock){
                        consumerLock.wait();
                    }
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                System.out.println("\nCONSUMER THREAD - Qualche drone ha terminato una consegna, ricalcolo");
            }else{
                if(SendOrder(order)){
                    queue.setCurrent(null);
                    break;
                }
            }
        }

        System.out.println("CONSUMER THREAD - Ordine " + order.getId() + " consegnato al drone " + executor.getId());
    }

    public float Distance(Position a, Position b){
        return (float)Math.sqrt( Math.pow((b.getX() - a.getX()), 2) + Math.pow((b.getY() - a.getY()), 2) );
    }

    public boolean SendOrder(Order order){
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:"+executor.getDronePort()).usePlaintext().build();
        DroneServiceGrpc.DroneServiceStub stub = DroneServiceGrpc.newStub(channel);

        DroneServiceOuterClass.OrderMessage request = DroneServiceOuterClass.OrderMessage.newBuilder()
                .setId(order.getId())
                .setPickUpX(order.getPickUpPoint().getX())
                .setPickUpY(order.getPickUpPoint().getY())
                .setDeliveryX(order.getDeliveryPoint().getX())
                .setDeliveryY(order.getDeliveryPoint().getY())
                .build();

        System.out.println("\nCONSUMER THREAD - Mando ordine " + order.getId() + " al drone " + executor.getId());

        try{
            stub.sendOrder(request, new StreamObserver<DroneServiceOuterClass.AckResponse>() {
                public void onNext(DroneServiceOuterClass.AckResponse response) {
                    executor.setDelivering(true);
                    network.updateDeliveryStatus(executor);
                    responseString=response.getAck();
                    System.out.println("CONSUMER THREAD - La risposta del drone è " + responseString);
                }
                public void onError(Throwable throwable) {
                    System.out.println("CONSUMER THREAD - onError sendOrder notify: " + throwable.getMessage());
                    System.out.println("CONSUMER THREAD - Drone " + executor.getId() + " non raggiungibile, va eliminato");
                    network.removeById(executor.getId());
                    network.printDroneList("CONSUMER THREAD");
                    responseString="NO";
                    channel.shutdownNow();
                }
                public void onCompleted() {
                    channel.shutdownNow();
                }
            });
            channel.awaitTermination(3, TimeUnit.SECONDS);
            while (!channel.isTerminated()){

            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return responseString.equals("OK");
    }

}
