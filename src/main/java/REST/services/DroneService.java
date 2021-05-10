package REST.services;

import REST.beans.Drone;
import REST.beans.DroneList;
import REST.beans.Position;
import REST.beans.ResponseAddModel;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("drone")
public class DroneService {
    @GET
    @Produces({"application/json", "application/xml"})
    /*
     Method to return the list of drones into the system with a GET request http.
    */
    public Response getAllDrones() {
        return Response.ok(DroneList.getInstance()).build();
    }

    @Path("add")
    @POST
    @Consumes({"application/json", "application/xml"})
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
        Position myPosition = new Position();
        if (droneNotFound) {
            List<Drone> droneList = DroneList.getInstance().getDrones();
            DroneList.getInstance().add(droneToAdd);
            ResponseAddModel responseAddModel = new ResponseAddModel(droneList, myPosition);
            return Response.ok(responseAddModel).build();

        } else
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\": \"Drone id not valid! Exist another drone with this id\"}").build();
    }
/*
    private int[] setPosition() {
        Random r = new Random();
        int randIntX = r.nextInt(10);
        int randIntY = r.nextInt(10);
        return new int[]{randIntX,randIntY};
    }
*/

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
        } else
            DroneList.getInstance().deleteDrone(idDroneToRemove);
        return Response.ok().entity("{\"message\": \"Drone removed from the system\"}").build();
    }

}
