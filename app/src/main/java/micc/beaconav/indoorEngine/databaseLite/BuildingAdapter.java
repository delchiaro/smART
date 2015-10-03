package micc.beaconav.indoorEngine.databaseLite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

import micc.beaconav.indoorEngine.ArtMarker;
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
                    " LEFT JOIN Artwork ON Artwork.ID_position = Position.id " +
                    " LEFT JOIN QRCode ON QRCode.ID_position = Position.id " +
                    " LEFT JOIN Beacon  ON Beacon.ID_position = Position.id ";
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


    public Cursor getPositionInAllRooms()
    {
        try
        {
            String sql ="SELECT Position.*, Artwork.*, QRCode.*, Beacon.*, Room.ID as ID_room " +
                    "FROM Position " +
                    "LEFT JOIN Artwork ON Position.ID = Artwork.ID_position " +
                    "LEFT JOIN QRCode ON Position.ID = QRCode.ID_position " +
                    "LEFT JOIN Beacon ON Position.ID = Beacon.ID_position " +
                    "JOIN Room ON Position.ID_room = Room.ID ";
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




    public static Building generateBuilding(String downloadedFilePath, Context context) {

        Building building;
        BuildingAdapter adapter = new BuildingAdapter(context);
        adapter.open(downloadedFilePath);
        Cursor roomsData = adapter.getRooms();

        // inizio a generare l'edificio


        // CARICO ROOMs
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
        }
        else
        {
            // TODO: gestire eccezione.. NESSUNA STANZA
            return null;
        }





        // CARICO VERTEXs

        Cursor vertexData = adapter.getVertexInAllRooms();
        if (vertexData != null && vertexData.moveToFirst()) {

            int ID_ci;


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
            Vertex oldVertex = null;
            boolean doorOpened = false;

            int roomID;
            do {
                roomID = vertexData.getInt(ID_ci);
                vertex_x = vertexData.getFloat(x_ci);
                vertex_y = vertexData.getFloat(y_ci);
                vertex_type = vertexTypeMap[vertexData.getInt(type_ci)];
                vertexID = vertexData.getInt(vertex_ID_ci);

                Room room = roomMap.get(roomID);
                Vertex vertex = new Vertex(vertex_x, vertex_y, vertex_type);
                room.pushVertex(vertex);


                // COSTRUZIONIE DELLE PORTE TRAMITE I VERTEX * * * * * * * * * * * * *
                // se il vertex corrente è una door o una aperture
                if (vertex_type == Vertex.Type.DOOR || vertex_type == Vertex.Type.APERTURE) {
                    // se non si era giá "aperta" la porta
                    if (doorOpened == false) {
                        // apriamo la porta e salviamo i dati di questo vertice
                        doorOpened = true;
                        oldVertex = vertex;
                        oldVertexId = vertexID;
                    }

                    // se invece la porta era giá stata aperta, vediamo se possiamo chiuderla senza errori
                    else {
                        if (oldVertex.getType() != vertex_type) {
                            // TODO: lanciare eccezione!!! La porta inizia con un tipo e finisce con un altro, inconsistenza sul DB lite !!
                        } else {
                            // in questo caso la porta è consistente.. cerco se era giá stata inserita nella mappa delle porte:

                            Integer id1 = oldVertexId;
                            Integer id2 = doorMap1.get(id1);
                            if (id2 == null) {
                                id1 = vertexID;
                                id2 = doorMap1.get(vertexID);

                            }
                            if (id2 == null) {
                                // La porta non era ancora stata inserita nella mappa delle porte! La inseriamo..
                                doorMap1.put(oldVertexId, vertexID);
                                doorMap2.put(vertexID, room);
                            } else {
                                // In questo caso la porta era giá stata inserita, possiamo ritrovare la stanza alla quale era collegata
                                // la porta, e quindi generare l'entitá porta su RAM (nel modello del software android):
                                Room linkedRoom = doorMap2.get(id2);
                                Room.addDoor(room, linkedRoom, vertex, oldVertex);


                                // Sará possibile volendo anche rimuovere dalla hashmap 1 e 2 le corrispondenti chiavi ma attenti!!
                                doorMap1.remove(id1);
                                doorMap2.remove(id2);
                                //TODO: controlla se non da errori.

                            }

                        }
                    }
                    doorOpened = false;
                    oldVertexId = null;
                    oldVertex = null;
                }
                // F I N E COSTRUZIONI DELLE PORTE * * * * *  * * * * * * * * * * * * *

            } while (vertexData.moveToNext());
        }
        else
        {
            // TODO: gestire eccezione.. NESSUN VERTICE
            return null;
        }
        // F I N E  CARICAMENTO VERTEX






        // C A R I C O    POSITIONs
        Cursor positionData = adapter.getPositionInAllRooms();
        if (positionData != null && positionData.moveToFirst())
        {
            int ID_ci = positionData.getColumnIndex("ID");
            int x_ci = positionData.getColumnIndex("x");
            int y_ci = positionData.getColumnIndex("y");
            int roomID_ci = positionData.getColumnIndex("ID_room");
            int imageID_ci = positionData.getColumnIndex("ID_image");
            int artworkID_ci = positionData.getColumnIndex("Artwork.ID");
            int artworkName_ci = positionData.getColumnIndex("Artwork.name");
            int artworkDescr_ci = positionData.getColumnIndex("Artwork.descr");
            int qrCode_ci = positionData.getColumnIndex("QRCode.code");
            int minor_ci = positionData.getColumnIndex("Beacon.minor");
            int major_ci = positionData.getColumnIndex("Beacon.major");

            int positionID;
            float x;
            float y;
            int roomID;
            int artworkID;
            int imageID;
            String artworkName;
            String artworkDescr;
            String QRCode;
            int minor;
            int major;


            do {

                positionID = positionData.getInt(ID_ci);
                x = positionData.getInt(x_ci);
                y = positionData.getInt(y_ci);
                roomID = positionData.getInt(roomID_ci);
                imageID = positionData.getInt(imageID_ci);
                artworkID = positionData.getInt(artworkID_ci);
                artworkName = positionData.getString(artworkName_ci);
                artworkDescr = positionData.getString(artworkDescr_ci);
                QRCode = positionData.getString(qrCode_ci);
                minor = positionData.getInt(minor_ci);
                major = positionData.getInt(major_ci);

                roomMap.get(roomID).addMarker(new ArtMarker(x, y, artworkName, artworkDescr, imageID , artworkID));


            } while (positionData.moveToNext());
        }

        // F I N E        CARICAMENTO POSITIONs




        adapter.close();
        return building;
    }

}
