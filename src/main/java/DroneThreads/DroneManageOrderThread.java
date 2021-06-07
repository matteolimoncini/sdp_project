package DroneThreads;

import REST.beans.Drone;
import REST.beans.Order;
import org.eclipse.paho.client.mqttv3.MqttException;

public class DroneManageOrderThread extends Thread {
    private Drone drone;
    @Override
    public void run() {
        try {
            this.drone.subscribeAndPutInQueue();

            Order firstPendingOrder;
            while(true){
                if(this.drone.areTherePendingOrders()){
                    firstPendingOrder = this.drone.getFirstPendingOrder();
                    Drone droneChoosen = this.drone.chooseDeliver(firstPendingOrder);
                    if (droneChoosen != null){
                        this.drone.removePendingOrder(firstPendingOrder);
                        droneChoosen.manageOrder(firstPendingOrder);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DroneManageOrderThread(Drone drone) {
        this.drone = drone;

    }
}
