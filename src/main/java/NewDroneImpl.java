import REST.beans.Drone;
import com.example.grpc.AddDrone;
import com.example.grpc.AddDrone.responseAddNewDrone;
import com.example.grpc.newDroneGrpc;
import com.example.grpc.newDroneGrpc.newDroneImplBase;
import io.grpc.stub.StreamObserver;

public class NewDroneImpl extends newDroneImplBase {
    private Drone myDrone;
    public NewDroneImpl(Drone myDrone) {
        this.myDrone = myDrone;
    }

    @Override
    public void messageAddDrone(AddDrone.addNewDrone request, StreamObserver<responseAddNewDrone> responseObserver) {

        Drone droneToInsert = new Drone(request.getIdDrone(),request.getIpAddress(),request.getPortNumber(),this.myDrone.getIpServerAdmin(),this.myDrone.getPortServerAdmin());
        this.myDrone.insertDroneInList(droneToInsert);

        //server
        System.out.println("message received: "+request.getMessage());
        System.out.println("inserted drone into the system: "+request.getIdDrone());
        //System.out.println(request.getXPosition());
        //System.out.println(request.getYPosition());

        responseAddNewDrone response = responseAddNewDrone.newBuilder().setIdDroneMaster(this.myDrone.getIdMaster()).build();
        responseObserver.onNext(response);
    }
}
