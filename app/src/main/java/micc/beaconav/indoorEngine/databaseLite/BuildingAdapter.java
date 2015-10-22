package micc.beaconav.indoorEngine.databaseLite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import micc.beaconav.indoorEngine.ArtworkPosition;
import micc.beaconav.indoorEngine.ArtworkRow;
import micc.beaconav.indoorEngine.beaconHelper.ABeaconProximityManager;
import micc.beaconav.indoorEngine.building.Building;
import micc.beaconav.indoorEngine.building.ConvexArea;
import micc.beaconav.indoorEngine.building.Floor;
import micc.beaconav.indoorEngine.building.Position;
import micc.beaconav.indoorEngine.building.Room;
import micc.beaconav.indoorEngine.building.Vertex;

/**
 * Created by nagash on 24/09/15.
 */
public class BuildingAdapter
{
    protected static final String TAG = "BuildingAdapter";


    private final Context mContext;
    private SQLiteDatabase mDb;
    private BuildingSqliteHelper mDbHelper;

    private String dbPath = null;


    public class DBLiteQueryException extends SQLException {
        private String sql_query;
        SQLException sqlException;
        DBLiteQueryException(SQLException mSQLException, String sql_query) {
            this.sqlException = mSQLException;
            this.sql_query = sql_query;
        }
        public String getSqlQuery(){ return sql_query; }
        public SQLException getSqlException() { return sqlException; }
    }

    public BuildingAdapter(Context context)
    {
        this.mContext = context;
        mDbHelper = new BuildingSqliteHelper( mContext);
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


    public Cursor getRooms()
    {
        String sql ="SELECT * FROM Room";
        try
        {
            Cursor mCur = mDb.rawQuery(sql, null);
            if(mCur != null)
                mCur.moveToFirst();
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getRooms() >>"+ mSQLException.toString());
            Log.e(TAG, "getRooms() >> query: "+sql);
            throw new DBLiteQueryException(mSQLException, sql);
        }
    }


    public Cursor getConvexAreas()
    {
        String sql ="SELECT ConvexArea.* "+
                "  FROM ConvexArea" +
                "  JOIN Room ON Room.ID = ConvexArea.ID_room";
        try
        {
            Cursor mCur = mDb.rawQuery(sql, null);
            if(mCur != null)
                mCur.moveToFirst();
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getConvexAreas() >>"+ mSQLException.toString());
            Log.e(TAG, "getConvexAreas() >> query: "+sql);
            throw new DBLiteQueryException(mSQLException, sql);
        }
    }


    public Cursor getVertexInAllConvexAreas()
    {
        String sql ="SELECT ConvexAreaVertices.*, Vertex.x as x, Vertex.y as y, Vertex.type as ID_type " +
                "FROM ConvexAreaVertices JOIN Vertex ON ConvexAreaVertices.ID_vertex = Vertex.ID " +
                "JOIN ConvexArea ON ConvexAreaVertices.ID_convexArea = ConvexArea.ID " +
                "ORDER BY ID_room ASC\t";
        try
        {

            Cursor mCur = mDb.rawQuery(sql, null);
            if(mCur != null)
                mCur.moveToFirst();
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getVertexInAllConvexAreas() >>"+ mSQLException.toString());
            Log.e(TAG, "getVertexInAllConvexAreas() >> query: "+sql);
            throw new DBLiteQueryException(mSQLException, sql);
        }
    }

    public Cursor getPoiInAllRooms()
    {
        String sql ="SELECT Position.ID_room as ID_room Position.*, Artwork.ID as idArtwork, Artwork.*, QRCode.code as QRCode, Beacon.minor, Beacon.major" +
                "FROM Position " +
                " LEFT JOIN Artwork ON Artwork.ID_position = Position.id " +
                " LEFT JOIN QRCode ON QRCode.ID_position = Position.id " +
                " LEFT JOIN Beacon  ON Beacon.ID_position = Position.id ";
        try
        {
            Cursor mCur = mDb.rawQuery(sql, null);
            if(mCur != null)
                mCur.moveToFirst();
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getPoiInAllRooms() >>"+ mSQLException.toString());
            Log.e(TAG, "getPoiInAllRooms() >> query: "+sql);
            throw new DBLiteQueryException(mSQLException, sql);
        }
    }


    public Cursor getVertexInAllRooms()
    {
        String sql ="SELECT Room.ID as ID_room, Vertex.*, RoomVertices.indexInRoom, VertexType.ID as vertexType, VertexType.descr as VertexTypeDescr " +
                "FROM RoomVertices " +
                "JOIN Vertex ON RoomVertices.ID_vertex = Vertex.ID " +
                "JOIN Room ON RoomVertices.ID_room = Room.ID " +
                "JOIN VertexType ON Vertex.type = VertexType.ID " +
                " ORDER BY ID_room ASC, indexInRoom ASC";
        try
        {
            Cursor mCur = mDb.rawQuery(sql, null);
            if(mCur != null)
                mCur.moveToFirst();
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getVertexInAllRooms() >>"+ mSQLException.toString());
            Log.e(TAG, "getVertexInAllRooms() >> query: "+sql);
            throw new DBLiteQueryException(mSQLException, sql);
        }
    }


    public Cursor getPositionInAllRooms()
    {
        String sql ="SELECT Position.*, " +
                "Artwork.ID as artworkID, Artwork.name as artworkName, Artwork.descr as artworkDescr, " +
                " QRCode.code as qrCode, Beacon.minor as beaconMinor, Beacon.major as beaconMajor," +
                " Room.ID as roomID, ConvexArea.ID as convexAreaID, Image.link_xhdpi as imageLink " +
                "FROM Position " +
                "LEFT JOIN Artwork ON Position.ID = Artwork.ID_position " +
                "LEFT JOIN QRCode ON Position.ID = QRCode.ID_position " +
                "LEFT JOIN Beacon ON Position.ID = Beacon.ID_position " +
                "LEFT JOIN Image ON Artwork.ID_image = Image.ID " +
                "JOIN Room ON Position.ID_room = Room.ID "+
                "JOIN ConvexArea ON Position.ID_convexArea = ConvexArea.ID ";
        try
        {
            Cursor mCur = mDb.rawQuery(sql, null);
            if(mCur != null)
                mCur.moveToFirst();
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getPositionInAllRooms() >>"+ mSQLException.toString());
            Log.e(TAG, "getPositionInAllRooms() >> query: "+sql);
            throw new DBLiteQueryException(mSQLException, sql);
        }
    }


}
