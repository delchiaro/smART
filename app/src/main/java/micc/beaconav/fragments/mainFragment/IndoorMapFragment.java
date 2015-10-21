//package micc.beaconav.fragments.mainFragment;
//
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Matrix;
//import android.graphics.PointF;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.os.Bundle;
//import android.util.FloatMath;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.estimote.sdk.Beacon;
//import com.sothree.slidinguppanel.SlidingUpPanelLayout;
//import com.nhaarman.supertooltips.ToolTip;
//import com.nhaarman.supertooltips.ToolTipView;
//
//import java.lang.ref.WeakReference;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//
//import micc.beaconav.FragmentHelper;
//import micc.beaconav.R;
//import micc.beaconav.db.dbHelper.museum.MuseumRow;
//import micc.beaconav.db.timeStatistics.TimeStatisticsManager;
//import micc.beaconav.indoorEngine.ArtworkMarker;
//import micc.beaconav.indoorEngine.ArtworkRow;
//import micc.beaconav.indoorEngine.IndoorMap;
//import micc.beaconav.indoorEngine.ProportionsHelper;
//import micc.beaconav.indoorEngine.beaconHelper.BeaconProximityListener;
//import micc.beaconav.indoorEngine.building.Building;
//import micc.beaconav.indoorEngine.spot.Spot;
//import micc.beaconav.indoorEngine.spot.marker.Marker;
//import micc.beaconav.indoorEngine.spot.marker.MarkerManager;
//import micc.beaconav.indoorEngine.spot.marker.OnMarkerSelectedListener;
//import micc.beaconav.indoorEngine.LocalizationSpot;
//import micc.beaconav.indoorEngine.dijkstraSolver.PathSpot;
//import micc.beaconav.indoorEngine.dijkstraSolver.PathSpotManager;
//import micc.beaconav.indoorEngine.beaconHelper.ABeaconProximityManager;
//import micc.beaconav.indoorEngine.beaconHelper.BeaconBestProximityListener;
//import micc.beaconav.indoorEngine.beaconHelper.GoodBadBeaconProximityManager;
//
///**
// * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
// */
//public class IndoorMapFragment extends Fragment
//        implements View.OnTouchListener, BeaconProximityListener, BeaconBestProximityListener,
//        OnMarkerSelectedListener<ArtworkMarker>
//{
//
//
//    private static int PPM = ProportionsHelper.PPM; // pixel per meter
//    // these matrices will be used to move and zoom image
//    private Matrix bgMatrix = new Matrix();
//    private Matrix bgSavedMatrix = new Matrix();
//    private Matrix fgMatrix = new Matrix();
//    private Matrix fgSavedMatrix = new Matrix();
//
//    WeakReference<Bitmap> backgroundBmp; // FIXED MEMORY LEAK CON WEAK REFERENCE !!!!
//
//    ImageView backgroundImgView;
//    ImageView foregroundImgView;
//    ImageView navigationImgView;
//    ImageView localizationImgView;
//
//    ViewGroup container = null;
//
//    private Matrix buildingMatrix = new Matrix();
//
//    IndoorMap indoorMap;
//    Building building;
//    MarkerManager markerManager;
//    PathSpotManager pathSpotManager;
//    PathSpotManager myLocationPathSpotManager;
//
//    private MuseumRow museumRow = null;
//    public void initMuseumRow(MuseumRow row) { this.museumRow = row; }
//
//
//    GoodBadBeaconProximityManager proximityManager;
//
////    HashMap<Integer, Marker> marker_beacon_map = new HashMap<>();
////    HashMap<Integer, PathSpot>   pathSpot_beacon_map = new HashMap<>();
//
//    HashMap<Integer, Spot> beacon_spot_map = new HashMap<>();
//    HashMap<String, Spot> qr_spot_map = new HashMap<>();
//
//
//    private ToolTip artworkproximityToolTip = new ToolTip()
//            .withText("Premi per\ninformazioni\nsull'opera")
//            .withTextColor(Color.BLACK)
//            .withColor(Color.WHITE)
//            .withAnimationType(ToolTip.AnimationType.FROM_MASTER_VIEW);
//
//    private ToolTipView toolTipView;
//
//
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        proximityManager = new GoodBadBeaconProximityManager(this.getActivity(), this);
//        proximityManager.scan();
//
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        proximityManager.stopScan();
//        proximityManager = null;
//        if(toolTipView != null)
//            toolTipView.remove();
//    }
//
//
//
//    Date startNavigationDate;
//    Date startProximityDate;
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        startNavigationDate = new Date();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if(this != null) {
//            long lastTimeInApp = TimeStatisticsManager.readInAppTime(this.museumRow);
//            long timeInSeconds = ((new Date()).getTime() - startNavigationDate.getTime()) / 1000;
//            TimeStatisticsManager.writeInAppTime(this.museumRow, timeInSeconds + lastTimeInApp);
//        }
//    }
//
//
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        //backgroundImgView.setImageResource(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//        //backgroundBmp.recycle();
//
//        // FORZA il liberamentodella memoria bitmap background
//        // (non è questo che elimina il memory leak ma la weakReference)
//        backgroundBmp.get().recycle();
//        backgroundBmp = null;
//        backgroundImgView = null;
//        foregroundImgView = null;
//        navigationImgView = null;
//        localizationImgView = null;
//
//        container = null;
//        ArtworkMarker.flushDrawable();
//
//        //libera la memoria!!
//        //http://stackoverflow.com/questions/13421945/retained-fragments-with-ui-and-memory-leaks
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        this.container = container;
//
//        return inflater.inflate(R.layout.fragment_indoor_map, container, false);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        FragmentHelper.instance().getMainActivity().showMenuItemStopPath();
//
//        if(container != null)
//        {
//            backgroundImgView = (ImageView) container.findViewById(R.id.backgroundImageView);
//            backgroundImgView.setOnTouchListener(this);
//            backgroundImgView.setScaleType(ImageView.ScaleType.MATRIX);
//
//            foregroundImgView = (ImageView) container.findViewById(R.id.foregroundImageView);
//            navigationImgView = (ImageView) container.findViewById(R.id.navigationLayerImageView);
//
//            localizationImgView = (ImageView) container.findViewById(R.id.localizationLayerImageView);
//        }
//
//
//
//
//
//
//        // BUILDING DEFINITION
//        building = new Building(50*PPM, 50*PPM);
//
////////        // FLOOR DEFINITION
////////        Floor floor = new Floor(0);
////////        building.add(floor);
////////
////////
////////        // ROOM DEFINITION
////////        Room corridoio = new Room();
////////        floor.add(corridoio);
////////
////////        Room ingressoFerracani = new Room();
////////        floor.add(ingressoFerracani);
////////
////////        Room stanzaFerracani = new Room();
////////        floor.add(stanzaFerracani);
////////
////////        Room bagno = new Room();
////////        floor.add(bagno);
////////
////////        Room stanzaPC = new Room();
////////        floor.add(stanzaPC);
////////
////////        Room stanzaRasta = new Room();
////////        floor.add(stanzaRasta);
////////
////////        Room ingresso = new Room();
////////        floor.add(ingresso);
////////
////////        Room stanzaEntrataSegreta = new Room();
////////        floor.add(stanzaEntrataSegreta);
////////
////////        Room corridoio2 = new Room();
////////        floor.add(corridoio2);
////////
////////        Room stanzaBertini = new Room();
////////        floor.add(stanzaBertini);
////////
////////        Room bagno2 = new Room();
////////        floor.add(bagno2);
////////
////////        Room stanzaPulizie = new Room();
////////        floor.add(stanzaPulizie);
////////
////////
////////
////////
////////        DoorSpot[] doorsAppoggio;
////////
////////
////////        // VertexDefinitions and Spot Definitions
////////        corridoio.pushWall(new PointF(11f,   1f));
////////        corridoio.pushWall(new PointF(16f, 1f));
////////            corridoio.pushAperture(new PointF(16f, 4f));
////////            doorsAppoggio = Room.addDoorSpot(corridoio, 15f, 5f, true, bagno, 17f, 5f, true);
////////            DoorSpot door_corridoio_bagno = doorsAppoggio[0];
////////            corridoio.pushWall(new PointF(16f, 6f));
////////
////////            corridoio.pushAperture(new PointF(16f, 12f));
////////            Room.addDoorSpot(corridoio, 15f, 13f, true, stanzaPC, 17f, 13f, true);
////////            corridoio.pushWall(new PointF(16f, 14f));
////////
////////            corridoio.pushAperture(new PointF(16f, 24f));
////////            doorsAppoggio = Room.addDoorSpot(corridoio, 15f, 25f, DoorSpot.Visibility.VISIBLE, true, ingressoFerracani, 17f, 25f, true);
////////            DoorSpot door_corridoio_ingressoFerracani = doorsAppoggio[0];
////////            corridoio.pushWall(new PointF(16f, 26f));
////////
////////
////////        corridoio.pushWall(new PointF(16f, 31f));
////////
////////            corridoio.pushAperture(new PointF(15f, 31f));
////////            doorsAppoggio = Room.addDoorSpot(corridoio, 14f, 30f, true, ingresso, 14f, 32f, true);
////////            DoorSpot door_corridoio_ingresso = doorsAppoggio[0];
////////            corridoio.pushWall(new PointF(13f, 31f));
////////
////////        corridoio.pushWall(new PointF(11f,   31f));
////////
////////
////////
////////
////////
////////        ingressoFerracani.pushWall(new PointF(16f, 21f));
////////
////////            ingressoFerracani.pushAperture(new PointF(18f, 21f));
////////            doorsAppoggio = Room.addDoorSpot(ingressoFerracani, 19f, 22f, true, stanzaRasta, 19f, 20f, DoorSpot.Visibility.VISIBLE, true);
////////            DoorSpot door_stanzaRasta_ingressoFerracani = doorsAppoggio[1];
////////            ingressoFerracani.pushWall(new PointF(20f, 21f));
////////
////////        ingressoFerracani.pushWall(new PointF(22f, 21f));
////////
////////            ingressoFerracani.pushAperture(new PointF(22f, 24f));
////////            Room.addDoorSpot(ingressoFerracani, 21f, 25f, true, stanzaFerracani, 23f, 25f, true);
////////            ingressoFerracani.pushWall(new PointF(22f, 26f));
////////
////////        ingressoFerracani.pushWall(new PointF(22f, 30f));
////////        ingressoFerracani.pushWall(new PointF(16f, 30f));
////////
////////
////////
////////
////////        stanzaFerracani.pushWall(new PointF(22f, 21f));
////////        stanzaFerracani.pushWall(new PointF(30f,  21f));
////////        stanzaFerracani.pushWall(new PointF(30f,  30f));
////////        stanzaFerracani.pushWall(new PointF(22f, 30f));
////////
////////
////////        bagno.pushWall(new PointF(16f, 1f));
////////        bagno.pushWall(new PointF(22f, 1f));
////////        bagno.pushWall(new PointF(22f, 8f));
////////        bagno.pushWall(new PointF(16f, 8f));
////////
////////
////////
////////        stanzaPC.pushWall(new PointF(16f, 8f));
////////        stanzaPC.pushWall(new PointF(30f, 8f));
////////        stanzaPC.pushWall(new PointF(30f, 18f));
////////        stanzaPC.pushWall(new PointF(16f, 18f));
////////
////////
////////
////////
////////        stanzaRasta.pushWall(new PointF(16f, 18f));
////////        stanzaRasta.pushWall(new PointF(30f, 18f));
////////        stanzaRasta.pushWall(new PointF(30f, 21f));
////////        stanzaRasta.pushWall(new PointF(16f, 21f));
////////
////////
////////
////////        stanzaEntrataSegreta.pushWall(new PointF(0f, 31f));
////////        stanzaEntrataSegreta.pushWall(new PointF(11f, 31f));
////////            stanzaEntrataSegreta.pushAperture(new PointF(11f, 34f));
////////            doorsAppoggio = Room.addDoorSpot(stanzaEntrataSegreta, 10f, 35f, DoorSpot.Visibility.VISIBLE, true, ingresso, 12f, 35f, true);
////////            DoorSpot door_entrataSegreta_ingresso = doorsAppoggio[0];
////////            DoorSpot door_ingresso_entrataSegreta = doorsAppoggio[1];
////////            stanzaEntrataSegreta.pushWall(new PointF(11f, 36));
////////        stanzaEntrataSegreta.pushWall(new PointF(11f, 44f));
////////        stanzaEntrataSegreta.pushWall(new PointF(0f, 44f));
////////
////////
////////
////////
////////        ingresso.pushWall(new PointF(11f, 31f));
////////        ingresso.pushWall(new PointF(16f, 31f));
////////        ingresso.pushWall(new PointF(16f, 30f));
////////        ingresso.pushWall(new PointF(20f, 30f));
////////        ingresso.pushWall(new PointF(20f, 44f));
////////        ingresso.pushWall(new PointF(11f, 44f));
////////
////////
////////
////////        corridoio2.pushWall(new PointF(20f, 30f));
////////        corridoio2.pushWall(new PointF(49f, 30f));
////////        corridoio2.pushWall(new PointF(49f, 35f));
////////
////////            corridoio2.pushAperture(new PointF(46f, 35f));
////////            doorsAppoggio = Room.addDoorSpot(corridoio2, 45f, 34f, DoorSpot.Visibility.VISIBLE, true, stanzaPulizie, 45f, 36f, true);
////////            DoorSpot door_corridioio2_pulizie = doorsAppoggio[0];
////////            corridoio2.pushWall(new PointF(44f, 35f));
////////
////////            corridoio2.pushAperture(new PointF(37f, 35f));
////////            Room.addDoorSpot(corridoio2, 36f, 34f, true, bagno2, 36f, 36f, true);
////////            corridoio2.pushWall(new PointF(35f, 35f));
////////
////////            corridoio2.pushAperture(new PointF(28f, 35f));
////////            doorsAppoggio = Room.addDoorSpot(corridoio2, 27f, 34f, DoorSpot.Visibility.VISIBLE, true, stanzaBertini, 34f, 36f, true);
////////            DoorSpot door_corridoio2_stanzaBertini = doorsAppoggio[0];
////////            corridoio2.pushWall(new PointF(26f, 35f));
////////
////////        corridoio2.pushWall(new PointF(20f, 35f));
////////
////////            corridoio2.pushAperture(new PointF(20f, 34f));
////////            doorsAppoggio = Room.addDoorSpot(corridoio2, 21f, 33f, DoorSpot.Visibility.VISIBLE, true, ingresso, 19f, 33f, true);
////////            DoorSpot door_corridio2_ingresso = doorsAppoggio[0];
////////            corridoio2.pushWall(new PointF(20f, 32f));
////////
////////
////////
////////
////////
////////        stanzaBertini.pushWall(new PointF(20f, 35f));
////////        stanzaBertini.pushWall(new PointF(32f, 35f));
////////        stanzaBertini.pushWall(new PointF(44f, 44f));
////////        stanzaBertini.pushWall(new PointF(20f, 44f));
////////
////////
////////        bagno2.pushWall(new PointF(32f, 35f));
////////        bagno2.pushWall(new PointF(42f, 35f));
////////        bagno2.pushWall(new PointF(42f, 44f));
////////        bagno2.pushWall(new PointF(32f, 44f));
////////
////////
////////        stanzaPulizie.pushWall(new PointF(42f, 35f));
////////        stanzaPulizie.pushWall(new PointF(49f, 35f));
////////        stanzaPulizie.pushWall(new PointF(49f, 44f));
////////        stanzaPulizie.pushWall(new PointF(42f, 44f));
////////
//////
//////
//////
//////
//////
//////
//////
//////        // SPOT DEFINITIONS
////////        final ArtworkMarker spot1;
////////        final ArtworkMarker spot2;
////////        final ArtworkMarker spot3;
//////
//////
//////
//////            // CORRIDOIO
//////        corridoio.getRoomSpot().x(13.5f);
//////        corridoio.getRoomSpot().y(18);
//////
//////
//////        final PathSpot pathSpotCorridoio = new PathSpot(13.5f, 5f);
//////        //qr_spot_map.put("path_corridoio", pathSpotCorridoio);
//////        pathSpotCorridoio.addLinkBidirectional(door_corridoio_bagno);
//////
//////        final ArtworkMarker artSpotCorridoio = new ArtworkMarker(13.5f, 2f, corridoio);
//////        qr_spot_map.put("art_corridoio2", artSpotCorridoio);
//////        artSpotCorridoio.setNearestPathSpot(pathSpotCorridoio);
//////
//////        door_corridoio_ingresso.addLinkBidirectional(door_corridoio_ingressoFerracani);
//////
//////            // INGRESSO FERRACANI
//////        ingressoFerracani.getRoomSpot().x(18.5f);
//////        ingressoFerracani.getRoomSpot().y(25);
//////
//////
//////        // INGRESSO
//////        ingresso.getRoomSpot().x(14f);
//////        ingresso.getRoomSpot().y(33f);
//////        final PathSpot pathSpotIngresso = new PathSpot(13.5f, 37f);
//////        qr_spot_map.put("path_scale", pathSpotIngresso);
//////        pathSpotIngresso.addLinkBidirectional(ingresso.getRoomSpot());
//////        pathSpotIngresso.addLinkBidirectional(door_ingresso_entrataSegreta);
//////        pathSpotIngresso.addLinkBidirectional(door_corridio2_ingresso);
//////
//////
//////        // STANZA ENTRATA SEGRETA
////////        final PathSpot pathSpotDivani = new PathSpot(9f, 42f);
//////        stanzaEntrataSegreta.getRoomSpot().x(5);
//////        stanzaEntrataSegreta.getRoomSpot().y(38);
//////        final ArtworkMarker artSpotDivani = new ArtworkMarker(10f, 42f, stanzaEntrataSegreta);
//////        //artSpotDivani.setNearestPathSpot(pathSpotDivani);
//////
//////        qr_spot_map.put("art_divani", artSpotDivani);
//////        //pathSpotDivani.addLinkBidirectional(stanzaEntrataSegreta.getRoomSpot());
//////
//////
//////            // CORRIDOIO 2
//////        corridoio2.getRoomSpot().x(37);
//////        corridoio2.getRoomSpot().y(32);
//////        door_corridioio2_pulizie.x(45);
//////        door_corridioio2_pulizie.y(32);
//////        // final ArtworkMarker artSpotCorridoio2 = new ArtworkMarker(44, 31, corridoio2);
//////        // artSpotCorridoio2.setNearestPathSpot(door_corridioio2_pulizie);
//////        // qr_spot_map.put("art_corridoio2", artSpotCorridoio2);
//////        door_corridio2_ingresso.addLinkBidirectional(corridoio2.getRoomSpot());
//////
//////
//////            // STANZA FERRACANI
//////        final ArtworkMarker artSpotStanzaFerracani = new ArtworkMarker(28f,25f, stanzaFerracani);
//////        int beaconID = GoodBadBeaconProximityManager.getID(31950, 39427);
//////        beacon_spot_map.put(beaconID, artSpotStanzaFerracani);
//////
//////        int virtualBeaconID = GoodBadBeaconProximityManager.getID(4123, 24794);
//////        beacon_spot_map.put(virtualBeaconID, artSpotCorridoio);
//////
//////
////
////        DbManager.getLastArtworkDownloader().addHandler(new JSONHandler<ArtworkRow>() {
////            @Override
////            public void onJSONDownloadFinished(ArtworkRow[] result) {
////                if (result.length >= 7)
////                {
////                    artSpotStanzaFerracani.setArtworkRow(result[2]); // bacco di caravaggio
////                   // artSpotCorridoio2.setArtworkRow(result[6]); // madonna del cardellino (Raffaello)
////                    artSpotCorridoio.setArtworkRow(result[6]); // madonna del cardellino (Raffaello)
////                    artSpotDivani.setArtworkRow(result[4]);// madonna di ognissanti
////
////                }
////            }
////        });
////
//
//
//
//
////        spot1 = new ArtworkMarker( 2f, 2f);
////        spot2 = new ArtworkMarker( 8f, 28);
////        spot3 = new ArtworkMarker( 11f, 27f);
////        corridoio.add(spot1);
////        stanzaFerracani.add(spot2);
////        stanzaFerracani.add(spot3);
////
////        qr_spot_map.put("qr_prova", spot1);
////        qr_spot_map.put("qr_prova2", spot2);
////        qr_spot_map.put("qr_prova3", spot3);
//
//        // DRAWABLES DEFINITION
//
//
////        BeaconHelper beaconHelper = new BeaconHelper(this.getActivity());
////        beaconHelper.addProximityListener(this);
////        beaconHelper.scanBeacons();
//
//
//
//        // DRAWING:
//
//
//
//        backgroundBmp = generateBackgroundBmp(building);
//        //backgroundImgView.setImageDrawable(indoorMap); // disegno background in vettoriale
//        // disegno background stampando il vettoriale su un bitmap
//        backgroundImgView.setImageBitmap(backgroundBmp.get() );
//
//
//        //indoorMap = new IndoorMap(building);
//        markerManager = building.getActiveMarkerManager();
//        markerManager.setOnMarkerSpotSelectedListener(this);
//
//        foregroundImgView.setImageDrawable(markerManager.newWrapperDrawable());
//
//        //foregroundImgView.setOnTouchListener(markerManager);
//
//        // pathSpotManager = building.drawBestPath(corridoio.getRoomSpot(), stanzaFerracani.getRoomSpot());
//        pathSpotManager = new PathSpotManager();
//        navigationImgView.setImageDrawable(pathSpotManager.newWrapperDrawable());
//
//        myLocationPathSpotManager = new PathSpotManager();
////        navigationImgView.setImageDrawable(myLocationPathSpotManager.newWrapperDrawable());
//
//
//
//        translateAll(40, 40);
//        navigationImgView.invalidate();
//
//    }
//
//
//    ArtworkMarker selectedMarker = null;
//    ArtworkMarker proximityMarker = null;
//    LocalizationSpot localizedSpot = null;
////    LocalizationSpot lastLocalizedPathSpot = null;
//
//
//
//
//    // GESTIONE MARKER SELEZIONATO:
//    @Override
//    public void onMarkerSpotSelected(ArtworkMarker newSelectedMarker) {
//
//        Marker oldSelectedMarker = selectedMarker;
//        if(oldSelectedMarker != null && oldSelectedMarker.isSelected() ) {
//            oldSelectedMarker.deselect();
//          //  FragmentHelper.instance().showArtworkListFragment(FragmentHelper.instance().artworkList_museumRow);
//        }
//
//        selectedMarker = newSelectedMarker;
//
//        if(selectedMarker != null)
//        {
//            selectedMarker.select();
//           // TODO:
//           // FragmentHelper.instance().showArtworkDetailsFragment((selectedMarker).getArtworkRow());
//        }
//        else
//        {
//            FragmentHelper.instance().showArtworkListFragment(FragmentHelper.instance().artworkList_museumRow);
//            markerManager.invalidate();
//        }
//
//
//
//    }
//
//    @Override
//    public void onNullMarkerSpotSelected() {
//        this.onMarkerSpotSelected(null);
//    }
//
//    public void simulateArtSpotSelection(ArtworkRow row) {
//        if(row != null && row.getPosition() != null)
//        {
//            Marker oldSelectedMarker = selectedMarker;
//            if(oldSelectedMarker != null && oldSelectedMarker.isSelected())
//                  oldSelectedMarker.deselect();
//
//            // TODO: CHECK NOT NULL?
//            selectedMarker = row.getPosition().getMarker();
//            selectedMarker.select();
//            markerManager.invalidate();
//        }
//    }
//
//
//    // GESTIONE QR CODE SCANNER (MARKER PIÙ VICINO E POSIZIONE CORRENTE)
//    public void onCameraScanResult(String qr_code_result) {
//        Spot scannedSpot = qr_spot_map.get(qr_code_result);
//        if(scannedSpot != null)
//        {
//            if( scannedSpot instanceof PathSpot )
//            {
//                newCurrentLocation((PathSpot)scannedSpot);
//                hideDijkstraPath();
//                lostCurrentLocation();
//            }
//            else if( scannedSpot instanceof ArtworkMarker)
//            {
//                ArtworkMarker scannedArtworkMarker = (ArtworkMarker) scannedSpot;
//                if(scannedArtworkMarker.getArtworkPosition() != null )
//                {
//                    this.onMarkerSpotSelected(scannedArtworkMarker);
//                    FragmentHelper.instance().getMainActivity().getSlidingUpPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
//                }
//                if( scannedArtworkMarker != null)
//                {
//                    //  // TODO: nearestPathSpot
//                    // newRealtimeLocalizationPosition(scannedArtworkMarker.getNearestPathSpot());
//                    hideDijkstraPath();
//                    lostCurrentLocation();
//                }
//
//            }
//        }
//    }
//
//    // GESTIONE BEACON (MARKER PIÙ VICINO E POSIZIONE CORRENTE)
//
//    public void removeCurrentLocation() {
//        boolean changed = false;
//
//        if(localizedSpot != null) {
//            pathSpotManager.remove(localizedSpot);
//            localizedSpot = null;
//            changed = true;
//            pathSpotManager.invalidate();
//        }
//    }
//
//    public void newCurrentQrLocation(PathSpot scannedSpot)
//    {
//        if(this.localizedSpot!= null)
//        {
//            if(this.localizedSpot.isCurrentlyLocalized())
//            {
//                // non fare nulla, sei giá localizzato da un beacon
//            }
//            else
//            {
//                this.myLocationPathSpotManager.remove(localizedSpot);
//                localizedSpot = new LocalizationSpot(scannedSpot);
//                localizedSpot.setCurrentlyLocalized(true);
//                this.myLocationPathSpotManager.add(localizedSpot);
//                localizationImgView.setImageDrawable(myLocationPathSpotManager.newWrapperDrawable());
//                this.myLocationPathSpotManager.invalidate();
//            }
//        }
//        else
//        {
//            localizedSpot = new LocalizationSpot( scannedSpot);
//            localizedSpot.setCurrentlyLocalized(true);
//            this.myLocationPathSpotManager.add(localizedSpot);
//            localizationImgView.setImageDrawable(myLocationPathSpotManager.newWrapperDrawable());
//
//            this.myLocationPathSpotManager.invalidate();
//        }
//    }
//    public void newCurrentLocation(PathSpot newMyLocationPathSpot) {
//        if(newMyLocationPathSpot != null)
//        {
//            if(localizedSpot != null)
//                myLocationPathSpotManager.remove(localizedSpot);
//
//            // TODO: localizedPathSpot
////            if(lastLocalizedPathSpot != null)
////                myLocationPathSpotManager.remove(lastLocalizedPathSpot);
////
////            lastLocalizedPathSpot = null;
////            newMyLocationPathSpot.setStepNumber(PathSpot.STEP_NUMBER_MY_POSITION);
//            localizedSpot =  new LocalizationSpot(newMyLocationPathSpot);
//            localizedSpot.setCurrentlyLocalized(true);
//
//            Toast toast = Toast.makeText(getActivity(),
//                    "Sei stato localizzato da un sensore!", Toast.LENGTH_SHORT);
//            toast.show();
//
//            myLocationPathSpotManager.add(localizedSpot);
//            localizationImgView.setImageDrawable(myLocationPathSpotManager.newWrapperDrawable());
//            myLocationPathSpotManager.invalidate();
//        }
//        else lostCurrentLocation();
//    }
//
//    public void lostCurrentLocation() {
//        if(localizedSpot != null ) {
//            localizedSpot.setCurrentlyLocalized(false);
//            //lastLocalizedPathSpot.setStepNumber(PathSpot.STEP_MY_LAST_POSITION);
//            //localizationImgView.setImageDrawable(myLocationPathSpotManager.newWrapperDrawable());
//            myLocationPathSpotManager.invalidate();
//
//            Toast toast = Toast.makeText(getActivity(),
//                    "Non sei più localizzato dai sensori.. la tua ultima posizione è stata registrata." +
//                            "\nAvvicinati ad un sensore o fai la foto ad un codice QR per aggiornare la tua posizione", Toast.LENGTH_LONG);
//            toast.show();
//        }
//    }
//
//
//
//
//    @Override
//    public void OnNewBeaconBestProximity(Beacon bestProximity, Beacon oldBestProximity) {
//
//        Spot beaconAssociatedSpot  = beacon_spot_map.get(ABeaconProximityManager.getID(bestProximity));
//
//        if( beaconAssociatedSpot instanceof ArtworkMarker)
//        {
//            // OLD PROXIMITY MARKER: (for statistics)
//            if(this.proximityMarker != null && this.proximityMarker.getArtworkPosition() != null && startProximityDate != null)
//            {
//                long lastTimeInApp = TimeStatisticsManager.readInAppTime(this.proximityMarker.getArtworkPosition().getArtworkRow());
//                long timeInSeconds = ((new Date()).getTime() - startProximityDate.getTime()) / 1000;
//                TimeStatisticsManager.writeInProximityTime(this.proximityMarker.getArtworkPosition().getArtworkRow(), timeInSeconds + lastTimeInApp);
//            }// TODO: COMMENTA SE CRASHA
//
//            // NEW PROXIMITY MARKER:
//            this.proximityMarker = (ArtworkMarker) beaconAssociatedSpot;
//
//            // TODO: nearestPathSpot
////            if( proximityMarker.getNearestPathSpot() != null)
////            {
////                newRealtimeLocalizationPosition(proximityMarker.getNearestPathSpot());
////                startProximityDate = new Date(); // for statistics
////            }
//        }
//        else if( beaconAssociatedSpot instanceof  PathSpot )
//        {
//            this.proximityMarker = null;
//            newCurrentLocation((PathSpot) beaconAssociatedSpot);
//        }
//
//        if(proximityMarker != null)
//        {
//            //lastLocalizedPathSpot = proximityMarker.getNearestPathSpot();
//            showProximityArtworkBeaconFAB();
//        }
//        else
//        {
//            hideProximityArtworkBeaconFAB();
//        }
//
//    }
//    @Override
//    public void OnNoneBeaconBestProximity(Beacon oldBestProximity) {
//        lostCurrentLocation();
//        hideProximityArtworkBeaconFAB();
//    }
//
//
//    public void showProximityArtworkBeaconFAB() {
//        Toast toast;
//        toolTipView = FragmentHelper.instance().getMainActivity()
//                .getArtworkFoundTooltipContainer().showToolTipForView(artworkproximityToolTip, FragmentHelper.instance().getMainActivity().findViewById(R.id.notifyToIndoor));
//
//        FragmentHelper.instance().getMainActivity().getFloatingActionButtonNotifyBeaconProximity().setVisibility(View.VISIBLE);
//        toast = Toast.makeText(getActivity(), "BEACON VICINO!!!", Toast.LENGTH_LONG);
//        toast.show();
//
//        FragmentHelper.instance().getMainActivity().getFloatingActionButtonNotifyBeaconProximity().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onMarkerSpotSelected(proximityMarker);
//                markerManager.invalidate();
//                toolTipView.remove();
//                FragmentHelper.instance().getMainActivity().getSlidingUpPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
//            }
//        });
//
//    }
//
//    public void hideProximityArtworkBeaconFAB() {
//        // rimuovi la notifica di una opera d'arte vicina. Nascondi pulsante per visualizzare l'opera
//        FragmentHelper.instance().getMainActivity().getFloatingActionButtonNotifyBeaconProximity().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        FragmentHelper.instance().getMainActivity().getFloatingActionButtonNotifyBeaconProximity().setVisibility(View.INVISIBLE);
//        toolTipView.remove();
//        //Toast toast = Toast.makeText(getActivity(), "BEACON LONTANO ...", Toast.LENGTH_SHORT);
//        //toast.show();
//    }
//
//
//
//    public int navigateToSelectedMarker() {
//
//
//        if(this.localizedSpot!= null && selectedMarker != null)
//        {
////            if(selectedMarker.getNearestPathSpot() != null)
////            {
////                navigationImgView.setImageDrawable(null);
////                pathSpotManager =  building.drawBestPath(localizedSpot.getAssociatedPathSpot(), selectedMarker.getNearestPathSpot());
////                navigationImgView.setImageDrawable(pathSpotManager.newWrapperDrawable());
////                pathSpotManager.invalidate();
////                return 1;
////            }
////            else
////            {
////                Toast toast = Toast.makeText(getActivity(),
////                        "Purtroppo l'opera d'arte selezionata non è stata localizzata sulla mappa...", Toast.LENGTH_SHORT);
////                toast.show();
////                return -1;
////            }
//            return 1;
//        }
//        else
//        {
//            Toast toast = Toast.makeText(getActivity(),
//                    "Purtroppo non siamo riusciti a localizzarti.. avvicinati ad un beacon o scannerizza un QR code", Toast.LENGTH_LONG);
//            toast.show();
//            return 0;
//        }
//
//    }
//
//
//
//
//    public void hideDijkstraPath() {
//        if(navigationImgView != null)
//           navigationImgView.setImageDrawable(null);
//    }
//
//
//    @Override
//    public void OnBeaconProximity(List<Beacon> proximityBeacons)
//    {
////
////        if(BeaconHelper.isInProximity(proximityBeacons.get(0) ))
////        {
////            //spot2.sele;
////            spot2.drawable().invalidateSelf();
////            foregroundImgView.invalidate();
////        }
//    }
//
//
//
//    public WeakReference<Bitmap> generateBackgroundBmp(Building building) {
//
//        WeakReference<Bitmap> frame =  new WeakReference<Bitmap>(Bitmap.createBitmap((int)building.getWidth(), (int)building.getHeight(), Bitmap.Config.ARGB_8888));
//
//        Canvas frameBuildingCanvas = new Canvas(frame.get());
//        frameBuildingCanvas.setMatrix(this.buildingMatrix);
//        building.draw(frameBuildingCanvas);
//
//        return frame;
//    }
//
//
//
//    private void translateAll(int x, int y) {
//
//        bgMatrix.postTranslate(x, y);
//        this.backgroundImgView.setImageMatrix(bgMatrix);
//        markerManager.translate(x, y);
//        pathSpotManager.translate(x, y);
//        myLocationPathSpotManager.translate(x, y);
//
//    }
//
//
//
//
//
//
//
//
//
//
//    // we can be in one of these 3 states
//    private static final int NONE = 0;
//    private static final int DRAG = 1;
//    private static final int ZOOM = 2;
//    private int mode = NONE;
//    // remember some things for zooming
//    private PointF start = new PointF();
//    private PointF mid = new PointF();
//    private float oldDist = 1f;
//    private float d = 0f;
//    private float newRot = 0f;
//    private float[] lastEvent = null;
//
//
//    private float scaleFactor = 1;
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event)
//    {
//
//        ImageView view = (ImageView) v;
//        float dx = 0;
//        float dy = 0;
//        // TODO: adesso disabilitiamo il gestore di eventi touch quando rileviamo 3 o più tocchi!!
//        // in futuro dovremo riabilitarlo. Lo abbiamo disabilitato per evitare un fastidioso bug,
//        // cioè che zoomando con 2 dita e poi toccando con un terzo la posizione degli spot
//        // veniva modificata erroneamente, come se il terzo dito avesse introdotto un altro zoom
//        // che però in realtá non introduce!
//        // Poichè non zooma il bg non dovrebbe influenzare nemmeno il foreground, ma invece lo fa,
//        // verificare perchè lo fa e fare in modo che non influenzi il foreground come non influenza
//        // il background.
//        if (lastEvent != null && event.getPointerCount() >= 3)
//        {
//            if( mode == ZOOM )
//            {
//                markerManager.holdScalingFactor();
//                pathSpotManager.holdScalingFactor();
//                myLocationPathSpotManager.holdScalingFactor();
//            }
//            mode = NONE;
//            lastEvent = null;
//        }
//        else switch (event.getAction() & MotionEvent.ACTION_MASK)
//        {
//            case MotionEvent.ACTION_DOWN:
//                bgSavedMatrix.set(bgMatrix);
//                fgSavedMatrix.set(fgMatrix);
//                start.set(event.getX(), event.getY());
//                mode = DRAG;
//                lastEvent = null;
//                break;
//
//            case MotionEvent.ACTION_POINTER_DOWN:
//                oldDist = spacing(event);
//                if (oldDist > 10f) {
//                    bgSavedMatrix.set(bgMatrix);
//                    fgSavedMatrix.set(fgMatrix);
//                    midPoint(mid, event);
//                    mode = ZOOM;
//                }
//                lastEvent = new float[4];
//                lastEvent[0] = event.getX(0);
//                lastEvent[1] = event.getX(1);
//                lastEvent[2] = event.getY(0);
//                lastEvent[3] = event.getY(1);
//                //TODO: DISATTIVATA ROTAZIONE, riabilitarla gestendo spostamento marker (come in zoom)
//                // d = rotation(event);
//
//                break;
//
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_POINTER_UP:
//                if(mode == ZOOM) {
//                    markerManager.holdScalingFactor();
//                    pathSpotManager.holdScalingFactor();
//                    myLocationPathSpotManager.holdScalingFactor();
//
//                }
//                mode = NONE;
//                lastEvent = null;
//
//
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                if (mode == DRAG)
//                {
//                    bgMatrix.set(bgSavedMatrix);
//                    fgMatrix.set(fgSavedMatrix);
//                    dx = event.getX() - start.x;
//                    dy = event.getY() - start.y;
//                    bgMatrix.postTranslate(dx, dy);
//                    fgMatrix.postTranslate(dx, dy);
//                }
//                else if (mode == ZOOM)
//                {
//                    float newDist = spacing(event);
//                    if (newDist > 10f) {
//                        bgMatrix.set(bgSavedMatrix);
//                        fgMatrix.set(fgSavedMatrix);
//
//                        float newScale = (newDist / oldDist);
//                        bgMatrix.postScale(newScale, newScale, mid.x, mid.y);
//                        // scala la matrice dello sfondo (mappa)
//
//
//
//                        markerManager.translateByRealtimeScaling(newScale);
//                        pathSpotManager.translateByRealtimeScaling(newScale);
//                        myLocationPathSpotManager.translateByRealtimeScaling(newScale);
//                        // scala la posizione del marker dovuta allo zoom dello sfondo,
//                        // in tempo reale senza zoomarlo, in modo che il centro del marker
//                        // sia sempre nello stesso punto dello sfondo (mappa) scalato.
//
//                    }
////
////                    if (lastEvent != null && event.getPointerCount() == 3) {
////                        newRot = rotation(event);
////                        float r = newRot - d;
////                        float[] values = new float[9];
////                        bgMatrix.getValues(values);
////                        float tx = values[2];
////                        float ty = values[5];
////                        float sx = values[0];
////                        float xc = (view.getWidth() / 2) * sx;
////                        float yc = (view.getHeight() / 2) * sx;
////
////                        //TODO: DISATTIVATA ROTAZIONE, riabilitarla gestendo spostamento marker (come in zoom)
////                        // bgMatrix.postRotate(r, tx + xc, ty + yc);
////                    }
//                }
//                break;
//        }
//
//        this.backgroundImgView.setImageMatrix(bgMatrix);
//
//        float[] matrixVal = new float[9];
//        bgMatrix.getValues(matrixVal);
//
//
//
//
//        markerManager.translate(matrixVal[2], matrixVal[5]);
//        pathSpotManager.translate(matrixVal[2], matrixVal[5]);
//        myLocationPathSpotManager.translate(matrixVal[2], matrixVal[5]);
//        //markerManager.invalidate();
//
//        //indoorMap.invalidateSelf(); //per disegno mappa indoor in vettoriale
//
////        this.ball.x = matrixVal[2];
////        this.ball.y = matrixVal[5];
////
////        this.ball2.x = matrixVal[2];
////        this.ball2.y = matrixVal[5];
////        this.drawable.invalidateSelf();
//        //this.foregroundImgView.setImageMatrix(fgMatrix);
//
//        if(event.getPointerCount() == 1 && dx == 0 && dy == 0 ) // lastEvent == null )// mode != ZOOM && mode !=DRAG)
//          markerManager.onTouch(view, event);
//        return true;
//    }
//
//    /**
//     * Determine the space between the first two fingers
//     */
//    private float spacing(MotionEvent event) {
//        float x = event.getX(0) - event.getX(1);
//        float y = event.getY(0) - event.getY(1);
//        return FloatMath.sqrt(x * x + y * y);
//    }
//
//    /**
//     * Calculate the mid point of the first two fingers
//     */
//    private void midPoint(PointF point, MotionEvent event) {
//        float x = event.getX(0) + event.getX(1);
//        float y = event.getY(0) + event.getY(1);
//        point.set(x / 2, y / 2);
//    }
//
//    /**
//     * Calculate the degree to be rotated by.
//     *
//     * @param event
//     * @return Degrees
//     */
//    private float rotation(MotionEvent event) {
//        double delta_x = (event.getX(0) - event.getX(1));
//        double delta_y = (event.getY(0) - event.getY(1));
//        double radians = Math.atan2(delta_y, delta_x);
//        return (float) Math.toDegrees(radians);
//    }
//
//
//}
