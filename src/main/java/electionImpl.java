import REST.beans.Drone;
import com.example.grpc.Election;
import com.example.grpc.electionGrpc;
import com.example.grpc.electionGrpc.electionImplBase;
import io.grpc.stub.StreamObserver;

public class electionImpl extends electionImplBase{
    private Drone drone;
    public electionImpl(Drone drone) {
        this.drone = drone;
    }

    @Override
    public void election(Election.message request, StreamObserver<Election.message> responseObserver) {
        System.out.print("message received: "+request.getType()+" from: "+request.getIdDrone());
    }
}
