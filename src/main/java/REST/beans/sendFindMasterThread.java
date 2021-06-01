package REST.beans;

import com.example.grpc.WhoIsMaster;
import com.example.grpc.WhoIsMaster.responseWhoIsMaster;
import com.example.grpc.findIdMasterGrpc;
import com.example.grpc.findIdMasterGrpc.findIdMasterStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import static com.example.grpc.WhoIsMaster.whoIsMaster;

public class sendFindMasterThread extends Thread {
    private Drone newDrone;
    private String ipReceiverDrone;
    private int portReceiverDrone;
    private String message;

    public sendFindMasterThread(String ipReceiverDrone, int portReceiverDrone, String message, Drone newDrone) {
        this.ipReceiverDrone = ipReceiverDrone;
        this.portReceiverDrone = portReceiverDrone;
        this.message = message;
        this.newDrone = newDrone;
    }

    @Override
    public void run() {
        String targetAddress = this.ipReceiverDrone +":"+this.portReceiverDrone;
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(targetAddress).usePlaintext().build();
        findIdMasterStub stub = findIdMasterGrpc.newStub(channel);
        whoIsMaster request = whoIsMaster
                .newBuilder()
                .setIdDrone(this.newDrone.getIdDrone())
                .setIpAddress(this.newDrone.getIpAddress())
                .setPortNumber(this.newDrone.getPortNumber())
                .setMessage(this.message)
                .build();

        stub.messageHowIsMaster(request, new StreamObserver<responseWhoIsMaster>() {
            @Override
            public void onNext(responseWhoIsMaster value) {
                System.out.println(value.getResponseIdMaster());
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
