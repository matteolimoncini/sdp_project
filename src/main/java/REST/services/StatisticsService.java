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


    @Path("delivery/avg")
    //insert timestamp t1 and t2 like params
    @GET
    @Consumes({"application/json"})
    @Produces({"application/json"})
    /*
     Method to remove drone from the system with a DELETE request http.
     Server receive drone ID.

     If ID is already used remove drone from the system.
     If ID not in list throw exception.

    */

    public Response removeDrone() {//@PathParam("time1") Integer idDrone

        // TODO implement this
        return Response.ok().build();
    }


}
