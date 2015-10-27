package micc.beaconav.indoorEngine;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import micc.beaconav.FragmentHelper;
import micc.beaconav.R;
import micc.beaconav.db.dbHelper.museum.MuseumRow;
import micc.beaconav.indoorEngine.building.Building;
import micc.beaconav.indoorEngine.databaseLite.BuildingAdapter;
import micc.beaconav.indoorEngine.databaseLite.BuildingFactory;
import micc.beaconav.indoorEngine.databaseLite.downloader.BuildingDownloader;
import micc.beaconav.indoorEngine.databaseLite.downloader.BuildingDownloaderListener;
import micc.beaconav.outdoorEngine.Map;

/**
 * Created by nagash on 15/03/15.
 */
public class IndoorMapFragmentLite extends Fragment
    implements BuildingDownloaderListener
{


    //private static String museumUrl = "http://trinity.micc.unifi.it/museumapp/Applicazione/merciai/editorMuseum/db/database.sqlite";
   // private static String museumUrl = "http://whitelight.altervista.org/database.sqlite";

    private static String museumsUrl =  "http://trinity.micc.unifi.it/museumapp/museums/";
    private static String filename = "database.sqlite";

    private MuseumRow museum;

    IndoorMap indoorMap = null;

    ViewGroup container = null;
    ImageView backgroundImgView;
    ImageView foregroundImgView;
    ImageView navigationImgView;
    ImageView localizationImgView;
    FrameLayout frameLayout;
    TextView tv;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.container = container;
        return inflater.inflate(R.layout.fragment_indoor_map_lite, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        Map map = FragmentHelper.instance().getOutdoorMap();

        if(map != null)
            map.clearProximityNotificationTooltip();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        FragmentHelper.instance().getMainActivity().showMenuItemStopPath();

        if(container != null)
        {
            backgroundImgView = (ImageView) container.findViewById(R.id.backgroundImageView);
            foregroundImgView = (ImageView) container.findViewById(R.id.foregroundImageView);
            navigationImgView = (ImageView) container.findViewById(R.id.navigationLayerImageView);
            localizationImgView = (ImageView) container.findViewById(R.id.localizationLayerImageView);
            frameLayout = (FrameLayout) container.findViewById(R.id.indoorFrameLayout);
            tv = (TextView) this.getActivity().findViewById(R.id.textView7);
        }

        BuildingDownloader downloader;

        if(museum != null) {
            String museumUrl = museumsUrl + museum.getID() + "/" + filename;
            downloader = new BuildingDownloader(this.getActivity(), museumUrl, this);
            downloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            tv.setText(this.getString(R.string.indoor__message__downloading));
        }
        else tv.setText(this.getString(R.string.indoor__error__cant_start_download__museum_is_null));
    }


    public void setMuseum(MuseumRow museum) {
        this.museum = museum;
    }

    @Override
    public void onDownloadFinished(String downloadedFilePath) {
        // TODO: volendo è possibile rendere anche la generazione del Building asincrona con un thread..

        BuildingFactory buildingFactory = new BuildingFactory(downloadedFilePath, this.getActivity());
        tv.setText(this.getString(R.string.indoor__message__generating_building));
        Building building = null;
        try {
            building = buildingFactory.generateBuilding();
        }
        catch (BuildingAdapter.CantOpenDatabaseFileException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), this.getString(R.string.indoor__error__cant_generate_building__file_not_found),
                    Toast.LENGTH_LONG).show();
            tv.setText(this.getString(R.string.indoor__error__cant_generate_building__file_not_found_short));

            return;
        }
        catch (BuildingAdapter.CantOpenDatabaseException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), getString(R.string.indoor__error__cant_access_local_db), Toast.LENGTH_LONG).show();
            tv.setText(getString(R.string.indoor__error__cant_access_local_db));
            return;
        }

        if(building != null)
        {
            tv.setText(getString(R.string.indoor__message__generated_building));
            indoorMap = new IndoorMap(building,  backgroundImgView, foregroundImgView, navigationImgView, localizationImgView,
                    this.getActivity(), FragmentHelper.instance().getMainActivity());
            frameLayout.removeView(tv);

            FragmentHelper.instance().showArtworkListFragment(museum, indoorMap.getBuilding());

        }
        else
        {
            indoorMap = null;
            //tv.setText("Can't generate building...");

            FragmentHelper.instance().showArtworkListFragment(museum, null);

        }


    }

    @Override
    public void onDownloadFail(String notDownloadedFilePath) {
        Toast.makeText(getActivity(), getString(R.string.indoor__error__onDownloadFail), Toast.LENGTH_LONG).show();
        onDownloadFinished(notDownloadedFilePath);
    }


    public IndoorMap getIndoorMap() {
        return indoorMap;
    }

    public MuseumRow getMuseumRow() {
        return museum;
    }

    public Building getBuilding() {
        return indoorMap.getBuilding();
    }





}
