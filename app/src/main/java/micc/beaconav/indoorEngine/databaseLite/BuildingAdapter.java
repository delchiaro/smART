package micc.beaconav.indoorEngine.databaseLite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;

/**
 * Created by nagash on 24/09/15.
 */
public class BuildingAdapter{

    final String TAG = "DataAdapter";

    private final Context mContext;

    private String dbPath = null;

    private SQLiteDatabase mDb;
    private BuildingSqliteHelper mDbHelper;



    public BuildingAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new BuildingSqliteHelper( context );
    }


    public BuildingAdapter open(String dbPath) throws SQLException
    {
        close();
        try
        {
            mDbHelper.importDatabase(dbPath);
            mDb = mDbHelper.getReadableDatabase();
            this.dbPath = dbPath;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void close()
    {
        mDbHelper.close();
        dbPath = null;
    }



    public Cursor getTestData()
    {
        try
        {
            String sql ="SELECT * FROM Vertex";
            Cursor mCur = mDb.rawQuery(sql, null);
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }

    }


}
