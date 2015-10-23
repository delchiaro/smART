package micc.beaconav;

import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
//import android.app.Activity;
import android.support.v4.app.Fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import micc.beaconav.db.timeStatistics.TimeStatisticsManager;
import micc.beaconav.gui.animationHelper.BackgroundColorChangerHSV;
import micc.beaconav.gui.animationHelper.DpHelper;
import micc.beaconav.gui.animationHelper.LayoutDimensionChanger;
import micc.beaconav.gui.animationHelper.ScrollViewResizer;
import micc.beaconav.fragments.backPressedListeners.OnBackPressedListener;
import micc.beaconav.fragments.backPressedListeners.VoidOnBackPressedListener;
import micc.beaconav.__test.JSONTest;
import micc.beaconav.__test.testAdaptedLocationActivity;
import micc.beaconav.__test.testLastLocationActivity;


import com.getbase.floatingactionbutton.FloatingActionButton;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */

public class MainActivity extends FragmentActivity
{

    private static final String TAG = "MainActivity";
    private DpHelper dpHelper;
    private Context context;


    private TextView textViewFormat, textViewContent;
    private String scanFormat, scanContent; //variabili per i risultati della scan


// * * * * * * * * * * * * COSTANTI (evitiamo i numeri magici) * * * * * * * * * * * * * * * * * *
    final float ANCHOR_POINT = 0.5f;
    final int SLIDING_BAR_EXPANDED_HEIGHT_DP = 120;
    final int SLIDING_BAR_HEIGHT_DP = 64;
    final int SEARCH_BAR_PADDING_DP = 8;
    final  int SEARCH_BAR_HIDED_PADDING_DP = -60;

//* * * * * * * * * * * *  FLAGS  * * * * * * * * * * * * * * *
    private boolean fadeOutAnimationStarted = false;
    private boolean colorAnimationStarted = false;
    private boolean heightAnimationStarted = false;

// * * * * * * * * * * * *  DEFINIZIONE E INIZIALIZZAZIONE LAYOUT * * * * * * * * * * * * * * * * *
    private RelativeLayout fragmentHeaderContainer;
    private LinearLayout mSlidingBar;
    private RelativeLayout fragmentListContainer;
    private SlidingUpPanelLayout mSlidingUpPanelLayout;
    private SearchView mSearch;
    private FloatingActionButton floatingActionButton;
    private FloatingActionButton floatingActionButtonQRScanBtn;
    private FloatingActionButton floatingActionButtonNotifyBeaconProximity;
    private FloatingActionButton floatingActionButtonNotifyToIndoor;
    private ToolTipRelativeLayout toIndoorTooltipContainer;
    private ToolTipRelativeLayout artworkFoundTooltipContainer;



    private void initFragments(){
        FragmentHelper.setMainActivity(this);
        FragmentHelper.instance().showOutdoorFragment();
    }

    private void initActivityAndXML() {
    // FIND VIEW BY I_D * * * * * * * * * * * * * * * * * * * * * * * *
        //mSearch = (SearchView) findViewById(R.id.search_view);
        mSlidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        //mSlidingUpPanelLayout.setEnableDragViewTouchEvents(true);


        floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_button);
        floatingActionButtonQRScanBtn = (FloatingActionButton) findViewById(R.id.scanCodeBtn);
        floatingActionButtonNotifyBeaconProximity = (FloatingActionButton) findViewById(R.id.notifyArtworkProximity);
        floatingActionButtonNotifyToIndoor = (FloatingActionButton) findViewById(R.id.notifyToIndoor);
        fragmentHeaderContainer = (RelativeLayout) findViewById(R.id.fragment_sliding_header_container);
        mSlidingBar = (LinearLayout) findViewById(R.id.slidingBar);
        fragmentListContainer = (RelativeLayout) findViewById(R.id.fragment_list_container);
        textViewContent = (TextView) findViewById(R.id.scan_format);
        textViewFormat = (TextView) findViewById(R.id.scan_content);
        toIndoorTooltipContainer = (ToolTipRelativeLayout) findViewById(R.id.to_indoor_tooltip_container);
        artworkFoundTooltipContainer = (ToolTipRelativeLayout) findViewById(R.id.artwork_found_tooltip_container);


    // INIT * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
        mSlidingUpPanelLayout.setAnchorPoint(ANCHOR_POINT);
        mSlidingUpPanelLayout.setDragView(fragmentHeaderContainer);
        // sliding avviene solo se si scrolla sulla slidingBar e non se si scrolla il contenuto
    }

    public void initEventListeners()  {

        mSlidingUpPanelLayout.setPanelSlideListener(new PanelSlideListener() {


            SlidingBarExtensionAnimationManager slidingBarHeightAnimMan = new SlidingBarExtensionAnimationManager(mSlidingBar, fragmentHeaderContainer, context);
            ObjectAnimator slidingBarHeightAnimation;
            BackgroundColorChangerHSV slidingBarColorChanger = new BackgroundColorChangerHSV(fragmentHeaderContainer, 255, 152, 0);
            ObjectAnimator slidingBarColorAnimation;
            ScrollViewResizer scrollViewResizer = new ScrollViewResizer(mSlidingUpPanelLayout, fragmentListContainer);

//            MarginChanger marginChanger = new MarginChanger((RelativeLayout.MarginLayoutParams) mSearch.getLayoutParams());
//            ObjectAnimator searchBarAnimation;


            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                scrollViewResizer.resizeScrollView(1 - slideOffset);

                if (slideOffset >= 0.002) {
                    if (colorAnimationStarted == false) {
                        colorAnimationStarted = true;

                        if (themeColor == ThemeColor.ORANGE) {

                            slidingBarHeightAnimation = ObjectAnimator.ofFloat(slidingBarColorChanger, "saturation", 0, slidingBarColorChanger.getS());
                            slidingBarHeightAnimation.setDuration(500);
                            slidingBarHeightAnimation.start();
                        }
                    }
                }

                if (slideOffset < 0.002) {
                    if (colorAnimationStarted == true) {
                        colorAnimationStarted = false;

                        if (themeColor == ThemeColor.ORANGE) {
                            //slidingBarHeightAnimation = ObjectAnimator.ofFloat(slidingBarColorChanger, "saturation", slidingBarColorChanger.getS(), 0);
                            slidingBarHeightAnimation = ObjectAnimator.ofFloat(slidingBarColorChanger, "saturation", slidingBarColorChanger.getS(), 0);
                            slidingBarHeightAnimation.setDuration(200);
                            slidingBarHeightAnimation.start();
                        }
                    }

                }


                if (slideOffset >= 0.4) {

                    if (fadeOutAnimationStarted == false) {

//                        Animation a = new Animation() {
//                            @Override
//                            protected void applyTransformation(float interpolatedTime, Transformation t) {
//                                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mSearch.getLayoutParams();
//                                params.topMargin = DpHelper.dpToPx(SEARCH_BAR_PADDING_DP, context) + (int) (dpHelper.dpToPx(SEARCH_BAR_HIDED_PADDING_DP) * interpolatedTime);
//                                mSearch.setLayoutParams(params);
//                            }
//                        };
//                        a.setDuration(200);
//                        mSearch.startAnimation(a);

                        fadeOutAnimationStarted = true;

                         /*
                            SOSTITUITO COL CODICE SOPRA, NON SO PERCHÈ QUESTO NON FUNZIONA BENE:
                             animMargin = ObjectAnimator.ofInt(marginChanger, "topMargin", DpHelper.dpToPx(8, context), DpHelper.dpToPx(60, context));
                             animMargin.setDuration(1000);
                             animMargin.start();
                         */


                    }
                } else if (slideOffset < 0.4) {
                    if (fadeOutAnimationStarted == true) {
//                        Animation a = new Animation() {
//                            @Override
//                            protected void applyTransformation(float interpolatedTime, Transformation t) {
//                                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mSearch.getLayoutParams();
//                                params.topMargin = dpHelper.dpToPx(SEARCH_BAR_PADDING_DP) + (int) (dpHelper.dpToPx(SEARCH_BAR_HIDED_PADDING_DP) * (1 - interpolatedTime));
//                                mSearch.setLayoutParams(params);
//                            }
//                        };
//                        a.setDuration(200);
//                        mSearch.startAnimation(a);
//                        fadeOutAnimationStarted = false;


                        /*
                           SOSTITUITO COL CODICE SOPRA, NON SO PERCHÈ QUEST NON FUNZIONA BENE:
                            marginChanger = new MarginChanger((RelativeLayout.MarginLayoutParams)mSearch.getLayoutParams() );
                            animMargin = ObjectAnimator.ofInt(marginChanger, "topMargin", DpHelper.dpToPx(60, context), DpHelper.dpToPx(8, context));
                            animMargin.setDuration(1000);
                            animMargin.start();
                        */
                    }
                }

                if (slideOffset >= 0.95) {
                    if (heightAnimationStarted != true) {
                        slidingBarHeightAnimation = ObjectAnimator.ofInt(slidingBarHeightAnimMan, "dpHeight", SLIDING_BAR_HEIGHT_DP, SLIDING_BAR_EXPANDED_HEIGHT_DP);
                        slidingBarHeightAnimation.setDuration(200);
                        slidingBarHeightAnimation.start();
                        heightAnimationStarted = true;
                    }

                }

                if (slideOffset < 0.95) {
                    if (heightAnimationStarted != false) {
                        slidingBarHeightAnimation = ObjectAnimator.ofInt(slidingBarHeightAnimMan, "dpHeight", SLIDING_BAR_EXPANDED_HEIGHT_DP, SLIDING_BAR_HEIGHT_DP);
                        slidingBarHeightAnimation.setDuration(500);
                        slidingBarHeightAnimation.start();
                        heightAnimationStarted = false;
                    }
                }

            }


            @Override
            public void onPanelExpanded(View panel) {
                Log.i(TAG, "onPanelExpanded");

                scrollViewResizer.resizeScrollView(0.0f);

            }

            @Override
            public void onPanelCollapsed(View panel) {
                Log.i(TAG, "onPanelCollapsed");

            }

            @Override
            public void onPanelAnchored(View panel) {
                Log.i(TAG, "onPanelAnchored");

                scrollViewResizer.resizeScrollView(ANCHOR_POINT);

            }

            @Override
            public void onPanelHidden(View panel) {
                Log.i(TAG, "onPanelHidden");
            }
        });

        floatingActionButtonQRScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.scanCodeBtn) {
                    IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
                    scanIntegrator.initiateScan();
                }
            }
        });


    }

    public void initUniversalImageLoader() {

        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(3) // default
                .memoryCache(new FIFOLimitedMemoryCache(20 * 1024 *1024)) //20MB ram cache
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);


    }


// * * * * * * * * * * * * * * *  EVENT MANAGER DELLA ACTIVITY * * * * * * * * * * * * * * *

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.context = this;
        dpHelper = new DpHelper(this);
        TimeStatisticsManager.init(this);


        initActivityAndXML();
        initEventListeners();
        initFragments();
        initUniversalImageLoader();
        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null)
        {
            // Restore value of members from saved state
        }
        else
        {
            // Probably initialize members with default values for a new instance
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
       // initFragments();
    }


    @Override
    protected void onPause() {
        super.onPause();
        System.exit(0);
        //TODO: rimuovere questo exit erisolvere bug che quando pauso l'app (premo home) e la riapro poi cliccando su un google map marker crasha.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null && scanningResult.getContents() != null)
        {
            scanContent = scanningResult.getContents();
            scanFormat = scanningResult.getFormatName();
            if(FragmentHelper.instance().indoorMapFragmentLite != null)
                FragmentHelper.instance().indoorMapFragmentLite.getIndoorMap().onCameraScanResult(scanContent);
            // textViewFormat.setText("FORMAT: " + scanFormat);
            // textViewContent.setText("CONTENT: " + scanContent);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (mSlidingUpPanelLayout != null) {
            if (mSlidingUpPanelLayout.getPanelState() == PanelState.HIDDEN) {
                item.setTitle(R.string.action_show);
            } else {
                item.setTitle(R.string.action_hide);
            }
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_toggle:
            {
                if (mSlidingUpPanelLayout != null) {
                    if (mSlidingUpPanelLayout.getPanelState() != PanelState.HIDDEN) {
                        mSlidingUpPanelLayout.setPanelState(PanelState.HIDDEN);
                        item.setTitle(R.string.action_show);
                    } else {
                        mSlidingUpPanelLayout.setPanelState(PanelState.EXPANDED);
                        item.setTitle(R.string.action_hide);
                    }
                }
                return true;
            }
            case R.id.action_anchor:
            {
                if (mSlidingUpPanelLayout != null) {
                    if (mSlidingUpPanelLayout.getAnchorPoint() == 1.0f) {
                        mSlidingUpPanelLayout.setAnchorPoint(0.7f);
                        mSlidingUpPanelLayout.setPanelState(PanelState.ANCHORED);
                        item.setTitle(R.string.action_anchor_enable);
                    } else {
                        mSlidingUpPanelLayout.setAnchorPoint(1.0f);
                        mSlidingUpPanelLayout.setPanelState(PanelState.COLLAPSED);
                        item.setTitle(R.string.action_anchor_disable);
                    }
                }
                return true;
            }
            case R.id.json_test:
            {
                Intent intent = new Intent(MainActivity.this, JSONTest.class);
                startActivity(intent);
                return true;
            }
            case R.id.location_test:
            {
                Intent intent = new Intent(MainActivity.this, testAdaptedLocationActivity.class);
                startActivity(intent);
                return true;

            }
            case R.id.single_location_test:
            {
                Intent intent = new Intent(MainActivity.this, testLastLocationActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.proximity_test:
            {
               FragmentHelper.instance().mapFragment.toggleFakeProximity();
               return true;
            }
            case R.id.stop_best_path:
            {
                Fragment indoorMapFrag = FragmentHelper.instance().indoorMapFragmentLite;
                if(indoorMapFrag != null)
                   FragmentHelper.instance().indoorMapFragmentLite.getIndoorMap().hideDijkstraPath();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private static final int REQUEST_ENABLE_BT = 1234;

    @Override
    public View onCreateView(String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view =  super.onCreateView(name, context, attrs);
        return view;
    }


//* * * * * * * * * * * * GETTERS * * * * * * * * * * * * * * * * * * *

    public SlidingUpPanelLayout getSlidingUpPanelLayout()
    {
        return this.mSlidingUpPanelLayout;
    }
    public RelativeLayout getFragmentHeaderContainer()
    {
        return this.fragmentHeaderContainer;
    }
    public FloatingActionButton getFloatingActionButton() {
        return floatingActionButton;
    }
    public FloatingActionButton getFloatingActionButtonQRScanBtn() {
        return floatingActionButtonQRScanBtn;
    }
    public FloatingActionButton getFloatingActionButtonNotifyBeaconProximity() {
        return floatingActionButtonNotifyBeaconProximity;
    }
    public FloatingActionButton getFloatingActionButtonNotifyToIndoor() {
        return floatingActionButtonNotifyToIndoor;
    }

    public ToolTipRelativeLayout getToIndoorTooltipContainer() {
        return toIndoorTooltipContainer;
    }

    public ToolTipRelativeLayout getArtworkFoundTooltipContainer() {
        return artworkFoundTooltipContainer;
    }

    //    public SearchView getSearchBar() {
//        return mSearch;
//    }
    // * * * * * * * * * * * * * * *  ALTRI EVENT MANAGER CREATI BEACONAV * * * * * * * * * * * * * * *



    public enum ThemeColor {  ORANGE, PURPLE, RED; }
    private ThemeColor themeColor = ThemeColor.ORANGE;

    public void setThemeColor(ThemeColor newColor) {
        switch(newColor)
        {
            case ORANGE:
                if(mSlidingUpPanelLayout.getPanelState() == PanelState.COLLAPSED )
                    fragmentHeaderContainer.setBackgroundColor(getResources().getColor(R.color.white));
                else fragmentHeaderContainer.setBackgroundColor(getResources().getColor(R.color.orange));

                floatingActionButton.setColorNormal(getResources().getColor(R.color.orange));
                floatingActionButton.setColorPressed(getResources().getColor(R.color.orange_pressed));
                this.themeColor = ThemeColor.ORANGE;
                break;

            case PURPLE:
                fragmentHeaderContainer.setBackgroundColor(getResources().getColor(R.color.material_deep_purple));
                floatingActionButton.setColorNormal(getResources().getColor(R.color.material_deep_purple));
                floatingActionButton.setColorPressed(getResources().getColor(R.color.material_deep_purple_pressed));
                this.themeColor = ThemeColor.PURPLE;
                break;

            case RED:
                fragmentHeaderContainer.setBackgroundColor(getResources().getColor(R.color.material_red));
                floatingActionButton.setColorNormal(getResources().getColor(R.color.material_red));
                floatingActionButton.setColorPressed(getResources().getColor(R.color.material_red_pressed));
                this.themeColor = ThemeColor.RED;
                break;
        }
    }

    public void setFABListener(View.OnClickListener onClickListener)
    {
        floatingActionButton.setOnClickListener(onClickListener);
    }

    public void hideFAB() {
        // TODO
    }
    public void showFab() {
        // TODO
    }


    public void hideMenuItemStopPath() {

        MenuItem stopPathItem = (MenuItem) findViewById(R.id.stop_best_path);
        if(stopPathItem != null)
           stopPathItem.setVisible(false);
    }
    public void showMenuItemStopPath() {
        MenuItem stopPathItem = (MenuItem) findViewById(R.id.stop_best_path);
        if(stopPathItem != null)
             stopPathItem.setVisible(true);
    }



    //Listener di default per il back, quando siamo nella lista di musei
    private OnBackPressedListener backPressedListener = new VoidOnBackPressedListener();

    public void setOnBackPressedListener(OnBackPressedListener listener) {
        this.backPressedListener = listener;
    }
    @Override
    public void onBackPressed() {
        backPressedListener.doBack();
    }


}


// HELPER PER ANIMAZIONE
class SlidingBarExtensionAnimationManager {

    LayoutDimensionChanger c1;
    LayoutDimensionChanger c2;
    SlidingUpPanelLayout v3;
    Context context;

    SlidingBarExtensionAnimationManager(ViewGroup v1, ViewGroup v2, Context context)
    {
        this.c1 = new LayoutDimensionChanger(v1, context);
        this.c2 = new LayoutDimensionChanger(v2, context);
        this.context = context;
    }
    public void setDpHeight(int dpHeight)
    {
        c1.setDpHeight(dpHeight);
        c2.setDpHeight(dpHeight);
    }
}

