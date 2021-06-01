import REST.beans.*;

import java.util.List;

public class DroneMain {
    public static void main(String[] args) {
        Drone drone = new Drone(13, "localhost", 1112, "localhost", 1337);
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
        Thread manageOrderThread;
        Thread sendNewDroneAdded;
        Thread serverThread;
        Thread sendFindMaster;
        List<Drone> drones = drone.getDrones();

        //start grpc server thread
        serverThread = new ServerGrpcThread(drone);
        serverThread.start();

        //send a message to all other drone that i'm entering in the system
        if (drones != null)
            for (Drone droneReceiver : drones) {
                sendNewDroneAdded = new sendNewDroneAddedThread(droneReceiver.getIpAddress(), droneReceiver.getPortNumber(), "new drone added", drone);
                sendNewDroneAdded.start();
            }

        if (drones != null) {
            if (drones.size() == 0) {
                //i am alone in the system, i'm master
                //TODO became master
                assert true;
            } else {
                //find the master
                Drone firstDrone = drones.get(0);
                sendFindMaster = new sendFindMasterThread(firstDrone.getIpAddress(),firstDrone.getPortNumber(),"who is master?",drone   );
            }

        }


        //start a thread that wait that user type "quit" and exit
        quitThread = new DroneThreadQuit();
        quitThread.start();

        //start a thread that send global stats if this drone is master
        sendGlobalStatsThread = new DroneGlobalStatsThread(drone);
        sendGlobalStatsThread.start();

        manageOrderThread = new DroneManageOrderThread(drone);


        while (quitThread.isAlive()) {


            //send global stats if is master and thread to send global stats is crashed
            if (!sendGlobalStatsThread.isAlive() && drone.iAmMaster())
                sendGlobalStatsThread.start();

            //manage one order


        }
        //manage exit from the system

        drone.sendGlobalStatistics();


    }
}
