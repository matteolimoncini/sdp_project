package DroneThreads;

import DroneThreads.ElectionThread;
import REST.beans.Drone;
import com.example.grpc.Ping;
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
                    //System.out.println("inside ping thread loop for");
                    //System.out.println("id:"+d.getIdDrone());
                    String ipReceiverDrone = d.getIpAddress();
                    int portReceiverDrone = d.getPortNumber();
                    String targetAddress = ipReceiverDrone + ":" + portReceiverDrone;
                    //System.out.println("target address:" + targetAddress);
                    final ManagedChannel channel = ManagedChannelBuilder.forTarget(targetAddress).usePlaintext().build();
                    if (channel.isShutdown()) {
                        System.out.println("channel closed");
                    }
                    pingServiceGrpc.pingServiceStub stub = pingServiceGrpc.newStub(channel);
                    Ping.ping request = Ping.ping
                            .newBuilder()
                            .setMessage("are u alive? send by " + this.drone.getIdDrone())
                            .build();

                    stub.pingDrones(request, new StreamObserver<Ping.responsePing>() {
                        @Override
                        public void onNext(Ping.responsePing value) {
                            //System.out.println("PING:" + value);
                        }

                        @Override
                        public void onError(Throwable t) {
                            System.out.println("PING FAILED!");
                            drone.removeDroneFromList(d);
                            System.out.println("DRONE REMOVED");
                            if(drone.getDrones()!= null && drone.getDrones().size()>0) {
                                if (drone.getIdMaster().equals(d.getIdDrone())) {
                                    //start election
                                    System.out.println("Election starting...");
                                    Thread electionThread = new ElectionThread(drone);
                                    electionThread.start();
                                }
                            }
                            else{
                                System.out.println("i am alone in system, became master");
                                drone.setIdMaster(drone.getIdDrone());
                            }
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

