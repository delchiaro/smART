package micc.beaconav.indoorEngine.building;

import android.graphics.Canvas;
import android.graphics.PointF;

import java.util.Collection;
import java.util.Iterator;

import micc.beaconav.indoorEngine.spot.marker.Marker;
import micc.beaconav.indoorEngine.spot.marker.MarkerManager;
import micc.beaconav.util.containerContained.ContainerContained;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class Floor extends ContainerContained<Building, Room>  // extends Drawable
{
    private int _floorIndex = 0;

    // not used for now
    private PointF padding = new PointF(0,0);


    private MarkerManager<Marker> _markerManager = new MarkerManager<>();





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






    void addMarker(Marker marker) {
        this._markerManager.add(marker);
    }
    void addAllMarkers(Collection<Marker> markers) {
        this._markerManager.addAll(markers);
    }
    MarkerManager getMarkerManager() {
        return this._markerManager;
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