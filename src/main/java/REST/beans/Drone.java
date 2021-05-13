package REST.beans;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.eclipse.paho.client.mqttv3.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Drone {
    private Integer idDrone;
    private String ipAddress;
    private Integer portNumber;
    private Integer idMaster;
    private Integer battery;
    private boolean processingDelivery;
    private Position myPosition;

    private MqttClient clientDrone;
    //String clientId;


    public Drone() {
    }

    public Drone(Integer id, String ipAddress, Integer portNumber) {
        this.idDrone = id;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.battery = 100;
        this.processingDelivery = false;
    }

    public Integer getId() {
        return idDrone;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Integer getPortNumber() {
        return portNumber;
    }

    private boolean iAmMaster() {
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
        List<Drone> drones = this.getDroneList();
        //not consider drone already at work

        //choose the drone nearest with greater battery

        //select drone with id greater

        return idDrone;
    }



    private List<Drone> getDroneList() {
        ArrayList<Drone> drones = new ArrayList<>();

        Client client = Client.create();

        WebResource webResource = client.resource("http://localhost:1337/drone");

        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        String output = response.getEntity(String.class);

        System.out.println("Output from Server .... \n");
        System.out.println(output);

        //TODO convert output into drones

        return drones;
    }
}
