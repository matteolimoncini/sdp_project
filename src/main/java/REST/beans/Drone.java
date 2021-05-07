package REST.beans;

public class Drone {
    private Integer id;
    private String ipAddress;
    private Integer portNumber;

    public Drone(Integer id, String ipAddress, Integer portNumber) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
    }

    public Integer getId() {
        return id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Integer getPortNumber() {
        return portNumber;
    }
}
