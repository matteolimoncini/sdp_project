package REST.services;

import REST.beans.Drone;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("statistics")
public class StatisticsService {

    @Path("globals/{lastN}")
    @GET
    @Consumes({"application/json", "application/xml", "text/plain"})
    @Produces({"application/json"})
    /*
     Method to extract the last n global statistics with timestamp about smart-city
    */
    public Response getGlobal(Integer lastN) {
        // TODO implement this
        return Response.ok().entity("{\"message\": \"Not implemented\"}").build();
    }

    //insert timestamp t1 and t2 like params
    @Path("delivery/avg")
    @GET
    @Consumes({"application/json"})
    @Produces({"application/json"})
    /*
     Method to extract the average number of delivery by all drones in the smart city between two timestamp.
     It works with a GET request http.

     Server receive two timestamp t1 and t2.
    */
    public Response deliveryAvg() {//@PathParam("time1") Integer idDrone

        // TODO implement this
        return Response.ok().build();
    }

    @Path("kilometers/avg")
    //insert timestamp t1 and t2 like params
    @GET
    @Consumes({"application/json"})
    @Produces({"application/json"})
    /*
     Method to extract the average number of kilometers covered by all drones in the smart city between two timestamp.
     It works with a GET request http.

     Server receive two timestamp t1 and t2.
    */
    public Response kilometersAvg() {//@PathParam("time1") Integer idDrone

        // TODO implement this
        return Response.ok().build();
    }


}
