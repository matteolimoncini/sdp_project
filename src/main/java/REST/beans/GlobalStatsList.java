package REST.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GlobalStatsList {

    private List<GlobalStats> globalStatsList;
    private static GlobalStatsList instance;

    public GlobalStatsList() {
        globalStatsList = new ArrayList<>();
    }


    //singleton per ritornare l'istanza di globalStatsList
    public synchronized static GlobalStatsList getInstance() {
        if (instance==null)
            instance = new GlobalStatsList();
        return instance;
    }

    public synchronized void addGlobalStats(GlobalStats globalStats) {
        globalStatsList.add(globalStats);
    }
    public synchronized List<GlobalStats> getGlobalStatsList() {
        return new ArrayList<>(globalStatsList);
    }

    public synchronized List<GlobalStats> getGlobalStatsList(int lastN) {
        int length = globalStatsList.size();
        if (lastN > length)
            lastN = length;
        ArrayList<GlobalStats> globalStatsReturn = new ArrayList<>(globalStatsList.subList(globalStatsList.size() - lastN, globalStatsList.size()));
        Collections.reverse(globalStatsReturn);
        return globalStatsReturn;
    }

    public synchronized List<GlobalStats> getGlobalStatsList(String t1, String t2) {
        Timestamp iTimestamp;
        boolean greaterOrEqualThanT1;
        boolean lessOrEqualThanT2;
        System.out.println(t1);
        System.out.println(t2);
        Timestamp timestamp1 = Timestamp.valueOf(t1);
        Timestamp timestamp2 = Timestamp.valueOf(t2);
        List<GlobalStats> subList = new ArrayList<>();

        for (GlobalStats stats : globalStatsList) {
            iTimestamp = new Timestamp(stats.getTimestamp());
            greaterOrEqualThanT1 = iTimestamp.compareTo(timestamp1) >= 0;
            lessOrEqualThanT2 = iTimestamp.compareTo(timestamp2) <= 0;
            if (greaterOrEqualThanT1 && lessOrEqualThanT2)
                subList.add(stats);
        }
        return subList;
    }

    @Override
    public String toString() {
        return "GlobalStatsList{" +
                "globalStatsList=" + globalStatsList +
                '}';
    }
}
