package DroneThreads;

import REST.BufferImpl;
import REST.beans.Drone;
import SimulatorPm10.Measurement;

import java.util.List;

public class PollutionThread extends Thread{
    private Drone drone;
    private BufferImpl buffer;

    public PollutionThread(Drone drone, BufferImpl buffer) {
        this.drone = drone;
        this.buffer=buffer;
    }


    @Override
    public void run() {
        while (true) {
             List<Measurement> measurementList= buffer.readAllAndClean();

            //put in local avg of this values
            double sum = 0;
            for (Measurement m:measurementList) {
                sum += m.getValue();
            }
            double avg =sum/measurementList.size();
            drone.addToMeasurementList(avg);
        }
    }
}
