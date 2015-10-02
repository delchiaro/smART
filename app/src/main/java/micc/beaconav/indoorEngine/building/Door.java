package micc.beaconav.indoorEngine.building;

import micc.beaconav.indoorEngine.building.spot.Spot;
import micc.beaconav.indoorEngine.building.spot.path.PathSpot;

/**
 * Created by Nagash on 10/1/2015.
 */
public class Door
{
    private Vertex v1;
    private Vertex v2;

    private Room r1;
    private Room r2;

    public Door(Vertex v1, Vertex v2, Room room1, Room room2)
    {
        this.r1 = room1;
        this.r2 = room2;
        this.v1 = v1;
        this.v2 = v2;
    }

    public PathSpot generatePathSpot() {
        return  new PathSpot(v1.getX()+v2.getX()/2, v1.getY()+v2.getY()/2);
    }

}
