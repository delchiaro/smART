package micc.beaconav.indoorEngine.building;

import micc.beaconav.indoorEngine.building.ConvexArea;
import micc.beaconav.indoorEngine.building.Room;

/**
 * Created by Nagash on 10/8/2015.
 */
public interface InroomObject {
    Room getRoomContainer();
    ConvexArea getConvexAreaContainer();
}
