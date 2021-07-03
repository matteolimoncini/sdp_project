import DroneThreads.*;
import REST.BufferImpl;
import REST.beans.*;
import SimulatorPm10.PM10Simulator;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.List;

public class DroneMain {
    public static void main(String[] args) {
        Drone drone = new Drone(3, "localhost", 8333, "localhost", 1337);
        System.out.println("I am drone: "+drone.getIdDrone());
        drone.addDrone();

        //when we add a new drone we start a thread that wait that user type "quit" and exit
        Thread quitThread;
        GlobalStatsToServerThread sendGlobalStatsThread;
        Thread sendNewDroneAdded;
        ServerGrpcThread serverThread;
        PingThread pingThread;
        Thread manageOrder;
        PM10Simulator pm10Sim;

        List<Drone> drones = drone.getDrones();

        //start grpc server thread
        serverThread = new ServerGrpcThread(drone);
        serverThread.start();

        //start a thread that wait that user type "quit" and exit
        quitThread = new QuitThread(drone);
        quitThread.start();

        if (drones != null) {
            if (drones.size() == 0) {
                //i am alone in the system, i'm master
                drone.setIdMaster(drone.getIdDrone());
                System.out.println("I am master");
            } else {
                //find the master
                //send a message to all other drone that i'm entering in the system
                for (Drone droneReceiver : drones) {
                    sendNewDroneAdded = new NewDroneThread(droneReceiver.getIpAddress(), droneReceiver.getPortNumber(), "new drone added", drone);
                    sendNewDroneAdded.start();
                }
            }

        }else{
            drone.setIdMaster(drone.getIdDrone());
            System.out.println("I am master");
        }

        //start a thread to manage ping between drones
        pingThread = new PingThread(drone);
        pingThread.start();

        BufferImpl buffer = new BufferImpl();

        //start a thread to manage p10 simulator
        pm10Sim = new PM10Simulator(buffer);
        pm10Sim.start();

        //start a thread to manage pollution values
        PollutionThread pollutionThread = new PollutionThread(drone,buffer);
        pollutionThread.start();

        //start a thread that send global stats if this drone is master
        sendGlobalStatsThread = new GlobalStatsToServerThread(drone);

        //start a thread to manage orders
        manageOrder = new ManageOrderThread(drone);


        while (quitThread.isAlive() || drone.getElectionInProgress()) {
            if (drone.getBattery()<15){
                System.out.println("battery level is less than 15%");
                System.out.println("drone with id "+drone.getIdDrone()+" need to exit from the system");
                break;
            }

            if(drone.isMaster() && !manageOrder.isAlive()) {
                //System.out.println("waiting that all drone sent their position...");
                while (drone.isMaster() && drone.getDrones()!= null && (drone.getCountPosition() < drone.getDrones().size())) {
                    assert true;
                }
                //System.out.println("received all positions");
                drone.setCountPosition(0);
                manageOrder.start();
            }


            //send global stats if is master and thread to send global stats is crashed
            if (!sendGlobalStatsThread.isAlive() && drone.isMaster()){
                assert true;
                sendGlobalStatsThread.start();
            }

        }

        //manage exit from the system

        drone.setQuit(true);

        //if is master disconnect from broker mqtt
        if(drone.isMaster()){
            try {
                drone.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }

        }
        //System.out.println("disconnected mqtt");

        //if is master manage pending orders
        try {
            manageOrder.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Managed pending orders");


        //wait until current order is in progress

        try {
            synchronized (drone) {
                drone.wait();
            }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        /*while (drone.isProcessingDelivery()){
            assert true;
        }
         */

        //stop pollutions threads
        pm10Sim.stopMeGently();
        pollutionThread.stopMeGently();

        //close communication channels with others drones
        serverThread.stopMeGently();
        System.out.println("Communication channels with others drones closed");

        //if is master send global stats to server
        if(drone.isMaster()){
            drone.sendGlobalStatisticsToServer();
            System.out.println("Sent global statistics to server");
        }

        sendGlobalStatsThread.stopMeGently();
        //System.out.println("executed stop me gently");

        //ask to exit to server
        drone.removeDrone();

        //stop ping thread
        pingThread.stopMeGently();

        //close all thread
        System.exit(0);
    }
}
