package ServerPkg;

import DronazonPkg.Drone;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class SmartCity {
    private static SmartCity instance;

    @XmlElement(name="droneList")
    private SortedSet<Drone> droneSet;

    public SmartCity() {
        droneSet=new TreeSet<Drone>();
    }

    public synchronized static SmartCity getInstance() {
        if(instance==null){
            instance=new SmartCity();
        }
        return instance;
    }

    public synchronized SortedSet<Drone> getDroneSet() {
        return new TreeSet<>(droneSet);
    }

    public synchronized boolean addByObject(Drone newDrone){

        for(Drone d:droneSet){
            if (d.getId()==newDrone.getId()){
                System.out.println("Drone con lo stesso ID già presente nella rete");
                return false;
            }
        }

        System.out.println("Aggiungo drone con id: " + newDrone.getId());
        droneSet.add(newDrone);
        printDroneSet();
        return true;
    }

    public void printDroneSet(){
        SortedSet<Drone> droneSetCopy=getDroneSet();
        if(droneSetCopy.size()==0){
            System.out.println("Nella SmartCity non ci sono droni");
            return;
        }
        System.out.println("Nella SmartCity ci sono i seguenti droni: ");
        for (Drone d : droneSetCopy){
            System.out.println(d.toString());
        }
    }

    public synchronized boolean removeByObject(Drone targetDrone){
        if(!droneSet.contains(targetDrone)){
            System.out.println("Drone non presente nella rete");
            return false;
        }
        System.out.println("Rimuovo drone con id: " + targetDrone.getId());
        droneSet.remove(targetDrone);
        printDroneSet();
        return true;
    }

    public synchronized boolean removeById(int targetId){
        for(Drone d:droneSet){
            if (d.getId()==targetId){
                return removeByObject(d);
            }
        }
        System.out.println("Drone non trovato");
        return false;
    }

    public ArrayList<Drone> getDroneList(){
        ArrayList<Drone> droneList=new ArrayList<>(getDroneSet());

        if(droneSet.size()==0){
            System.out.println("Nella SmartCity non ci sono droni");
        }

        return droneList;
    }

}
