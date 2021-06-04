package DroneThreads;

import REST.beans.Drone;
import REST.beans.Order;
import org.eclipse.paho.client.mqttv3.MqttException;

public class DroneManageOrderThread extends Thread {
    private Drone drone;
    @Override
    public void run() {
        super.run(); //TODO implement this
    }

    public DroneManageOrderThread(Drone drone) {
        try {
            this.drone.subscribeAndPutInQueue();

            Order firstPendingOrder;
            while(true){
                if(this.drone.areTherePendingOrders()){
                    firstPendingOrder = this.drone.getFirstPendingOrder();
                    Drone droneChoosen = this.drone.chooseDeliver(firstPendingOrder);
                    droneChoosen.manageOrder(firstPendingOrder);
                }
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
