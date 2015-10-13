package micc.beaconav.indoorEngine.building;

import android.support.annotation.NonNull;

import micc.beaconav.indoorEngine.spot.Spot;
import micc.beaconav.indoorEngine.spot.marker.Marker;
import micc.beaconav.indoorEngine.spot.marker.IMarkerObserver;

/**
 * Created by Nagash on 10/3/2015.
 */
public class Position implements InroomObject, IMarkerObserver {


    private Room _room;
    private ConvexArea _convexArea;
    private Spot _spot;


    public Position(Spot spot, Room room) throws Marker.IrreplaceableObserverException {
        this._spot = spot;
        this._room = room;
        if(this._spot instanceof Marker)
        {
            ((Marker)this._spot).setObserver(this);
        }
    }

    @Override
    public Room getRoomContainer() {
        return _room;
    }

    @Override
    public ConvexArea getConvexAreaContainer() {
        return _convexArea;
    }



    @Override
    public void onMarkerSelected(@NonNull Marker selectedMarker) {

    }

    @Override
    public void onMarkerDeselected(@NonNull Marker selectedMarker) {

    }

    @Override
    public void onMarkerReselected(@NonNull Marker selectedMarker) {

    }
}
