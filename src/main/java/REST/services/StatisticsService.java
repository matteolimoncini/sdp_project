package REST.services;

import REST.beans.Drone;
import REST.beans.DroneList;
import REST.beans.GlobalStats;
import REST.beans.GlobalStatsList;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("statistics")
public class StatisticsService {

    @Path("globals")
    @POST
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json"})
    /*
     Method to add the global statistics about smart-city.
     Only master drone can add global statistic with POST request
    */
    public Response postGlobal(GlobalStats globalStats) {
        System.out.println(globalStats.getAvgDelivery());
        System.out.println(globalStats.getAvgKilometers());
        System.out.println(globalStats.getAvgPollution());
        System.out.println(globalStats.getAvgBattery());

        GlobalStatsList.getInstance().addGlobalStats(globalStats);
        return Response.ok().entity("{\"message\": \"Global statistics added\"}").build();
    }

    @Path("globals/{lastN}")
    @GET
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json"})
    /*
     Method to extract the last n global statistics with timestamp about smart-city
    */
    public Response getGlobal(@PathParam("lastN") Integer lastN) {
        List<GlobalStats> globalStatsList = GlobalStatsList.getInstance().getGlobalStatsList(lastN);
        return Response.ok(globalStatsList).build();
    }

    @Path("delivery/avg")
    @GET
    @Consumes({"application/json"})
    @Produces({"application/json"})
    /*
     Method to extract the average number of delivery by all drones in the smart city between two timestamp.
     It works with a GET request http.

     Server receive two timestamp t1 and t2.
    */
    public Response deliveryAvgGet(@QueryParam("t1") String t1,@QueryParam("t2") String t2) {

        // TODO implement this
        return Response.ok().build();
    }

    @Path("kilometers/avg")
    @GET
    @Consumes({"application/json"})
    @Produces({"application/json"})
    /*
     Method to extract the average number of kilometers covered by all drones in the smart city between two timestamp.
     It works with a GET request http.

     Server receive two timestamp t1 and t2.
    */
    public Response kilometersAvg(@QueryParam("t1") String t1,@QueryParam("t2") String t2) {
        System.out.println(t1);
        System.out.println(t2);
        // TODO implement this
        return Response.ok().build();
    }


}
