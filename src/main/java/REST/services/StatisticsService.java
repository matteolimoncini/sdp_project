package REST.services;

import REST.beans.AvgStatisticsModel;
import REST.beans.ExceptionModel;
import REST.beans.GlobalStats;
import REST.beans.GlobalStatsList;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
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
        //System.out.println(globalStats.getAvgDelivery());
        //System.out.println(globalStats.getAvgKilometers());
        //System.out.println(globalStats.getAvgPollution().get(0));
        //System.out.println(globalStats.getAvgBattery());
        //System.out.println(globalStats.getTimestamp());
        try {
            GlobalStatsList.getInstance().addGlobalStats(globalStats);
            return Response.ok().build();//.entity("{\"message\": \"Global statistics added\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ExceptionModel("Generic exception:" + e)).build();
        }
    }

    @Path("globals/{lastN}")
    @GET
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json"})
    /*
     Method to extract the last n global statistics with timestamp about smart-city
    */
    public Response getGlobal(@PathParam("lastN") Integer lastN) {
        try {
            List<GlobalStats> globalStatsList = GlobalStatsList.getInstance().getGlobalStatsList(lastN);
            GenericEntity<List<GlobalStats>> output = new GenericEntity<List<GlobalStats>>(globalStatsList) {
            };
            //System.out.println(globalStatsList.toString());
            //System.out.println(output.toString());
            return Response.ok(output).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ExceptionModel("Generic exception:" + e)).build();
        }
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
    public Response deliveryAvgGet(@QueryParam("t1") String t1, @QueryParam("t2") String t2) {
        try {

            double avgDelivery;
            int sumDelivery = 0;
            List<GlobalStats> globalStatsList;

            globalStatsList = GlobalStatsList.getInstance().getGlobalStatsList(t1, t2);

            for (GlobalStats globalStats : globalStatsList)
                sumDelivery += globalStats.getAvgDelivery();

            avgDelivery = (double) sumDelivery / globalStatsList.size();
            return Response.ok().entity(new AvgStatisticsModel(avgDelivery)).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ExceptionModel("Timestamp format not correct")).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ExceptionModel("Generic exception:" + e)).build();
        }
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
    public Response kilometersAvg(@QueryParam("t1") String t1, @QueryParam("t2") String t2) {
        try {

            double avgKilometers;
            int sumKilometers = 0;
            List<GlobalStats> globalStatsList = null;
            globalStatsList = GlobalStatsList.getInstance().getGlobalStatsList(t1, t2);
            for (GlobalStats globalStats : globalStatsList)
                sumKilometers += globalStats.getAvgKilometers();

            avgKilometers = (double) sumKilometers / globalStatsList.size();
            return Response.ok().entity(new AvgStatisticsModel(avgKilometers)).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ExceptionModel("Timestamp format not correct")).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ExceptionModel("Generic exception:" + e)).build();
        }
    }


}
