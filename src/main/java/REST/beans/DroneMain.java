package REST.beans;

public class DroneMain {
    public static void main(String[] args) {
        Drone drone = new Drone(111,"localhost",810);
        drone.addDrone();
        /*
        - sempre essere in attesa che io scriva quit sul terminale
        - effettuare una consegna
        - inviare global statistics al server (se master)
        - elezione master
        -
         */

        //when we add a new drone we start a thread that wait that user type "quit" and exit
        Thread quitThread;
        Thread sendGlobalStatsThread;

        quitThread = new DroneThreadQuit();
        quitThread.start();

        sendGlobalStatsThread = new DroneGlobalStatsThread(drone);
        sendGlobalStatsThread.start();

        while (quitThread.isAlive()){
            //send global stats
            if (!sendGlobalStatsThread.isAlive() && drone.iAmMaster())
                sendGlobalStatsThread.start();

            //manage one order



        }


        drone.sendGlobalStatistics();
    }
}
