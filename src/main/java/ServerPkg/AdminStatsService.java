package ServerPkg;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("StatsService")
public class AdminStatsService {

    @Path("isWorking")
    @GET
    @Produces("text/plain")
    public String helloWorld(){
        String response="StatsService working";
        System.out.println("\n" + response);
        return response;
    }

    @Path("list")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getDroneList(){
        System.out.println("\ngetDroneList");
        StatsServiceResponse ssr = new StatsServiceResponse();
        ssr.setDroneList(SmartCity.getInstance().getDroneList());
        System.out.println(ssr.toString());
        return Response.ok().entity(ssr).build();
    }

    @Path("last/{num}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getLastGlobalStats(@PathParam("num") int num){
        System.out.println("\ngetLastGlobalStats - " + String.valueOf(num));
        StatsServiceResponse ssr = new StatsServiceResponse();
        ssr.setLastGlobalStats(StatisticsManager.getInstance().getLastStats(num));
        System.out.println(ssr.toString());
        return Response.ok().entity(ssr).build();
    }

    @Path("averageDel/{a}/{b}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getAverageDel(@PathParam("a") long a, @PathParam("b") long b){
        System.out.println("\ngetAverageDel " + String.valueOf(a) + " " + String.valueOf(b));
        StatsServiceResponse ssr = new StatsServiceResponse();
        ssr.setAverage(StatisticsManager.getInstance().getAverageDeliveries(a, b));
        System.out.println(ssr.toString());
        return Response.ok().entity(ssr).build();
    }

    @Path("averageKm/{a}/{b}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getAverageKm(@PathParam("a") long a, @PathParam("b") long b){
        System.out.println("\ngetAverageKm " + String.valueOf(a) + " " + String.valueOf(b));
        StatsServiceResponse ssr = new StatsServiceResponse();
        ssr.setAverage(StatisticsManager.getInstance().getAverageKm(a, b));
        System.out.println(ssr.toString());
        return Response.ok().entity(ssr).build();
    }

}
