package REST.beans;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

public class Order {
    @Expose
    private int id;
    @Expose
    private Position pickUpPoint;
    @Expose
    private Position deliveryPoint;

    public Order() {

    }

    public Order(int id, Position pickUpPoint, Position deliveryPoint) {
        this.id = id;
        this.pickUpPoint = pickUpPoint;
        this.deliveryPoint = deliveryPoint;
    }

    public int getId() {
        return id;
    }

    public Position getPickUpPoint() {
        return pickUpPoint;
    }

    public Position getDeliveryPoint() {
        return deliveryPoint;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
