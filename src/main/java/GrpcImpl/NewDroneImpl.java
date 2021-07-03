package GrpcImpl;

import REST.beans.Drone;
import REST.beans.Position;
import com.example.grpc.AddDrone;
import com.example.grpc.AddDrone.responseAddNewDrone;
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
        droneToInsert.setMyPosition(new Position(request.getXPosition(),request.getYPosition()));
        this.myDrone.insertDroneInList(droneToInsert);

        //System.out.println("message received: "+request.getMessage());
        System.out.println("Inserted drone into the system with id "+request.getIdDrone());
        //System.out.println(request.getXPosition());
        //System.out.println(request.getYPosition());

        responseAddNewDrone response = responseAddNewDrone.newBuilder().setIdDroneMaster(this.myDrone.getIdMaster()).setElectionInProgress(this.myDrone.getElectionInProgress()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
