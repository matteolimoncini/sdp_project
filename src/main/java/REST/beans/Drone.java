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

    public Drone(){}

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

    private boolean iAmMaster(){
        return idMaster.equals(id);
    }

    public void subscribe(){
        if(!iAmMaster()){
            System.err.println("only master can subscribe");
            //throw exception?
        }
        MqttClient client;
        MqttConnectOptions connOpts;
        String broker = "tcp://localhost:1883";
        String clientId = MqttClient.generateClientId();
        String topic = "dronazon/smarcity/orders";
        int qos = 2;

        try {
            client = new MqttClient(broker, clientId);
            connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // Connect the client
            System.out.println(clientId + " Connecting Broker " + broker);
            client.connect(connOpts);
            System.out.println(clientId + " Connected - Thread PID: " + Thread.currentThread().getId());

            // Callback
            client.setCallback(new MqttCallback() {

                public void messageArrived(String topic, MqttMessage message) {
                    // Called when a message arrives from the server that matches any subscription made by the client
                    String time = new Timestamp(System.currentTimeMillis()).toString();
                    String receivedMessage = new String(message.getPayload());
                    System.out.println(clientId +" Received a Message! - Callback - Thread PID: " + Thread.currentThread().getId() +
                            "\n\tTime:    " + time +
                            "\n\tTopic:   " + topic +
                            "\n\tMessage: " + receivedMessage +
                            "\n\tQoS:     " + message.getQos() + "\n");
                }

                public void connectionLost(Throwable cause) {
                    System.out.println(clientId + " Connection lost! cause:" + cause.getMessage()+ "-  Thread PID: " + Thread.currentThread().getId());
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not used here
                }

            });
            System.out.println(clientId + " Subscribing ... - Thread PID: " + Thread.currentThread().getId());
            client.subscribe(topic,qos);
            System.out.println(clientId + " Subscribed to topics : " + topic);

            //TODO manageOrder

            client.disconnect();

        } catch (MqttException me ) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("exception " + me);
            me.printStackTrace();
        }
    }
}
