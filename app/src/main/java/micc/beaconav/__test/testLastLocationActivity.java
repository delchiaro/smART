package micc.beaconav.__test;

//FULL example with single locationa ccess (not continuous update)


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import micc.beaconav.R;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class testLastLocationActivity extends FragmentActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {
    LocationClient mLocationClient;
    LocationRequest locationRequest; // serve?

    private TextView addressLabel;
    private TextView locationLabel;
    private Button getLocationBtn;
    private Button disconnectBtn;
    private Button connectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_last_location);

        locationLabel = (TextView) findViewById(R.id.locationLabel);
        addressLabel = (TextView) findViewById(R.id.addressLabel);
        getLocationBtn = (Button) findViewById(R.id.getLocation);
        disconnectBtn = (Button) findViewById(R.id.disconnect);
        connectBtn = (Button) findViewById(R.id.connect);

        setUpEventManager();

        // Create the LocationRequest object
        mLocationClient = new LocationClient(this, this, this);
        locationRequest = new LocationRequest();
        // Use high accuracy
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        locationRequest.setInterval(5000);
        // Set the fastest update interval to 1 second
        locationRequest.setFastestInterval(1000);

    }

    private void setUpEventManager() {
        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                displayCurrentLocation();
            }
        });


        disconnectBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                disconnect();
            }
        });

        connectBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                connect();
            }
        });

    }

    // EVENT MANAGER ! ! !
    private void disconnect() {
        mLocationClient.disconnect();
        locationLabel.setText("Got disconnected....");
    }

    private void connect() {
        mLocationClient.connect();
        locationLabel.setText("Got connected....");
    }

    public void displayCurrentLocation() {
        this.displayCurrentLocation(true);
    }

    public void displayCurrentLocation(boolean displayAddress) {
        // TODO: GESTIRE ECCEZIONE (Force Close se GPS non e` attivo)
        // Get the current location's latitude & longitude
        Location currentLocation = mLocationClient.getLastLocation();
        String msg = "Current Location: " +
                Double.toString(currentLocation.getLatitude()) + "," +
                Double.toString(currentLocation.getLongitude());

        // Display the current location in the UI
        locationLabel.setText(msg);

        // To display the current address in the UI
        if (displayAddress) (new GetAddressTask(this)).execute(currentLocation);
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        this.connect();
    }

    @Override
    protected void onStop() {
        // Disconnect the client.
        super.onStop();
        this.disconnect();
    }

    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

        Location location = mLocationClient.getLastLocation();
        if (location == null)
            mLocationClient.requestLocationUpdates(locationRequest, this);
        else
            Toast.makeText(this, "Location: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Display the error code on failure
        Toast.makeText(this, "Connection Failure : " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocationClient.removeLocationUpdates(this);
        displayCurrentLocation(true);
        // Use the location here!!!
    }

    /*
     * Following is a subclass of AsyncTask which has been used to get
     * address corresponding to the given latitude & longitude.
     */
    private class GetAddressTask extends AsyncTask<Location, Void, String> {
        Context mContext;

        public GetAddressTask(Context context) {
            super();
            mContext = context;
        }

        /*
         * When the task finishes, onPostExecute() displays the address.
         */
        @Override
        protected void onPostExecute(String address) {
            // Display the current address in the UI
            addressLabel.setText(address);
        }

        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder =
                    new Geocoder(mContext, Locale.getDefault());
            // Get the current location from the input parameter list
            Location loc = params[0];
            // Create a list to contain the result address
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
            } catch (IOException e1) {
                Log.e("LocationSampleActivity",
                        "IO Exception in getFromLocation()");
                e1.printStackTrace();
                return ("IO Exception trying to get address");
            } catch (IllegalArgumentException e2) {
                // Error message to post in the log
                String errorString = "Illegal arguments " +
                        Double.toString(loc.getLatitude()) +
                        " , " +
                        Double.toString(loc.getLongitude()) +
                        " passed to address service";
                Log.e("LocationSampleActivity", errorString);
                e2.printStackTrace();
                return errorString;
            }
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {
                // Get the first address
                Address address = addresses.get(0);
            /*
            * Format the first line of address (if available),
            * city, and country name.
            */
                String addressText = String.format(
                        "%s, %s, %s",
                        // If there's a street address, add it
                        address.getMaxAddressLineIndex() > 0 ?
                                address.getAddressLine(0) : "",
                        // Locality is usually a city
                        address.getLocality(),
                        // The country of the address
                        address.getCountryName());
                // Return the text
                return addressText;
            } else {
                return "No address found";
            }
        }
    }// AsyncTask class
}