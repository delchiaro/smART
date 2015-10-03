package micc.beaconav.indoorEngine.spot.marker;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 *
 */
public interface OnMarkerSelectedListener< MS extends Marker> {

    public void onMarkerSpotSelected( MS selectedMarker );
    public void onNullMarkerSpotSelected( );

}
