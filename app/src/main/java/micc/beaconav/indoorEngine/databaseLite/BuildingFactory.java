package micc.beaconav.indoorEngine.databaseLite;

import android.content.Context;
import android.database.Cursor;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import micc.beaconav.indoorEngine.ArtworkPosition;
import micc.beaconav.indoorEngine.ArtworkRow;
import micc.beaconav.indoorEngine.ProportionsHelper;
import micc.beaconav.indoorEngine.beaconHelper.ABeaconProximityManager;
import micc.beaconav.indoorEngine.building.Building;
import micc.beaconav.indoorEngine.building.ConvexArea;
import micc.beaconav.indoorEngine.building.ConvexCut;
import micc.beaconav.indoorEngine.building.Floor;
import micc.beaconav.indoorEngine.building.Position;
import micc.beaconav.indoorEngine.building.Room;
import micc.beaconav.indoorEngine.building.Segment;
import micc.beaconav.indoorEngine.building.Vertex;

/**
 * Created by Nagash on 17/10/2015.
 */
public class BuildingFactory
{

    private final Context context;
    private final String downloadedFilePath;
    private final BuildingAdapter adapter;
    private Building building = null;

    public BuildingFactory(String downloadedFilePath, Context context) {
        this.context = context;
        this.downloadedFilePath = downloadedFilePath;
        adapter = new BuildingAdapter(context);
    }




    HashMap<Integer, Room> roomMap;
    HashMap<Integer, ConvexArea> convexAreaMap;
    BiMap<Integer, Vertex> vertexMap;

    Vertex.Type[] vertexTypeMap = {Vertex.Type.WALL, Vertex.Type.DOOR, Vertex.Type.APERTURE};
                            //    vertexTypeMap[0] = Vertex.Type.WALL;
                            //    vertexTypeMap[1] = Vertex.Type.DOOR;
                            //    vertexTypeMap[2] = Vertex.Type.APERTURE;







    HashMap<String, Position> QRCodePositionMap = null;
    HashMap<Integer, Position> BeaconPositionMap = null;


    private final boolean loadConvexAreasAndRooms()
    {

        // * * * * * * * * * * * * LETTURA CONVEX AREAs* * * * * * * * * * * * *
        Cursor convexAreaData = adapter.getConvexAreas();

        Floor floor = building.getActiveFloor(); //TODO: in futuro con navigazione su più piani, da ciclare ogni floor.


        if (convexAreaData != null && convexAreaData.moveToFirst()) {


            roomMap = new HashMap<>(convexAreaData.getCount());
            convexAreaMap = new HashMap<>(convexAreaData.getCount());

            int convexAreaID;
            int roomID;
            String roomName;
            String roomDescr;

            int ID_convexArea_ci = convexAreaData.getColumnIndex("ID");

            int ID_room_ci = convexAreaData.getColumnIndex("ID_room");
            int roomName_ci = convexAreaData.getColumnIndex("name");
            int roomDescr_ci = convexAreaData.getColumnIndex("descr");

            do {
                convexAreaID = convexAreaData.getInt(ID_convexArea_ci);
                roomID = convexAreaData.getInt(ID_room_ci);
                Room r = roomMap.get(roomID);
                if(r == null)
                {
                    r = new Room();
                    roomMap.put(roomID, r);
                    floor.add(r);
                }

                ConvexArea ca = new ConvexArea();
                r.add(ca);
                convexAreaMap.put(convexAreaID, ca);

            } while (convexAreaData.moveToNext());

            return true;
        }
        else
        {
            // TODO: gestire eccezione.. NESSUNA STANZA
            return false;
        }




    }


    private final boolean loadVerticesInRooms()
    {

        // CARICO VERTEXs in rooms

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


            this.vertexMap = HashBiMap.create(vertexData.getCount());
            // lega ogni vertex (versione RAM) con il suo ID del DB

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

                vertex = vertexMap.get(vertexID);
                if(vertex == null)
                {
                    // se non era giá nella mappa lo genero e lo aggiungo alla mappa
                    vertex = new Vertex(vertex_x, vertex_y, vertex_type);
                    vertexMap.put(vertexID, vertex);
                }

                room = roomMap.get(roomID);
                room.pushVertex(vertex);

            } while (vertexData.moveToNext());

        }
        else
        {
            // TODO: gestire eccezione.. NESSUN VERTICE
            return false;
        }

        return true;

    }


    private final boolean loadVerticesInConvexAreas()
    {

        // CARICO VERTEXs in ConvexAreas
        Cursor caVertexData = adapter.getVertexInAllConvexAreas();
        if (caVertexData != null && caVertexData.moveToFirst()) {
            int convexAreaID;
            int vertexID;
            float vertex_x;
            float vertex_y;
            Vertex.Type vertex_type;

            int ID_convexArea_ci = caVertexData.getColumnIndex("ID_convexArea");
            int ID_vertex_ci = caVertexData.getColumnIndex("ID_vertex");
            int vertex_x_ci = caVertexData.getColumnIndex("x");
            int vertex_y_ci = caVertexData.getColumnIndex("y");
            int vertex_type_ci = caVertexData.getColumnIndex("ID_type");

            do {
                convexAreaID = caVertexData.getInt(ID_convexArea_ci);
                vertexID = caVertexData.getInt(ID_vertex_ci);
                vertex_x = caVertexData.getFloat(vertex_x_ci);
                vertex_y = caVertexData.getFloat(vertex_y_ci);
                vertex_type = vertexTypeMap[ caVertexData.getInt(vertex_type_ci) ];
                ConvexArea ca = convexAreaMap.get(convexAreaID);

                if(ca != null)
                {
                    Vertex v = vertexMap.get(vertexID);
                    if(v==null)
                    {
                        // NON DOVREBBE MAI VERIFICARSI!!! TUTTI I VERTICI DELLA CONVEX AREA
                        // DOVREBBERO ESSERE VERTICI DELLA ROOM E QUINDI DOVREBBERO ESSERE GIÁ STATI INSERITI
                        v = new Vertex(vertex_x, vertex_y, vertex_type);
                        vertexMap.put(vertexID, v);
                    }
                    ca.addVertex(v);
                }
                else
                {
                    // NON DOVREBBE MAI VERIFICARSI!! LA QUERY SELEZIONA VERTEX ASSOCIATI AD UNA CONVEX AREA..
                }

            } while (caVertexData.moveToNext());

        }
        else
        {
            // TODO: gestire eccezione.. NESSUNA STANZA
            return false;
        }
        return true;
    }



    HashMap<Vertex, Vertex> doorMap1;// lega un ID di un vertice porta con un ID del vertice porta accoppiato
    HashMap<Vertex, Room> doorMap2;// lega un ID di un vertice porta accoppiato, con una room.

    private final boolean buildDoorsAndConvexCut()
    {

        Floor floor = building.getActiveFloor();
        // TODO: iterare su tutti i floor quando si implementa navigazione su più piani.

        doorMap1 = new HashMap<>();// lega un ID di un vertice porta con un ID del vertice porta accoppiato
        doorMap2 = new HashMap<>();// lega un ID di un vertice porta accoppiato, con una room.

        for( Room room : floor)
        {
            buildDoors(room);
            buildConvexCut(room);
        }

        return true;

    }


    private final boolean buildDoors(Room room)
    {


        int NV = room.nVertices();

        if (NV <= 2) {
            return false;
        }

        Vertex v = room.getVertex(0);
        Vertex old_v = null;

        boolean doorJustClosed = false;

        for (int i = 1; i < NV + 1; i++)
        {
            old_v = v;
            v = room.getVertex(i % NV);

            if (doorJustClosed)
            {
                // se al ciclo precedente si è chiusa una porta allora saltiamo
                // il vertice successivo.
                doorJustClosed = false;
            }
            else if (v.getType() != Vertex.Type.WALL && v.getType() == old_v.getType())
            {

//                Integer old_v_ID = vertexMap.inverse().get(old_v);
//                Integer vID = vertexMap.inverse().get(v);



                // cerco nella mappa se è giá stata rilevata la porta con estremitá old_v
                Vertex v1 = old_v;
                Vertex v2 = doorMap1.get(v1);

                if (v2 == null)
                {
                    // se non è stata ancora rilevata controllo che non sia stata memorizzata
                    // con l'altra estremitá (v)
                    v1 = v;
                    v2 = doorMap1.get(v);
                }

                if (v2 == null)
                {
                    // La porta non era ancora stata inserita nella mappa delle porte! La inseriamo..
                    doorMap1.put(old_v, v);
                    doorMap2.put(v, room);
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

        return true;
    }
    private final boolean buildConvexCut(Room r)
    {

        // Aggiungo tutti i muri della stanza ad una mappa
        int nVerticesInRoom = r.nVertices();
        HashSet<Segment> segmentsInRoom = new HashSet<>(nVerticesInRoom-1);
        Vertex old_v = r.getVertex(0);
        for( int i = 1; i < nVerticesInRoom+1; i++)
        {

            Vertex v = r.getVertex(i%nVerticesInRoom);
            segmentsInRoom.add(new Segment(v, old_v));
            old_v = v;
        }

        // Aggiungo tutti i segmenti della convex area che non sono muri della stanza
        // ad una mappa di 'porte virtuali' che si affacciano su altre convexArea.
        HashMap<Segment, ConvexArea> virtualDoorsInCA = new HashMap<>(nVerticesInRoom);

        for(ConvexArea ca : r)
        {

            int nVerticesInCA = ca.nVertices();

            old_v = ca.getVertex(0);
            for( int i = 1; i < nVerticesInCA+1; i++)
            {
                Vertex v = ca.getVertex(i%nVerticesInCA);
                Segment seg = new Segment(v, old_v);

                // Aggiungo solo i segmenti che non sono segmenti della stanza (che non sono muri o porte vere)
                if( segmentsInRoom.contains(seg) == false)
                {
                    ConvexArea linked_ca = virtualDoorsInCA.get(seg);
                    if( linked_ca != null )
                    {
                        // se nella mappa esisteva giá una convex area con un virtual wall dato dallo
                        // stesso segmento seg, allora aggiungo il ConvexCut alla room!
                        ConvexArea.addConvexCut(seg, ca, linked_ca);
                        virtualDoorsInCA.remove(seg);
                    }
                    else virtualDoorsInCA.put(new Segment(v, old_v), ca);
                }
                old_v = v;

            }

        }

        return true;

    }



    private final boolean loadPositions()
    {


        // C A R I C O    POSITIONs

        QRCodePositionMap = new HashMap<>();
        BeaconPositionMap = new HashMap<>();

        Cursor positionData = adapter.getPositionInAllRooms();
        if (positionData != null && positionData.moveToFirst())
        {

            int roomID_ci = positionData.getColumnIndex("ID_room");
            int convexAreaID_ci = positionData.getColumnIndex("ID_convexArea");
            int ID_ci = positionData.getColumnIndex("ID");

            int x_ci = positionData.getColumnIndex("x");
            int y_ci = positionData.getColumnIndex("y");
            int imageID_ci = positionData.getColumnIndex("ID_image");
            int artworkID_ci = positionData.getColumnIndex("Artwork.ID");
            int artworkName_ci = positionData.getColumnIndex("Artwork.name");
            int artworkDescr_ci = positionData.getColumnIndex("Artwork.descr");
            int qrCode_ci = positionData.getColumnIndex("QRCode.code");
            int minor_ci = positionData.getColumnIndex("Beacon.minor");
            int major_ci = positionData.getColumnIndex("Beacon.major");



            int roomID;
            int convexAreaID;
            Integer artworkID;
            int positionID;
            float x;
            float y;
            int imageID;
            String artworkName;
            String artworkDescr;
            String QRCode;
            Integer minor;
            Integer major;


            do {
                roomID = positionData.getInt(roomID_ci);
                convexAreaID = positionData.getInt(convexAreaID_ci);
                artworkID = positionData.getInt(artworkID_ci);

                QRCode = positionData.getString(qrCode_ci);
                minor = positionData.getInt(minor_ci);
                major = positionData.getInt(major_ci);

                positionID = positionData.getInt(ID_ci);
                x = positionData.getInt(x_ci);
                y = positionData.getInt(y_ci);

                imageID = positionData.getInt(imageID_ci);
                artworkName = positionData.getString(artworkName_ci);
                artworkDescr = positionData.getString(artworkDescr_ci);


                ArtworkRow artworkRow = null;
                ConvexArea convexArea = convexAreaMap.get(convexAreaID);
                Position position = null;
                if( artworkID != null )
                {
                    artworkRow = new ArtworkRow(artworkID, artworkName, artworkDescr, null, imageID, null, null, null, null);
                    position = new ArtworkPosition(x, y, artworkRow);
                    //roomMap.get(roomID).get(convexAreaID).new ArtworkMarker(x, y, artworkRow));
                }
                else
                {
                    position = new Position(x, y);
                }

                convexArea.add(position);


                if(QRCode != null)
                {
                    QRCodePositionMap.put(QRCode, position);
                }
                if(minor != null && major != null)
                {
                    int beaconID = ABeaconProximityManager.getID(minor, major);
                    BeaconPositionMap.put(beaconID, position);
                }

            } while (positionData.moveToNext());
        }
        return true;

    }



    public Building generateBuilding()
    {
        adapter.open(downloadedFilePath);

        // inizio a generare l'edificio
        building = new Building(70* ProportionsHelper.PPM, 70* ProportionsHelper.PPM);

        building.add(new Floor());


        HashMap<Integer, Room> roomMap = new HashMap<>();
        HashMap<Integer, ConvexArea> convexAreaMap = new HashMap<>();

        boolean success = false;
        success = loadConvexAreasAndRooms();
        success = loadVerticesInRooms();
        success = loadVerticesInConvexAreas();
        success = buildDoorsAndConvexCut();
        success = loadPositions();

        adapter.close();
        this.building = building;
        return building;
    }
    public Building getLatGeneratedBulding() {
        return building;
    }
}
