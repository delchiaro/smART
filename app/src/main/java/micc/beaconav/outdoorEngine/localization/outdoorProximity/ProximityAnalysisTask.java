package micc.beaconav.outdoorEngine.localization.outdoorProximity;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nagash on 23/01/15.
 */
public class ProximityAnalysisTask
{

    private static boolean isAnalyzing = false;
    private static boolean hadAnalyzed = false;

    private ProximityManager manager;
    private LatLng myPosition;
    private ProximityObject[] proxObjects;
    private int radius;

    ProximityAnalysisTask(int radius, ProximityManager manager, LatLng myPosition, ProximityObject... proximityObjects)
    {
        this.manager = manager;
        this.proxObjects = proximityObjects;
        this.myPosition = myPosition;
        this.radius = radius;
    }

    public ProximityObject startAnalysis()
    {

        if(proxObjects != null && proxObjects.length >= 1)
        {
            Location currentLocation = new Location("currentLocation");
            currentLocation.setLatitude(myPosition.latitude);
            currentLocation.setLongitude(myPosition.longitude);

            Location testLocation = new Location("testLocation");
            testLocation.setLatitude(proxObjects[0].getLatitude());
            testLocation.setLongitude(proxObjects[0].getLongitude());

            ProximityObject best = proxObjects[0];
            float bestDistance = currentLocation.distanceTo(testLocation);
            float testDistance;

            for(int i = 1 ; i < proxObjects.length ; i++)
            {
                testLocation.setLatitude(proxObjects[i].getLatitude());
                testLocation.setLongitude(proxObjects[i].getLongitude());
                testDistance = currentLocation.distanceTo(testLocation);
                if(testDistance < bestDistance)
                {
                    bestDistance = testDistance;
                    best = proxObjects[i];
                }
            }

            if(bestDistance<radius)
                return best;
        }
        return null;
    }



    protected void onPostExecute(ProximityObject proximityObject) {

    }
}
