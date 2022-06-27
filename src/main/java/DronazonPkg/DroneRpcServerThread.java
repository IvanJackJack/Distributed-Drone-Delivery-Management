package DronazonPkg;

import io.grpc.Server;

public class DroneRpcServerThread implements Runnable{
    Server server;

    public DroneRpcServerThread(Server server) {
        this.server = server;
    }

    public void start(){ }

    public void run(){
        try {
            server.start();
            System.out.println("SERVER RPC THREAD - Server started");
            server.awaitTermination();
            System.out.println("SERVER RPC THREAD - Server closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
