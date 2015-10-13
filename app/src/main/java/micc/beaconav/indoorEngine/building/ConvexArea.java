package micc.beaconav.indoorEngine.building;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import micc.beaconav.indoorEngine.spot.marker.Marker;
import micc.beaconav.indoorEngine.spot.marker.IMarkerObserver;
import micc.beaconav.util.containerContained.Contained;

/**
 * Created by nagash on 25/09/15.
 */
public class ConvexArea extends Contained<Room> implements IMarkerObserver
{

    private ArrayList<Marker> _markers = new ArrayList<>();
    private Marker _markerSelected = null;


    final public Room getContainerRoom() {
        return super.getContainer();
    }
    final public Building getCointainerBuilding() {
        return super.getContainer().getCointainerBuilding();
    }
    final public Floor getContainerFloor() {
        return super.getContainer().getContainerFloor();
    }




    public void addMarker(Marker marker) throws Marker.IrreplaceableObserverException {
        // TODO: controlla che il marker sia veramente all'interno di questa stanza!

        this._markers.add(marker);
        this.getContainerFloor().addMarker(marker);
        marker.setObserver(this);

    }

    @Override
    public void  onMarkerSelected(@NonNull Marker selectedMarker) {
        _markerSelected = selectedMarker;
    }

    @Override
    public void onMarkerDeselected(@NonNull Marker selectedMarker) {
        _markerSelected = null;
    }

    @Override
    public void onMarkerReselected(@NonNull Marker selectedMarker) {

    }
}
