package REST.beans;

import java.util.Scanner;

public class DroneThreadQuit extends Thread{
    @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        String input;
        String quit = "quit";
        do{
            input = in.nextLine();
        }while(!input.equals(quit));
        System.exit(0);
    }
}
