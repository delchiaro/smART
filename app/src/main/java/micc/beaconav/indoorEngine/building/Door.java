package micc.beaconav.indoorEngine.building;

import micc.beaconav.indoorEngine.spot.__old.path.PathSpot;

/**
 * Created by Nagash on 10/1/2015.
 */
public class Door
{
    private Vertex vertexA;
    private Vertex vertexB;

    private Room r1;
    private Room r2;

    private ConvexArea ca1;
    private ConvexArea ca2;

    // package protected!
    // Solo tramite l'oggetto Room si possono aggiungere porte
    // (tramite metodo statico addDoor)
    /**
     * Automatically search for convex area that contains vertexA and vertexB of the door,
     *  inside room1 and room2.
     */
    Door(Vertex vertexA, Vertex vertexB, Room room1, Room room2)
    {

        this.r1 = room1;
        this.r2 = room2;
        this.vertexA = vertexA;
        this.vertexB = vertexB;

        for(ConvexArea ca : room1)
        {
            if(ca.containsConsecutiveVertex(vertexA, vertexB)) {
                ca1 = ca;
                break;
            }
        }
        for(ConvexArea ca : room2)
        {
            if(ca.containsConsecutiveVertex(vertexA, vertexB)) {
                ca2 = ca;
                break;
            }
        }

    }

    public Vertex getFirstVertex() {
        return vertexA;
    }
    public Vertex getSecondVertex() {
        return vertexB;
    }
    public Room getFirstRoom() {
        return r1;
    }
    public Room getSecondRoom() {
        return r2;
    }

    public PathSpot generatePathSpot() {
        return  new PathSpot(vertexA.getX()+ vertexB.getX()/2, vertexA.getY()+ vertexB.getY()/2);
    }

}
