package REST.services;

import REST.beans.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            String input = gson.toJson(DroneList.getInstance());
            return Response.ok().entity(input).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ExceptionModel("Generic exception:" + e)).build();
        }
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
        try {
            System.out.print("Added drone. Id: " + droneToAdd.getIdDrone() + ", address: " + droneToAdd.getIpAddress() + ":" + droneToAdd.getPortNumber() + ", ");

            int pos = DroneList.getInstance().checkDrone(droneToAdd.getIdDrone());
            boolean droneNotFound = pos == -1;
            Position myPosition = new Position();
            if (droneNotFound) {
                List<Drone> droneList = DroneList.getInstance().getDrones();
                DroneList.getInstance().add(droneToAdd);
                ResponseAddModel responseAddModel = new ResponseAddModel(droneList, myPosition);
                Position dronePosition = responseAddModel.getMyPosition();
                System.out.println("position: (" + dronePosition.getxCoordinate() + "," + dronePosition.getyCoordinate() + ")");
                if (droneList == null || droneList.isEmpty()) {
                    System.out.println("There aren't other drones in the systems");
                } else {
                    System.out.print("Other drones in the systems are: ");
                    for (int i = 0; i < droneList.size(); i++) {
                        Drone d = droneList.get(i);
                        System.out.print(d.getIdDrone());
                        if (i < droneList.size() - 1) {
                            System.out.print(", ");
                        }
                        if (i == droneList.size() - 1) {
                            System.out.print("\n");
                        }
                    }
                }
                return Response.ok().entity(responseAddModel).build();

            } else
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ExceptionModel("Drone id not valid! Exist another drone with this id")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ExceptionModel("Generic exception:" + e)).build();
        }
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
        try {
            int pos = DroneList.getInstance().checkDrone(idDroneToRemove);
            boolean droneNotFound = pos == -1;
            if (droneNotFound) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ExceptionModel("Drone id not valid! Not exist drone with this id")).build();
            } else
                DroneList.getInstance().deleteDrone(idDroneToRemove);
            return Response.ok().entity("{\"message\": \"Drone removed from the system\"}").build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ExceptionModel("Generic exception:" + e)).build();
        }
    }

}
