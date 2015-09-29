package micc.beaconav.outdoorEngine.navigation;


import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class Route
{
    private List<Leg> legs;


    public Route(JSONObject jRoute ) throws JSONException
    {
        legs = new ArrayList<Leg>();

        JSONArray jLegs = jRoute.getJSONArray("legs");
        for(int j=0 ; j<jLegs.length() ; j++)
        {
            JSONObject jsonLeg = (JSONObject)jLegs.get(j);
            legs.add(new Leg(jsonLeg ));
        }

    }

    public void draw(GoogleMap gmap,PolylineOptions polylineOptions)
    {
        for(int i = 0 ; i < legs.size() ; i++ )
        {
            legs.get(i).draw(gmap, polylineOptions);
        }
    }


}
