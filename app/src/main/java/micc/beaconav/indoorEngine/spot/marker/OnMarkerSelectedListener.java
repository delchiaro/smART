package micc.beaconav.indoorEngine.spot.marker;

import android.support.annotation.NonNull;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 *
 */
public interface OnMarkerSelectedListener< MS extends Marker> {

    public void onMarkerSpotSelected( @NonNull MS selectedMarker );
    public void onNullMarkerSpotSelected( );

}
