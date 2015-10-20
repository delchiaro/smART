package micc.beaconav.indoorEngine.building;


import micc.beaconav.indoorEngine.dijkstraSolver.PathSpot;

/**
 * Created by nagash on 25/09/15.
 */
public class ConvexCut
{
    private Segment segment;

    private ConvexArea area1;
    private ConvexArea area2;


    private PathSpot pathSpotA;
    private PathSpot pathSpotB;


    ConvexCut(Segment segment, ConvexArea convexArea1, ConvexArea convexArea2)
    {
        this.area1 = convexArea1;
        this.area2 = convexArea2;
        this.segment = segment;
        pathSpotA = new PathSpot(segment.getVertex1().getX(), segment.getVertex1().getY());
        pathSpotB = new PathSpot(segment.getVertex2().getX(), segment.getVertex2().getY());

//        Position pA_ca1 = new Position(segment.getVertex1().getX(), segment.getVertex1().getY());
//        Position pB_ca1 = new Position(segment.getVertex2().getX(), segment.getVertex2().getY());
//        Position pA_ca2 = new Position(segment.getVertex1().getX(), segment.getVertex1().getY());
//        Position pB_ca2 = new Position(segment.getVertex2().getX(), segment.getVertex2().getY());
//
//
//        area1.add(pA_ca1);
//        area1.add(pB_ca1);
//        area2.add(pA_ca2);
//        area2.add(pB_ca2);
//
//        _doorSpotA_ca1 = new DoorSpot(pA_ca1, null, DoorSpot.Visibility.HIDDEN);
//        _doorSpotB_ca1 = new DoorSpot(pB_ca1, null, DoorSpot.Visibility.HIDDEN);;
//        _doorSpotA_ca2 = new DoorSpot(pA_ca2, null, DoorSpot.Visibility.HIDDEN);;
//        _doorSpotB_ca2 = new DoorSpot(pA_ca2, null, DoorSpot.Visibility.HIDDEN);;

    }


//
//    private DoorSpot _doorSpotA_ca1;
//    private DoorSpot _doorSpotB_ca1;
//    private DoorSpot _doorSpotA_ca2;
//    private DoorSpot _doorSpotB_ca2;
//

//
//    public DoorSpot getPatSpotA_CA1() { return this._doorSpotA_ca1; }
//    public DoorSpot getPatSpotB_CA1() { return this._doorSpotB_ca1; }
//
//    public DoorSpot getPatSpotA_CA2() { return this._doorSpotA_ca2; }
//    public DoorSpot getPatSpotB_CA2() { return this._doorSpotB_ca2; }
//

    public PathSpot getPathSpotA() { return pathSpotA; }
    public PathSpot getPathSpotB() { return pathSpotB; }

    public ConvexArea getConvexArea1() { return area1; }
    public ConvexArea getConvexArea2() { return area2; }

    public Segment getSegment() { return segment;}


}
