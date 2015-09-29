package micc.beaconav.outdoorEngine.navigation;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;
import java.util.ArrayList;
import com.google.android.gms.maps.GoogleMap;


/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class Step
{

    List<LatLng>  points;

    public Step(JSONObject jStep) throws JSONException
    {
        String polyline = (String) (  (JSONObject) ( jStep.get("polyline")  )).get("points");
        List<LatLng> list = decodePoly(polyline);
        this.points = new ArrayList<LatLng>(list);
    }


    public void draw(GoogleMap gmap, PolylineOptions polylineOptions)
    {
           polylineOptions.addAll(points);
           gmap.addPolyline(polylineOptions);
    }




    private List<LatLng> decodePoly(String encoded)
    {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

}
