package DroneThreads;

import REST.beans.Drone;

public class DroneGlobalStatsThread extends Thread {
    private volatile boolean stopCondition = false;
    private Drone drone;

    public DroneGlobalStatsThread(Drone drone) {
        this.drone = drone;
    }

    public void stopMeGently() {
        stopCondition = true;
    }

    @Override
    public void run() {
        if (!drone.isMaster())
            return;

        try {
            while (!stopCondition) {
                drone.sendGlobalStatistics();
                Thread.sleep(1000 * 10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
