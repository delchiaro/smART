package micc.beaconav.indoorEngine.building;

import android.graphics.Canvas;
import android.graphics.PointF;

import java.util.Collection;
import java.util.Iterator;

import micc.beaconav.indoorEngine.building.spot.marker.MarkerSpot;
import micc.beaconav.indoorEngine.building.spot.marker.MarkerSpotManager;
import micc.beaconav.indoorEngine.building.spot.path.PathSpot;
import micc.beaconav.indoorEngine.building.spot.path.PathSpotManager;
import micc.beaconav.util.containerContained.ContainerContained;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class Floor extends ContainerContained<Building, Room>  // extends Drawable
{
    private int _floorIndex = 0;

    // not used for now
    private PointF padding = new PointF(0,0);


    private PathSpotManager<PathSpot> _pathSpotManager = new PathSpotManager<>();
    private MarkerSpotManager<MarkerSpot> _markerSpotManager = new MarkerSpotManager<>();





    public Floor() {}
	public Floor(int floorIndex) {
        this._floorIndex = floorIndex;
    }
    public Floor(int floorIndex, PointF padding) {
        this._floorIndex = floorIndex;
        this.padding = padding;
    }


    public final int getFloorIndex() {
        return _floorIndex;
    }






    public void addMarker(MarkerSpot marker) {
        this._markerSpotManager.add(marker);
    }
    public void addAllMarkers(Collection<MarkerSpot> markers) {
        this._markerSpotManager.addAll(markers);
    }
    public MarkerSpotManager getMarkerManager() {
        return this._markerSpotManager;
    }




    public void addPathSpot(PathSpot pathSpot) {
        this._pathSpotManager.add(pathSpot);
    }
    public void addAllPathSpot(Collection<PathSpot> pathSpot) {
        this._pathSpotManager.addAll(pathSpot);
    }
    public PathSpotManager getPathSpotManager() {
        return this._pathSpotManager;
    }







    public void draw(Canvas canvas) {

        Iterator<Room> roomIter = super.getIterator();
        while(roomIter.hasNext())
        {
            roomIter.next().drawWalls(canvas, padding); //delego disegno ad ogni stanza
        }


        roomIter = super.getIterator();
        while(roomIter.hasNext())
        {
            roomIter.next().drawDoorsAndAperture(canvas, padding); //delego disegno ad ogni stanza
        }


    }

    public final Building getContainerBuilding() {
        return super.getContainer();
    }

}