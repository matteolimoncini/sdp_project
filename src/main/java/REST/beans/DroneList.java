package REST.beans;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DroneList {
    private static DroneList instance;
    @Expose
    private final List<Drone> drones;

    private DroneList() {
        drones = new ArrayList<>();
    }

    //singleton to return the instance of droneList
    public synchronized static DroneList getInstance() {
        if (instance==null)
            instance = new DroneList();
        return instance;
    }
    public synchronized void add(Drone droneToAdd) {
        drones.add(droneToAdd);
        drones.sort(Comparator.comparing(Drone::getIdDrone));
    }

    public synchronized List<Drone> getDrones() {
        return new ArrayList<>(drones);
    }

    public synchronized void deleteDrone(int idDroneToRemove) {
        for (int i = 0; i < drones.size(); i++) {
            if (drones.get(i).getIdDrone() == idDroneToRemove)
                drones.remove(i);
        }
    }

    public synchronized int checkDrone(int idDroneToCheck) {
        Drone drone;
        List<Drone> copy = getDrones();
        for (int i = 0; i < copy.size(); i++) {
            drone = copy.get(i);
            int idDrone = drone.getIdDrone();
            if (idDrone == idDroneToCheck)
                return i;
        }
        return -1;
    }
}
