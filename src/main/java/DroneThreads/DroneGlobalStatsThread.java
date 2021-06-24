package DroneThreads;

import REST.beans.Drone;

public class DroneGlobalStatsThread extends Thread {
    private Drone drone;

    public DroneGlobalStatsThread(Drone drone) {
        this.drone = drone;
    }

    @Override
    public void run() {
        if (!drone.isMaster())
            return;

        try {
            while (true) {
                drone.sendGlobalStatistics();
                Thread.sleep(1000 * 10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
