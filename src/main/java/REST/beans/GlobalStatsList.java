package REST.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GlobalStatsList {
    private int avgDelivery;
    private int avgKilometers;
    private int avgPollution;
    private int avgBattery;
    private int timestamp;

    private List<GlobalStats> globalStatsList;
    private static GlobalStatsList instance;

    public GlobalStatsList() {
        globalStatsList = new ArrayList<GlobalStats>();
    }


    //singleton per ritornare l'istanza di globalStatsList
    public synchronized static GlobalStatsList getInstance() {
        if (instance==null)
            instance = new GlobalStatsList();
        return instance;
    }

    public void addGlobalStats(GlobalStats globalStats) {
        globalStatsList.add(globalStats);
    }
    public synchronized List<GlobalStats> getGlobalStatsList() {
        return new ArrayList<>(globalStatsList);
    }

    public synchronized List<GlobalStats> getGlobalStatsList(int lastN) {
        int length = globalStatsList.size();
        if (lastN > length)
            lastN = length;
        return new ArrayList<>(globalStatsList.subList(0,lastN));
    }
}
