package ServerPkg;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;

public class ServerAdmin {
    public final static String IP = "localhost";
    public final static int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServerFactory.create("http://"+ IP +":"+PORT+"/");
        server.start();
        System.out.println("ServerAdmin running on: http://"+ IP +":"+PORT);
        System.out.println("---Press a key to stop---");
        System.in.read();
        System.out.println("Stopping server");
        server.stop(0);
        System.out.println("Server stopped");
        System.exit(0);
    }
}
