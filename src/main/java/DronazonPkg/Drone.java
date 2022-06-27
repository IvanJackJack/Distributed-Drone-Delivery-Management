package DronazonPkg;

import SensorPkg.AverageBufferQueue;
import SensorPkg.MeasurementAverage;
import java.util.ArrayList;
import java.util.Random;

public class Drone implements Comparable<Drone>{
    public int id;
    public boolean master;

    public Position position;
    public boolean delivering;
    public int energy;
    public boolean partecipant;
    public Order order;
    public boolean rankingUp;

    public String droneIP;
    public int dronePort;
    public int droneMasterPort;

    public final String serverIP = "localhost";
    public final int serverPORT = 8080;

    public Drone(){
        id = new Random().nextInt(40);
        dronePort = 1 + new Random().nextInt(65000);

        master = false;
        delivering = false;
        position=new Position(-1,-1);
        droneIP="localhost";
        energy=100;
        droneMasterPort = -1;
        partecipant=false;
        order=null;
        rankingUp=false;
    }

    public Drone(int id, int dronePort) {
        this.id = id;
        this.dronePort = dronePort;

        master = false;
        delivering = false;
        position=new Position(-1,-1);
        droneIP="localhost";
        energy=100;
        droneMasterPort = -1;
        partecipant=false;
        order=null;
        rankingUp=false;
    }

    public DeliveryStats Delivery(AverageBufferQueue averageBuffer){
        System.out.println("\nDRONE - Inizio consegna ordine: " + order.getId());

        try { Thread.sleep(5000); } catch (Exception e) { }

        float km = Distance(getPosition(), order.getPickUpPoint());
        km += Distance(order.getPickUpPoint(), order.getDeliveryPoint());

        setPosition(order.getDeliveryPoint());
        reduceEnergy();

        ArrayList<MeasurementAverage> aveListCopy = averageBuffer.getAllAverageAndClear();

        long timestamp = System.currentTimeMillis();
        DeliveryStats ds=new DeliveryStats(timestamp, getPosition(), km, getEnergy(), aveListCopy);

        System.out.println("\nDRONE - Ho completato la consegna con id: " + order.getId());
        setOrder(null);

        return ds;
    }

    public float Distance(Position a, Position b){
        return (float)Math.sqrt( Math.pow((b.getX() - a.getX()), 2) + Math.pow((b.getY() - a.getY()), 2) );
    }

    public synchronized void reduceEnergy(){
        energy -= 10;
    }

    @Override
    public String toString() {
        return "Drone{" +
                "id=" + id +
                ", master=" + master +
                ", position=" + position +
                ", energy=" + energy +
                ", delivering=" + delivering +
                ", dronePort=" + dronePort +
                ", droneMasterPort=" + droneMasterPort +
                '}';
    }

    public int compareTo(Drone d) {
        if (id < d.id)
            return -1;
        if (id > d.id)
            return 1;
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDronePort() {
        return dronePort;
    }

    public synchronized boolean isMaster() {
        return master;
    }

    public synchronized void setMaster(boolean master) {
        this.master = master;
    }

    public synchronized Order getOrder() {
        return order;
    }

    public synchronized void setOrder(Order order) {
        this.order = order;
    }

    public synchronized Position getPosition() {
        return position;
    }

    public synchronized void setPosition(Position position) {
        this.position = position;
    }

    public synchronized boolean isDelivering() {
        return delivering;
    }

    public synchronized void setDelivering(boolean delivering) {
        this.delivering = delivering;
    }

    public synchronized int getEnergy() {
        return energy;
    }

    public synchronized void setEnergy(int energy) {
        this.energy = energy;
    }

    public synchronized int getDroneMasterPort() {
        return droneMasterPort;
    }

    public synchronized void setDroneMasterPort(int droneMasterPort) {
        this.droneMasterPort = droneMasterPort;
    }

    public synchronized boolean isPartecipant() {
        return partecipant;
    }

    public synchronized void setPartecipant(boolean partecipant) {
        this.partecipant = partecipant;
    }

    public synchronized boolean isRankingUp() {
        return rankingUp;
    }

    public synchronized void setRankingUp(boolean rankingUp) {
        this.rankingUp = rankingUp;
    }
}
