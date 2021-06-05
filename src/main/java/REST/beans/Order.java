package REST.beans;

import REST.beans.Position;
import com.google.gson.Gson;

public class Order {
    private int id;
    private Position pickUpPoint;
    private Position deliveryPoint;

    public Order(){

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
        /*
        return "REST.beans.Order{" +
                "id=" + id +
                ", pickUpPoint=" + pickUpPoint.toString() +
                ", deliveryPoint=" + deliveryPoint.toString() +
                '}';

         */
    }
}
