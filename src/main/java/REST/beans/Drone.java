package REST.beans;

import com.example.grpc.GlobalStatsToMaster;
import com.example.grpc.globalStatsServiceGrpc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import io.grpc.Context;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.eclipse.paho.client.mqttv3.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Drone {
    @Expose
    private Integer idDrone;
    @Expose
    private String ipAddress;
    @Expose
    private Integer portNumber;
    private Integer idMaster;
    private MqttClient clientDrone;
    private Integer battery;
    private boolean processingDelivery;
    private Position myPosition;
    private String ipServerAdmin;
    private Integer portServerAdmin;
    private boolean participant;
    private List<GlobalStats> gStatsList;
    private List<Drone> drones;
    private List<Order> pendingOrders;
    private List<Double> measurementList;
    private int countPosition;
    private boolean quit;
    private boolean electionInProgress;
    @JsonIgnore
    private final Object syncCurrentOrder;
    private double kmTravelled;
    private int numDelivery;

    public Drone() {
        this.idMaster = -1;
        this.battery = 100;
        this.processingDelivery = false;
        this.participant = false;
        this.pendingOrders = new ArrayList<>();
        this.gStatsList = new ArrayList<>();
        this.drones = new ArrayList<>();
        this.pendingOrders = new ArrayList<>();
        this.measurementList = new ArrayList<>();
        this.countPosition = 0;
        this.quit = false;
        this.electionInProgress = false;
        this.syncCurrentOrder = new Object();
        this.kmTravelled = 0;
        this.numDelivery = 0;
    }

    public Drone(Integer id, String ipAddress, Integer portNumber, String ipServerAdmin, Integer portServerAdmin) {
        this.idMaster = -1;
        this.idDrone = id;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.ipServerAdmin = ipServerAdmin;
        this.portServerAdmin = portServerAdmin;
        this.battery = 100;
        this.processingDelivery = false;
        this.participant = false;
        this.gStatsList = new ArrayList<>();
        this.drones = new ArrayList<>();
        this.pendingOrders = new ArrayList<>();
        this.measurementList = new ArrayList<>();
        this.countPosition = 0;
        this.quit = false;
        this.electionInProgress = false;
        this.syncCurrentOrder = new Object();
        this.kmTravelled = 0;
        this.numDelivery = 0;
    }

    public synchronized List<Drone> getDrones() {
        if (drones != null)
            return new ArrayList<>(drones);
        return null;
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

    public synchronized boolean getElectionInProgress() {
        return electionInProgress;
    }

    public synchronized void setElectionInProgress(boolean electionInProgress) {
        this.electionInProgress = electionInProgress;
    }

    public synchronized Position getMyPosition() {
        return myPosition;
    }

    public synchronized void setMyPosition(Position myPosition) {
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

    public synchronized void setIdMaster(Integer idMaster) {
        this.idMaster = idMaster;
    }

    public boolean getParticipant() {
        return participant;
    }

    public void setParticipant(boolean participant) {
        this.participant = participant;
    }

    public int getCountPosition() {
        return this.countPosition;
    }

    public void setCountPosition(int countPosition) {
        this.countPosition = countPosition;
    }

    public void addCountPosition() {
        this.countPosition++;
    }

    public synchronized Integer getBattery() {
        return battery;
    }

    public synchronized void setBattery(Integer battery) {
        this.battery = battery;
    }

    public double getKmTravelled() {
        return kmTravelled;
    }

    public void setKmTravelled(double kmTravelled) {
        this.kmTravelled = kmTravelled;
    }

    public int getNumDelivery() {
        return numDelivery;
    }

    public void setNumDelivery(int numDelivery) {
        this.numDelivery = numDelivery;
    }

    public Object getSyncCurrentOrder() {
        return syncCurrentOrder;
    }

    public synchronized List<Double> getMeasurementList() {
        if (measurementList != null)
            return new ArrayList<>(measurementList);
        return null;
    }

    public synchronized void setMeasurementList(List<Double> measurementList) {
        this.measurementList = measurementList;
    }

    public synchronized void addToMeasurementList(Double measurement) {
        this.measurementList.add(measurement);
    }

    public synchronized List<GlobalStats> getgStatsList() {
        if (gStatsList != null)
            return new ArrayList<>(gStatsList);
        return null;
    }

    public synchronized void setgStatsList(List<GlobalStats> gStatsList) {
        this.gStatsList = gStatsList;
    }

    public synchronized void addToStatsList(GlobalStats gStat) {
        gStatsList.add(gStat);
    }

    public synchronized boolean isQuit() {
        return quit;
    }

    public synchronized void setQuit(boolean quit) {
        this.quit = quit;
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
        return ((this.pendingOrders != null) && (this.pendingOrders.size() > 0)) ? this.pendingOrders.get(0) : null;
    }

    public synchronized void removePendingOrder(Order order) {
        if (this.pendingOrders != null && this.pendingOrders.size() > 0)
            this.pendingOrders.remove(order);
    }

    public synchronized boolean areTherePendingOrders() {
        return this.pendingOrders != null && this.pendingOrders.size() > 0;
    }

    public synchronized boolean isMaster() {
        return idMaster.equals(idDrone);
    }

    public synchronized boolean isProcessingDelivery() {
        return processingDelivery;
    }

    public synchronized void setProcessingDelivery(boolean processingDelivery) {
        this.processingDelivery = processingDelivery;
    }

    public void disconnect() throws MqttException {
        if (!isMaster()) {
            System.err.println("only master can disconnect");
            return;
        }
        if (this.clientDrone.isConnected()) {
            this.clientDrone.disconnect();
            System.out.println("Subscriber disconnected");
        }
    }

    public void subscribeAndPutInQueue() throws MqttException {
        if (!isMaster()) {
            System.err.println("only master can subscribe");
            return;
        }

        String clientId = MqttClient.generateClientId();
        String broker = "tcp://localhost:1883";
        String topic = "dronazon/smartcity/orders";
        int qos = 2;

        this.clientDrone = new MqttClient(broker, clientId);
        // Connect the client
        this.clientDrone.connect();
        //System.out.println(clientId + " Connecting Broker " + broker);
        //System.out.println(clientId + " Connected - Thread PID: " + Thread.currentThread().getId());

        // Callback
        this.clientDrone.setCallback(new MqttCallback() {

            public void messageArrived(String topic, MqttMessage message) {
                String receivedMessage = new String(message.getPayload());
                Gson gson = new Gson();
                Order order = gson.fromJson(receivedMessage, Order.class);
                Position pickUpPoint = order.getPickUpPoint();
                Position deliveryPoint = order.getDeliveryPoint();
                System.out.println("Received order from Dronazon with id: " + order.getId() + " from (" + pickUpPoint.getxCoordinate() + "," + pickUpPoint.getyCoordinate() +
                        ") to (" + deliveryPoint.getxCoordinate() + "," + deliveryPoint.getyCoordinate() + ")");
                Drone.this.addPendingOrder(order);
            }

            public void connectionLost(Throwable cause) {
                System.out.println(clientId + " Connection lost! cause:" + cause.getMessage() + "-  Thread PID: " + Thread.currentThread().getId());
            }

            public void deliveryComplete(IMqttDeliveryToken token) {
                // Not used here
            }

        });
        //System.out.println(clientId + " Subscribing ... - Thread PID: " + Thread.currentThread().getId());
        this.clientDrone.subscribe(topic, qos);
        System.out.println("Connected broker and subscribed to topic : " + topic);
    }

    public Drone chooseDeliver(Order order) {
        if (!isMaster()) {
            System.err.println("only master can manage orders");
            return null;
        }

        //extract drone list
        List<Drone> drones = this.getDrones();
        List<Drone> dronesCopyChooseDeliver;
        if (drones == null) {
            dronesCopyChooseDeliver = new ArrayList<>();
        } else {
            dronesCopyChooseDeliver = new ArrayList<>(drones);
        }

        dronesCopyChooseDeliver.add(this);
        dronesCopyChooseDeliver.removeIf(d -> d.getBattery() < 15);

        //choose the drone nearest
        int xPickUpPoint = order.getPickUpPoint().getxCoordinate();
        int yPickUpPoint = order.getPickUpPoint().getyCoordinate();
        int xDrone;
        int yDrone;
        double distance;
        double minDistance = Double.MAX_VALUE;
        Drone chosenDrone = null;
        for (Drone currentDrone : dronesCopyChooseDeliver) {
            //not consider drone already at work
            if (currentDrone.isProcessingDelivery()) {
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
        return chosenDrone;
    }

    public void manageOrder(Order order) {
        this.setProcessingDelivery(true);
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        Position oldPosition = this.getMyPosition();
        System.out.println("Order in progress by drone with id:" + this.getIdDrone());
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Order " + order.getId() + " completed");
        this.setBattery(this.getBattery() - 10);
        //System.out.println("battery level decreased");
        System.out.println("now battery level is: " + this.getBattery());
        this.setProcessingDelivery(false);
        synchronized (this.getSyncCurrentOrder()) {
            this.getSyncCurrentOrder().notify();
        }
        this.setNumDelivery(this.getNumDelivery()+1);
        this.sendStatToMaster(order, timestamp, oldPosition);
    }

    private double distance(int xOne, int yOne, int xTwo, int yTwo) {
        return Math.sqrt(Math.pow((xTwo - xOne), 2) + Math.pow((yTwo - yOne), 2));
    }

    public synchronized Drone getNextInRing() {
        assert this.drones != null;
        for (Drone d : this.drones) {
            if (d.getIdDrone() > this.getIdDrone()) {
                System.out.println("id drone: " + d.getIdDrone());
                return d;
            }
        }
        //System.out.println("size:" + this.getDrones().size());
        if (this.getDrones().size() >= 1) {
            return this.getDrones().get(0);
        }

        return null;
    }

    public synchronized Drone getNextNextInRing() {
        int size = this.drones.size();
        int i;
        for (i = 0; i < size; i++) {
            Drone d = this.drones.get((i+1)%size);
            if (d.getIdDrone() > this.getIdDrone())
                return d;
        }
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

    public synchronized void addDrone() {
        //System.out.println("start addDrone");
        List<Drone> droneList;

        Client client = Client.create();
        String url = "http://" + this.getIpServerAdmin() + ":" + this.getPortServerAdmin();
        WebResource webResource = client.resource(url + "/drone/add");

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String input = gson.toJson(this);
        ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error : " + response.getEntity(ExceptionModel.class).getMessage());
        }

        ResponseAddModel output = response.getEntity(ResponseAddModel.class);

        droneList = output.getDrones();
        Position myPosition = output.getMyPosition();
        this.setMyPosition(myPosition);

        //System.out.println("Output from Server .... \n");
        System.out.println("My position is: (" + myPosition.getxCoordinate() + "," + myPosition.getyCoordinate() + ")");
        if (droneList == null || droneList.isEmpty()) {
            System.out.println("There aren't other drones in the systems");
        } else {
            System.out.print("other drones in the systems are: ");
            for (int i = 0; i < droneList.size(); i++) {
                Drone d = droneList.get(i);
                System.out.print(d.getIdDrone());
                if (i < droneList.size() - 1) {
                    System.out.print(",");
                }
                if (i == droneList.size() - 1) {
                    System.out.print("\n");
                }
            }
        }
        this.drones = droneList;
    }

    public void removeDrone() {
        Client client = Client.create();
        String url = "http://" + this.getIpServerAdmin() + ":" + this.getPortServerAdmin();
        WebResource webResource = client.resource(url + "/drone/delete/" + this.getIdDrone());
        ClientResponse response = webResource.type("application/json").delete(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error : " + response.getEntity(ExceptionModel.class).getMessage());
        }
        System.out.println("Drone removed from the system");
    }

    private void sendStatToMaster(Order order, Timestamp timestamp, Position oldPosition) {
        //System.out.println("sending statistics to master...");

        Drone masterDrone = null;
        List<Drone> dronesList = this.getDrones();


        if ((dronesList != null && dronesList.size() != 0) || (this.isMaster())) {

            //System.out.println("starting grpc...");
            if (dronesList != null && dronesList.size() != 0) {
                for (Drone d : dronesList)
                    if (d.getIdDrone().equals(this.getIdMaster())) {
                        masterDrone = d;
                        break;
                    }
            }
            if (this.isMaster()) {
                masterDrone = this;
            }
            //assertNotNull(masterDrone);

            String ipMaster = masterDrone.getIpAddress();
            Integer portMaster = masterDrone.getPortNumber();
            String targetAddress = ipMaster + ":" + portMaster;

            Context.current().fork().run(() -> {
                final ManagedChannel channel = ManagedChannelBuilder.forTarget(targetAddress).usePlaintext().build();

                globalStatsServiceGrpc.globalStatsServiceStub stub = globalStatsServiceGrpc.newStub(channel);

                Position deliveryPoint = order.getDeliveryPoint();
                Position pickUpPoint = order.getPickUpPoint();
                double distanceFromOldToPickup = distance(oldPosition.getxCoordinate(), oldPosition.getyCoordinate(), pickUpPoint.getxCoordinate(), pickUpPoint.getyCoordinate());
                double distanceFromPickupTpDelivery = distance(pickUpPoint.getxCoordinate(), pickUpPoint.getxCoordinate(), deliveryPoint.getxCoordinate(), deliveryPoint.getyCoordinate());
                double totKm = distanceFromPickupTpDelivery + distanceFromOldToPickup;
                this.setKmTravelled(this.getKmTravelled()+totKm);
                GlobalStatsToMaster.globalStatsToMaster request1 = GlobalStatsToMaster.globalStatsToMaster
                        .newBuilder()
                        .setIdDrone(this.getIdDrone())
                        .setTimestamp(timestamp.getTime())
                        .setBattery(this.getBattery())
                        .setNewPositionX(deliveryPoint.getxCoordinate())
                        .setNewPositionY(deliveryPoint.getyCoordinate())
                        .setKmTravelled(totKm)
                        .addAllAvgPm10(this.getMeasurementList())
                        .build();
                stub.globalStatsMaster(request1, new StreamObserver<GlobalStatsToMaster.responseGlobalStats>() {
                    @Override
                    public void onNext(GlobalStatsToMaster.responseGlobalStats value) {
                        //System.out.println("global stats received");
                    }

                    @Override
                    public void onError(Throwable t) {
                        channel.shutdown();
                    }

                    @Override
                    public void onCompleted() {
                        channel.shutdown();
                    }
                });
                try {
                    channel.awaitTermination(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void sendGlobalStatisticsToServer() {

        Client client = Client.create();
        double sumDel = 0, sumKm = 0, sumPol = 0, sumBat = 0;
        int len = 0, lenPol = 0;
        for (GlobalStats g : this.getgStatsList()) {
            sumDel += g.getAvgDelivery();
            sumKm += g.getAvgKilometers();
            sumBat += g.getAvgBattery();
            len += 1;

            List<Double> pollution = g.getAvgPollution();
            for (Double d : pollution) {
                sumPol += d;
                lenPol += 1;
            }
        }

        if (len != 0) {
            //calculate avg
            GlobalStats globalStats = new GlobalStats(sumDel / len, sumKm / len, sumPol / lenPol, sumBat / len);
            System.out.println("Sent global statistics to server");

            //remove all elements from the list
            this.setgStatsList(new ArrayList<>());

            String url = "http://" + this.getIpServerAdmin() + ":" + this.getPortServerAdmin();
            WebResource webResource = client.resource(url + "/statistics/globals");
            Gson gson = new Gson();
            String input = gson.toJson(globalStats);
            //System.out.println(input);
            ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error : " + response.getEntity(ExceptionModel.class).getMessage());
            }
        }

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
