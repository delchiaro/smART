package micc.beaconav.fragments.slidingContentFragment.slidingContentDescription;


import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Date;

import micc.beaconav.FragmentHelper;
import micc.beaconav.R;
import micc.beaconav.db.timeStatistics.TimeStatisticsManager;
import micc.beaconav.gui.animationHelper.DpHelper;
import micc.beaconav.indoorEngine.ArtworkRow;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class ArtworkDescrFragment extends Fragment {


    private ArtworkRow  artworkRow           = null;


    private RelativeLayout layoutContainer = null;
    private RelativeLayout layoutImage = null;
    private RelativeLayout layoutInfo = null;
    private RelativeLayout layoutDescr = null;
    private RelativeLayout layoutArtistInfo = null;

    private ImageView   imageViewArtwork     = null;
    private WebView     webViewArtworkDescr = null;
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


    private boolean tryToShowInfo(TextView view, String label, String info)
    {
        if(info == null || info.equals("")) {
            view.setVisibility(View.GONE);
            return false;
        }
        else {
            view.setText(label + info);
            return true;
        }
    }

    private boolean tryToShowInfo(TextView view, int id_resource_label, String info)
    {
        return tryToShowInfo(view, getString(id_resource_label), info);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        layoutImage      = (RelativeLayout) getView().findViewById(R.id.layout_image);
        layoutContainer  = (RelativeLayout) getView().findViewById(R.id.artworkInfoLayout);
        layoutInfo       = (RelativeLayout) getView().findViewById(R.id.layout_info);
        layoutDescr      = (RelativeLayout) getView().findViewById(R.id.layout_descr);
        layoutArtistInfo = (RelativeLayout) getView().findViewById(R.id.layout_artist_info);


        webViewArtworkDescr = (WebView) getView().findViewById(R.id.artworkDescription);
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

            // Info
            boolean atLeastInfo = false;
            atLeastInfo |= tryToShowInfo(textViewArtistName, R.string.indoor__label__artist, artworkRow.getArtistName());
            atLeastInfo |= tryToShowInfo(textViewYear,         R.string.indoor__label__year,        artworkRow.getCreationYear());
            atLeastInfo |= tryToShowInfo(textViewType,         R.string.indoor__label__tecnique,    artworkRow.getType());
            atLeastInfo |= tryToShowInfo(textViewDimensions,   R.string.indoor__label__dimensions,  artworkRow.getDimensions());
            atLeastInfo |= tryToShowInfo(textViewLocation,     R.string.indoor__label__location,    artworkRow.getLocation());
            if(atLeastInfo == false)
                layoutInfo.setVisibility(View.GONE);

            // Description
            setDescription(true);


            // Artist Info
            boolean hasArtistInfo = false;
            hasArtistInfo = tryToShowInfo(textViewArtistDescr,  "", artworkRow.getArtistDescr());
            if( hasArtistInfo == false )
                layoutArtistInfo.setVisibility(View.GONE);






            //ImagesDownloader dbImagesDownloader = new ImagesDownloader();
          //  "https://pbs.twimg.com/profile_images/458905059204423680/T3ZMCaFQ.jpeg"
            //dbImagesDownloader.loadImage(webViewArtwork, artworkRow.get_artworkImageUrl(), this.getActivity().getApplicationContext());

            if( artworkRow.get_artworkImageUrl() == null )
            {
                imageViewArtwork.setVisibility(View.GONE);
                layoutImage.setVisibility(View.GONE);
            }
            else
            {
                DisplayImageOptions displayOption = new DisplayImageOptions.Builder()
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .build();
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(artworkRow.get_artworkImageUrl(), imageViewArtwork, displayOption);
            }
        }
        else
        {
            layoutImage.setVisibility(View.GONE);
            imageViewArtwork.setVisibility(View.GONE);
            layoutInfo.setVisibility(View.GONE);
            layoutDescr.setVisibility(View.GONE);
            layoutArtistInfo.setVisibility(View.GONE);
        }


        navToArtworkBtn = FragmentHelper.instance().getMainActivity().getFloatingActionButton();
        navToArtworkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (artworkRow != null && artworkRow instanceof ArtworkRow) {
                    if (FragmentHelper.instance().indoorMapFragmentLite != null) {
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
        if(webViewArtworkDescr != null ) {
            setDescription(false);
        }
    }


    private void setDescription(boolean constructor)
    {
        String descr = artworkRow.getDescription();
        if(descr != null && (!descr.equals("")) )
        {
            if(!constructor) {
                webViewArtworkDescr.setVisibility(View.VISIBLE);
                layoutDescr.setVisibility(View.VISIBLE);
            }
            // TODO: SET WEBVIEW TEXT SIZE
            //WebSettings webSettings = webViewArtworkDescr.getSettings();
            //webSettings.setDefaultFontSize(FragmentHelper.spToPx(6));

            String text = getResources().getString(R.string.html_header);
            text += artworkRow.getDescription();
            text += getResources().getString(R.string.html_footer);
            webViewArtworkDescr.loadData(text, "text/html", null);
        }
        else
        {
            webViewArtworkDescr.setVisibility(View.GONE);
            layoutDescr.setVisibility(View.GONE);
        }
    }


}
