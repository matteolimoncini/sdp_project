package REST.beans;

import org.eclipse.paho.client.mqttv3.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Drone {
    private Integer id;
    private String ipAddress;
    private Integer portNumber;
    private Integer idMaster;
    private Integer battery;
    private boolean processingDelivery;

    private MqttClient client;
    String clientId;


    public Drone() {
    }

    public Drone(Integer id, String ipAddress, Integer portNumber) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.battery = 100;
        this.processingDelivery = false;
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

    private boolean iAmMaster() {
        return idMaster.equals(id);
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
        if (this.client.isConnected()) {
            this.client.disconnect();
            System.out.println("Subscriber " + this.clientId + " disconnected");
        }
        System.out.println("Subscriber already disconnected");
    }

    public void subscribe() throws MqttException {
        if (!iAmMaster()) {
            System.err.println("only master can subscribe");
            //throw exception?
        }

        MqttConnectOptions connOpts;
        clientId = MqttClient.generateClientId();
        String broker = "tcp://localhost:1883";
        String topic = "dronazon/smarcity/orders";
        int qos = 2;

        this.client = new MqttClient(broker, clientId);

        // Connect the client
        System.out.println(clientId + " Connecting Broker " + broker);
        System.out.println(clientId + " Connected - Thread PID: " + Thread.currentThread().getId());

        // Callback
        this.client.setCallback(new MqttCallback() {

            public void messageArrived(String topic, MqttMessage message) {
                // Called when a message arrives from the server that matches any subscription made by the client
                String time = new Timestamp(System.currentTimeMillis()).toString();
                String receivedMessage = new String(message.getPayload());
                System.out.println(clientId + " Received a Message! - Callback - Thread PID: " + Thread.currentThread().getId() +
                        "\n\tTime:    " + time +
                        "\n\tTopic:   " + topic +
                        "\n\tMessage: " + receivedMessage +
                        "\n\tQoS:     " + message.getQos() + "\n");
            }

            public void connectionLost(Throwable cause) {
                System.out.println(clientId + " Connection lost! cause:" + cause.getMessage() + "-  Thread PID: " + Thread.currentThread().getId());
            }

            public void deliveryComplete(IMqttDeliveryToken token) {
                // Not used here
            }

        });
        System.out.println(clientId + " Subscribing ... - Thread PID: " + Thread.currentThread().getId());
        this.client.subscribe(topic, qos);
        System.out.println(clientId + " Subscribed to topics : " + topic);

        //TODO manageOrder

    }
}
