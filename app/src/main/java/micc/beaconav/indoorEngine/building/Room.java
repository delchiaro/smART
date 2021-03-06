package micc.beaconav.indoorEngine.building;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Iterator;

import micc.beaconav.indoorEngine.ProportionsHelper;
import micc.beaconav.util.containerContained.ContainerContained;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class Room  extends ContainerContained<Floor, ConvexArea>
        //extends Drawable
{
    private static final int PPM = ProportionsHelper.PPM; // Pixel Per Miter
    private static final int WALL_WIDTH_CM = 15;


    private Paint wallsPaint = MapPaint.wall_default_25.getPaint();
    private Paint doorPaint = MapPaint.door_default_25.getPaint();
    private Paint floorPaint = MapPaint.floor_default.getPaint();
    private Paint aperturePaint = MapPaint.aperture_default.getPaint();


    //private DrawableSpotManager<DrawableSpot> _drawableSpotManager = new DrawableSpotManager<>();


    private ArrayList<Vertex> _vertices = new ArrayList<Vertex>();
    private ArrayList<Door> _doors = new ArrayList<Door>();


    public Iterable<Vertex> vertices() {
        return _vertices;
    }

// * * * * * * * *  D O O R S * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *


    /**
     * Automatically search for convex area that contains v1 and v2 of the door,
     *  inside room1 and room2.
     *  Load in the room (or generate) all the convex areas before adding doors!
     */
    public static Door addDoor(Room r1, Room r2, Vertex v1, Vertex v2)
    {
        Door door = null;
        Vertex r1v1 = r1._vertices.get(r1._vertices.indexOf(v1));
        Vertex r1v2 = r1._vertices.get(r1._vertices.indexOf(v2));
        Vertex r2v1 = r2._vertices.get(r2._vertices.indexOf(v1));
        Vertex r2v2 = r2._vertices.get(r2._vertices.indexOf(v2));

        if(  r1v1 != null && r1v2 != null && ( ( r1v1 == r2v1 ) || (r1v1 == r2v2) ) && ( (r1v2 == r2v2) || (r1v2 == r2v1) ) )
        {

            door = new Door(v1, v2, r1, r2);
            r1._addDoor(door);
            r2._addDoor(door);
        }
        return door;
    }
//    public Door connectWithDoor(Room otherRoom, Vertex v1, Vertex v2)
//    {
//        return addDoor(this, otherRoom, v1, v2);
//    }

    private void _addDoor(Door door) {
        this._doors.add(door);
    }
    public Iterator<Door> getDoors() {
        return this._doors.iterator();
    }
    public boolean containsDoor(final Door door){
        return this._doors.contains(door);
    }
    public Door getDoor(int index) {
        return this._doors.get(index);
    }

    public ArrayList<Door> doors() {
        return this._doors;
    }

// * * * * * * * *  S P O T S  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *






    // * * * * * * * * *  VERTICES - ( PAINTING: WALLS, DOORS, APERTURE ) * * * * * * * * * * * * *
    private final void addVertex(Vertex vertex){
        this._vertices.add(vertex);
    }
    public final void pushVertex(Vertex newVertex){
        addVertex(newVertex);
    }
    public final void pushAperture(PointF newVertexCoord) {
        addVertex(new Vertex(newVertexCoord, Vertex.Type.APERTURE));
    }
    public void pushDoor(PointF newVertexCoord) {
        addVertex(new Vertex(newVertexCoord, Vertex.Type.DOOR));
    }
    public void pushWall(PointF newVertexCoord) {
        addVertex(new Vertex(newVertexCoord, Vertex.Type.WALL));
    }


    public int indexOfVertex(Vertex vertex){
        return this._vertices.indexOf(vertex);
    }
    public void addVertex(Vertex vertex, int index){
        this._vertices.add(index, vertex);
    }
    public Vertex getVertex(int index){
        return this._vertices.get(index);
    }

    public int nVertices(){
        return this._vertices.size();
    }




    // * * * * * * * * * C O N T A I N E R S * * * * * * * * * * * * * * * * * * * * * * * * * * *
    final public Building getCointainerBuilding() {
        return super.getContainer().getContainerBuilding();
    }
    final public Floor getContainerFloor() {
        return super.getContainer();
    }


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
    protected void drawDoorsAndAperture(Canvas canvas, PointF padding) {

        Vertex vertex;
        int nVertices = _vertices.size();

        if(_vertices.size() > 2)
        {
            Vertex oldVertex = null;
            vertex = _vertices.get(0);


            boolean doorJustClosed = false;
            // nVertices + 1 perchè voglio anche ritornare sull'ultimo vertice per controllare la coppia
            // di vertici [nVertices]-[0] oltre che [0]-[1], [1]-[2], ..., [nVertices-1]-[nVertices]
            for (int i = 1; i < nVertices+1; i++)
            {
                oldVertex = vertex;
                vertex = _vertices.get(i%nVertices); // coda circolare

                if(doorJustClosed)
                {
                    // se al ciclo precedente si è chiusa una porta (disegnata) allora saltiamo
                    // il vertice successivo.
                    doorJustClosed = false;
                }
                else if( vertex.getType() == oldVertex.getType())
                {
                    // solo se i due vertex sono entrambi door o entrambi aperture disegno entrambe
                    switch (oldVertex.getType())
                    {
                        case DOOR:
                            // PRIMA RIMUOVO IL MURO E POI INSERISCO LA PORTA..
                            // se non è necessario eliminare il disegno della apertura
                            canvas.drawLine(oldVertex.getX() * PPM, oldVertex.getY() * PPM,
                                    vertex.getX() * PPM, vertex.getY() * PPM, this.aperturePaint);
                            canvas.drawLine(oldVertex.getX() * PPM, oldVertex.getY() * PPM,
                                    vertex.getX() * PPM, vertex.getY() * PPM, this.doorPaint);
                            doorJustClosed = true;
                            break;

                        case APERTURE:
                            canvas.drawLine(oldVertex.getX() * PPM, oldVertex.getY() * PPM,
                                    vertex.getX() * PPM, vertex.getY() * PPM, this.aperturePaint);
                            doorJustClosed = true;
                            break;

                    }
                }

            }
        }


        if(MapPaint.DEBUG_CONVEX_AREA)
        {
            for( ConvexArea ca : this )
            {
                ca.drawWalls(canvas, padding);
            }
        }

    }






































//
//    @Override
//    public void addAll(List<Spot> spotList) {
//        super.addAll(spotList);
//        for(Spot s : spotList)
//        {
//            tryAddToManager(s);
//        }
//    }
//
//
//    @Override
//    public void add(Spot newSpot) {
//        super.add(newSpot);
//        tryAddToManager(newSpot);
//    }
//
//    @Override
//    public void add(int location, Spot newSpot) {
//        super.add(location, newSpot);
//        tryAddToManager(newSpot);
//    }

//
//    public void addPathSpot(PathSpot pathSpot, boolean linkToRoomSpot) {
//        this.add(pathSpot);
//        if(linkToRoomSpot) pathSpot.addLinkBidirectional(this.getRoomSpot());
//    }
//
//    public void addMarker(Marker marker) {
//        this.add(marker);
//    }
//



//    public void addPathSpot(PathSpot pathSpot, PathSpot... linkedSpot) {
//        addPathSpot(pathSpot, false, linkedSpot);
//    }
//    public void addPathSpotNet(PathSpot pathSpot, boolean linkToRoomSpot, PathSpot... netLinkedSpot) {
//        this._pathSpotManager.add(pathSpot);
//        if(linkToRoomSpot) pathSpot.addLinkBidirectional(this.getRoomSpot());
//        int netSize = netLinkedSpot.length;
//        pathSpot.addLinkNet(netLinkedSpot);\\
//    }
//    public void addPathSpotNet(PathSpot pathSpot, PathSpot... netLinkedSpot) {
//        addPathSpotNet(pathSpot, false, netLinkedSpot );
//    }
//    public void addPathSpotSequence(PathSpot pathSpot, boolean linkToRoomSpot, PathSpot... netLinkedSpot) {
//        this._pathSpotManager.add(pathSpot);
//        if(linkToRoomSpot) pathSpot.addLinkBidirectional(this.getRoomSpot());
//        int netSize = netLinkedSpot.length;
//        pathSpot.addLinkNet(netLinkedSpot);
//    }
//    public void addPathSpotNet(PathSpot pathSpot, PathSpot... netLinkedSpot) {
//        addPathSpotNet(pathSpot, false, netLinkedSpot );
//    }


//
//
//    public static DoorSpot[] addDoorSpot(Room r1, float x1, float y1, DoorSpot.Visibility visibility1,boolean linkRoom1Spot,
//                                   Room r2, float x2, float y2,  DoorSpot.Visibility visibility2, boolean linkRoom2Spot) {
//
//        DoorSpot d1 = new DoorSpot(x1, y1, null, visibility1);
//        DoorSpot d2 = new DoorSpot(x2, y2, d1, visibility2);
//        r1.addPathSpot(d1, linkRoom1Spot);
//        r2.addPathSpot(d2, linkRoom2Spot);
//        DoorSpot[] ret = new DoorSpot[2];
//        ret[0] = d1;
//        ret[1] = d2;
//        return ret;
//    }
//
//

//
//
//
//    public static DoorSpot[] addDoorSpot(Room r1, float x1, float y1, boolean linkRoom1Spot,
//                                   Room r2, float x2, float y2, boolean linkRoom2Spot) {
//
//        return addDoorSpot(r1, x1, y1, DoorSpot.Visibility.HIDDEN, linkRoom1Spot, r2, x2, y2, DoorSpot.Visibility.HIDDEN, linkRoom2Spot);
//    }
//
//
//    public static DoorSpot[] addDoorSpot(Room r1, float x1, float y1, DoorSpot.Visibility visibility1,
//                                   Room r2, float x2, float y2,  DoorSpot.Visibility visibility2) {
//
//        return addDoorSpot(r1, x1, y1, visibility1, true, r2, x2, y2, visibility2, true);
//
//    }
//
//
//    public static DoorSpot[] addDoorSpot(Room r1, float x1, float y1, DoorSpot.Visibility visibility1,
//                                   Room r2, float x2, float y2) {
//
//        return addDoorSpot(r1, x1, y1, visibility1, true, r2, x2, y2, DoorSpot.Visibility.HIDDEN, true);
//
//    }
//
//
//    public static DoorSpot[] addDoorSpot(Room r1, float x1, float y1,
//                                   Room r2, float x2, float y2,  DoorSpot.Visibility visibility2) {
//
//        return addDoorSpot(r1, x1, y1, DoorSpot.Visibility.HIDDEN, true, r2, x2, y2, visibility2, true);
//
//    }
//
//
//    public static DoorSpot[] addDoorSpot(Room r1, float x1, float y1,boolean linkRoom1Spot,
//                                   Room r2, float x2, float y2,  DoorSpot.Visibility visibility2, boolean linkRoom2Spot) {
//
//        return addDoorSpot(r1, x1, y1, DoorSpot.Visibility.HIDDEN, linkRoom1Spot, r2, x2, y2, visibility2, linkRoom2Spot);
//
//    }
//
//
//    public static DoorSpot[] addDoorSpot(Room r1, float x1, float y1, DoorSpot.Visibility visibility1,boolean linkRoom1Spot,
//                                   Room r2, float x2, float y2, boolean linkRoom2Spot) {
//        return addDoorSpot(r1, x1, y1, visibility1, linkRoom1Spot, r2, x2, y2, DoorSpot.Visibility.HIDDEN, linkRoom2Spot);
//
//    }
//
//    public static DoorSpot[] addDoorSpot(Room r1, float x1, float y1, Room r2, float x2, float y2) {
//        return addDoorSpot(r1, x1, y1, DoorSpot.Visibility.HIDDEN, true, r2, x2, y2, DoorSpot.Visibility.HIDDEN, true);
//    }
//





}