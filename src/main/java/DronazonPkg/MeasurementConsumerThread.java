package DronazonPkg;

import SensorPkg.AverageBufferQueue;
import SensorPkg.BufferQueue;
import SensorPkg.Measurement;
import SensorPkg.MeasurementAverage;
import java.util.List;

public class MeasurementConsumerThread implements Runnable{
    private BufferQueue bufferQueue;
    private AverageBufferQueue averageBufferQueue;
    private boolean end;

    public MeasurementConsumerThread(BufferQueue bufferQueue, AverageBufferQueue averageBufferQueue) {
        this.bufferQueue = bufferQueue;
        this.averageBufferQueue = averageBufferQueue;
        end=false;
    }

    public void start(){    }

    public void run(){
        while (!end){
            System.out.println("MEASUREMENT THREAD - Aspetto 8 misurazioni...");
            List<Measurement> mesListCopy = bufferQueue.readAllAndClean();
            MeasurementAverage mesa = calculateAverage(mesListCopy);
            System.out.println("MEASUREMENT THREAD - La media è: " + mesa.getAverage() + " ottenuta al tempo " + mesa.getTimestamp());
            averageBufferQueue.addAverage(mesa);
        }
    }

    public MeasurementAverage calculateAverage(List<Measurement> mesList){
        double average=0;
        System.out.println("MEASUREMENT THREAD - Calcolo media sulle seguenti misurazioni:");
        for(Measurement mes : mesList){
            System.out.println("MEASUREMENT THREAD - " + mes.getValue());
            average+=mes.getValue();
        }
        if(average!=0){
            average /= 8;
        }
        long timestamp = System.currentTimeMillis();
        MeasurementAverage mesa=new MeasurementAverage(average, timestamp);
        return mesa;
    }

}

