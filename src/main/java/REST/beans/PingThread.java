package REST.beans;

import com.example.grpc.AddDrone;
import com.example.grpc.Ping;
import com.example.grpc.newDroneGrpc;
import com.example.grpc.pingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.List;

public class PingThread extends Thread {
    private Drone drone;

    public PingThread(Drone drone) {
        this.drone = drone;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(10 * 1000);

                List<Drone> drones = this.drone.getDrones();
                if (drones == null)
                    continue;
                for (Drone d : drones) {
                    String ipReceiverDrone = d.getIpAddress();
                    int portReceiverDrone = d.getPortNumber();
                    String targetAddress = ipReceiverDrone + ":" + portReceiverDrone;
                    System.out.println("target address:" + targetAddress);
                    final ManagedChannel channel = ManagedChannelBuilder.forTarget(targetAddress).usePlaintext().build();
                    pingServiceGrpc.pingServiceStub stub = pingServiceGrpc.newStub(channel);
                    Ping.ping request = Ping.ping
                            .newBuilder()
                            .setMessage("are u alive? send by "+this.drone.getIdDrone())
                            .build();

                    stub.pingDrones(request, new StreamObserver<Ping.responsePing>() {
                        @Override
                        public void onNext(Ping.responsePing value) {
                            System.out.println("PING:"+value);
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

