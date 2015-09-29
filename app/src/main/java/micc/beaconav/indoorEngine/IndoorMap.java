package micc.beaconav.indoorEngine;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.HashMap;

import micc.beaconav.indoorEngine.building.Building;
import micc.beaconav.indoorEngine.building.spot.Spot;
import micc.beaconav.indoorEngine.databaseLite.downloader.BuildingDownloader;
import micc.beaconav.indoorEngine.databaseLite.downloader.BuildingDownloaderListener;
import micc.beaconav.indoorEngine.databaseLite.BuildingSqliteHelper;
import micc.beaconav.indoorEngine.databaseLite.TestAdapter;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class IndoorMap implements BuildingDownloaderListener
{
    private static int PPM = 300; // Pixel Per Meter
    private Context context;
    private Building building;
    private BuildingSqliteHelper buildingSqliteHelper = null;
    //private LocalizationSpotManager _localizationSpot;




    HashMap<Integer, Spot> spot_beacon_map = new HashMap<>();
    HashMap<Integer, Spot> spot_QR_map = new HashMap<>();


    BuildingDownloader downloader;

    public IndoorMap( Context context, String museumUrl ) {
        this.context = context;
        downloader = new BuildingDownloader(context, museumUrl, this);
        //this.building = building;
        downloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    @Override
    public void onDownloadFinished(String downloadedFilePath)
    {
        try {
            downloader.importDatabase(downloadedFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        TestAdapter adapter = new TestAdapter(context);
        adapter.open(downloadedFilePath);
        Cursor testdata = adapter.getTestData();

        String data;
        if(testdata.moveToFirst())
        {
            do
            {
                data = testdata.getString(0);
            }while (testdata.moveToNext());
        }

            adapter.close();
    }
}
