package micc.beaconav.outdoorEngine.localization.outdoorProximity;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class ProximityManager
{
    private boolean analyzing = false;

    private ProximityObject[] proxObjects;
    private int proximityRadius;

    ConcurrentLinkedQueue<ProximityObject> skimmedProxyObjects = null;
    private Location skimmingLocation = null;
    private int skimmingRadius;


    ProximityObject lastProximityObject = null;
    ProximityNotificationHandler handler;
    ProximityAsyncTask asyncTask = null;


    LinkedList<ProximityAnalysisTask> tasks = new LinkedList<>();









    public ProximityManager(int proximityRadius, int skimmingRadius, ProximityNotificationHandler handler, ProximityObject[] objects)
    {
        this.handler = handler;
        this.proxObjects = objects;
        this.proximityRadius = proximityRadius;
        this.skimmingRadius = skimmingRadius;
    }
    public ProximityManager(int proximityRadius, int skimmingRadius,ProximityNotificationHandler handler)
    {
        this(proximityRadius, skimmingRadius, handler, null);
    }






    public boolean pushProximityAnalysis(double myLat, double myLong)
    {
        Location myLoc = new Location("myLocation");
        myLoc.setLatitude(myLat);
        myLoc.setLongitude(myLong);

        if(skimmingLocation == null || myLoc.distanceTo(skimmingLocation) > skimmingRadius - proximityRadius)
        {
            makeNewSkimming(myLoc);
        }


        if(skimmedProxyObjects != null && skimmedProxyObjects.size() > 0)
        {
            ProximityAnalysisTask task = new ProximityAnalysisTask(proximityRadius, this, new LatLng(myLat, myLong),
                    skimmedProxyObjects.toArray(new ProximityObject[skimmedProxyObjects.size()]));
            //task.startAnalysis();
            tasks.push(task);

//            if(analyzing == false )
//                startAnalysis();

            return true;
        }

        else return false;
    }





    public void startAnalysis() {
        if(analyzing == false )
        {
            this.asyncTask = new ProximityAsyncTask(this);
            this.asyncTask.execute("null");
            analyzing = true;
        }
    }

    public void stopAnalysis() {
        this.analyzing = false;
    }
    public void abortAnalysis() {
        analyzing = false;
        if(asyncTask != null)
        {
            asyncTask.cancel(true);
            asyncTask = null;
        }
    }

    void onProximityAnalysisExecuted(ProximityObject object)
    {
        if(lastProximityObject != object)
        {
            this.lastProximityObject = object;
            this.handler.handleProximityNotification(object);
        }

        if(analyzing == true)
        {
            asyncTask = new ProximityAsyncTask(this);
            asyncTask.execute("null");
        }
    }




    private void makeNewSkimming(Location newSkimmingLoc)
    {
        this.skimmedProxyObjects = new ConcurrentLinkedQueue<>();
        if(proxObjects != null && proxObjects.length > 0)
        {
            Location loc = new Location("testLoc");
            for(ProximityObject po : proxObjects)
            {
                loc.setLatitude(po.getLatitude());
                loc.setLongitude(po.getLongitude());

                if (newSkimmingLoc.distanceTo(loc) < this.skimmingRadius)
                {
                    skimmedProxyObjects.add(po);
                    this.skimmingLocation = newSkimmingLoc;
                }
            }
        }
    }





    public ProximityObject getLastProximityObject()
    {
        return lastProximityObject;
    }


    public void setProximityObjects(ProximityObject[] objects)
    {
        this.proxObjects = objects;
    }





}
