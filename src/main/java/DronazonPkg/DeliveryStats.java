package DronazonPkg;

import SensorPkg.MeasurementAverage;
import java.util.ArrayList;

public class DeliveryStats {
    public long timestamp;
    public Position destination;
    public float km;
    public int energy;
    public ArrayList<MeasurementAverage> aveList;

    public DeliveryStats(long timestamp, Position destination, float km, int energy, ArrayList<MeasurementAverage> aveList) {
        this.timestamp = timestamp;
        this.destination = destination;
        this.km = km;
        this.energy = energy;
        this.aveList = aveList;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public float getKm() {
        return km;
    }

    public int getEnergy() {
        return energy;
    }

    public ArrayList<MeasurementAverage> getAveList() {
        return aveList;
    }

    @Override
    public String toString() {
        return "DeliveryStats{" +
                "timestamp=" + timestamp +
                ", destination=" + destination +
                ", km=" + km +
                ", energy=" + energy +
                ", aveList=" + aveList +
                '}';
    }
}
