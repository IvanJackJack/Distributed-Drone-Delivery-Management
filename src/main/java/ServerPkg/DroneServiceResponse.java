package ServerPkg;

import DronazonPkg.Drone;
import DronazonPkg.Position;
import java.util.Random;
import java.util.SortedSet;

public class DroneServiceResponse {
    public SortedSet<Drone> droneSetCopy;
    public Position position;

    public DroneServiceResponse(){  }

    public DroneServiceResponse(SortedSet<Drone> droneSetCopy) {
        this.droneSetCopy = droneSetCopy;
        position =new Position(new Random().nextInt(10),new Random().nextInt(10));
    }

    public SortedSet<Drone> getDroneSetCopy() {
        return droneSetCopy;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "DroneServiceResponse{" +
                "droneSetCopy=" + droneSetCopy +
                ", pos=" + position +
                '}';
    }
}
