package REST.beans;

import com.example.grpc.AddDrone;
import com.example.grpc.newDroneGrpc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.eclipse.paho.client.mqttv3.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Drone {
    @Expose
    private Integer idDrone;
    @Expose
    private String ipAddress;
    @Expose
    private Integer portNumber;
    private Integer idMaster = -1;
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
    private List<Order> pendingOrders;
    private int countPosition = 0;
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
        this.pendingOrders = new ArrayList<>();
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
        this.pendingOrders = new ArrayList<>();
    }

    public synchronized List<Drone> getDrones() {
        return drones;
    }

    public Integer getIdDrone() {
        return idDrone;
    }

    public void setIdDrone(Integer idDrone) {
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

    public Position getMyPosition() {
        return myPosition;
    }

    public void setMyPosition(Position myPosition) {
        this.myPosition = myPosition;
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

    public int getCountPosition() {
        return this.countPosition;
    }

    public void setCountPosition(int countPosition) {
        this.countPosition = countPosition;
    }

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }


    public synchronized void setProcessingDelivery(boolean processingDelivery) {
        this.processingDelivery = processingDelivery;
    }

    public synchronized List<Order> getPendingOrders() {
        return this.pendingOrders;
    }

    public synchronized void setPendingOrders(List<Order> pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public synchronized void addPendingOrder(Order order) {
        this.pendingOrders.add(order);
    }

    public synchronized Order getFirstPendingOrder() {
        if (this.pendingOrders != null && this.pendingOrders.size() > 0) {
            Order order = this.pendingOrders.get(0);
            this.pendingOrders.remove(0);
            return order;
        } else
            return null;
    }

    public synchronized void removePendingOrder(Order order) {
        if (this.pendingOrders != null && this.pendingOrders.size() > 0)
            this.pendingOrders.remove(order);
    }

    public synchronized boolean areTherePendingOrders() {
        return this.pendingOrders != null && this.pendingOrders.size() > 0;
    }

    public boolean isMaster() {
        return idMaster.equals(idDrone);
    }

    private synchronized boolean isProcessingDelivery() {
        return processingDelivery;
    }

    private boolean batteryLow() {
        int lowbatteryThreshold = 15;
        return this.battery < lowbatteryThreshold;
    }

    private void quit() throws MqttException {
        this.disconnect();
    }

    public void disconnect() throws MqttException {
        if (!isMaster()) {
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

    public void subscribeAndPutInQueue() throws MqttException {
        if (!isMaster()) {
            System.err.println("only master can subscribe");
            return;
            //throw exception?
        }

        MqttConnectOptions connOpts;
        String clientId = MqttClient.generateClientId();
        String broker = "tcp://localhost:1883";
        String topic = "dronazon/smartcity/orders";
        int qos = 2;

        this.clientDrone = new MqttClient(broker, clientId);
        this.clientDrone.connect();
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
                Order order = gson.fromJson(receivedMessage, Order.class);

                Drone.this.addPendingOrder(order);
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

    public Drone chooseDeliver(Order order) {
        int idDrone = -1;
        if (!isMaster()) {
            System.err.println("only master can manage orders");
            return null;
            //throw exception?
        }

        //extract drone list

        List<Drone> dronesCopy = this.getDrones();
        if (dronesCopy == null || dronesCopy.size()==0)
            return this;


        //choose the drone nearest
        int xPickUpPoint = order.getPickUpPoint().getxCoordinate();
        int yPickUpPoint = order.getPickUpPoint().getyCoordinate();
        int xDrone;
        int yDrone;
        double distance;
        double minDistance = Double.MAX_VALUE;
        Drone chosenDrone = null;
        for (Drone currentDrone : dronesCopy) {
            //not consider drone already at work
            if (currentDrone.isProcessingDelivery()){
                continue;
            }
            xDrone = currentDrone.getMyPosition().getxCoordinate();
            yDrone = currentDrone.getMyPosition().getyCoordinate();
            distance = distance(xDrone, yDrone, xPickUpPoint, yPickUpPoint);
            if (distance < minDistance) {
                minDistance = distance;
                chosenDrone = currentDrone;
            } else if (distance == minDistance) {
                if (chosenDrone != null && chosenDrone.getBattery() < currentDrone.getBattery()) {
                    chosenDrone = currentDrone;
                } else if (chosenDrone != null && chosenDrone.getBattery().equals(currentDrone.getBattery())) {
                    if (chosenDrone.getIdDrone() < currentDrone.getIdDrone()) {
                        chosenDrone = currentDrone;
                    }
                }

            }
        }
        if (chosenDrone == null)
            return this;
        System.out.println("###CHOSEN DRONE: "+chosenDrone.getIdDrone()+"########");
        return chosenDrone;
    }
        /*
        //remove drone with distance > mindistance
        for (Drone d : dronesCopy) {
            xDrone = d.getMyPosition().getxCoordinate();
            yDrone = d.getMyPosition().getyCoordinate();
            distance = distance(xDrone, yDrone, xPickUpPoint, yPickUpPoint);
            if (distance > minDistance) dronesCopy.remove(d);
        }

        //select drone with greatest battery
        dronesCopy.sort(Comparator.comparing(Drone::getBattery));
        Integer maxBattery = dronesCopy.get(0).getBattery();
        dronesCopy.removeIf(d -> !d.getBattery().equals(maxBattery));

        //select drone with id greater
        dronesCopy.sort(Comparator.comparing(Drone::getIdDrone));
        */

    public void manageOrder(Order order) {
        this.setProcessingDelivery(true);
        System.out.println("order in progress by drone with:"+this.getIdDrone());
        try {
            Thread.sleep(15 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("order completed");
        this.setProcessingDelivery(false);
        this.sendStatToMaster();
    }

    private double distance(int xOne, int yOne, int xTwo, int yTwo) {
        return Math.sqrt(Math.pow((xTwo - xOne), 2) + Math.pow((yTwo - yOne), 2));
    }

    public synchronized Drone getNextInRing() {
        assert this.drones != null;
        for (Drone d : this.drones) {
            System.out.println("HERE");
            if (d.getIdDrone() > this.getIdDrone()) {
                return d;
            }
        }
        System.out.println("size:" + drones.size());
        if (this.drones.size() >= 1) {
            return this.drones.get(0);
        }
        System.out.println(this.idDrone);
        return null;
    }

    public synchronized void insertDroneInList(Drone insertDrone) {
        if (this.drones == null)
            this.drones = new ArrayList<>();
        this.drones.add(insertDrone);
        this.drones.sort(Comparator.comparing(Drone::getIdDrone));
    }

    public synchronized void removeDroneFromList(Drone removeDrone) {
        this.drones.remove(removeDrone);
    }

    public void addDrone() {
        System.out.println("start addDrone");
        List<Drone> droneList;

        Client client = Client.create();
        String url = "http://" + this.getIpServerAdmin() + ":" + this.getPortServerAdmin();
        WebResource webResource = client.resource(url + "/drone/add");

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String input = gson.toJson(this);
        ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);
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

    public void removeDrone() {
        Client client = Client.create();
        String url = "http://" + this.getIpServerAdmin() + ":" + this.getPortServerAdmin();
        WebResource webResource = client.resource(url + "/drone/delete/" + this.getIdDrone());

        ClientResponse response = webResource.type("application/json").delete(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        String output = response.getEntity(String.class);

        System.out.println("Output from Server .... \n");
        System.out.println(output);

    }

    private void sendStatToMaster() {
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
        System.out.println("sending statistics to master...");
        /*
        Drone masterDrone = null;
        for (Drone d:this.getDrones())
            if (d.isMaster()) {
                masterDrone = d;
                break;
            }
        assert masterDrone!=null;
        String ipMaster = masterDrone.getIpAddress();
        Integer portMaster = masterDrone.getPortNumber();
        String targetAddress = ipMaster +":"+portMaster;

        final ManagedChannel channel = ManagedChannelBuilder.forTarget(targetAddress).usePlaintext().build();

        */
        /*newDroneGrpc.newDroneStub stub = newDroneGrpc.newStub(channel);

        AddDrone.addNewDrone request = AddDrone.addNewDrone
                .newBuilder()
                .set
                .build();

         */
    }

    public void sendGlobalStatistics() {
        /*if (!iAmMaster()) {
            System.err.println("only master can send global statistics");
            return ;
            //throw exception?
        }*/

        Client client = Client.create();

        WebResource webResource = client.resource("http://localhost:1337/statistics/globals");
        Gson gson = new Gson();
        String input = gson.toJson(this.globalStats);
        ClientResponse response = webResource.accept("application/json").post(ClientResponse.class, input);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        String output = response.getEntity(String.class);

        System.out.println("Output from Server .... \n");
        System.out.println(output);


    }

    public void addCountPosition() {
        this.countPosition++;
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
