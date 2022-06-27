package ServerPkg;

import DronazonPkg.DeliveryStats;
import SensorPkg.MeasurementAverage;
import java.util.ArrayList;
import java.util.Date;

public class GlobalStats {
    public float averageDeliveries;
    public float averageKm;
    public float averagePollution;
    public float averageEnergy;
    public long timestamp;

    public GlobalStats() {    }

    public void reset(){
        averageDeliveries=0;
        averageKm=0;
        averagePollution=0;
        averageEnergy=0;
        timestamp=0;
    }

    public void calculate(ArrayList<DeliveryStats> statsList, int numDrones){
        //questo controllo è qui giusto per sicurezza, ma non dovrebe mai verificarsi il caso
        if(statsList.size()==0){
            System.out.println("Non si sono stats!!!");
            reset();
            timestamp = System.currentTimeMillis();
            return;
        }

        for(DeliveryStats ds : statsList){
            averageEnergy += ds.getEnergy();
            averageKm += ds.getKm();
            averageDeliveries += 1;

            float partial=0;
            for(MeasurementAverage ma : ds.getAveList()){
                partial += ma.getAverage();
            }
            if(partial != 0){
                partial /= ds.getAveList().size();
            }

            averagePollution += partial;
        }

        if(averageDeliveries != 0)
            averageDeliveries /= numDrones;
        if(averageKm != 0)
            averageKm /= numDrones;
        if(averagePollution != 0)
            averagePollution /= numDrones;
        if(averageEnergy != 0)
            averageEnergy /= (statsList.size());

        timestamp = new Date().getTime();
    }

    public float getAverageDeliveries() {
        return averageDeliveries;
    }

    public float getAverageKm() {
        return averageKm;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "GlobalStats{" +
                "averageDeliveries=" + averageDeliveries +
                ", averageKm=" + averageKm +
                ", averagePollution=" + averagePollution +
                ", averageEnergy=" + averageEnergy +
                ", timestamp=" + timestamp +
                '}';
    }
}
