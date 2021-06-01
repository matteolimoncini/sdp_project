package REST.beans;

import com.example.grpc.newDroneGrpc;
import com.example.grpc.newDroneGrpc.newDroneStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import static com.example.grpc.AddDrone.*;

public class sendNewDroneAddedThread extends Thread {
    private Drone newDrone;
    private String ipReceiverDrone;
    private int portReceiverDrone;
    private String message;

    public sendNewDroneAddedThread(String ipReceiverDrone, int portReceiverDrone, String message, Drone newDrone) {
        this.ipReceiverDrone = ipReceiverDrone;
        this.portReceiverDrone = portReceiverDrone;
        this.message = message;
        this.newDrone = newDrone;
    }

    @Override
    public void run() {
        String targetAddress = this.ipReceiverDrone +":"+this.portReceiverDrone;
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(targetAddress).usePlaintext().build();
        newDroneStub stub = newDroneGrpc.newStub(channel);
        addNewDrone request = addNewDrone
                .newBuilder()
                .setIdDrone(this.newDrone.getIdDrone())
                .setIpAddress(this.newDrone.getIpAddress())
                .setPortNumber(this.newDrone.getPortNumber())
                .setMessage(this.message)
                .setXPosition(this.newDrone.getMyPosition().getxCoordinate())
                .setYPosition(this.newDrone.getMyPosition().getyCoordinate())
                .build();

        stub.messageAddDrone(request, new StreamObserver<responseAddNewDrone>() {
            @Override
            public void onNext(responseAddNewDrone value) {
                System.out.println(value.getResponse());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {

            }
        });
    }
}
