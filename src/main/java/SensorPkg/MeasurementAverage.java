package SensorPkg;

public class MeasurementAverage {
    private double average;
    private long timestamp;

    public MeasurementAverage(double average, long timestamp) {
        this.average = average;
        this.timestamp = timestamp;
    }

    public double getAverage() {
        return average;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "MeasurementAverage{" +
                "average=" + average +
                ", timestamp=" + timestamp +
                '}';
    }
}
