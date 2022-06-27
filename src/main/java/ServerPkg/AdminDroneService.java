package ServerPkg;

import DronazonPkg.Drone;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("DroneService")
public class AdminDroneService {

    @Path("isWorking")
    @GET
    @Produces("text/plain")
    public String helloWorld(){
        String response="DroneService working";
        System.out.println("\n" + response);
        return response;
    }

    @Path("add")
    @POST
    @Produces({"application/json", "application/xml"})
    @Consumes({"application/json", "application/xml"})
    public Response addDrone(Drone newDrone){
        System.out.println("\naddDrone " + newDrone.toString());

        if (SmartCity.getInstance().addByObject(newDrone)){
            DroneServiceResponse dsr=new DroneServiceResponse(SmartCity.getInstance().getDroneSet());
            System.out.println("response: " + dsr.toString());
            return Response.ok().entity(dsr).build();
        }
        return Response.status(Response.Status.CONFLICT).build();
    }

    @Path("remove")
    @DELETE
    @Consumes({"application/json", "application/xml"})
    public Response removeDrone(Drone targetDrone){
        System.out.println("\nremoveDrone " + targetDrone.toString());
        if (SmartCity.getInstance().removeByObject(targetDrone)){
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Path("receive")
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response addStats(GlobalStats newStats){
        System.out.println("\naddStats " + newStats.toString());
        StatisticsManager.getInstance().add(newStats);
        StatisticsManager.getInstance().printStatsList();
        return Response.ok().build();
    }

}
