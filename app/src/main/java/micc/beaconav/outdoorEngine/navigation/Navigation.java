package micc.beaconav.outdoorEngine.navigation;


import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class Navigation
{
    List<Route> routes;




    public Navigation(JSONObject jObject )
    {


        try
        {
            routes = new ArrayList<Route>();

            JSONArray jRoutes  = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for(int i=0;i<jRoutes.length();i++)
            {
                JSONObject jsonRoute = (JSONObject)jRoutes.get(i);
                routes.add( new Route( jsonRoute ) );
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (Exception e) {}
    }


    public void draw(GoogleMap gmap, PolylineOptions polylineOptions, int routeIndex)
    {
        Route route = this.routes.get(routeIndex);
        route.draw(gmap, polylineOptions);
    }
}
