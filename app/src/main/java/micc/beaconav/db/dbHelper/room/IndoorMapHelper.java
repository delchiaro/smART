//package micc.beaconav.db.dbHelper.room;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.iterator;
//import java.util.List;
//import java.util.SortedSet;
//import java.util.TreeSet;
//
//import micc.beaconav.db.dbHelper.museum.MuseumRow;
//import micc.beaconav.db.dbJSONManager.JSONDownloader;
//import micc.beaconav.indoorEngine.building.building;
//import micc.beaconav.indoorEngine.building.ConvexArea;
//import micc.beaconav.indoorEngine.building.Floor;
//import micc.beaconav.indoorEngine.building.Room;
//
///**
// * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
// */
//public class IndoorMapHelper {
//    private final MuseumRow museumRow;
//
//    JSONDownloader<RoomRow, RoomSchema>             roomDownloader;
//    JSONDownloader<ConvexAreaRow, ConvexAreaSchema> convexAreaDownloader;
//    JSONDownloader<VertexRow, VertexSchema>         vertexDownloader;
//    JSONDownloader<SpotRow, SpotSchema>             spotDownloader;
//    JSONDownloader<IngressRow, IngressSchema>       ingressDownloader;
//
//    building generatedBuilding = null;
//
//    public IndoorMapHelper(MuseumRow museum) {
//        this.museumRow = museum;
//        // fai partire tutti i download
//    }
//
//    public building getBuilding() {
//
//        roomDownloader.startDownload();
//        // TODO: da fare su un altro thread
//        // TODO: passare l'ID del museo
//
//        // TODO: scaricare tutte le convexArea, vertexRow, spot, ingress.. che sono nel museo con ID scelto
//        // finiti i download deve costruire il building e ritornarlo.
//        RoomRow[]       rooms       = roomDownloader.getDownloadedRows();
//        ConvexAreaRow[] convexAreas = convexAreaDownloader.getDownloadedRows();
//        VertexRow[]     vertices    = vertexDownloader.getDownloadedRows();
//        SpotRow[]       spots       = spotDownloader.getDownloadedRows();
//        IngressRow[]    ingresses   = ingressDownloader.getDownloadedRows();
//
//
//        SortedSet<ConvexAreaRow> convexAreaSet = new TreeSet<ConvexAreaRow>(Arrays.asList(convexAreas));
//        //SortedSet<VertexRow>
//        // TODO: comparator
//
//
//
//        building building = new building(museumRow.width.getValue().intValue(), museumRow.height.getValue().intValue());
//        List<Floor> floors = new ArrayList<>();
//        for(int r = 0; r < rooms.length; r++)
//        {
//            Floor roomFloor = floors.get(rooms[r].floorIndex.getValue());
//            if( roomFloor == null )
//            {
//                roomFloor = new Floor();
//                floors.add(new Floor());
//            }
//            Room currentRoom = rooms[r].toRoom();
//            roomFloor.add( currentRoom);
//
//
//            iterator<ConvexAreaRow> areaIter = convexAreaSet.iterator();
//            while(areaIter.hasNext())
//            {
//                ConvexAreaRow areaRow = areaIter.next();
//                if(areaRow.ID_room.getValue() < rooms[r].ID.getValue()) continue;
//                else if(areaRow.ID_room.getValue() < rooms[r].ID.getValue()) break;
//
//                ConvexArea currentConvexArea = areaRow.toConvexArea();
//                currentRoom.add(currentConvexArea);
//
//                // TODO: iterare anche su vertex, spot e ingresses.
//
//            }
//
//        }
//
//        return null;
//    }
//
//
//
//
//}
