package DronazonPkg;

import java.util.ArrayList;

public class DeliveryStatsQueue {
    private ArrayList<DeliveryStats> statsList;

    public DeliveryStatsQueue() {
        statsList=new ArrayList<DeliveryStats>();
    }

    public synchronized ArrayList<DeliveryStats> getStatsList() {
        return new ArrayList<>(statsList);
    }

    public synchronized ArrayList<DeliveryStats> readAllAndClear(){
        ArrayList<DeliveryStats> copy = new ArrayList<>(statsList);
        statsList.clear();
        return copy;
    }

    public synchronized void add(DeliveryStats ds){
        statsList.add(ds);
    }

    public void printAll(String source){
        ArrayList<DeliveryStats> copy = getStatsList();
        System.out.println("\n" + source + " - Le stats in coda sono: ");
        for(DeliveryStats ds : copy){
            System.out.println(ds.toString());
        }
    }

    @Override
    public String toString() {
        return "DeliveryStatsQueue{" +
                "statsList=" + statsList +
                '}';
    }
}
