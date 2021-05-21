package REST.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseAddModel {
    private List<Drone> drones;
    private Position myPosition;

    public ResponseAddModel() {
    }

    public ResponseAddModel(List<Drone> drones, Position myPosition) {
        this.drones = drones;
        this.myPosition = myPosition;
    }

    public List<Drone> getDrones() {
        return drones;
    }

    public void setDrones(List<Drone> drones) {
        this.drones = drones;
    }

    public Position getMyPosition() {
        return myPosition;
    }

    public void setMyPosition(Position myPosition) {
        this.myPosition = myPosition;
    }

    @Override
    public String toString() {
        return "ResponseAddModel{" +
                "drones=" + drones +
                ", myPosition=" + myPosition +
                '}';
    }
}
