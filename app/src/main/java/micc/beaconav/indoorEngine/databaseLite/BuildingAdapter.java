package micc.beaconav.indoorEngine.databaseLite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import micc.beaconav.indoorEngine.ArtMarker;
import micc.beaconav.indoorEngine.ArtworkRow;
import micc.beaconav.indoorEngine.building.Building;
import micc.beaconav.indoorEngine.building.Floor;
import micc.beaconav.indoorEngine.building.Room;
import micc.beaconav.indoorEngine.building.Vertex;

/**
 * Created by nagash on 24/09/15.
 */
public class BuildingAdapter
{
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


            HashMap<Vertex, Integer> vertex_ID_map = new HashMap<>(vertexData.getCount()); // lega ogni vertex (versione RAM) con il suo ID del DB
            HashMap<Integer, Vertex> ID_vertex_map = new HashMap<>(vertexData.getCount()); // lega ogni vertex (versione RAM) con il suo ID del DB

            Vertex.Type[] vertexTypeMap = new Vertex.Type[3];
            vertexTypeMap[0] = Vertex.Type.WALL;
            vertexTypeMap[1] = Vertex.Type.DOOR;
            vertexTypeMap[2] = Vertex.Type.APERTURE;


            int roomID;
            Integer oldRoomID = null;

            Vertex vertex;
            Room room = null;

            do {
                roomID = vertexData.getInt(ID_ci);
                vertex_x = vertexData.getFloat(x_ci);
                vertex_y = vertexData.getFloat(y_ci);
                vertex_type = vertexTypeMap[vertexData.getInt(type_ci)];
                vertexID = vertexData.getInt(vertex_ID_ci);

                vertex = ID_vertex_map.get(vertexID);
                if(vertex == null)
                {
                    // se non era giá nella mappa lo genero e lo aggiungo alla mappa
                    vertex = new Vertex(vertex_x, vertex_y, vertex_type);
                    ID_vertex_map.put(vertexID, vertex);
                }

                room = roomMap.get(roomID);
                room.pushVertex(vertex);

            } while (vertexData.moveToNext());




            // I N I Z I O COSTRUZIONI DELLE PORTE * * * * * * * * * * * * * * * * * *

            HashMap<Vertex, Vertex> doorMap1 = new HashMap<>();// lega un ID di un vertice porta con un ID del vertice porta accoppiato
            HashMap<Vertex, Room> doorMap2 = new HashMap<>();// lega un ID di un vertice porta accoppiato, con una room.


            Iterator<Room> rumIter = floor.getIterator();
            while(rumIter.hasNext())
            {
                room = rumIter.next();

                // FINALIZZO LA VECCHIA STANZA ROOM CERCANDO LE PORTE
                int NV = room.nVertices();
                Vertex v = null;
                Vertex old_v = null;

                if (NV > 2)
                {
                    v = room.getVertex(0);
                    boolean doorJustClosed = false;

                    for (int i = 1; i < NV + 1; i++) {
                        old_v = v;
                        v = room.getVertex(i % NV);

                        if (doorJustClosed)
                        {
                            // se al ciclo precedente si è chiusa una porta allora saltiamo
                            // il vertice successivo.
                            doorJustClosed = false;
                        }
                        else if (v.getType() != Vertex.Type.WALL)
                        {
                            if (v.getType() == old_v.getType())
                            {
                                Integer old_v_ID = vertex_ID_map.get(old_v);
                                Integer vID = vertex_ID_map.get(v);

                                Vertex v1 = old_v;
                                Vertex v2 = doorMap1.get(v1);

                                // cerco nella mappa se è giá stata rilevata la porta con estremitá oldVertexId

                                if (v2 == null)
                                {
                                    // se non è stata ancora rilevata controllo che non sia stata memorizzata
                                    // con l'altra estremitá (vertexID)
                                    v1 = v;
                                    v2 = doorMap1.get(v);
                                }

                                if (v2 == null)
                                {
                                    // La porta non era ancora stata inserita nella mappa delle porte! La inseriamo..
                                    doorMap1.put(v, old_v);
                                    doorMap2.put(old_v, room);
                                }
                                else
                                {
                                    // In questo caso la porta era giá stata inserita, possiamo ritrovare la stanza alla quale era collegata
                                    // la porta, e quindi generare l'entitá porta su RAM (nel modello del software android):
                                    Room linkedRoom = doorMap2.get(v2);
                                    Room.addDoor(room, linkedRoom, v1, v2);


                                    // Sará possibile volendo anche rimuovere dalla hashmap 1 e 2 le corrispondenti chiavi ma attenti!!
                                    doorMap1.remove(v1);
                                    doorMap2.remove(v2);
                                    //TODO: controlla se non da errori.
                                }
                                doorJustClosed = true;

                            }
                        }

                    }

                }


            }

            // F I N E COSTRUZIONI DELLE PORTE * * * * *  * * * * * * * * * * * * *


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
            Integer artworkID;
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

                ArtworkRow artworkRow = null;
                if( artworkID != null )
                {
                    artworkRow = new ArtworkRow(artworkID, artworkName, artworkDescr, null, imageID, null, null, null, null);
                    roomMap.get(roomID).addMarker(new ArtMarker(x, y, artworkRow));
                }
                else
                    roomMap.get(roomID).addMarker(new Indoo);

            } while (positionData.moveToNext());
        }

        // F I N E        CARICAMENTO POSITIONs




        adapter.close();
        return building;
    }

}
