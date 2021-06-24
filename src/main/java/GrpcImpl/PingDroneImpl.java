package GrpcImpl;

import REST.beans.Drone;

import com.example.grpc.pingServiceGrpc.pingServiceImplBase;
import io.grpc.stub.StreamObserver;
import com.example.grpc.Ping;
import com.example.grpc.Ping.responsePing;
import com.example.grpc.pingServiceGrpc;

public class PingDroneImpl extends pingServiceImplBase{
    private Drone myDrone;
    public PingDroneImpl(Drone myDrone) {
        this.myDrone = myDrone;
    }

    @Override
    public void pingDrones(Ping.ping request, StreamObserver<responsePing> responseObserver) {

        //server
        System.out.println("message received: "+request.getMessage());

        Ping.responsePing response = Ping.responsePing.newBuilder().setResponse("I AM ALIVE! "+this.myDrone.getIdDrone()).build();
        responseObserver.onNext(response);
        //responseObserver.onCompleted();
    }
}
