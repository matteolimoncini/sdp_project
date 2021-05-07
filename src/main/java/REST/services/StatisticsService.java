package REST.services;

import REST.beans.Drone;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("drone")
public class StatisticsService {

    @Path("add")
    @POST
    @Consumes({"application/json", "application/xml", "text/plain"})
    @Produces({"application/json"})
    /*
     Method to add drone into the system with a POST request http.
     Server receive ID, ip address and port number.

     If ID is not already used insert drone into the system.
     If ID is already in use throw exception.
     If insert successfully return drone's list present in the city.

    */
    public Response addDrone(Drone d) {
        // TODO implement this
        return Response.ok().entity("{\"message\": \"Parola inserita\"}").build();
        //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\": \"Parola già inserita\"}").build();
    }


    @Path("delete/{idDrone}")
    @DELETE
    @Consumes({"application/json"})
    @Produces({"application/json"})
    /*
     Method to remove drone from the system with a DELETE request http.
     Server receive drone ID.

     If ID is already used remove drone from the system.
     If ID not in list throw exception.

    */
    public Response removeDrone(@PathParam("idDrone") Integer idDrone) {
        // TODO implement this
        return Response.ok().build();
    }

}
