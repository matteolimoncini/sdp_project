package REST.beans;

import com.example.grpc.Election;

import com.example.grpc.electionGrpc;
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

        //invia al successivo un msg ELECTION,<id>
        String targetAddress = drone.getNextInRing().getIpAddress() + ":" + drone.getNextInRing().getPortNumber();
        final ManagedChannel channel = ManagedChannelBuilder.forTarget(targetAddress).usePlaintext().build();
        electionGrpc.electionStub stub = electionGrpc.newStub(channel);
        Election.message request = Election.message
                .newBuilder()
                .setType("ELECTION")
                .setIdDrone(drone.getIdDrone())
                .build();
        stub.election(request, new StreamObserver<Election.message>() {
            @Override
            public void onNext(Election.message value) {
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
