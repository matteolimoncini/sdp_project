package DroneThreads;

import REST.beans.Drone;

public class DroneManageOrderThread extends Thread {
    private Drone drone;
    @Override
    public void run() {
        super.run(); //TODO implement this
    }

    public DroneManageOrderThread(Drone drone) {
        this.drone = drone;
    }
}
