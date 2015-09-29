package micc.beaconav.outdoorEngine.localization;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Nagash on 03/01/2015.
 */
public class GoogleLocalizationAdapter implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{



    private final String TAG = "GoogleLocalizationAdapter_DEBUG";

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private GoogleLocalizationAdaptedActivity observerContext;


    public GoogleLocalizationAdapter(Context context, GoogleLocalizationAdaptedActivity adapterContext)
    {
        this.observerContext = adapterContext;
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }



    public void start(){
        mGoogleApiClient.connect();
    }

    public void stop(){
        mGoogleApiClient.disconnect();
    }



    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection has failed");
    }

    @Override
    public void onLocationChanged(Location location){
        // TODO: prendere la posizione location e utilizzarla in qualche modo, fornendola verso l'esterno in un formato utilizzabile (adapting)
        // mLocationView.setText("Location received: " + location.toString());

        this.observerContext.onLocationChanged(location);

    }
}
