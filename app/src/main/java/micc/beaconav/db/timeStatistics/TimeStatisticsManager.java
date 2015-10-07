package micc.beaconav.db.timeStatistics;

import android.content.Context;
import android.content.SharedPreferences;

import micc.beaconav.FragmentHelper;
import micc.beaconav.MainActivity;
import micc.beaconav.R;
import micc.beaconav.db.dbHelper.IArtRow;
import micc.beaconav.db.dbHelper.museum.MuseumRow;
import micc.beaconav.indoorEngine.ArtworkRow;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class TimeStatisticsManager {


    private static MainActivity activity;

    public static void init(MainActivity act) {
        activity= act;
    }


    public static void writeInAppTime(IArtRow row, long timeInSeconds)
    {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if(row instanceof ArtworkRow)
           editor.putLong(getInAppKey( (ArtworkRow)row ), timeInSeconds);

        else if(row instanceof MuseumRow)
            editor.putLong(getInAppKey( (MuseumRow)row ), timeInSeconds);

        editor.commit();
    }

    public static void writeInProximityTime(IArtRow row, long timeInSeconds)
    {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if(row instanceof ArtworkRow )
            editor.putLong(getInProximityKey( (ArtworkRow)row ), timeInSeconds);

        else if(row instanceof MuseumRow)
            editor.putLong(getInProximityKey( (MuseumRow)row ), timeInSeconds);

        editor.commit();
    }

    public static long readInAppTime(IArtRow row) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_WORLD_WRITEABLE);
        long defaultValue = 0;
        if(row instanceof ArtworkRow )
            return sharedPref.getLong(getInAppKey( (ArtworkRow)row ), defaultValue);

        else if(row instanceof MuseumRow)
            return sharedPref.getLong(getInAppKey( (MuseumRow)row ), defaultValue);

        else return -1;//error

    }


    public static long readInProximityTime(IArtRow row) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_WORLD_WRITEABLE);
        long defaultValue = 0;
        if(row instanceof ArtworkRow )
            return sharedPref.getLong(getInProximityKey( (ArtworkRow)row ), defaultValue);

        else if(row instanceof MuseumRow)
            return sharedPref.getLong(getInProximityKey( (MuseumRow)row ), defaultValue);

        else return -1;//error

    }


    private static String getInProximityKey(ArtworkRow row) {
        return "artwork_inprox_time_" + row.getID();
    }

    private static String getInProximityKey(MuseumRow row) {
        return "museum_inprox_time_" + row.getID();
    }

    private static String getInAppKey(ArtworkRow row) {
        return "artwork_inapp_time_" + row.getID();
    }

    private static String getInAppKey(MuseumRow row) {
        return "museum_inapp_time_" + row.getID();
    }
}
