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
public class Leg
{
    List<Step> steps;


    public Leg(JSONObject jLeg ) throws JSONException
    {
        steps = new ArrayList<Step>();
        JSONArray jSteps = jLeg.getJSONArray("steps");
        for(int j=0 ; j<jSteps.length() ; j++)
        {
            JSONObject jsonStep = (JSONObject)jSteps.get(j);
            steps.add(new Step(jsonStep));
        }
    }

    public void draw(GoogleMap gmap, PolylineOptions polylineOptions)
    {
        for(int i = 0 ; i < steps.size() ; i++ )
        {
            steps.get(i).draw(gmap, polylineOptions);
        }
    }


}
