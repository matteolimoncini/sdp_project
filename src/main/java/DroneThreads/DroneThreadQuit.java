package DroneThreads;

import REST.beans.Drone;

import java.util.Scanner;

public class DroneThreadQuit extends Thread{
    private Drone drone;
    public DroneThreadQuit(Drone drone) {
        this.drone=drone;
    }

    @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        String input;
        String quit = "quit";
        do{
            input = in.nextLine();
        }while(!input.equals(quit));

    }
}
