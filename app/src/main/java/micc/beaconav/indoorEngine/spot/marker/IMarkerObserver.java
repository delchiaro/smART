package micc.beaconav.indoorEngine.spot.marker;

import android.support.annotation.NonNull;

/**
 * Created by Nagash on 10/8/2015.
 */
public interface IMarkerObserver {
    public void onMarkerSelected( @NonNull Marker selectedMarker );
    public void onMarkerDeselected( @NonNull Marker selectedMarker );
    public void onMarkerReselected( @NonNull Marker selectedMarker );
}
