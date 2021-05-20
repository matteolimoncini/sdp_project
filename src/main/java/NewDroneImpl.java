import com.example.grpc.AddDrone;
import com.example.grpc.newDroneGrpc;
import com.example.grpc.newDroneGrpc.newDroneImplBase;
import io.grpc.stub.StreamObserver;

public class NewDroneImpl extends newDroneImplBase {
    @Override
    public void messageAddDrone(AddDrone.addNewDrone request, StreamObserver<AddDrone.responseAddNewDrone> responseObserver) {
        //server
        System.out.println(request.getMessage());
    }
}
