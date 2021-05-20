import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ServerGrpcThread extends Thread {
    private int portNumber;
    public ServerGrpcThread(Integer portNumber) {
        this.portNumber = portNumber;
    }

    @Override
    public void run() {
        try {
            Server server = ServerBuilder
                .forPort(this.portNumber)
                .addService(new NewDroneImpl())
                .build();

            server.start();
            server.awaitTermination();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
