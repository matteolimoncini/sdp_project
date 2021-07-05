package DroneThreads;

import REST.beans.Drone;

public class PrintThread extends Thread{
    private final Drone drone;
    private boolean stopCondition;
    public PrintThread(Drone drone) {
        this.drone=drone;
        this.stopCondition=false;
    }

    @Override
    public void run() {
        while (!stopCondition){
            try {
                Thread.sleep(10*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("\nNum delivery: "+drone.getNumDelivery());
            System.out.printf("Kilometers travelled: %.2f",drone.getKmTravelled());
            System.out.println("\nBattery: "+ drone.getBattery()+"\n");
        }
    }

    public void stopMeGently() {
        this.stopCondition=true;
    }
}
