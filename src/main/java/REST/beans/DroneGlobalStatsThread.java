package REST.beans;

public class DroneGlobalStatsThread extends Thread {
    private Drone drone;

    public DroneGlobalStatsThread(Drone drone) {
        this.drone = drone;
    }

    @Override
    public void run() {
        super.run();
        /*
        try {
            while (true) {
                if (!drone.iAmMaster())
                    return;
                drone.sendGlobalStatistics();
                Thread.sleep(1000 * 10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

         */
    }
}
