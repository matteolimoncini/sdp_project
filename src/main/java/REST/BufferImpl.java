package REST;

import SimulatorPm10.Buffer;
import SimulatorPm10.Measurement;

import java.util.ArrayList;
import java.util.List;

public class BufferImpl implements Buffer {
    private List<Measurement> measurementList = new ArrayList<>();
    @Override
    public synchronized void addMeasurement(Measurement m) {
        measurementList.add(m);
        if(measurementList.size()==8){
            this.notify();
        }
    }

    @Override
    public synchronized List<Measurement> readAllAndClean() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Measurement> returnList= new ArrayList<>(measurementList);

        int size = measurementList.size();
        measurementList = measurementList.subList(0, size/2);

        /*for (int i = 0; i < measurementList.size(); i++) {
            Measurement m = measurementList.get(i);
            if (i>measurementList.size()/2){
                measurementList.remove(m);
            }
        }
         */

        return returnList;
    }
}
