package REST.services;

import REST.beans.Dictionary;
import REST.beans.Drone;
import REST.beans.Word;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("drone")
public class DroneService {
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getAllWords() {
        return Response.ok(Dictionary.getInstance()).build();
    }

    @Path("add")
    @POST
    @Consumes({"application/json", "application/xml", "text/plain"})
    /*
     Method to add drone into the system with a POST request http.
     Server receive ID, ip address and port number.

     If ID is not already used insert drone into the system.
     If insert successfully return drone's list present in the city.

    */
    public Response addDrone(Drone d) {
        // TODO implement this
        return Response.ok().entity("{\"message\": \"Parola inserita\"}").build();
        //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\": \"Parola già inserita\"}").build();
    }

    @Path("get/{word}")
    @GET
    @Produces({"text/plain"})
    public Response getWordDefinition(@PathParam("word") String word){
        return Response.ok().build();
    }

    @Path("delete/{idDrone}")
    @DELETE
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response removeDrone(@PathParam("idDrone") Integer idDrone) {
        // TODO implement this
        return Response.ok().build();
    }

}
