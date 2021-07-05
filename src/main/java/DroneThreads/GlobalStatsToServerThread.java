package DroneThreads;

import REST.beans.Drone;

public class GlobalStatsToServerThread extends Thread {
    private boolean stopCondition = false;
    private Drone drone;

    public GlobalStatsToServerThread(Drone drone) {
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
                drone.sendGlobalStatisticsToServer();
                Thread.sleep(1000 * 10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
