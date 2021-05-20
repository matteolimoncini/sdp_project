package REST.beans;

import com.example.grpc.AddDrone;
import com.example.grpc.newDroneGrpc;
import com.example.grpc.newDroneGrpc.newDroneStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import static com.example.grpc.AddDrone.*;

public class sendNewDroneThread extends Thread {
    private int idNewDrone;
    private String ipNewDrone;
    private int portNewDrone;
    private String message;

    public sendNewDroneThread(int idNewDrone, String ipNewDrone, int portNewDrone, String message) {
        this.idNewDrone = idNewDrone;
        this.ipNewDrone = ipNewDrone;
        this.portNewDrone = portNewDrone;
        this.message = message;
    }

    @Override
    public void run() {
        String targetAddress = this.ipNewDrone+":"+this.portNewDrone;
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(targetAddress).usePlaintext().build();
        newDroneStub stub = newDroneGrpc.newStub(channel);
        addNewDrone request = addNewDrone
                .newBuilder()
                .setIdDrone(this.idNewDrone)
                .setIpAddress(this.ipNewDrone)
                .setPortNumber(this.portNewDrone)
                .setMessage(this.message)
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
