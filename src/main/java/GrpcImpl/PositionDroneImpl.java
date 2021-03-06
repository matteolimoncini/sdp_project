package GrpcImpl;

import REST.beans.Drone;
import REST.beans.Position;
import com.example.grpc.SendPosition;
import com.example.grpc.positionServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.List;

public class PositionDroneImpl extends positionServiceGrpc.positionServiceImplBase {
    private Drone myDrone;

    public PositionDroneImpl(Drone myDrone) {
        this.myDrone = myDrone;
    }

    @Override
    public void positionDrone(SendPosition.position request, StreamObserver<SendPosition.responsePosition> responseObserver) {
        //super.positionDrone(request, responseObserver);
        int xPosition = request.getXPosition();
        int yPosition = request.getYPosition();
        int idDrone = request.getIdDrone();
        //System.out.println("message received. idDrone: "+ idDrone +"position: "+ xPosition +":"+ yPosition);
        List<Drone> listDrones = myDrone.getDrones();
        for (Drone d : listDrones) {
            if (d.getIdDrone().equals(idDrone)) {
                d.setMyPosition(new Position(xPosition, yPosition));
                break;
            }
        }
        myDrone.addCountPosition();
        SendPosition.responsePosition response = SendPosition.responsePosition.newBuilder().setMessageResponse("").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
