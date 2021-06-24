package REST.beans;

import com.google.gson.annotations.Expose;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import java.sql.Timestamp;
import java.util.Date;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GlobalStats {

    private double avgDelivery;
    private double avgKilometers;
    private double avgPollution;
    private double avgBattery;
    private long timestamp;

    public GlobalStats() {
    }

    public GlobalStats(double avgDelivery, double avgKilometers, double avgPollution, double avgBattery, long timestamp) {
        this.avgDelivery = avgDelivery;
        this.avgKilometers = avgKilometers;
        this.avgPollution = avgPollution;
        this.avgBattery = avgBattery;
        //String t = timestamp.substring(3,timestamp.length()-3);
        this.timestamp = timestamp;
    }
    public GlobalStats(double avgDelivery, double avgKilometers, double avgPollution, double avgBattery) {
        this.avgDelivery = avgDelivery;
        this.avgKilometers = avgKilometers;
        this.avgPollution = avgPollution;
        this.avgBattery = avgBattery;
        //String t = timestamp.substring(3,timestamp.length()-3);
        Date date = new Date();
        this.timestamp = date.getTime();
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

    public long getTimestamp() {
        return timestamp;
    }

    public String getTimestampAsString() {
        return "";
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

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
