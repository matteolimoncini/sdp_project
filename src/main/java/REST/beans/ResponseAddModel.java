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

    public ResponseAddModel(List<Drone> drones, Position myPosition) {
        this.drones = drones;
        this.myPosition = myPosition;
    }

    public ResponseAddModel() {
    }
}
