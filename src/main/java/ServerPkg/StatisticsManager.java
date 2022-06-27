package ServerPkg;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class StatisticsManager {
    private static StatisticsManager instance;

    @XmlElement(name="statsList")
    private ArrayList<GlobalStats> statsList;

    public StatisticsManager() {
        statsList=new ArrayList<GlobalStats>();
    }

    public synchronized static StatisticsManager getInstance() {
        if(instance==null){
            instance=new StatisticsManager();
        }
        return instance;
    }

    public synchronized void add(GlobalStats newGs){
        statsList.add(newGs);
    }

    public synchronized ArrayList<GlobalStats> getStatsList() {
        return new ArrayList<>(statsList);
    }

    public void printStatsList(){
        ArrayList<GlobalStats> statsListCopy = getStatsList();
        if(statsListCopy.size()==0){
            System.out.println("Non ci sono stats nel manager");
            return;
        }
        System.out.println("Nel manager ci sono le seguenti stats: ");
        for (GlobalStats gs : statsListCopy){
            System.out.println(gs.toString());
        }
    }

    public ArrayList<GlobalStats> getLastStats(int n){
        ArrayList<GlobalStats> statsListCopy = getStatsList();

        if(statsListCopy.size() == 0){
            System.out.println("Non ci sono stats");
            return statsListCopy;
        }

        if(n > statsListCopy.size()){
            System.out.println("Non ci sono abbastanza stats, restituisco le ultime " + statsListCopy.size());
            return statsListCopy;
        }

        return new ArrayList<>(statsListCopy.subList(statsListCopy.size() - n, statsListCopy.size()));
    }

    public float getAverageDeliveries(long t1, long t2){
        ArrayList<GlobalStats> statsListCopy = getStatsList();
        ArrayList<GlobalStats> statsBetween = new ArrayList<GlobalStats>();

        for (GlobalStats gs : statsListCopy){
            if(gs.getTimestamp() >= t1 && gs.getTimestamp() <= t2){
                statsBetween.add(gs);
            }
        }

        float average=0;

        if(statsBetween.size() == 0){
            System.out.println("Non ci sono stats nel timeframe desiderato");
            return average;
        }

        for (GlobalStats gs : statsBetween){
            average += gs.getAverageDeliveries();
        }
        average /= statsBetween.size();

        return average;
    }

    public float getAverageKm(long t1, long t2){
        ArrayList<GlobalStats> statsListCopy = getStatsList();
        ArrayList<GlobalStats> statsBetween = new ArrayList<GlobalStats>();

        for (GlobalStats gs : statsListCopy){
            if(gs.getTimestamp() >= t1 && gs.getTimestamp() <= t2){
                statsBetween.add(gs);
            }
        }

        float average=0;

        if(statsBetween.size() == 0){
            System.out.println("Non ci sono stats nel timeframe desiderato");
            return average;
        }

        for (GlobalStats gs : statsBetween){
            average += gs.getAverageKm();
        }
        average /= statsBetween.size();

        return average;
    }

}
