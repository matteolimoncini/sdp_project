package DroneThreads;

import REST.beans.Drone;
import REST.beans.Position;
import com.example.grpc.SendPosition;
import com.example.grpc.SendPosition.position;
import com.example.grpc.positionServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PositionDroneThread extends Thread {
    private Drone drone;
    public PositionDroneThread(Drone drone) {
        this.drone = drone;
    }

    @Override
    public void run() {
        List<Drone> drones = drone.getDrones();
        int portNumberMaster = -1;
        String ipAddressMaster = null;

        for (Drone d:drones) {
            if(d.getIdDrone().equals(drone.getIdMaster()))
                portNumberMaster = d.getPortNumber();
            ipAddressMaster = d.getIpAddress();
        }
        Position myPosition = drone.getMyPosition();
        String addressMaster = ipAddressMaster + ":" + portNumberMaster;
        final ManagedChannel channelWithMaster = ManagedChannelBuilder.forTarget(addressMaster).usePlaintext().build();
        positionServiceGrpc.positionServiceStub stubWithMaster = positionServiceGrpc.newStub(channelWithMaster);
        position messagePosition = position
                .newBuilder()
                .setIdDrone(drone.getIdDrone())
                .setXPosition(drone.getMyPosition().getxCoordinate())
                .setYPosition(drone.getMyPosition().getyCoordinate())
                .build();

        stubWithMaster.positionDrone(messagePosition,new StreamObserver<SendPosition.responsePosition>() {

            @Override
            public void onNext(SendPosition.responsePosition value) {

            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
                channelWithMaster.shutdown();
            }
        });
        try {
            channelWithMaster.awaitTermination(10,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
