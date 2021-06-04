package DroneThreads;

import REST.beans.Drone;
import org.eclipse.paho.client.mqttv3.MqttException;

public class DroneManageOrderThread extends Thread {
    private Drone drone;
    @Override
    public void run() {
        super.run(); //TODO implement this
    }

    public DroneManageOrderThread(Drone drone) {
        try {
            this.drone.subscribe();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
