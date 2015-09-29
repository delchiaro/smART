package micc.beaconav.indoorEngine.databaseLite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by nagash on 15/03/15.
 */
public class BuildingSqliteHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    //private static String DB_NAME = "building";
    private String DIR_PATH = ""; // = "/data/data/{package_name}/databases/";
    private static String FILE_NAME = "dblite.db";

    // private static String TEMP_DIR_PATH = Environment.getExternalStorageDirectory().getPath() + "/beaconav/";
    private SQLiteDatabase mDataBase;
    private final Context mContext;





    public BuildingSqliteHelper(Context context)
    {
        super(context, FILE_NAME, null, DATABASE_VERSION);
        this.mContext = context;

        if(android.os.Build.VERSION.SDK_INT >= 17)
            DIR_PATH = mContext.getApplicationInfo().dataDir + "/databases/";

        else
            DIR_PATH = "/data/data/" + mContext.getPackageName() + "/databases";
    }






    /**
     * Copies the database file at the specified location over the current
     * internal application database.
     * */
    public boolean importDatabase(String dbPath) throws IOException {
        // Close the SQLiteOpenHelper so it will commit the created empty
        close();
        File directory = new File(DIR_PATH);
        directory.mkdirs();
        File newDb = new File(DIR_PATH + FILE_NAME);
        File importDb = new File(dbPath);
        if (importDb.exists()) {
            FileUtils.copyFile(new FileInputStream(importDb), new FileOutputStream(newDb));
            // Access the copied database so SQLiteHelper will cache it and mark
            // it as created.
            getWritableDatabase().close();
            return true;
        }
        return false;
    }

    //Check that the database exists here: /data/data/your package/databases/Da Name
    private boolean checkDataBase()
    {
        File dbFile = new File(DIR_PATH + FILE_NAME);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        return; // questo software fa da client non scrive niente sul db dell'edificio
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        return; // questo software fa da client non scrive niente sul db dell'edificio
    }
}
