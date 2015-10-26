package micc.beaconav.fragments.slidingContentFragment.slidingContentDescription;


import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Date;

import micc.beaconav.FragmentHelper;
import micc.beaconav.R;
import micc.beaconav.db.imageDownloader.ImagesDownloader;
import micc.beaconav.db.timeStatistics.TimeStatisticsManager;
import micc.beaconav.indoorEngine.ArtworkRow;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class ArtworkDescrFragment extends Fragment {


    private ArtworkRow  artworkRow           = null;
    //private WebView     webViewArtwork       = null;
    private ImageView   imageViewArtwork     = null;
    private TextView    textViewArtworkDescr = null;
    private TextView    textViewArtistDescr  = null;
    private TextView    textViewYear         = null;
    private TextView    textViewLocation     = null;
    private TextView    textViewArtistName   = null;
    private TextView    textViewDimensions   = null;
    private TextView    textViewType         = null;

    private FloatingActionButton navToArtworkBtn = null;


    Date startNavigationDate;

    public ArtworkDescrFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_artwork_descr, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textViewArtworkDescr = (TextView) getView().findViewById(R.id.artworkDescription);
        //webViewArtwork       = (WebView)  getView().findViewById(R.id.artworkImage);
        imageViewArtwork     = (ImageView) getView().findViewById(R.id.artworkImage);
        textViewArtistName   = (TextView) getView().findViewById(R.id.artistName);
        textViewYear         = (TextView) getView().findViewById(R.id.year);
        textViewLocation     = (TextView) getView().findViewById(R.id.location);
        textViewArtistDescr  = (TextView) getView().findViewById(R.id.artistDescription);
        textViewDimensions   = (TextView) getView().findViewById(R.id.dimensions);
        textViewType         = (TextView) getView().findViewById(R.id.type);



        if(artworkRow != null)
        {
            textViewArtworkDescr.setText(artworkRow.getDescription());
            textViewArtistName.setText(getString(R.string.indoor__label__artist)+ artworkRow.getArtistName());
            textViewYear.setText(getString(R.string.indoor__label__year) + artworkRow.getCreationYear());
            textViewLocation.setText(getString(R.string.indoor__label__dimensions) + artworkRow.getLocation());
            textViewArtistDescr.setText(artworkRow.getArtistDescr());
            textViewDimensions.setText(getString(R.string.indoor__label__dimensions) + artworkRow.getDimensions());
            textViewType.setText(getString(R.string.indoor__label__tecnique) + artworkRow.getType());

            //ImagesDownloader dbImagesDownloader = new ImagesDownloader();
          //  "https://pbs.twimg.com/profile_images/458905059204423680/T3ZMCaFQ.jpeg"
            //dbImagesDownloader.loadImage(webViewArtwork, artworkRow.get_artworkImageUrl(), this.getActivity().getApplicationContext());

            DisplayImageOptions displayOption = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(artworkRow.get_artworkImageUrl(),imageViewArtwork, displayOption );

        }


        navToArtworkBtn = FragmentHelper.instance().getMainActivity().getFloatingActionButton();
        navToArtworkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( artworkRow != null && artworkRow instanceof ArtworkRow)
                {
                    if(FragmentHelper.instance().indoorMapFragmentLite != null)
                    {
                        FragmentHelper.instance().indoorMapFragmentLite.getIndoorMap().navigateToSelectedMarker();
                        FragmentHelper.instance().getMainActivity().getSlidingUpPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }
                }
            }
        });

    }



    @Override
    public void onResume() {
        super.onResume();
        startNavigationDate = new Date();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (artworkRow != null) {
            long lastTimeInApp = TimeStatisticsManager.readInAppTime(this.artworkRow);
            long timeInSeconds = ((new Date()).getTime() - startNavigationDate.getTime()) / 1000;
            TimeStatisticsManager.writeInAppTime(this.artworkRow, timeInSeconds + lastTimeInApp);
        }
    }

    public void setArtworkRow(ArtworkRow row){
        this.artworkRow = row;
        if(textViewArtworkDescr != null) {
            textViewArtworkDescr.setText(artworkRow.getDescription());
        }
    }



}
