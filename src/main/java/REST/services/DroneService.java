package REST.services;

import REST.beans.Drone;
import REST.beans.DroneList;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("drone")
public class DroneService {
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getAllWords() {
        return Response.ok(DroneList.getInstance()).build();
    }

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
    public Response addDrone(Drone droneToAdd) {
        int pos = DroneList.getInstance().checkDrone(droneToAdd.getId());
        boolean droneNotFound = pos == -1;
        if (droneNotFound) {
            DroneList.getInstance().add(droneToAdd);
            return Response.ok().entity("{\"message\": \"Drone added to the system\"}").build();
        }
        else
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\": \"Drone id not valid! Exist another drone with this id\"}").build();
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
    public Response removeDrone(@PathParam("idDrone") Integer idDroneToRemove) {
        int pos = DroneList.getInstance().checkDrone(idDroneToRemove);
        boolean droneNotFound = pos == -1;
        if (droneNotFound) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\": \"Drone id not valid! Not exist drone with this id\"}").build();
        }
        else
            DroneList.getInstance().deleteDrone(idDroneToRemove);
            return Response.ok().entity("{\"message\": \"Drone removed from the system\"}").build();
    }

}
