package micc.beaconav.outdoorEngine;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.maps.android.SphericalUtil;
import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipView;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import micc.beaconav.FragmentHelper;
import micc.beaconav.R;
import micc.beaconav.db.dbHelper.DbManager;
import micc.beaconav.db.dbHelper.museum.MuseumRow;
import micc.beaconav.db.dbJSONManager.JSONHandler;
import micc.beaconav.outdoorEngine.navigation.GMapRouteManager;
import micc.beaconav.outdoorEngine.navigation.Navigation;
import micc.beaconav.outdoorEngine.localization.outdoorProximity.ProximityManager;
import micc.beaconav.outdoorEngine.localization.outdoorProximity.ProximityNotificationHandler;
import micc.beaconav.outdoorEngine.localization.outdoorProximity.ProximityObject;


/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class Map implements JSONHandler<MuseumRow>, ProximityNotificationHandler
{

    private final static int PROXIMITY_RADIUS = 300;//50;//26 // in  metri
    private final static int PROXIMITY_SKIMMING_RADIUS = 2000; // in metri





    private boolean neverLocalized = true;

    private final static Context context = FragmentHelper.instance().getMainActivity();

    private ToolTip toolTip = new ToolTip()
            .withText(context.getString(R.string.outdoor__message__tooltip_museum_proximity))
            .withTextColor(Color.WHITE)
            .withColor(FragmentHelper.instance().getMainActivity().getResources().getColor(R.color.material_red))
            .withAnimationType(ToolTip.AnimationType.FROM_MASTER_VIEW);

    private ToolTipView toolTipView;




    private GoogleMap gmap; // Might be null if Google Play services APK is not available.


    private Marker selectedMuseumMarker = null;
    MarkerOptions museumMarkerOptions = new MarkerOptions();
    MarkerOptions selectedMuseumMarkerOptions = new MarkerOptions();


    private List<MuseumRow> rows = null;
    private BiMap<Marker, MuseumRow>  museumMarkersMap = null;

    private MuseumMarkerManager markerManager = null;


    private boolean polyline = false;

    private LatLng lastLocation = null;

    private Circle circle;
    private CircleOptions circleOptions;
    private LatLngBounds latLngBounds;


    private ProximityManager proximityManager;
    private boolean fakeProximity = false;
    private ProximityObject lastProxyMuseum = null;




    public Map(GoogleMap mMap, MuseumMarkerManager markerManager)
    {
        this.gmap = mMap;
        this.neverLocalized = true;
        this.gmap.setMyLocationEnabled(true);
        this.gmap.getUiSettings().setZoomControlsEnabled(false);
        FragmentHelper fh = FragmentHelper.instance();
        this.gmap.setPadding(fh.dpToPx(5),  fh.dpToPx(5), fh.dpToPx(5), fh.dpToPx(5));

        this.markerManager = markerManager;

        startLocalization();

        setUpDbObjects();
        setUpEvents();


        LatLng coord = new LatLng(43.8007117, 11.2435291);
        // Instantiates a new CircleOptions object and defines the center and radius
        circleOptions = new CircleOptions()
                .center(new LatLng(37.4, -122.1))
                .radius(0)// In meters
                .strokeColor(Color.parseColor("#FF9800"))
                .strokeWidth(5)
                .fillColor(Color.parseColor("#20FFA726"));
        // Get back the mutable Circle
        circle = gmap.addCircle(circleOptions);

        museumMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        selectedMuseumMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
    }

    private void setUpDbObjects()
    {
        DbManager.museumDownloader.addHandler(this);
        DbManager.museumDownloader.startDownload();
    }

    @Override
    public void onJSONDownloadFinished(MuseumRow[] result) {

        this.rows = Arrays.asList(result);
        drawMarkers();

        if(rows != null && proximityManager!= null)
          proximityManager.setProximityObjects(rows.toArray(new ProximityObject[rows.size()]));
    }




    public void setCamera(LatLng newLatLng, float newZoom){
       gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, newZoom));
    }
    public void setCamera(LatLng newLatLng){
        gmap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng));
    }

    public void zoomOnLatLng(LatLng latLng, float zoom)
    {
        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }
    public void goOnLatLng(LatLng latLng)
    {
        gmap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void handleProximityNotification(final ProximityObject object)
    {

        if(this.fakeProximity)
        {
            if (this.lastProxyMuseum == null || this.lastProxyMuseum != null && this.lastProxyMuseum != object)
            {
                zoomOnLatLng(new LatLng(object.getLatitude(), object.getLongitude()), 16);
                this.lastProxyMuseum = object;
            }
            else lastProxyMuseum = object;
        }
        else
        {
            if(object == null)
            {
                FragmentHelper.instance().getMainActivity().getFloatingActionButtonNotifyToIndoor().setVisibility(View.INVISIBLE);
                toolTipView.remove();
            }
            else
            {
                FragmentHelper.instance().getMainActivity().getFloatingActionButtonNotifyToIndoor().setVisibility(View.VISIBLE);
                toolTipView = FragmentHelper.instance().getMainActivity().getToIndoorTooltipContainer().showToolTipForView(toolTip, FragmentHelper.instance().getMainActivity().findViewById(R.id.notifyToIndoor));

                FragmentHelper.instance().getMainActivity().getFloatingActionButtonNotifyToIndoor().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (object instanceof MuseumRow) {
                            FragmentHelper.instance().showIndoorFragment((MuseumRow) object);

                            FragmentHelper.instance().getMainActivity().getFloatingActionButtonNotifyToIndoor().setVisibility(View.INVISIBLE);

                            toolTipView.remove();
                        }
                    }
                });
            }


        }
    }

    
    public void setFakeProximity(boolean val){
        this.fakeProximity = val;
    }
    public boolean getFakeProximity(){
        return this.fakeProximity;
    }
    public void resetLastProxyMuseum(){
        this.lastProxyMuseum = null;
    }


    public void clearProximityNotificationTooltip()
    {
        if(this.toolTipView != null)
            this.toolTipView.remove();
    }



    private void setUpEvents()
    {

        gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {
                simulateMarkerClick(marker);
                return true;
            }
        } );

        gmap.setOnMapLongClickListener(new OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {
                unsetMuseumMarker();
            }
        });
        gmap.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                    unsetMuseumMarker();
            }
        });

        gmap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

               // if(location!=null) {
                    lastLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    circle.setCenter(lastLocation);
                    if(proximityManager != null)
                        proximityManager.pushProximityAnalysis(location.getLatitude(), location.getLongitude());

                    if (neverLocalized) {
                        neverLocalized = false;
                        //setCamera(lastLocation, 4);
                        zoomOnLatLng(lastLocation, 14);
                    }
               // }
            }
        });


    }

    public void setCircleRadius(int radius)
    {
        circle.setRadius(radius);
        if( getCurrentLatLng() != null)
        {
            latLngBounds = new LatLngBounds.Builder().
                    include(SphericalUtil.computeOffset(getCurrentLatLng(), radius, 0)).
                    include(SphericalUtil.computeOffset(getCurrentLatLng(), radius, 90)).
                    include(SphericalUtil.computeOffset(getCurrentLatLng(), radius, 180)).
                    include(SphericalUtil.computeOffset(getCurrentLatLng(), radius, 270)).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, 3);
            gmap.animateCamera(cameraUpdate);
        }
    }



    private final void setSelectedMuseumMarker(Marker selectedMarker)
    {
        this.selectedMuseumMarker = selectedMarker;
        drawMarkers();
    }

    public final void unsetMuseumMarker()
    {
        if(selectedMuseumMarker != null)
        {
            selectedMuseumMarker = null;
            drawMarkers();
            markerManager.onDeselectMuseumMarker();
        }
    }

    public final Marker getSelectedMuseumMarker() {
        return selectedMuseumMarker;
    }
    public final MuseumRow getSelectedMuseumRow() {
        if( this.selectedMuseumMarker != null )
            return museumMarkersMap.get(this.selectedMuseumMarker);
        else return null;
    }
    public LatLng getSelectedMuseumLatLng(){
        if(this.selectedMuseumMarker != null)
            return selectedMuseumMarker.getPosition();
        else return null;
    }



    public final Location getCurrentLocation()
    {
        return this.gmap.getMyLocation();
    }
    public final LatLng getCurrentLatLng()
    {
        if(this.getCurrentLocation() != null)
            return new LatLng(this.getCurrentLocation().getLatitude(), this.getCurrentLocation().getLongitude());
        else return null;
    }






    protected void drawMarkers()
    {
        if(this.polyline)
        {
            initMapDrawing();
        }



        if(this.rows != null)
        {
            if(this.museumMarkersMap != null )
            {
                Iterator<Marker> iter = this.museumMarkersMap.keySet().iterator();
                while(iter.hasNext())
                {
                    iter.next().remove();
                }

            }

            this.museumMarkersMap = HashBiMap.create();

            Iterator<MuseumRow> iter = this.rows.iterator();
            while(iter.hasNext())
            {
                MuseumRow row = iter.next();
                museumMarkerOptions.position(new LatLng(row.getLatitude(), row.getLongitude()));
                Marker addedMarker = gmap.addMarker(museumMarkerOptions);
                this.museumMarkersMap.put( addedMarker, row);
            }

            if(this.selectedMuseumMarker != null)
            {
                MuseumRow removedRow = museumMarkersMap.get(selectedMuseumMarker);
                museumMarkersMap.remove(selectedMuseumMarker);
                // rimuovo il marker dalla mappa salvandomi il relativo MuseumRow

                selectedMuseumMarkerOptions.position(selectedMuseumMarker.getPosition());
                selectedMuseumMarker.remove();
                // rimuovo il marker da google maps (gmap) salvandomi la posizioni

                selectedMuseumMarker = gmap.addMarker(selectedMuseumMarkerOptions);
                // riinserisco in google map il marker

                museumMarkersMap.put(selectedMuseumMarker, removedRow);
                //riinserisco il marker-museumRow nella mappa
            }
        }
    }

    protected void initMapDrawing()
    {
        gmap.clear();
        this.polyline = false;
        if(lastLocation != null)
            circleOptions.center(lastLocation);

        circle = gmap.addCircle(circleOptions);

    }





    public void simulateUnselectMarker() {
        setSelectedMuseumMarker(null);
    }
    public void simulateMarkerClick(Marker markerToClick) {
        MuseumRow row = museumMarkersMap.get(markerToClick);
        goOnLatLng(markerToClick.getPosition());
        markerManager.onClickMuseumMarker(row);
        setSelectedMuseumMarker(markerToClick);
    }

    public void simulateMuseumClick(MuseumRow museumRow) {
        Marker markerToClick = this.museumMarkersMap.inverse().get(museumRow);
        this.simulateMarkerClick(markerToClick);
    }






    public void route()
    {
        if(getCurrentLatLng() != null && getSelectedMuseumLatLng() != null) {
            route(getCurrentLatLng(), getSelectedMuseumLatLng());
            latLngBounds = new LatLngBounds.Builder().include(getCurrentLatLng())
                                                     .include(getSelectedMuseumLatLng()).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, 300);// questo int rappresenta il padding, sarebbe buono ottimizzarlo a seconda della lunghezza del percorso
            gmap.animateCamera(cameraUpdate);
        }
    }

    public void route(LatLng startLocation)
    {
        if(this.selectedMuseumMarker != null)
            route(startLocation, this.selectedMuseumMarker.getPosition());
    }

    public void route(LatLng origin, LatLng dest)
    {
        if(origin != null && dest != null) {
            RouteTask task = new RouteTask();

            /********************** IF MULTITASKING DOES NOT WORK TRY THIS: ******************************************
            */
             if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, origin, dest);
             else
                task.execute(origin, dest);
            /*
             ***************************************************************************************************/
        }
    }

    private Navigation _route(LatLng origin, LatLng dest)
    {
        GMapRouteManager routeManager = new GMapRouteManager();
        return routeManager.requestRoute(origin, dest);
    }

    private void _drawNavigation(Navigation nav)
    {
        //disegnare la navigazione sulla mappa
        PolylineOptions lineOptions = new PolylineOptions();
        lineOptions.width(10);
        lineOptions.color(Color.RED);

        nav.draw(gmap, lineOptions, 0);
        // TODO: aggiungere schermata con caricamento e/o disegnare la polyline usando solo punti "abbastanza lontani" calcolando la distanza euclidea e confrontandola col livello di zoom..

        this.polyline = true;
    }



    private void startProximityManager() {
        this.proximityManager = new ProximityManager( PROXIMITY_RADIUS, PROXIMITY_SKIMMING_RADIUS, this);
        this.proximityManager.startAnalysis();
    }
    private void stopProximityManager() {
        if(proximityManager!= null)
          this.proximityManager.stopAnalysis();
        this.proximityManager = null;
    }


    public void stopLocalization() {
        this.gmap.setMyLocationEnabled(false);
        stopProximityManager();
    }

    private void startLocalization() {
        this.gmap.setMyLocationEnabled(true);
        startProximityManager();
    }



    private class RouteTask extends AsyncTask<LatLng, Void, Navigation>
    {

        protected Navigation doInBackground(LatLng ... pt)
        {
            Log.d("DEBUG", "Downloading directions from google servers...");
            Navigation nav = _route(pt[0], pt[1]);
            return nav;
        }

        protected void onPostExecute(Navigation nav)
        {
            _drawNavigation(nav);
        }

    }


}
