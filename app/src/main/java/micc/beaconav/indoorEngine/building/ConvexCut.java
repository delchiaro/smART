package micc.beaconav.indoorEngine.building;

import micc.beaconav.indoorEngine.spot.__old.path.PathSpot;

/**
 * Created by nagash on 25/09/15.
 */
public class ConvexCut
{
    private Segment segment;

    private ConvexArea ca1;
    private ConvexArea ca2;

    ConvexCut(Segment segment, ConvexArea convexArea1, ConvexArea convexArea2)
    {
        this.ca1 = convexArea1;
        this.ca2 = convexArea2;
        this.segment = segment;
    }

    public PathSpot generateMiddlePathSpot() {
        return  new PathSpot( (segment.getVertex1().getX()+segment.getVertex2().getX())/2,
                (segment.getVertex1().getY()+segment.getVertex2().getY())/2);
    }



}
