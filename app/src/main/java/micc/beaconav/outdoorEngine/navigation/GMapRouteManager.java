package micc.beaconav.outdoorEngine.navigation;

import com.google.android.gms.maps.model.LatLng;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONObject;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class GMapRouteManager
{
    // TODO: includere le procedure per il parsing json e richieste ecc..


    public Navigation requestRoute(LatLng origin, LatLng dest)
    {
        Navigation navigation = null;

        String directionUrlRequest = getDirectionsUrl(origin, dest);
        String jsonData = "";  // For storing data from web service
        try
        {
            jsonData = downloadUrl(directionUrlRequest);  // Fetching the data from web service

        }
        catch(Exception e)
        {
            Log.d("Background Task",e.toString());
        }

        JSONObject jObject;

        try
        {
            jObject = new JSONObject(jsonData);
            navigation = new Navigation(jObject);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return navigation;

    }

    // REQUEST METHODS

    private String getDirectionsUrl(LatLng origin, LatLng dest)
    {
        return getDirectionsUrl(origin, dest, null);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest, List<LatLng> waypoints)
    {
        String str_origin = "origin="+origin.latitude+","+origin.longitude;     // Origin of routex
        String str_dest = "destination="+dest.latitude+","+dest.longitude;      // Destination of route
        String sensor = "sensor=false";// Sensor enabled
        String str_key = ""; //"key=AIzaSyAntHiY1HUIX-0_xvxW1K08DX4cTMJ5zSE";
        // Waypoints

        String str_waypoints = "";
        if( waypoints != null )
        {
            for (int i = 2; i < waypoints.size(); i++) {
                LatLng point = waypoints.get(i);
                if (i == 2) str_waypoints = "waypoints=";
                str_waypoints += point.latitude + "," + point.longitude + "|";
            }
        }

        // building the parameters to the web service
        String parameters = str_origin + "&" + str_dest+ "&" + str_key + "&" + sensor + "&" + str_waypoints;

        // Output format
        String output = "json";

        // building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        return url;
    }

    private String downloadUrl(String strUrl) throws IOException
    {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try
        {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();   // Creating an http connection to communicate with url
            urlConnection.connect();                                    // Connecting to url

            iStream = urlConnection.getInputStream();                   // Reading data from url

            BufferedReader  br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer    sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null)
            {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }
        catch(Exception e)
        {
            Log.d("Exception while downloading url", e.toString());
        }
        finally
        {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


}
