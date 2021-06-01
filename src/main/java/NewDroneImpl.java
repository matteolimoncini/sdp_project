import REST.beans.Drone;
import com.example.grpc.AddDrone;
import com.example.grpc.newDroneGrpc;
import com.example.grpc.newDroneGrpc.newDroneImplBase;
import io.grpc.stub.StreamObserver;

public class NewDroneImpl extends newDroneImplBase {
    private Drone myDrone;
    public NewDroneImpl(Drone myDrone) {
        this.myDrone = myDrone;
    }

    @Override
    public void messageAddDrone(AddDrone.addNewDrone request, StreamObserver<AddDrone.responseAddNewDrone> responseObserver) {

        Drone droneToInsert = new Drone(request.getIdDrone(),request.getIpAddress(),request.getPortNumber(),this.myDrone.getIpServerAdmin(),this.myDrone.getPortServerAdmin());
        this.myDrone.insertDroneInList(droneToInsert);

        //server
        System.out.println(request.getMessage());
        System.out.println(request.getIdDrone());
        System.out.println(request.getXPosition());
        System.out.println(request.getYPosition());


    }
}
