package micc.beaconav.db.dbHelper.room;

import java.util.List;

import micc.beaconav.indoorEngine.building.Room;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class RoomGenerator
{
    public static Room generateRoomFromVertices(List<VertexRow> vertexRows)
    {
        if(vertexRows == null ) return null;

        Room ret = new Room();
        for(int i = 0; i < vertexRows.size(); i++)
            ret.addVertex(vertexRows.get(i).toVertex(),i);

        return ret;
    }


    public static Room generateRoomFromVertices(VertexRow[] vertexRows)
    {
        if(vertexRows == null ) return null;

        Room ret = new Room();
        for(int i = 0; i < vertexRows.length; i++)
            ret.addVertex(vertexRows[i].toVertex(),i);

        return ret;
    }


}
