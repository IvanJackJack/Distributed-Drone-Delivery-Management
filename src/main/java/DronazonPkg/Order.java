package DronazonPkg;

import java.util.Arrays;
import java.util.Random;

public class Order {
    public int id;
    public Position pickUpPoint;
    public Position deliveryPoint;

    public Order(int id, Position pickUpPoint, Position deliveryPoint) {
        this.id = id;
        this.pickUpPoint = pickUpPoint;
        this.deliveryPoint = deliveryPoint;
    }

    public Order(){
        id=new Random().nextInt(10000);
        pickUpPoint = new Position(new Random().nextInt(10),new Random().nextInt(10));
        deliveryPoint = new Position(new Random().nextInt(10),new Random().nextInt(10));
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
                ", pickUpPoint=" + pickUpPoint +
                ", deliveryPoint=" + deliveryPoint +
                '}';
    }
}
