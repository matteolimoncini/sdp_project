import REST.beans.Position;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Random;

public class Dronazon {
    public static void main(String[] args) throws InterruptedException, MqttException {
        MqttClient client;
        String payload;
        MqttMessage message;
        Order randomOrder;
        Random r = new Random();
        String clientId = MqttClient.generateClientId();
        String broker = "tcp://localhost:1883";
        String topic = "dronazon/smartcity/orders";
        int qos = 2;

        // connect as client MQTT as publisher to tcp://localhost:1883
        client = new MqttClient(broker, clientId);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        System.out.println(clientId + " Connecting Broker " + broker);
        client.connect(connOpts);
        System.out.println(clientId + " Connected");

        while (true) {
            //generate an order
            randomOrder = new Order(r.nextInt(), new Position(), new Position());
            payload = randomOrder.toString();
            message = new MqttMessage(payload.getBytes());
            message.setQos(qos);

            //publish order on dronazon/smartcity/orders/
            System.out.println(clientId + " Publishing message: " + payload + " ...");
            client.publish(topic, message);
            System.out.println(clientId + " Message published");

            Thread.sleep(5 * 1000);
        }
        /*
        if (client.isConnected())
            client.disconnect();
        System.out.println("Publisher " + clientId + " disconnected");

         */
    }
}
