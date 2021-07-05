package DroneThreads;

import REST.beans.Drone;
import com.example.grpc.Election.message;
import com.example.grpc.electionGrpc;
import com.example.grpc.electionGrpc.electionStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class ElectionThread extends Thread {
    private Drone drone;

    public ElectionThread(Drone drone) {
        this.drone = drone;
    }

    @Override
    public void run() {

        drone.setParticipant(true);
        //System.out.println(drone.getIdDrone()+" setted as partecipant");
        //System.out.println("ID DRONE: "+drone.getIdDrone());
        //System.out.println("drones:"+drone.getDrones());
        Drone nextDroneInRing = drone.getNextInRing();
        assert nextDroneInRing != null;
        //System.out.println(drone.getIdDrone()+" has next id:" +nextDroneInRing.getIdDrone());
        String targetAddress = nextDroneInRing.getIpAddress() + ":" + nextDroneInRing.getPortNumber();
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(targetAddress).usePlaintext().build();
        electionStub stub = electionGrpc.newStub(channel);
        drone.setElectionInProgress(true);
        message request = message
                .newBuilder()
                .setType("ELECTION")
                .setIdDrone(drone.getIdDrone())
                .setBatteryDrone(drone.getBattery())
                .build();
        stub.election(request, new StreamObserver<message>() {
            @Override
            public void onNext(message value) {

            }

            @Override
            public void onError(Throwable t) {
                channel.shutdown();
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
