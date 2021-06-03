package REST.beans;

import com.example.grpc.Election;

import com.example.grpc.Election.message;
import com.example.grpc.electionGrpc;
import com.example.grpc.electionGrpc.electionStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class ElectionThread extends Thread {
    private Drone drone;

    public ElectionThread(Drone drone) {
        this.drone = drone;
    }

    @Override
    public void run() {

        //marca come partecipante
        drone.setPartecipant(true);
        System.out.println(drone.getIdDrone()+" setted as partecipant");

        //invia al successivo un msg ELECTION,<id>
        System.out.println("ID DRONE: "+drone.getIdDrone());
        System.out.println("drones:"+drone.getDrones());
        Drone nextDroneInRing = drone.getNextInRing();
        assert nextDroneInRing!= null;
        System.out.println(drone.getIdDrone()+" has next id:" +nextDroneInRing.getIdDrone());
        String targetAddress = nextDroneInRing.getIpAddress() + ":" + nextDroneInRing.getPortNumber();
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(targetAddress).usePlaintext().build();
        electionStub stub = electionGrpc.newStub(channel);
        message request = message
                .newBuilder()
                .setType("ELECTION")
                .setIdDrone(drone.getIdDrone())
                .build();
        stub.election(request, new StreamObserver<message>() {
            @Override
            public void onNext(message value) {
                System.out.println("type drone" + value.getType() + "id drone:" + value.getIdDrone());

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                channel.shutdown();
            }
        });
    }

}
