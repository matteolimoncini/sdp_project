package GrpcImpl;

import REST.beans.Drone;
import com.example.grpc.*;
import io.grpc.stub.StreamObserver;

import REST.beans.Drone;

import com.example.grpc.pingServiceGrpc.pingServiceImplBase;
import io.grpc.stub.StreamObserver;
import com.example.grpc.SendPosition;
import com.example.grpc.SendPosition.responsePosition;

public class PositionDroneImpl extends positionServiceGrpc.positionServiceImplBase {
        private Drone myDrone;
        public PositionDroneImpl(Drone myDrone) {
            this.myDrone = myDrone;
        }

    @Override
    public void positionDrone(SendPosition.position request, StreamObserver<SendPosition.responsePosition> responseObserver) {
        super.positionDrone(request, responseObserver);
        System.out.println("message received. idDrone: "+request.getIdDrone()+"position: "+request.getXPosition()+":"+request.getYPosition());
        myDrone.addCountPosition();
        responseObserver.onCompleted();
    }

}
