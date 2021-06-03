package DroneThreads;

import GrpcImpl.NewDroneImpl;
import GrpcImpl.PingDroneImpl;
import GrpcImpl.electionImpl;
import REST.beans.Drone;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class ServerGrpcThread extends Thread {
    private Drone drone;

    public ServerGrpcThread(Drone drone) {
        this.drone = drone;
    }

    @Override
    public void run() {
        try {
            Server server = ServerBuilder
                    .forPort(this.drone.getPortNumber())
                    .addService(new NewDroneImpl(this.drone))
                    .addService(new PingDroneImpl(this.drone))
                    .addService(new electionImpl(this.drone))
                    .build();

            server.start();
            server.awaitTermination();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
