package micc.beaconav.indoorEngine.building;

import micc.beaconav.indoorEngine.dijkstraSolver.PathSpot;
import micc.beaconav.indoorEngine.DoorSpot;
import micc.beaconav.indoorEngine.spot.Spot;

/**
 * Created by Nagash on 10/1/2015.
 */
public class Door
{
    private Vertex vertexA;
    private Vertex vertexB;

    private PathSpot _middlePathSpot;
    private Room r1;
    private Room r2;

    private ConvexArea ca1;
    private ConvexArea ca2;

    private DoorSpot doorSpot;

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

//        Position pos1 = new Position((vertexA.getX()+vertexB.getX())/2, (vertexA.getY()+vertexB.getY())/2 );
//        Position pos2 = new Position((vertexA.getX()+vertexB.getX())/2, (vertexA.getY()+vertexB.getY())/2 );
//
//        ca1.add(pos1);
//        ca2.add(pos2);


        doorSpot = new DoorSpot((vertexA.getX()+vertexB.getX())/2, (vertexA.getY()+vertexB.getY())/2, DoorSpot.Visibility.HIDDEN);

    }


    public Vertex getVertexA() {
        return vertexA;
    }
    public Vertex getVertexB() {
        return vertexB;
    }
    public Room getRoom1() {
        return r1;
    }
    public Room getRoom2() {
        return r2;
    }
    public ConvexArea getConvexArea1() { return ca1; }
    public ConvexArea getConvexArea2() { return ca2; }



    public DoorSpot getDoorSpot() {
        return doorSpot;
    }


}
