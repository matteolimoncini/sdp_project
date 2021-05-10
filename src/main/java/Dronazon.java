import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Dronazon {
    public static void main(String[] args) throws InterruptedException, MqttException {
        MqttClient client;
        String broker = "tcp://localhost:1883";
        String clientId = MqttClient.generateClientId();
        String topic = "dronazon/smartcity/orders";
        int qos = 2;
        while (true){
            // connect as client MQTT as publisher to tcp://localhost:1883
            client = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println(clientId + " Connecting Broker " + broker);
            client.connect(connOpts);
            System.out.println(clientId + " Connected");
            //generate an order

            //publish order on dronazon/smartcity/orders/
            //TODO insert here order
            String payload = String.valueOf("order");
            MqttMessage message = new MqttMessage(payload.getBytes());

            message.setQos(qos);
            System.out.println(clientId + " Publishing message: " + payload + " ...");
            client.publish(topic, message);
            System.out.println(clientId + " Message published");

            if (client.isConnected())
                client.disconnect();
            System.out.println("Publisher " + clientId + " disconnected");

            Thread.sleep(5*1000);
        }
    }
}
