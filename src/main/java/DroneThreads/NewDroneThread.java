package DroneThreads;

import REST.beans.Drone;
import com.example.grpc.newDroneGrpc;
import com.example.grpc.newDroneGrpc.newDroneStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

import static com.example.grpc.AddDrone.*;

public class NewDroneThread extends Thread {
    private final Drone myDrone;
    private final String ipReceiverDrone;
    private final int portReceiverDrone;
    private final String message;

    public NewDroneThread(String ipReceiverDrone, int portReceiverDrone, String message, Drone newDrone) {
        this.ipReceiverDrone = ipReceiverDrone;
        this.portReceiverDrone = portReceiverDrone;
        this.message = message;
        this.myDrone = newDrone;
    }

    @Override
    public void run() {
        String targetAddress = this.ipReceiverDrone + ":" + this.portReceiverDrone;
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(targetAddress).usePlaintext().build();
        newDroneStub stub = newDroneGrpc.newStub(channel);
        addNewDrone request = addNewDrone
                .newBuilder()
                .setIdDrone(this.myDrone.getIdDrone())
                .setIpAddress(this.myDrone.getIpAddress())
                .setPortNumber(this.myDrone.getPortNumber())
                .setMessage(this.message)
                .setXPosition(this.myDrone.getMyPosition().getxCoordinate())
                .setYPosition(this.myDrone.getMyPosition().getyCoordinate())
                .build();

        stub.messageAddDrone(request, new StreamObserver<responseAddNewDrone>() {
            @Override
            public void onNext(responseAddNewDrone value) {
                System.out.println("Id master:" + value.getIdDroneMaster() + " election in progress: " + value.getElectionInProgress());
                if (!value.getElectionInProgress())
                    myDrone.setIdMaster(value.getIdDroneMaster());

            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
                channel.shutdown();
            }
        });
        try {
            channel.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
