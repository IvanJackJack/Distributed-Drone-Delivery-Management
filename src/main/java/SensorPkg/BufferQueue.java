package SensorPkg;

import java.util.ArrayList;
import java.util.List;

public class BufferQueue implements Buffer{
    private ArrayList<Measurement> measurementList;

    public BufferQueue() {
        measurementList = new ArrayList<Measurement>();
    }

    public synchronized void addMeasurement(Measurement m){
        measurementList.add(m);

        if(measurementList.size() >= 8){
            System.out.println("\nSENSOR - Ci sono 8 misurazioni");
            notify();
        }

    }

    public synchronized List<Measurement> readAllAndClean() {
        while(measurementList.size() < 8){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ArrayList<Measurement> copy = new ArrayList<Measurement>(measurementList);

        for(int i=0; i<4; i++){
            measurementList.remove(0);
        }

        return copy;
    }

}
