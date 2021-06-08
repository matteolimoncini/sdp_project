import DroneThreads.*;
import REST.beans.*;

import java.util.List;

public class DroneMain {
    public static void main(String[] args) {
        Drone drone = new Drone(3, "localhost", 1103, "localhost", 1337);
        System.out.println("i am drone: "+drone.getIdDrone());
        drone.addDrone();

        /*
        - sempre essere in attesa che io scriva quit sul terminale
        - effettuare una consegna
        - inviare global statistics al server (se master)
        - elezione master
        */


        //when we add a new drone we start a thread that wait that user type "quit" and exit
        Thread quitThread;
        Thread sendGlobalStatsThread;
        Thread manageOrderThread;
        Thread sendNewDroneAdded;
        Thread serverThread;
        Thread pingThread;
        Thread manageOrder;
        List<Drone> drones = drone.getDrones();

        //start grpc server thread
        serverThread = new ServerGrpcThread(drone);
        serverThread.start();

        //start a thread that wait that user type "quit" and exit
        quitThread = new DroneThreadQuit();
        quitThread.start();



        if (drones != null) {
            if (drones.size() == 0) {
                //i am alone in the system, i'm master
                drone.setIdMaster(drone.getIdDrone());
                System.out.println("i am master");
            } else {
                //find the master
                //send a message to all other drone that i'm entering in the system
                for (Drone droneReceiver : drones) {
                    sendNewDroneAdded = new sendNewDroneAddedThread(droneReceiver.getIpAddress(), droneReceiver.getPortNumber(), "new drone added", drone);
                    sendNewDroneAdded.start();
                }
            }

        }else{
            drone.setIdMaster(drone.getIdDrone());
            System.out.println("i am master");
        }

        pingThread = new PingThread(drone);
        pingThread.start();



        //start a thread that send global stats if this drone is master
        sendGlobalStatsThread = new DroneGlobalStatsThread(drone);
        //sendGlobalStatsThread.start();


        //manageOrderThread = new DroneManageOrderThread(drone);

        System.out.println("before while");

        manageOrder = new DroneManageOrderThread(drone);


        while (quitThread.isAlive()) {

            if(drone.isMaster() && !manageOrder.isAlive()) {
                System.out.println("waiting that all drone sent their position...");
                while (drone.isMaster() && drone.getDrones()!= null && (drone.getCountPosition() < drone.getDrones().size())) {
                    assert true;
                }
                System.out.println("received all positions");

                drone.setCountPosition(0);
                manageOrder.start();
            }


            //send global stats if is master and thread to send global stats is crashed
            if (!sendGlobalStatsThread.isAlive() && drone.isMaster()){
                assert true;
                //sendGlobalStatsThread.start();
            }

            //manage one order


        }
        System.out.println("fuori while");
        //TODO ?
        //drone.sendGlobalStatistics();

        //manage exit from the system

        //manage current order

        //if is master disconnect from broker mqtt

        //if is master manage pending orders

        //close communication channels with others drones
        serverThread.stop();
        //if is master send global stats to server

        //ask to exit to server
        drone.removeDrone();
        //close all thread
        System.exit(0);
    }
}
