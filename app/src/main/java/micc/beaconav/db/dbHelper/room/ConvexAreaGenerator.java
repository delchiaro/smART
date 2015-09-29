//package micc.beaconav.db.dbHelper.room;
//
//import micc.beaconav.indoorEngine.building.ConvexArea;
//import micc.beaconav.indoorEngine.building.Room;
//
///**
// * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
// */
//public class ConvexAreaGenerator {
//
//
//    public static ConvexArea generateConvexAreaFromVertices(Room containerRoom, VertexRow[] vertexRows, SpotRow[] spotRows)
//    {
//        if(vertexRows == null ) return null;
//
//        ConvexArea ret = new ConvexArea();
//        containerRoom.add(ret);
//        for(int i = 0; i < vertexRows.length; i++)
//            ret.addVertex(i, vertexRows[i].toVertex());
////
////        for(int i = 0; i < spotRows.length; i++)
////            ret.add(spotRows[i].toSpot());
//
//        return ret;
//    }
//
//
//}
