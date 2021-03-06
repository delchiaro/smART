package micc.beaconav.indoorEngine.building;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.HashSet;

import micc.beaconav.indoorEngine.ProportionsHelper;
import micc.beaconav.indoorEngine.spot.drawable.DrawableSpot;
import micc.beaconav.indoorEngine.spot.marker.IMarkerContainer;
import micc.beaconav.indoorEngine.spot.marker.Marker;
import micc.beaconav.util.containerContained.Contained;
import micc.beaconav.util.containerContained.ContainerContained;

/**
 * Created by nagash on 25/09/15.
 */
public class ConvexArea extends ContainerContained<Room, Position> implements IMarkerContainer
{

    private ArrayList<Vertex> _vertices = new ArrayList<Vertex>();

    private HashSet<ConvexCut> _convexCut = new HashSet<>();

    private Marker _markerSelected = null;

    public Iterable<Vertex> vertices(){
        return _vertices;
    }


    /**
     * Automatically search for convex area that contains v1 and v2 of the door,
     *  inside room1 and room2.
     *  Load in the room (or generate) all the convex areas before adding doors!
     */
    public static ConvexCut addConvexCut(Segment segment, ConvexArea ca1, ConvexArea ca2)
    {
        ConvexCut convexCut = null;

        // controllo per sicurezza che i due vertici siano nelle convex area..
        Vertex r1v1 = ca1._vertices.get(ca1._vertices.indexOf(segment.getVertex1()));
        Vertex r1v2 = ca1._vertices.get(ca1._vertices.indexOf(segment.getVertex2()));
        Vertex r2v1 = ca2._vertices.get(ca2._vertices.indexOf(segment.getVertex1()));
        Vertex r2v2 = ca2._vertices.get(ca2._vertices.indexOf(segment.getVertex2()));
        if(  r1v1 != null && r1v2 != null && ( ( r1v1 == r2v1 ) || (r1v1 == r2v2) ) && ( (r1v2 == r2v2) || (r1v2 == r2v1) ) )
        {

            convexCut = new ConvexCut(segment, ca1, ca2);
            ca1._addConvexCut(convexCut);
            ca2._addConvexCut(convexCut);
        }
        return convexCut;
    }
//    public Door connectWithDoor(Room otherRoom, Vertex v1, Vertex v2)
//    {
//        return addDoor(this, otherRoom, v1, v2);
//    }

    private void _addConvexCut(ConvexCut convexCut) {
        this._convexCut.add(convexCut);
    }

    public HashSet<ConvexCut> convexCuts() { return _convexCut; }

    public ConvexArea() {
        super();
    }
    public ConvexArea(ArrayList<Vertex> _vertices) {
        super();
        this._vertices = _vertices;
    }

    public boolean containsConsecutiveVertex(Vertex v1, Vertex v2) {
        Vertex old_v = null;

        // TODO: buggata??
        int index1 = _vertices.indexOf(v1); // restituisce -1 quando non è presente!
        int index2 = _vertices.indexOf(v2);

        if(index1 >= 0 && index2 >= 0)
        {
            int min = index1;
            int max = index2;
            if(index1 > index2)
            {
                min = index2;
                max = index1;
            }

            // il secondo predicato serve nel caso in cui uno dei due vertici sia l'ultimo elemento
            // e l'altro il primo.
            if(max-min == 1 || ( (max == _vertices.size()-1) && min == 0))
                return true;
        }

        return false;
    }

    final public Room getContainerRoom() {
        return super.getContainer();
    }
    final public Building getCointainerBuilding() {
        return super.getContainer().getCointainerBuilding();
    }
    final public Floor getContainerFloor() {
        return super.getContainer().getContainerFloor();
    }

    public final void addVertex(Vertex vertex){
        this._vertices.add(vertex);
    }


    public int nVertices() {
        return _vertices.size();
    }
    public Vertex getVertex(int index) {
        return _vertices.get(index);
    }






    private static final int PPM = ProportionsHelper.PPM; // Pixel Per Miter


    private Paint wallsPaint = MapPaint.wall_convexArea_10.getPaint();
    private Paint floorPaint = MapPaint.get_random_convexArea_floor().getPaint();

    protected void drawWalls(Canvas canvas, PointF padding) {

        //final DrawFilter filter = new PaintFlagsDrawFilter(Paint.ANTI_ALIAS_FLAG, 0);
        //canvas.setDrawFilter(filter);

        Path wallpath = new Path();




        // Disegno PAVIMENTO e MURI:
        Vertex vertex;
        int nVertices = _vertices.size();

        if(_vertices.size() > 2)
        {
            Vertex firstVertex  = _vertices.get(0);
            wallpath.moveTo(firstVertex.getX()* PPM, firstVertex.getY()* PPM); // used for first point

            Vertex secondVertex = _vertices.get(1);
            wallpath.lineTo(secondVertex.getX() * PPM, secondVertex.getY() * PPM);

            for(int i = 2; i < nVertices; i ++ )
            {
                vertex = _vertices.get(i);
                wallpath.lineTo(vertex.getX() * PPM, vertex.getY() * PPM);
            }

            wallpath.lineTo(firstVertex.getX() * PPM, firstVertex.getY() * PPM);
            wallpath.lineTo(secondVertex.getX() * PPM, secondVertex.getY() * PPM);
            canvas.drawPath(wallpath, floorPaint);
            canvas.drawPath(wallpath, wallsPaint);
        }


    }

}
