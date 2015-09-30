package micc.beaconav.indoorEngine;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import micc.beaconav.FragmentHelper;
import micc.beaconav.R;
import micc.beaconav.indoorEngine.building.Building;
import micc.beaconav.indoorEngine.building.Floor;
import micc.beaconav.indoorEngine.building.Room;
import micc.beaconav.indoorEngine.building.Vertex;
import micc.beaconav.indoorEngine.databaseLite.BuildingAdapter;
import micc.beaconav.indoorEngine.databaseLite.downloader.BuildingDownloader;
import micc.beaconav.indoorEngine.databaseLite.downloader.BuildingDownloaderListener;

/**
 * Created by nagash on 15/03/15.
 */
public class IndoorMapFragmentLite extends Fragment
    implements BuildingDownloaderListener

{


    private static String museumUrl = "http://trinity.micc.unifi.it/museumapp/database.sqlite";

    IndoorMap indoorMap = null;
    TextView tv;

    ViewGroup container = null;

    ImageView backgroundImgView;
    ImageView foregroundImgView;
    ImageView navigationImgView;
    ImageView localizationImgView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.container = container;
        return inflater.inflate(R.layout.fragment_indoor_map_lite, container, false);
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
        }

        BuildingDownloader downloader;
        downloader = new BuildingDownloader(this.getActivity(), museumUrl, this);
        downloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        tv = (TextView) this.getActivity().findViewById(R.id.textView7);
        tv.setText("downloading");
    }


    @Override
    public void onDownloadFinished(String downloadedFilePath) {
        // TODO: volendo è possibile rendere anche la generazione del Building asincrona con un thread..
        Building building = BuildingAdapter.generateBuilding(downloadedFilePath, this.getActivity());
        indoorMap = new IndoorMap(building,  backgroundImgView, foregroundImgView,
                                        navigationImgView, localizationImgView, this.getActivity());


        tv.setText("download finished");




    }










}
