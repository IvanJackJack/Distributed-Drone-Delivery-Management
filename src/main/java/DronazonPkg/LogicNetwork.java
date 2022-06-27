package DronazonPkg;

import java.util.*;
import java.util.ArrayList;
import java.util.SortedSet;

public class LogicNetwork {
    public ArrayList<Drone> droneList;

    public LogicNetwork() {    }

    public synchronized ArrayList<Drone> getDroneList() {
        return droneList;
    }

    public synchronized int getDroneListSize(){
        return droneList.size();
    }

    public void setDroneList(SortedSet<Drone> droneSet) {
        droneList = new ArrayList<>(droneSet);
    }

    public synchronized void add(Drone newDrone){
        //nel caso ho in memoria un drone che è uscito e poi ne è entrato un'altro con lo stesso ID, cancello quello vecchio
        for (Drone d : droneList){
            if(d.getId() == newDrone.getId()){
                droneList.remove(d);
                break;
            }
        }
        droneList.add(newDrone);
        droneList.sort(Comparator.comparing(Drone::getId));
    }

    public synchronized void removeById(int targetId){
        for (Drone d : droneList){
            if(d.getId() == targetId){
                droneList.remove(d);
                droneList.sort(Comparator.comparing(Drone::getId));
                return;
            }
        }
    }

    public void printDroneList(String source){
        System.out.println("\n" + source + " - I droni che ho in memoria sono: ");
        ArrayList<Drone> droneListCopy = new ArrayList<>(getDroneList());
        for (Drone d : droneListCopy){
            System.out.println(d.toString());
        }
    }

    public Drone nextDrone(Drone me){
        ArrayList<Drone> droneListCopy = new ArrayList<>(getDroneList());
        int size=droneListCopy.size();
        int myIndex=0;

        for (Drone d : droneListCopy){
            if(me.getId() == d.getId()){
                break;
            }
            myIndex++;
        }

        return droneListCopy.get((myIndex + 1) % size);
    }

    public Drone getById(int id){
        ArrayList<Drone> droneListCopy = new ArrayList<>(getDroneList());

        for (Drone d : droneListCopy){
            if(id == d.getId()){
                return d;
            }
        }
        return null;
    }

    public synchronized void updatePosition(Drone droneToUpdate){
        for (Drone d : droneList){
            if(droneToUpdate.getId() == d.getId()){
                d.setPosition(droneToUpdate.getPosition());
                break;
            }
        }
    }

    public synchronized void updateEnergy(Drone droneToUpdate){
        for (Drone d : droneList){
            if(droneToUpdate.getId() == d.getId()){
                d.setEnergy(droneToUpdate.getEnergy());
                break;
            }
        }
    }

    public synchronized void updateDeliveryStatus(Drone droneToUpdate){
        for (Drone d : droneList){
            if(droneToUpdate.getId() == d.getId()){
                d.setDelivering(droneToUpdate.isDelivering());
                break;
            }
        }
    }

}
