package REST.beans;

public class sendNewDroneThread extends Thread {
    private int idNewDrone;
    private String ipNewDrone;
    private int portNewDrone;
    private String message;

    public sendNewDroneThread(int idNewDrone, String ipNewDrone, int portNewDrone, String message) {
        this.idNewDrone = idNewDrone;
        this.ipNewDrone = ipNewDrone;
        this.portNewDrone = portNewDrone;
        this.message = message;
    }

    @Override
    public void run() {
        //TODO send here messagge
        // super.run();
    }
}
