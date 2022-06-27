package SensorPkg;

import java.util.ArrayList;

public class AverageBufferQueue {
    private ArrayList<MeasurementAverage> averageList;

    public AverageBufferQueue() {
        averageList=new ArrayList<MeasurementAverage>();
    }

    public synchronized void addAverage(MeasurementAverage ma){
        averageList.add(ma);
        System.out.println("\nAVERAGE QUEUE - Aggiunta media " + ma.toString() + "\nAVERAGE QUEUE - Le medie totali sono " + averageList.size());
    }

    public synchronized ArrayList<MeasurementAverage> getAllAverageAndClear(){
        ArrayList<MeasurementAverage> copy = new ArrayList<MeasurementAverage>(averageList);
        averageList.clear();
        return copy;
    }
}
