package DroneThreads;

import REST.beans.Drone;

import java.util.Scanner;

public class QuitThread extends Thread {
    private Drone drone;
    private boolean stopCondition;

    public QuitThread(Drone drone) {
        this.drone = drone;
        this.stopCondition = false;
    }

    @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        String input;
        String quit = "quit";
        do {
            input = in.nextLine();
        } while (!input.equals(quit) && !stopCondition);
    }

    public void stopMeGently() {
        this.stopCondition=true;
    }
}
