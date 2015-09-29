package micc.beaconav.indoorEngine.databaseLite;

import java.io.IOException;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TestAdapter
{
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private BuildingSqliteHelper mDbHelper;

    private String dbPath = null;


    public TestAdapter(Context context)
    {
        this.mContext = context;
        mDbHelper = new BuildingSqliteHelper( mContext);
    }


    public TestAdapter open(String dbPath) throws SQLException
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
            Log.e(TAG, "open >>"+ mSQLException.toString());
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
            //sql = "SELECT * FROM android_metadata";
            //sql = "SELECT name FROM sqlite_master WHERE type='table'";
            Cursor mCur = mDb.rawQuery(sql, null);
//
//            if(mCur != null)
//                mCur.moveToNext();
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
//        Cursor cursor = null;
//        try {
//            cursor = mDb.query("Vertex", new String[] { "ID", "x", "y", "type"}, null, null, null, null, null);
//
//        }
//        catch (SQLException mSQLException)
//        {
//            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
//            throw mSQLException;
//        }
//        return cursor;
    }

}

