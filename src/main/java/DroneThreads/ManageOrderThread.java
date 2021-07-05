package DroneThreads;

import REST.beans.Drone;
import REST.beans.Order;
import REST.beans.Position;
import com.example.grpc.PropagateOrder;
import com.example.grpc.PropagateOrder.propagateOrder;
import com.example.grpc.sendOrderGrpc;
import com.example.grpc.sendOrderGrpc.sendOrderStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.concurrent.TimeUnit;

public class ManageOrderThread extends Thread {
    private final Drone drone;
    private boolean stopCondition;

    public ManageOrderThread(Drone drone) {
        this.drone = drone;
        this.stopCondition=false;
    }

    @Override
    public void run() {
        try {
            //System.out.println("thread to manage order starting...");
            this.drone.subscribeAndPutInQueue();

            Order firstPendingOrder;
            while (!stopCondition) {
                if (this.drone.areTherePendingOrders()) {
                    //System.out.println("try to manage pending order...");
                    firstPendingOrder = this.drone.getFirstPendingOrder();
                    Drone droneChoosen = this.drone.chooseDeliver(firstPendingOrder);
                    if (droneChoosen != null) {
                        this.drone.removePendingOrder(firstPendingOrder);
                        droneChoosen.setProcessingDelivery(true);

                        Position pickUpPoint = firstPendingOrder.getPickUpPoint();
                        Position deliveryPoint = firstPendingOrder.getDeliveryPoint();
                        String targetAddress = droneChoosen.getIpAddress() + ":" + droneChoosen.getPortNumber();
                        final ManagedChannel channel = ManagedChannelBuilder.forTarget(targetAddress).usePlaintext().build();
                        sendOrderStub stub = sendOrderGrpc.newStub(channel);
                        propagateOrder request = propagateOrder
                                .newBuilder()
                                .setIdOrder(firstPendingOrder.getId())
                                .setXPositionPickup(pickUpPoint.getxCoordinate())
                                .setYPositionPickup(pickUpPoint.getyCoordinate())
                                .setXPositionDelivery(deliveryPoint.getxCoordinate())
                                .setYPositionDelivery(deliveryPoint.getyCoordinate())
                                .build();

                        stub.messagePropagateOrder(request, new StreamObserver<PropagateOrder.responseOrder>() {
                            @Override
                            public void onNext(PropagateOrder.responseOrder value) {

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

                } else {
                    if (this.drone.isQuit()) {
                        //System.out.println("no pending order and quit flag set as true");
                        this.stopCondition=true;
                    }
                }
            }
            //System.out.println("ending manage order thread...");

        }catch (MqttException e){
            System.err.println("Mqtt exception during disconnect");
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}
