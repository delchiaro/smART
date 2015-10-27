package micc.beaconav.outdoorEngine;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import micc.beaconav.FragmentHelper;
import micc.beaconav.outdoorEngine.Map;
import micc.beaconav.outdoorEngine.MuseumMarkerManager;
import micc.beaconav.R;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class MapFragment extends Fragment
{

    // Australia
    private static final LatLng startLatLng = new LatLng(-26.634145,134.620111);
    private static final float  startZoom = 3.5f;

//  Italy
//    private static final LatLng startLatLng = new LatLng(42.2226192,12.55);
//    private static final float  startZoom = 5.4f;

    private Map map; // Might be null if Google Play services APK is not available.
    private Context context;

    //private View myFragmentView = null;
    private ViewGroup container = null;


    public MapFragment(){}


    private MuseumMarkerManager manager = null;

    private Button buttonProximity;
    private Button buttonJson;
    private Button buttonLocation;
    private Button buttonSingleLocation;

// * * * * SET UP FRAGMENT * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *


    private void setUp()  {
//        buttonProximity      =  (Button) myFragmentView.findViewById(R.id.buttonProximity);
//        buttonJson           =  (Button) myFragmentView.findViewById(R.id.buttonJson);
//        buttonLocation       =  (Button) myFragmentView.findViewById(R.id.buttonLocation);
//        buttonSingleLocation =  (Button) myFragmentView.findViewById(R.id.buttonSingleLocation);
    }
    private void setUpMap() {
        map.setCamera(startLatLng, startZoom);
    }


    private GoogleMap getGMapFromXML() {
        // Try to obtain the map from the SupportMapFragment.
        return ( (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map) ).getMap();
    }







// * * * * SET UP EVENT LISTENER * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    public void setUpEventListeners() {

//        buttonJson.setOnClickListener(new View.OnClickListener()
//        {
//            @Override  public void onClick(View v) {
//                Intent intent = new Intent(context, JSONTest.class);
//                startActivity(intent);
//            }
//        });
//
//        buttonLocation.setOnClickListener(new View.OnClickListener()
//        {
//            @Override  public void onClick(View v) {
//                Intent intent = new Intent(context, testAdaptedLocationActivity.class);
//                startActivity(intent);
//            }
//        });
//        buttonSingleLocation.setOnClickListener(new View.OnClickListener()
//        {
//            @Override  public void onClick(View v) {
//                Intent intent = new Intent(context, testLastLocationActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        buttonProximity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) { toggleFakeProximity(v); }
//        });
    }

    public void onClickNavigate(View view)
    {
        map.route();
    }

    public void toggleFakeProximity() {
        map.resetLastProxyMuseum();
        if(map.getFakeProximity() == false)
            map.setFakeProximity(true);
        else map.setFakeProximity(false);

    }





// * * * * GETTERS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    public Map getMap(){
        return this.map;
    }




// * * * * OVERRIDE METHODS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//

//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (myFragmentView != null)
//        {
//            ViewGroup parent = (ViewGroup) myFragmentView.getParent();
//            if (parent != null) parent.removeView(myFragmentView);
//        }
//        try
//        {
//            myFragmentView = inflater.inflate(R.layout.fragment_map, container, false);
//        }
//        catch (InflateException e) { /* map is already there, just return view as it is */ }
//        FragmentHelper.instance().getMainActivity().hideMenuItemStopPath();
//
//        return myFragmentView;
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getArguments();
        context = this.getActivity();
        setUp();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        this.container = container;
        return inflater.inflate(R.layout.fragment_map, container, false);
        //return myFragmentView;
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        setUpEventListeners();
        this.manager = FragmentHelper.instance();
        //if(map == null) {
        map = new Map(getGMapFromXML(), manager);
        setUpMap();

        //}
    }

    @Override
    public void onPause() {
        super.onPause();
        map.stopLocalization();
        map.clearProximityNotificationTooltip();
        map.simulateUnselectMarker();
        map = null;
    }

    @Override
    public void onStop() {
        super.onStop();
//        map.stopLocalization();
//        map.clearProximityNotificationTooltip();

    }
}
