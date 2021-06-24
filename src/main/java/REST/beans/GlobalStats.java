package REST.beans;

import com.google.gson.annotations.Expose;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import java.sql.Timestamp;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
//implements Comparator<GlobalStats>, Comparable<GlobalStats>
public class GlobalStats {

    private double avgDelivery;
    private double avgKilometers;
    private double avgPollution;
    private double avgBattery;

    private Timestamp timestamp;

    public GlobalStats() {
    }

    public GlobalStats(double avgDelivery, double avgKilometers, double avgPollution, double avgBattery, String timestamp) {
        this.avgDelivery = avgDelivery;
        this.avgKilometers = avgKilometers;
        this.avgPollution = avgPollution;
        this.avgBattery = avgBattery;
        //String t = timestamp.substring(3,timestamp.length()-3);
        this.timestamp = Timestamp.valueOf(timestamp);
    }
    public GlobalStats(double avgDelivery, double avgKilometers, double avgPollution, double avgBattery) {
        this.avgDelivery = avgDelivery;
        this.avgKilometers = avgKilometers;
        this.avgPollution = avgPollution;
        this.avgBattery = avgBattery;
        //String t = timestamp.substring(3,timestamp.length()-3);
        Date date = new Date();
        this.timestamp = new Timestamp(date.getTime());
    }
    public double getAvgDelivery() {
        return avgDelivery;
    }

    public double getAvgKilometers() {
        return avgKilometers;
    }

    public double getAvgPollution() {
        return avgPollution;
    }

    public double getAvgBattery() {
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

    public void setAvgDelivery(double avgDelivery) {
        this.avgDelivery = avgDelivery;
    }

    public void setAvgKilometers(double avgKilometers) {
        this.avgKilometers = avgKilometers;
    }

    public void setAvgPollution(double avgPollution) {
        this.avgPollution = avgPollution;
    }

    public void setAvgBattery(double avgBattery) {
        this.avgBattery = avgBattery;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
