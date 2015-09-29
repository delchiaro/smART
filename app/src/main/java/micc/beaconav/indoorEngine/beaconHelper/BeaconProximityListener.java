package micc.beaconav.indoorEngine.beaconHelper;

import com.estimote.sdk.Beacon;

import java.util.List;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public interface BeaconProximityListener {

    public void OnBeaconProximity(List<Beacon> proximityBeacons);


}
