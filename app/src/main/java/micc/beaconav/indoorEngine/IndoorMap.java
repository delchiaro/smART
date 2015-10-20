package micc.beaconav.indoorEngine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import micc.beaconav.FragmentHelper;
import micc.beaconav.MainActivity;
import micc.beaconav.R;
import micc.beaconav.indoorEngine.beaconHelper.ABeaconProximityManager;
import micc.beaconav.indoorEngine.beaconHelper.BeaconBestProximityListener;
import micc.beaconav.indoorEngine.beaconHelper.BeaconProximityListener;
import micc.beaconav.indoorEngine.beaconHelper.GoodBadBeaconProximityManager;
import micc.beaconav.indoorEngine.building.Building;
import micc.beaconav.indoorEngine.building.Position;
import micc.beaconav.indoorEngine.spot.marker.Marker;
import micc.beaconav.indoorEngine.spot.marker.MarkerManager;
import micc.beaconav.indoorEngine.spot.marker.OnMarkerSelectedListener;
import micc.beaconav.indoorEngine.dijkstraSolver.PathSpot;
import micc.beaconav.indoorEngine.dijkstraSolver.PathSpotManager;
import micc.beaconav.indoorEngine.databaseLite.downloader.BuildingDownloader;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class IndoorMap

        implements BeaconProximityListener,
                    BeaconBestProximityListener,
        OnMarkerSelectedListener<ArtworkMarker>,
                    View.OnTouchListener

{


    private Context context;





    private ToolTip artworkproximityToolTip = new ToolTip()
            .withText("Premi per\ninformazioni\nsull'opera")
            .withTextColor(Color.BLACK)
            .withColor(Color.WHITE)
            .withAnimationType(ToolTip.AnimationType.FROM_MASTER_VIEW);

    private ToolTipView toolTipView;


    MarkerManager markerManager;
    PathSpotManager pathSpotManager;
    PathSpotManager myLocationPathSpotManager;

    private Building building;

    GoodBadBeaconProximityManager proximityManager;







    private static int PPM = ProportionsHelper.PPM; // pixel per meter
    // these matrices will be used to move and zoom image
    private Matrix bgMatrix = new Matrix();
    private Matrix bgSavedMatrix = new Matrix();
    private Matrix fgMatrix = new Matrix();
    private Matrix fgSavedMatrix = new Matrix();


    private Matrix buildingMatrix = new Matrix();
    WeakReference<Bitmap> backgroundBmp; // FIXED MEMORY LEAK CON WEAK REFERENCE !!!!
    ImageView backgroundImgView;
    ImageView foregroundImgView;
    ImageView navigationImgView;
    ImageView localizationImgView;

    public IndoorMap( Building building, ImageView foregroundImgView, ImageView backgroundImgView,
                      ImageView navigationImgView, ImageView localizationImgView,Context context, MainActivity mainActivity) {
        this.context = context;
        this.building = building;
        this.backgroundImgView  = backgroundImgView;
        this.foregroundImgView = foregroundImgView;
        this.navigationImgView = navigationImgView;
        this.localizationImgView = localizationImgView;

        backgroundImgView.setScaleType(ImageView.ScaleType.MATRIX);
        backgroundImgView.setOnTouchListener(this);


        backgroundBmp = generateBackgroundBmp(building);
        //backgroundImgView.setImageDrawable(indoorMap); // disegno background in vettoriale
        // disegno background stampando il vettoriale su un bitmap
        backgroundImgView.setImageBitmap(backgroundBmp.get() );


        //indoorMap = new IndoorMap(building);
        markerManager = building.getActiveMarkerManager();
        markerManager.setOnMarkerSpotSelectedListener(this);

        foregroundImgView.setImageDrawable(markerManager.newWrapperDrawable());

        //foregroundImgView.setOnTouchListener(markerManager);

        // pathSpotManager = building.drawBestPath(corridoio.getRoomSpot(), stanzaFerracani.getRoomSpot());
        pathSpotManager = new PathSpotManager();
        navigationImgView.setImageDrawable(pathSpotManager.newWrapperDrawable());

        myLocationPathSpotManager = new PathSpotManager();
//        navigationImgView.setImageDrawable(myLocationPathSpotManager.newWrapperDrawable());

        translateAll(40, 40);
        navigationImgView.invalidate();

        proximityManager = new GoodBadBeaconProximityManager(mainActivity, this);
    }


    public WeakReference<Bitmap> generateBackgroundBmp(Building building) {
        WeakReference<Bitmap> frame =  new WeakReference<Bitmap>(Bitmap.createBitmap((int)building.getWidth(), (int)building.getHeight(), Bitmap.Config.ARGB_8888));
        Canvas frameBuildingCanvas = new Canvas(frame.get());
        frameBuildingCanvas.setMatrix(this.buildingMatrix);
        building.draw(frameBuildingCanvas);

        return frame;
    }












/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
/* * * * * * * * * * * * GESTIONE TOUCH EVENT IMAGE VIEW MAPPA (ZOOM - DRAG) * * * * * * * * * * * * * */
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private void translateAll(int x, int y) {

        bgMatrix.postTranslate(x, y);
        this.backgroundImgView.setImageMatrix(bgMatrix);
        markerManager.translate(x, y);
        pathSpotManager.translate(x, y);
        myLocationPathSpotManager.translate(x, y);

    }

    // we can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    // remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    private float[] lastEvent = null;


    private float scaleFactor = 1;

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {

        ImageView view = (ImageView) v;
        float dx = 0;
        float dy = 0;
        // TODO: adesso disabilitiamo il gestore di eventi touch quando rileviamo 3 o più tocchi!!
        // in futuro dovremo riabilitarlo. Lo abbiamo disabilitato per evitare un fastidioso bug,
        // cioè che zoomando con 2 dita e poi toccando con un terzo la posizione degli spot
        // veniva modificata erroneamente, come se il terzo dito avesse introdotto un altro zoom
        // che però in realtá non introduce!
        // Poichè non zooma il bg non dovrebbe influenzare nemmeno il foreground, ma invece lo fa,
        // verificare perchè lo fa e fare in modo che non influenzi il foreground come non influenza
        // il background.
        if (lastEvent != null && event.getPointerCount() >= 3)
        {
            if( mode == ZOOM )
            {
                markerManager.holdScalingFactor();
                pathSpotManager.holdScalingFactor();
                myLocationPathSpotManager.holdScalingFactor();
            }
            mode = NONE;
            lastEvent = null;
        }
        else switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                bgSavedMatrix.set(bgMatrix);
                fgSavedMatrix.set(fgMatrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                lastEvent = null;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    bgSavedMatrix.set(bgMatrix);
                    fgSavedMatrix.set(fgMatrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                //TODO: DISATTIVATA ROTAZIONE, riabilitarla gestendo spostamento marker (come in zoom)
                // d = rotation(event);

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if(mode == ZOOM) {
                    markerManager.holdScalingFactor();
                    pathSpotManager.holdScalingFactor();
                    myLocationPathSpotManager.holdScalingFactor();

                }
                mode = NONE;
                lastEvent = null;


                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG)
                {
                    bgMatrix.set(bgSavedMatrix);
                    fgMatrix.set(fgSavedMatrix);
                    dx = event.getX() - start.x;
                    dy = event.getY() - start.y;
                    bgMatrix.postTranslate(dx, dy);
                    fgMatrix.postTranslate(dx, dy);
                }
                else if (mode == ZOOM)
                {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        bgMatrix.set(bgSavedMatrix);
                        fgMatrix.set(fgSavedMatrix);

                        float newScale = (newDist / oldDist);
                        bgMatrix.postScale(newScale, newScale, mid.x, mid.y);
                        // scala la matrice dello sfondo (mappa)



                        markerManager.translateByRealtimeScaling(newScale);
                        pathSpotManager.translateByRealtimeScaling(newScale);
                        myLocationPathSpotManager.translateByRealtimeScaling(newScale);
                        // scala la posizione del marker dovuta allo zoom dello sfondo,
                        // in tempo reale senza zoomarlo, in modo che il centro del marker
                        // sia sempre nello stesso punto dello sfondo (mappa) scalato.

                    }
//
//                    if (lastEvent != null && event.getPointerCount() == 3) {
//                        newRot = rotation(event);
//                        float r = newRot - d;
//                        float[] values = new float[9];
//                        bgMatrix.getValues(values);
//                        float tx = values[2];
//                        float ty = values[5];
//                        float sx = values[0];
//                        float xc = (view.getWidth() / 2) * sx;
//                        float yc = (view.getHeight() / 2) * sx;
//
//                        //TODO: DISATTIVATA ROTAZIONE, riabilitarla gestendo spostamento marker (come in zoom)
//                        // bgMatrix.postRotate(r, tx + xc, ty + yc);
//                    }
                }
                break;
        }

        this.backgroundImgView.setImageMatrix(bgMatrix);

        float[] matrixVal = new float[9];
        bgMatrix.getValues(matrixVal);




        markerManager.translate(matrixVal[2], matrixVal[5]);
        pathSpotManager.translate(matrixVal[2], matrixVal[5]);
        myLocationPathSpotManager.translate(matrixVal[2], matrixVal[5]);
        //markerManager.invalidate();

        //indoorMap.invalidateSelf(); //per disegno mappa indoor in vettoriale

//        this.ball.x = matrixVal[2];
//        this.ball.y = matrixVal[5];
//
//        this.ball2.x = matrixVal[2];
//        this.ball2.y = matrixVal[5];
//        this.drawable.invalidateSelf();
        //this.foregroundImgView.setImageMatrix(fgMatrix);

        if(event.getPointerCount() == 1 && dx == 0 && dy == 0 ) // lastEvent == null )// mode != ZOOM && mode !=DRAG)
            markerManager.onTouch(view, event);
        return true;
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Calculate the degree to be rotated by.
     *
     * @param event
     * @return Degrees
     */
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }



















































































/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
/* * * * * * * * * * * * GESTIONE MARKERS, BEACON E NAVIGAZIONE INDOOR MAP * * * * * * * * * * * * * * */
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


    Date startNavigationDate;
    Date startProximityDate;

    //ArtworkMarker proximityMarker = null;
    //LocalizationSpot localizedSpot = null;
    //ArtworkMarker selectedMarker = null;

    ArtworkPosition selectedArtworkPosition = null;  // position/marker Selezionato.
    Position localizedPosition = null;               // position in cui siamo stati rilevati.
    ArtworkPosition localizedArtworkPosition = null; // se la position in cui siamo stati rilevati è una
                                                     // ArtworkPosition allora viene referenziata giá castata
                                                     // da questa variabile.







    // GESTIONE MARKER SELEZIONATO:
    @Override
    public void onMarkerSpotSelected(ArtworkMarker newSelectedMarker)
    {
        if(selectedArtworkPosition != null )
        {
            Marker oldSelectedMarker = selectedArtworkPosition.getMarker();
            if (oldSelectedMarker != null && oldSelectedMarker.isSelected())
            {
                oldSelectedMarker.deselect();
                markerManager.invalidate();
                // TODO: artworkList_museumRow e` sempre null...
                //  FragmentHelper.instance().showArtworkListFragment(FragmentHelper.instance().artworkList_museumRow);
            }
        }

        if(newSelectedMarker != null)
        {
            newSelectedMarker.select();
            selectedArtworkPosition = newSelectedMarker.getArtworkPosition();
            FragmentHelper.instance().showArtworkDetailsFragment(selectedArtworkPosition.getArtworkRow());
        }
        else
        {
            // TODO: artworkList_museumRow e` sempre null...
          //  FragmentHelper.instance().showArtworkListFragment(FragmentHelper.instance().artworkList_museumRow);
            selectedArtworkPosition = null;
            markerManager.invalidate();
        }
    }

    @Override
    public void onNullMarkerSpotSelected() {
        this.onMarkerSpotSelected(null);
    }

    public void simulateArtMarkerSelection(ArtworkRow row) {
        if(row != null && row.getPosition() != null)
        {
            if(selectedArtworkPosition != null)
            {
                Marker oldSelectedMarker = selectedArtworkPosition.getMarker();
                if(oldSelectedMarker != null && oldSelectedMarker.isSelected())
                    oldSelectedMarker.deselect();
            }

            selectedArtworkPosition = row.getPosition();
            selectedArtworkPosition.getMarker().select();
            markerManager.invalidate();
        }
    }


    // GESTIONE QR CODE SCANNER (MARKER PIÙ VICINO E POSIZIONE CORRENTE)
    public void onCameraScanResult(String qr_code_result)
    {
        Position scannedPosition = building..get(qr_code_result);
        if(scannedPosition != null)
        {
            if( scannedPosition instanceof ArtworkPosition)
            {
                ArtworkMarker scannedArtworkMarker = ((ArtworkPosition) scannedPosition).getMarker();
                this.onMarkerSpotSelected(scannedArtworkMarker);
                FragmentHelper.instance().getMainActivity().getSlidingUpPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);


                // TODO: artwork nearest Path Spot
//                if( scannedArtworkMarker.getNearestPathSpot() != null)
//                {
//                    newCurrentLocation(scannedArtworkMarker.getNearestPathSpot());
//                    hideDijkstraPath();
//                    lostCurrentLocation();
//                }
            }

            else
            {
                // TODO: scanned position ---> set current location
//                newCurrentLocation((PathSpot)scannedPosition);
//                hideDijkstraPath();
//                lostCurrentLocation();
            }
        }
    }



    // GESTIONE BEACON (MARKER PIÙ VICINO E POSIZIONE CORRENTE)

    public void removeCurrentLocation() {
        boolean changed = false;

        if(localizedSpot != null) {
            pathSpotManager.remove(localizedSpot);
            localizedSpot = null;
            changed = true;
            pathSpotManager.invalidate();
        }
    }

    public void newCurrentQrLocation(PathSpot scannedSpot)
    {
        if(this.localizedSpot!= null)
        {
            if(this.localizedSpot.isCurrentlyLocalized())
            {
                // non fare nulla, sei giá localizzato da un beacon
            }
            else
            {
                this.myLocationPathSpotManager.remove(localizedSpot);
                localizedSpot = new LocalizationSpot(scannedSpot);
                localizedSpot.setCurrentlyLocalized(true);
                this.myLocationPathSpotManager.add(localizedSpot);
                localizationImgView.setImageDrawable(myLocationPathSpotManager.newWrapperDrawable());
                this.myLocationPathSpotManager.invalidate();
            }
        }
        else
        {
            localizedSpot = new LocalizationSpot( scannedSpot);
            localizedSpot.setCurrentlyLocalized(true);
            this.myLocationPathSpotManager.add(localizedSpot);
            localizationImgView.setImageDrawable(myLocationPathSpotManager.newWrapperDrawable());

            this.myLocationPathSpotManager.invalidate();
        }
    }
    public void newCurrentLocation(PathSpot newMyLocationPathSpot) {
        if(newMyLocationPathSpot != null)
        {
            if(localizedSpot != null)
                myLocationPathSpotManager.remove(localizedSpot);
//            if(lastLocalizedPathSpot != null)
//                myLocationPathSpotManager.remove(lastLocalizedPathSpot);
//
//            lastLocalizedPathSpot = null;
//            newMyLocationPathSpot.setStepNumber(PathSpot.STEP_NUMBER_MY_POSITION);
            localizedSpot =  new LocalizationSpot(newMyLocationPathSpot);
            localizedSpot.setCurrentlyLocalized(true);

            // TODO: al posto di context c'era getActivity().. funziona ancora??
            Toast toast = Toast.makeText(context,
                    "Sei stato localizzato da un sensore!", Toast.LENGTH_SHORT);
            toast.show();

            myLocationPathSpotManager.add(localizedSpot);
            localizationImgView.setImageDrawable(myLocationPathSpotManager.newWrapperDrawable());
            myLocationPathSpotManager.invalidate();
        }
        else lostCurrentLocation();
    }

    public void lostCurrentLocation() {
        if(localizedSpot != null ) {
            localizedSpot.setCurrentlyLocalized(false);
            //lastLocalizedPathSpot.setStepNumber(PathSpot.STEP_MY_LAST_POSITION);
            //localizationImgView.setImageDrawable(myLocationPathSpotManager.newWrapperDrawable());
            myLocationPathSpotManager.invalidate();

            Toast toast = Toast.makeText(context,
                    "Non sei più localizzato dai sensori.. la tua ultima posizione è stata registrata." +
                            "\nAvvicinati ad un sensore o fai la foto ad un codice QR per aggiornare la tua posizione", Toast.LENGTH_LONG);
            toast.show();
        }
    }




    @Override
    public void OnNewBeaconBestProximity(Beacon bestProximity, Beacon oldBestProximity) {

        Position beaconAssociatedPosition  = beacon_position_map.get(ABeaconProximityManager.getID(bestProximity));

        localizedPosition = beaconAssociatedPosition;

        if( beaconAssociatedPosition instanceof ArtworkPosition)
        {
            // TODO: STATISTICS
            // OLD PROXIMITY MARKER: (for statistics)
//            if(this.proximityMarker != null && this.proximityMarker.getArtworkRow() != null && startProximityDate != null)
//            {
//                long lastTimeInApp = TimeStatisticsManager.readInAppTime(this.proximityMarker.getArtworkRow());
//                long timeInSeconds = ((new Date()).getTime() - startProximityDate.getTime()) / 1000;
//                TimeStatisticsManager.writeInProximityTime(this.proximityMarker.getArtworkRow(), timeInSeconds + lastTimeInApp);
//            }

            selectedArtworkPosition = (ArtworkPosition) beaconAssociatedPosition;
            showProximityArtworkBeaconFAB();
        }
        else
            hideProximityArtworkBeaconFAB();

    }
    @Override
    public void OnNoneBeaconBestProximity(Beacon oldBestProximity) {
        lostCurrentLocation();
        hideProximityArtworkBeaconFAB();
    }


    public void showProximityArtworkBeaconFAB() {
        Toast toast;
        toolTipView = FragmentHelper.instance().getMainActivity()
                .getArtworkFoundTooltipContainer().showToolTipForView(artworkproximityToolTip, FragmentHelper.instance().getMainActivity().findViewById(R.id.notifyToIndoor));

        FragmentHelper.instance().getMainActivity().getFloatingActionButtonNotifyBeaconProximity().setVisibility(View.VISIBLE);
        toast = Toast.makeText(context, "BEACON VICINO!!!", Toast.LENGTH_LONG);
        toast.show();

        FragmentHelper.instance().getMainActivity().getFloatingActionButtonNotifyBeaconProximity().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMarkerSpotSelected(selectedArtworkPosition.getMarker());
                markerManager.invalidate();
                toolTipView.remove();
                FragmentHelper.instance().getMainActivity().getSlidingUpPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });

    }

    public void hideProximityArtworkBeaconFAB() {
        // rimuovi la notifica di una opera d'arte vicina. Nascondi pulsante per visualizzare l'opera
        FragmentHelper.instance().getMainActivity().getFloatingActionButtonNotifyBeaconProximity().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        FragmentHelper.instance().getMainActivity().getFloatingActionButtonNotifyBeaconProximity().setVisibility(View.INVISIBLE);
        toolTipView.remove();
        //Toast toast = Toast.makeText(getActivity(), "BEACON LONTANO ...", Toast.LENGTH_SHORT);
        //toast.show();
    }



    public int navigateToSelectedMarker() {

        if(this.localizedSpot!= null && selectedMarker != null)
        {
            // TODO: artwork nearest Path Spot
//            if(selectedMarker.getNearestPathSpot() != null)
//            {
//                navigationImgView.setImageDrawable(null);
//                pathSpotManager =  building.drawBestPath(localizedSpot.getAssociatedPathSpot(), selectedMarker.getNearestPathSpot());
//                navigationImgView.setImageDrawable(pathSpotManager.newWrapperDrawable());
//                pathSpotManager.invalidate();
//                return 1;
//            }
//            else
//            {
//                Toast toast = Toast.makeText(context,
//                        "Purtroppo l'opera d'arte selezionata non è stata localizzata sulla mappa...", Toast.LENGTH_SHORT);
//                toast.show();
//                return -1;
//            }
            return -1000; // TODO: delete..
        }
        else
        {
            Toast toast = Toast.makeText(context,
                    "Purtroppo non siamo riusciti a localizzarti.. avvicinati ad un beacon o scannerizza un QR code", Toast.LENGTH_LONG);
            toast.show();
            return 0;
        }

    }




    public void hideDijkstraPath() {
        if(navigationImgView != null)
            navigationImgView.setImageDrawable(null);
    }


    @Override
    public void OnBeaconProximity(List<Beacon> proximityBeacons)
    {
//
//        if(BeaconHelper.isInProximity(proximityBeacons.get(0) ))
//        {
//            //spot2.sele;
//            spot2.drawable().invalidateSelf();
//            foregroundImgView.invalidate();
//        }
    }







}
