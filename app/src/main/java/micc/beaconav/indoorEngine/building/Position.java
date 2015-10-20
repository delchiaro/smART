package micc.beaconav.indoorEngine.building;

import micc.beaconav.indoorEngine.dijkstraSolver.PathSpot;
import micc.beaconav.indoorEngine.spot.Spot;
import micc.beaconav.indoorEngine.spot.marker.IMarkerContainer;
import micc.beaconav.indoorEngine.spot.marker.Marker;
import micc.beaconav.util.containerContained.Contained;
import micc.beaconav.util.containerContained.Container;

/**
 * Created by Nagash on 10/3/2015.
 */
public class Position extends Contained<ConvexArea> implements IMarkerContainer {


    // private Room _room;
    protected final Spot _spot;
    protected PathSpot _pathSpoth = null;



    public Position(float x, float y) {
        this(new Spot(x, y));
    }

    public Position(Spot spot) {
        super();
        this._spot = spot;
        if( spot instanceof PathSpot)
            this._pathSpoth = (PathSpot) spot;
        else
            this._pathSpoth = new PathSpot(spot);
    }

    @Override
    public void setContainer(ConvexArea container, Container.Key key) {
        super.setContainer(container, key);

        if(this._spot instanceof Marker)
        {
            getContainerConvexArea().getContainerFloor().addMarker((Marker) _spot);
        }
        else
        {
            getContainerConvexArea().getContainerFloor().addSpot(_spot);
        }
    }

    public final ConvexArea getContainerConvexArea()
    {
        //** @Return: getContainer() alias
        //*
        return getContainer();
    }

    public final Room getContainerRoom() {
        return getContainerConvexArea().getContainerRoom();
    }

    public final Floor getContainerFloor() {
        return getContainerRoom().getContainerFloor();
    }

    public Spot getSpot() {
        return this._spot;
    }


    public PathSpot getPathSpot() {
        return this._pathSpoth;
    }




    public boolean canSee(Position q)
    {
        if(q.getContainerRoom() == this.getContainerRoom())
        {
            if(q.getContainerConvexArea() == this.getContainerConvexArea())
                return true;
            else return canSee(q.getPathSpot());
        }
        else return false;

    }

    public boolean canSee(PathSpot q)
    {
        /**
         * Si suppone che Q sia nella stessa stanza di this (in caso contrario si ritornerebbe subito false).
         * Non si controlla che q sia nella stessa convex area di this (in tal caso si ritornerebbe subito true).
         * Questi controlli si demandano al chiamante.
         */
        Segment seg = new Segment(new Vertex(this.getPathSpot().x(), this.getPathSpot().y()), new Vertex(q.x(), q.y()) );
        Room r = this.getContainerRoom();

        int nVertices = r.nVertices();
        Vertex v1 = r.getVertex(0);
        Vertex v2 = null;
        Segment wallSegment = null;

        for( int i = 1 ; i < nVertices+1 ; i++)
        {
            v2 = v1;
            v1 = r.getVertex(i);
            wallSegment = new Segment(v1, v2);

            // appena trovo un muro che interseca, ritorno FALSE --> non posso vedere q
            if(Segment.intersect(seg, wallSegment) == true)
                return false;
        }

        return true; // se non ho intersecato nessun muro arrivo a questo statement, e ritorno true.
    }


}
