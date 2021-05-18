package REST.beans;

import com.sun.xml.txw2.annotation.XmlElement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
//implements Comparator<GlobalStats>, Comparable<GlobalStats>
public class GlobalStats {
    private int avgDelivery;
    private int avgKilometers;
    private int avgPollution;
    private int avgBattery;

    private Timestamp timestamp;

    public GlobalStats() {
    }

    public GlobalStats(int avgDelivery, int avgKilometers, int avgPollution, int avgBattery, String timestamp) {
        this.avgDelivery = avgDelivery;
        this.avgKilometers = avgKilometers;
        this.avgPollution = avgPollution;
        this.avgBattery = avgBattery;
        //String t = timestamp.substring(3,timestamp.length()-3);
        this.timestamp = Timestamp.valueOf(timestamp);
    }

    public int getAvgDelivery() {
        return avgDelivery;
    }

    public int getAvgKilometers() {
        return avgKilometers;
    }

    public int getAvgPollution() {
        return avgPollution;
    }

    public int getAvgBattery() {
        return avgBattery;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getTimestampAsString() {
        return timestamp.toString();
    }

    public void setGlobalStats(GlobalStats globalStats) {
        this.avgDelivery = globalStats.getAvgDelivery();
        this.avgKilometers = globalStats.getAvgKilometers();
        this.avgPollution = globalStats.getAvgPollution();
        this.avgBattery = globalStats.getAvgBattery();
        this.timestamp = globalStats.getTimestamp();
    }
/*
    @Override
    public int compareTo(GlobalStats o) {
        return this.getTimestamp().compareTo(o.getTimestamp());
    }

    @Override
    public int compare(GlobalStats o1, GlobalStats o2) {
        Timestamp t1 = o1.getTimestamp();
        Timestamp t2 = o2.getTimestamp();
        return t1.compareTo(t2);
    }

 */
}
