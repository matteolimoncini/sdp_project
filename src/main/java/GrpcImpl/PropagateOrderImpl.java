package GrpcImpl;
import REST.beans.Drone;
import REST.beans.Order;
import REST.beans.Position;
import com.example.grpc.PropagateOrder;
import com.example.grpc.sendOrderGrpc.sendOrderImplBase;
import io.grpc.stub.StreamObserver;

public class PropagateOrderImpl extends sendOrderImplBase{
    private Drone drone;
    public PropagateOrderImpl(Drone drone) {
        this.drone=drone;
    }

    @Override
    public void messagePropagateOrder(PropagateOrder.propagateOrder request, StreamObserver<PropagateOrder.responseOrder> responseObserver) {
        //TODO
        System.out.println("order received: "+request.getIdOrder());
        Position pickupPosition = new Position(request.getXPositionPickup(),request.getYPositionPickup());
        Position deliveryPosition = new Position(request.getXPositionDelivery(),request.getYPositionDelivery());

        Order order = new Order(request.getIdOrder(),pickupPosition,deliveryPosition);

        drone.manageOrder(order);
    }
}
