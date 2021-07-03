package DroneThreads;

import GrpcImpl.*;
import REST.beans.Drone;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class ServerGrpcThread extends Thread {
    private Drone drone;
    private Server server;

    public ServerGrpcThread(Drone drone) {
        this.drone = drone;
    }

    @Override
    public void run() {

        try {
            this.server = ServerBuilder
                    .forPort(this.drone.getPortNumber())
                    .addService(new NewDroneImpl(this.drone))
                    .addService(new PingDroneImpl(this.drone))
                    .addService(new ElectionImpl(this.drone))
                    .addService(new PositionDroneImpl(this.drone))
                    .addService(new PropagateOrderImpl(this.drone))
                    .addService(new GlobalStatsMasterImpl(this.drone))
                    .build();

            this.server.start();
            this.server.awaitTermination();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMeGently() {
        this.server.shutdownNow();
    }
}
