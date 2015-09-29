package micc.beaconav.indoorEngine.building.spot.marker;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 *
 */
public interface OnSpotMarkerSelectedListener< MS extends  MarkerSpot> {

    public void onMarkerSpotSelected( MS selectedMarker );
    public void onNullMarkerSpotSelected( );

}
