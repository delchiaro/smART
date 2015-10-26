package micc.beaconav.fragments.slidingContentFragment.slidingContentDescription;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import micc.beaconav.R;
import micc.beaconav.db.dbHelper.DbManager;
import micc.beaconav.db.dbHelper.artworkImage.ArtworkImageRow;
import micc.beaconav.db.dbHelper.museum.MuseumRow;
import micc.beaconav.FragmentHelper;
import micc.beaconav.db.dbJSONManager.JSONDownloader;
import micc.beaconav.db.dbJSONManager.JSONHandler;
import micc.beaconav.db.imageDownloader.ImagesDownloader;
import micc.beaconav.db.timeStatistics.TimeStatisticsManager;
import micc.beaconav.gui.animationHelper.DpHelper;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class MuseumDescrFragment extends Fragment implements JSONHandler<ArtworkImageRow>
{

    private FloatingActionButton toIndoorBtn    = null;
    private FloatingActionButton navToMuseumBtn = null;

    private MuseumRow museumRow               = null;



    Date startNavigationDate;



    public MuseumDescrFragment() {}


    @Override
    public void onResume() {
        super.onResume();
        startNavigationDate = new Date();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(museumRow != null) {
            long lastTimeInApp = TimeStatisticsManager.readInAppTime(this.museumRow);
            long timeInSeconds = ((new Date()).getTime() - startNavigationDate.getTime()) / 1000;
            TimeStatisticsManager.writeInAppTime(this.museumRow, timeInSeconds + lastTimeInApp);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_museum_descr, container, false);

    }





    private boolean tryToShowInfo(TextView view, String label, String info)
    {
        if(info == null || info.equals("")) {
            view.setVisibility(View.GONE);
            return false;
        }
        else {
            view.setText(label + info);
            view.setVisibility(View.VISIBLE);
            return true;
        }
    }

    private boolean tryToShowInfo(TextView view, int id_resource_label, String info)
    {
        return tryToShowInfo(view, getString(id_resource_label), info);
    }




    private RelativeLayout artworkInMuseumLayout  = null;
    private RelativeLayout museumDescrLayout      = null;
    private RelativeLayout museumMapPreviewLayout = null;

    private TextView  textViewMuseumDescr     = null;
    private LinearLayout imgContainer = null;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        toIndoorBtn         = (FloatingActionButton)getView().findViewById(R.id.toIndoorBtn);
        navToMuseumBtn      = FragmentHelper.instance().getMainActivity().getFloatingActionButton();

        artworkInMuseumLayout = (RelativeLayout)getView().findViewById(R.id.artwork_in_museum_layout);
        museumDescrLayout =     (RelativeLayout)getView().findViewById(R.id.museum_description_layout);
        museumMapPreviewLayout = (RelativeLayout)getView().findViewById(R.id.museum_map_preview_layout);

        textViewMuseumDescr = (TextView)getView().findViewById(R.id.museumDescription);
        imgContainer        = (LinearLayout)getView().findViewById(R.id.imgContainer);


        artworkInMuseumLayout.setVisibility(View.GONE);
        DbManager.getArtworkDownloader(museumRow.getID()).addHandler(this);
        DbManager.getArtworkDownloader(museumRow.getID()).startDownload();


        if(museumRow != null) {
            if( tryToShowInfo(textViewMuseumDescr, "", museumRow.getDescr() ) )
                museumDescrLayout.setVisibility(View.VISIBLE);
            else  museumDescrLayout.setVisibility(View.GONE);
        }
        else{
            tryToShowInfo(textViewMuseumDescr, "", null);
            museumDescrLayout.setVisibility(View.GONE);
        }

        toIndoorBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentHelper.instance().showIndoorFragment(museumRow);
                FragmentHelper.instance().getMainActivity().getSlidingUpPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                // TODO:    QUESTO CODICE SAREBBE MEGLIO SE SI RIUSCISSE A PORTARLO NELL'HELPER E/O MAIN ACTIVITY RENDENDOLO FRUIBILE CON METODO PUBBLICO
                // BASTEREBBE NEL FRAGMENT HELPER FARE DEI METODI CHE ALZANO E ABBASSANO IL PANEL DEL MAIN RICHIAMANDO UN METODO PUBBLICO DEL  MAIN CHE FA QUESTO.
            }
        });


        navToMuseumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (museumRow != null && museumRow instanceof MuseumRow)
                    FragmentHelper.instance().navigateToMuseumOnBtnClick((MuseumRow) museumRow, v);
                FragmentHelper.instance().getMainActivity().getSlidingUpPanelLayout().setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

    }




    @Override
    public void onJSONDownloadFinished(ArtworkImageRow[] result)
    {
        if(result.length > 0)
            artworkInMuseumLayout.setVisibility(View.VISIBLE);

        DisplayImageOptions displayOption = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ViewGroup.LayoutParams dimens = imgContainer.getLayoutParams();
        dimens.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dimens.width= ViewGroup.LayoutParams.WRAP_CONTENT;

        for(int i = 0; i < result.length ; i++)
        {
            ImageView imageView = new ImageView(this.getActivity());

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(result[i].getLink(), imageView, displayOption);

            imgContainer.addView(imageView);
            imgContainer.setLayoutParams(dimens);

            imageView.setMaxHeight(DpHelper.dpToPx(300, getActivity()));
            //imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

//        ImagesDownloader dbImagesDownloader = new ImagesDownloader();
//        dbImagesDownloader.loadGallery(imgContainer, images);

    }

    //Questo setter è fondamentale, al Fragment di quale museo sto parlando
    public void setMuseumRow(MuseumRow row){
        this.museumRow = row;
        if(textViewMuseumDescr != null)
        {
            textViewMuseumDescr.setText(museumRow.getDescr());
        }
    }


}
