import REST.beans.Position;

public class Order {
    private int id;
    private Position pickUpPoint;
    private Position deliveryPoint;

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
        return "Order{" +
                "id=" + id +
                ", pickUpPoint=" + pickUpPoint.toString() +
                ", deliveryPoint=" + deliveryPoint.toString() +
                '}';
    }
}
