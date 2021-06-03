package REST.beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.eclipse.paho.client.mqttv3.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
@XmlRootElement
public class Drone {
    @Expose
    private int idDrone;
    @Expose
    private String ipAddress;
    @Expose
    private Integer portNumber;
    private Integer idMaster=-1;
    private MqttClient clientDrone;


    private Integer battery;
    private boolean processingDelivery;
    private Position myPosition;
    private Timestamp lastDelivery;
    private double kmTotDelivery;
    private String ipServerAdmin;
    private Integer portServerAdmin;

    private GlobalStats globalStats;
    private boolean partecipant;

    private List<Drone> drones;
    //String clientId;

    /*
    id_drone:  batteria
               position
               timestamp
               tot_km_percorsi
               avg_pollution ?
               processingDelivery
     */


    public Drone() {
        this.battery = 100;
        this.processingDelivery = false;
        this.partecipant = false;
        this.drones = new ArrayList<>();

    }

    public Drone(Integer id, String ipAddress, Integer portNumber, String ipServerAdmin, Integer portServerAdmin) {
        this.idDrone = id;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.ipServerAdmin = ipServerAdmin;
        this.portServerAdmin = portServerAdmin;
        this.battery = 100;
        this.processingDelivery = false;
        this.partecipant = false;
        this.drones = new ArrayList<>();
    }

    public synchronized List<Drone> getDrones() {
        return drones;
    }

    public int getIdDrone() {
        return idDrone;
    }

    public void setIdDrone(int idDrone) {
        this.idDrone = idDrone;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(Integer portNumber) {
        this.portNumber = portNumber;
    }

    public void setMyPosition(Position myPosition) {
        this.myPosition = myPosition;
    }

    public Position getMyPosition() {
        return myPosition;
    }

    public String getIpServerAdmin() {
        return ipServerAdmin;
    }

    public Integer getPortServerAdmin() {
        return portServerAdmin;
    }

    public Integer getIdMaster() {
        return idMaster;
    }

    public void setIdMaster(Integer idMaster) {
        this.idMaster = idMaster;
    }

    public boolean getPartecipant() {
        return partecipant;
    }

    public void setPartecipant(boolean partecipant) {
        this.partecipant = partecipant;
    }

    public boolean iAmMaster() {
        return idMaster.equals(idDrone);
    }

    private boolean iAmProcessingDelivery() {
        return processingDelivery;
    }

    private boolean batteryLow(){
        int lowbatteryThreshold = 15;
        return this.battery<lowbatteryThreshold;
    }

    private void quit() throws MqttException {
        this.disconnect();
    }

    public void disconnect() throws MqttException {
        if (!iAmMaster()) {
            System.err.println("only master can disconnect");
            return;
            //throw exception?
        }
        if (this.clientDrone.isConnected()) {
            this.clientDrone.disconnect();
            System.out.println("Subscriber " + this.clientDrone.getClientId() + " disconnected");
        }
        System.out.println("Subscriber already disconnected");
    }

    public void subscribe() throws MqttException {
        if (!iAmMaster()) {
            System.err.println("only master can subscribe");
            return;
            //throw exception?
        }

        MqttConnectOptions connOpts;
        String clientId = MqttClient.generateClientId();
        String broker = "tcp://localhost:1883";
        String topic = "dronazon/smarcity/orders";
        int qos = 2;

        this.clientDrone = new MqttClient(broker, clientId);

        // Connect the client
        System.out.println(clientId + " Connecting Broker " + broker);
        System.out.println(clientId + " Connected - Thread PID: " + Thread.currentThread().getId());

        // Callback
        this.clientDrone.setCallback(new MqttCallback() {

            public void messageArrived(String topic, MqttMessage message) {
                // Called when a message arrives from the server that matches any subscription made by the client
                String time = new Timestamp(System.currentTimeMillis()).toString();
                String receivedMessage = new String(message.getPayload());
                System.out.println(clientId + " Received a Message! - Callback - Thread PID: " + Thread.currentThread().getId() +
                        "\n\tTime:    " + time +
                        "\n\tTopic:   " + topic +
                        "\n\tMessage: " + receivedMessage +
                        "\n\tQoS:     " + message.getQos() + "\n");
                Gson gson = new Gson();
                Order order = gson.fromJson(receivedMessage,Order.class);
                Drone.this.chooseDeliver(order);
            }

            public void connectionLost(Throwable cause) {
                System.out.println(clientId + " Connection lost! cause:" + cause.getMessage() + "-  Thread PID: " + Thread.currentThread().getId());
            }

            public void deliveryComplete(IMqttDeliveryToken token) {
                // Not used here
            }

        });
        System.out.println(clientId + " Subscribing ... - Thread PID: " + Thread.currentThread().getId());
        this.clientDrone.subscribe(topic, qos);
        System.out.println(clientId + " Subscribed to topics : " + topic);
    }

    private int chooseDeliver(Order order) {
        int idDrone=-1;
        if (!iAmMaster()) {
            System.err.println("only master can manage orders");
            return idDrone;
            //throw exception?
        }

        //extract drone list
        //List<Drone> drones =
        //not consider drone already at work

        //choose the drone nearest with greater battery

        //select drone with id greater

        return idDrone;
    }

    public synchronized Drone getNextInRing(){
        assert this.drones != null;
        for (Drone d:this.drones) {
            System.out.println("HERE");
            if (d.getIdDrone()>this.getIdDrone()){
                return d;
            }
        }
        assert drones.size()>0;
        System.out.println(drones.size());
        System.out.println(this.idDrone);
        return drones.get(0);
    }


    public synchronized void insertDroneInList(Drone insertDrone){
        if (this.drones == null)
            this.drones = new ArrayList<>();
        this.drones.add(insertDrone);
        this.drones.sort(Comparator.comparing(Drone::getIdDrone));
    }
    public synchronized void removeDroneFromList(Drone removeDrone){
        this.drones.remove(removeDrone);
    }

    public void addDrone() {
        System.out.println("start addDrone");
        List<Drone> droneList;

        Client client = Client.create();
        String url = "http://"+this.getIpServerAdmin()+":"+this.getPortServerAdmin();
        WebResource webResource = client.resource(url+"/drone/add");

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String input = gson.toJson(this);
        ClientResponse response = webResource.type("application/json").post(ClientResponse.class,input);
        System.out.println("middle addDrone");

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        ResponseAddModel output = response.getEntity(ResponseAddModel.class);

        droneList = output.getDrones();
        this.setMyPosition(output.getMyPosition());

        System.out.println("Output from Server .... \n");
        System.out.println(output);
        this.drones = droneList;
    }

    public void removeDrone(){
        Client client = Client.create();
        String url = "http://"+this.getIpServerAdmin()+":"+this.getPortServerAdmin();
        WebResource webResource = client.resource(url+"/drone/delete/"+this.getIdDrone());

        ClientResponse response = webResource.type("application/json").delete(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        String output = response.getEntity(String.class);

        System.out.println("Output from Server .... \n");
        System.out.println(output);

    }

    private void sendStatToMaster(){
        /*
            drone send after a delivery

            - timestamp
            - position = position of delivery
            - km_travelled
            - avg_pollution
            - battery

            - num_delivery
            - km_tot_travelled
            - battery
        */
    }
    public void sendGlobalStatistics (){
        /*if (!iAmMaster()) {
            System.err.println("only master can send global statistics");
            return ;
            //throw exception?
        }*/

        Client client = Client.create();

        WebResource webResource = client.resource("http://localhost:1337/statistics/globals");
        Gson gson = new Gson();
        String input = gson.toJson(this.globalStats);
        ClientResponse response = webResource.accept("application/json").post(ClientResponse.class,input);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        String output = response.getEntity(String.class);

        System.out.println("Output from Server .... \n");
        System.out.println(output);


    }

    @Override
    public String toString() {
        return "Drone{" +
                "idDrone=" + this.getIdDrone() +
                ", ipAddress='" + this.getIpAddress() + '\'' +
                ", portNumber=" + this.getPortNumber() +
                '}';
    }
}
