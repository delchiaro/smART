package micc.beaconav.indoorEngine.databaseLite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

import micc.beaconav.indoorEngine.building.Building;
import micc.beaconav.indoorEngine.building.Floor;
import micc.beaconav.indoorEngine.building.Room;
import micc.beaconav.indoorEngine.building.Vertex;

/**
 * Created by nagash on 24/09/15.
 */
public class BuildingAdapter{
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private BuildingSqliteHelper mDbHelper;

    private String dbPath = null;


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
        try
        {
            String sql ="SELECT * FROM Room";
            Cursor mCur = mDb.rawQuery(sql, null);
            if(mCur != null)
                mCur.moveToFirst();
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getPoiInAllRooms()
    {
        try
        {
            String sql ="SELECT Position.ID_room as ID_room Position.*, Artwork.ID as idArtwork, Artwork.*, QRCode.code as QRCode, Beacon.minor, Beacon.major" +
                    "FROM Position " +
                    " JOIN Artwork ON Artwork.ID_position = Position.id " +
                    " JOIN QRCode ON QRCode.ID_position = Position.id " +
                    " JOIN Beacon  ON Beacon.ID_position = Position.id ";
            Cursor mCur = mDb.rawQuery(sql, null);
            if(mCur != null)
                mCur.moveToFirst();
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }


    public Cursor getVertexInAllRooms()
    {
        try
        {
            String sql ="SELECT Room.ID as ID_room, Vertex.*, RoomVertices.indexInRoom, VertexType.ID as vertexType, VertexType.descr as VertexTypeDescr " +
                    "FROM RoomVertices " +
                    "JOIN Vertex ON RoomVertices.ID_vertex = Vertex.ID " +
                    "JOIN Room ON RoomVertices.ID_room = Room.ID " +
                    "JOIN VertexType ON Vertex.type = VertexType.ID " +
                    " ORDER BY ID_room ASC, indexInRoom ASC";
            Cursor mCur = mDb.rawQuery(sql, null);
            if(mCur != null)
                mCur.moveToFirst();
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
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



    public static Building generateBuilding(String downloadedFilePath, Context context) {

        Building building;
        BuildingAdapter adapter = new BuildingAdapter(context);
        adapter.open(downloadedFilePath);
        Cursor roomsData = adapter.getRooms();

        // inizio a generare l'edificio

        building = new Building(900, 900);
        building.add(new Floor());
        Floor floor = building.getActiveFloor();

        HashMap<Integer, Room> roomMap = new HashMap<>(roomsData.getCount());

        if (roomsData != null && roomsData.moveToFirst()) {
            int roomID;
            String roomName;
            String roomDescr;

            int ID_ci = roomsData.getColumnIndex("ID");
            int name_ci = roomsData.getColumnIndex("name");
            int descr_ci = roomsData.getColumnIndex("name");

            do {
                roomID = roomsData.getInt(ID_ci);
                roomName = roomsData.getString(name_ci);
                roomDescr = roomsData.getString(descr_ci);
                Room r = new Room();
                roomMap.put(roomID, r);
                floor.add(r);

            } while (roomsData.moveToNext());


            Cursor vertexData = adapter.getVertexInAllRooms();
            ID_ci = vertexData.getColumnIndex("ID_room");
            int x_ci = vertexData.getColumnIndex("x");
            int y_ci = vertexData.getColumnIndex("y");
            int type_ci = vertexData.getColumnIndex("vertexType");
            int vertex_ID_ci = vertexData.getColumnIndex("Vertex.ID");

            float vertex_x;
            float vertex_y;
            Vertex.Type vertex_type;
            int vertexID;


            HashMap<Integer, Integer> doorMap1 = new HashMap<>();
            HashMap<Integer, Room> doorMap2 = new HashMap<>();

            Vertex.Type[] vertexTypeMap = new Vertex.Type[3];
            vertexTypeMap[0] = Vertex.Type.WALL;
            vertexTypeMap[1] = Vertex.Type.DOOR;
            vertexTypeMap[2] = Vertex.Type.APERTURE;

            Integer oldVertexId = null;
            Vertex.Type oldVertexType = null;
            boolean doorOpened = false;

            if (vertexData != null && vertexData.moveToFirst()) {
                do {
                    roomID = vertexData.getInt(ID_ci);
                    vertex_x = vertexData.getFloat(x_ci);
                    vertex_y = vertexData.getFloat(y_ci);
                    vertex_type = vertexTypeMap[vertexData.getInt(type_ci)];
                    vertexID = vertexData.getInt(vertex_ID_ci);

                    Room room =  roomMap.get(roomID);
                    room.pushVertex(new Vertex(vertex_x, vertex_y, vertex_type));




                    // COSTRUZIONIE DELLE PORTE TRAMITE I VERTEX * * * * * * * * * * * * *
                    // se il vertex corrente è una door o una aperture
                    if ( vertex_type == Vertex.Type.DOOR || vertex_type == Vertex.Type.APERTURE )
                    {
                        // se non si era giá "aperta" la porta
                        if(doorOpened == false)
                        {
                            // apriamo la porta e salviamo i dati di questo vertice
                            doorOpened = true;
                            oldVertexId = vertexID;
                            oldVertexType = vertex_type;
                        }

                        // se invece la porta era giá stata aperta, vediamo se possiamo chiuderla senza errori
                        else
                        {
                            if( oldVertexType != vertex_type)
                            {
                                // TODO: lanciare eccezione!!! La porta inizia con un tipo e finisce con un altro, inconsistenza sul DB lite !!
                            }
                            else
                            {
                                // in questo caso la porta è consistente.. cerco se era giá stata inserita nella mappa delle porte:

                                Integer id2 = doorMap1.get(oldVertexId);
                                if(id2 == null)
                                    id2 = doorMap1.get(vertexID);

                                if(id2 == null)
                                {
                                    // La porta non era ancora stata inserita nella mappa delle porte! La inseriamo..
                                    doorMap1.put(oldVertexId, vertexID);
                                    doorMap2.put(vertexID, room);
                                }
                                else
                                {
                                    // In questo caso la porta era giá stata inserita, possiamo ritrovare la stanza alla quale era collegata
                                    // la porta, e quindi generare l'entitá porta su RAM (nel modello del software android):
                                    Room linkedRoom = doorMap2.get(id2);

                                    // TODO: new Door(room, linkedRoom);
                                    // Sará possibile volendo anche rimuovere dalla hashmap 1 e 2 le corrispondenti chiavi ma attenti!!

                                }

                            }
                        }
                        doorOpened = false;
                        oldVertexId = null;
                        oldVertexType = null;
                    }
                    // F I N E COSTRUZIONI DELLE PORTE * * * * *  * * * * * * * * * * * * *




                } while (vertexData.moveToNext());


            }
        }

        adapter.close();
        return building;
    }

}
