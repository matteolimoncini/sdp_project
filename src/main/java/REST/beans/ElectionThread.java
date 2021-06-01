package REST.beans;

public class ElectionThread extends Thread {
    private Drone drone;
    public ElectionThread(Drone drone) {
        this.drone=drone;
    }

    @Override
    public void run() {
        //marca come partecipante
        drone.setPartecipant(true);

        //invia al successivo un msg ELECTION,<id>


    }
}
