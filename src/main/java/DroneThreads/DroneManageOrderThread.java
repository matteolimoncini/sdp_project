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

public class DroneManageOrderThread extends Thread {
    private Drone drone;

    public DroneManageOrderThread(Drone drone) {
        this.drone = drone;

    }

    @Override
    public void run() {
        try {
            System.out.println("thread to manage order starting...");
            this.drone.subscribeAndPutInQueue();

            Order firstPendingOrder;
            while(true){
                if(this.drone.areTherePendingOrders()){
                    //System.out.println("try to manage pending order...");
                    firstPendingOrder = this.drone.getFirstPendingOrder();
                    Drone droneChoosen = this.drone.chooseDeliver(firstPendingOrder);
                    if (droneChoosen != null){
                        System.out.println("**droneChoosen != null**");
                        this.drone.removePendingOrder(firstPendingOrder);
                        droneChoosen.setProcessingDelivery(true);

                        Position pickUpPoint = firstPendingOrder.getPickUpPoint();
                        Position deliveryPoint = firstPendingOrder.getDeliveryPoint();
                        String targetAddress = droneChoosen.getIpAddress() +":"+droneChoosen.getPortNumber();
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
                                System.out.println("on next");
                            }

                            @Override
                            public void onError(Throwable t) {
                                System.err.print("on error! : ");
                                System.err.println(t.getMessage());
                            }

                            @Override
                            public void onCompleted() {
                                channel.shutdown();
                            }
                        });
                        //droneChoosen.manageOrder(firstPendingOrder);
                    }

                }
                else {
                    if(this.drone.isQuit()){
                        System.out.println("no pending order and quit flag set as true");
                        break;
                    }
                }
            }
            System.out.println("ending manage order thread...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
