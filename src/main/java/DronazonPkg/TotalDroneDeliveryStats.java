package DronazonPkg;

public class TotalDroneDeliveryStats {
    int deliveriesTotal;
    float kmTotal;
    int energy;

    public TotalDroneDeliveryStats() {
        deliveriesTotal=0;
        kmTotal=0;
        energy=100;
    }

    public synchronized int getDeliveriesTotal() {
        return deliveriesTotal;
    }

    public synchronized float getKmTotal() {
        return kmTotal;
    }

    public synchronized int getEnergy() {
        return energy;
    }

    public synchronized void update(float km, int e){
        energy=e;
        kmTotal += km;
        deliveriesTotal += 1;
    }

}
