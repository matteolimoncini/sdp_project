package REST.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GlobalStats {
    private int avgDelivery;
    private int avgKilometers;
    private int avgPollution;
    private int avgBattery;
    private int timestamp;

    private static GlobalStats instance;

    public GlobalStats() {
    }

    public GlobalStats(int avgDelivery, int avgKilometers, int avgPollution, int avgBattery, int timestamp) {
        this.avgDelivery = avgDelivery;
        this.avgKilometers = avgKilometers;
        this.avgPollution = avgPollution;
        this.avgBattery = avgBattery;
        this.timestamp = timestamp;
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

    public int getTimestamp() {
        return timestamp;
    }

    public void setGlobalStats(GlobalStats globalStats) {
        this.avgDelivery = globalStats.getAvgDelivery();
        this.avgKilometers = globalStats.getAvgKilometers();
        this.avgPollution = globalStats.getAvgPollution();
        this.avgBattery = globalStats.getAvgBattery();
        this.timestamp = globalStats.getTimestamp();
    }
}
