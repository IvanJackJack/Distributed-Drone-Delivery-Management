package ServerPkg;

import DronazonPkg.Drone;
import java.util.ArrayList;

public class StatsServiceResponse {
    public ArrayList<Drone> droneList;
    public ArrayList<GlobalStats> lastGlobalStats;
    public float average;

    public StatsServiceResponse() {
        droneList=new ArrayList<Drone>();
        lastGlobalStats=new ArrayList<GlobalStats>();
        average=0;
    }

    public ArrayList<Drone> getDroneList() {
        return droneList;
    }

    public void setDroneList(ArrayList<Drone> droneList) {
        this.droneList = droneList;
    }

    public ArrayList<GlobalStats> getLastGlobalStats() {
        return lastGlobalStats;
    }

    public void setLastGlobalStats(ArrayList<GlobalStats> lastGlobalStats) {
        this.lastGlobalStats = lastGlobalStats;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    @Override
    public String toString() {
        return "StatsServiceResponse{" +
                "droneList=" + droneList +
                ", lastGlobalStats=" + lastGlobalStats +
                ", average=" + average +
                '}';
    }
}
