package REST.beans;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DroneList {

    private List<Drone> drones;

    private static DroneList instance;

    private DroneList() {
        drones = new ArrayList<Drone>();
    }

    //singleton per ritornare l'istanza di droneList
    public synchronized static DroneList getInstance() {
        if (instance==null)
            instance = new DroneList();
        return instance;
    }
    public synchronized void add(Drone droneToAdd) {
        drones.add(droneToAdd);
    }

    public synchronized List<Drone> getDrones() {
        return new ArrayList<>(drones);
    }
    /*public Word getDefinition(String word) {
        List<Word> copy = getDrones();
        for (Word w: copy) {
            if (w.getWord().equals(word)) {
                return w;
            }
        }
        return null;
    }*/

    public synchronized void deleteDrone(Integer idDroneToRemove) {
        for (int i = 0; i < drones.size(); i++) {
            if (drones.get(i).getIdDrone().equals(idDroneToRemove))
                drones.remove(i);
        }
    }

    public synchronized int checkDrone(Integer idDroneToCheck) {
        Drone drone;
        List<Drone> copy = getDrones();
        for (int i = 0; i < copy.size(); i++) {
            drone = copy.get(i);
            Integer idDrone = drone.getIdDrone();
            if (idDrone.equals(idDroneToCheck))
                return i;
        }
        return -1;
    }

    /*public synchronized void updateDefinition(Word w) {
        int posParola = this.checkWord(w);
        drones.set(posParola, w);
    }*/
}
