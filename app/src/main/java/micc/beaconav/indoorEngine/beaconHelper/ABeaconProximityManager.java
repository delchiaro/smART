package micc.beaconav.indoorEngine.beaconHelper;

import android.app.Activity;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Utils;

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public abstract class ABeaconProximityManager implements BeaconProximityListener {


    private static int BEACON_DISTANCE = 2; // in metri

    BeaconBestProximityListener proximityBestListener;

    BeaconHelper helper;

    public ABeaconProximityManager (Activity activity, BeaconBestProximityListener listener) {
        proximityBestListener = listener;
        helper  = new BeaconHelper(activity);
        helper.addProximityListener(this);
    }

    public void scan() {
        helper.scanBeacons();
    }
    public void stopScan() {
        helper.stopScan();
    }







    public abstract void OnBeaconProximity(List<Beacon> proximityBeacons);






    protected static Beacon getNearestBeacon(List<Beacon> beacons) {

        if (beacons != null)
        {
            TreeMap<Double, Beacon> beaconsSortedMap = new TreeMap<>();

            for (Beacon b : beacons)
            {
                beaconsSortedMap.put(getDistance(b), b);
            } // ordino in un TreeMap i beacon per distanza

            Iterator<Beacon> beaconIter = beaconsSortedMap.values().iterator();
            if(beaconIter.hasNext())
                return beaconIter.next();// ritorno il primo (più vicino, distanza minore) o  null
            else return null;
        }
        else return null;
    }

    protected static double getDistance(Beacon beacon) {
        return Utils.computeAccuracy(beacon);
    }

    protected static boolean isInProximity(Beacon beacon)
    {
        double distance = getDistance(beacon);
        if(distance <= BEACON_DISTANCE)
            return true;
        else return false;
//        if(Utils.computeProximity(beacon) != Utils.Proximity.NEAR) // NEAR <= 0.5m
//            return true;
//        else return false;
    }



    public final static int getID(int major, int  minor) {
        return ((major<<16) + minor);
    }

    public final static int getID(Beacon beacon) {
        return getID(beacon.getMajor(), beacon.getMinor());
    }



}
