package micc.beaconav.indoorEngine.building;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import java.util.ArrayList;

import micc.beaconav.indoorEngine.ProportionsHelper;
import micc.beaconav.indoorEngine.building.painting.MapPaint;
import micc.beaconav.indoorEngine.building.spot.marker.MarkerSpot;
import micc.beaconav.indoorEngine.building.spot.path.PathSpot;
import micc.beaconav.indoorEngine.building.spot.path.RoomSpot;
import micc.beaconav.indoorEngine.building.spot.Spot;
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

    private boolean autogenRoomSpot = false;
    private boolean _roomSpot_needs_refresh = false;
    private final RoomSpot _roomSpot;





    public Room(float roomSpot_x, float roomSpot_y) {

        this._roomSpot = new RoomSpot(roomSpot_x, roomSpot_y, null);
    }
    public Room() {
        this(0, 0);
        autogenRoomSpot = true;
    }

    @Override
    public void setContainer(Floor container, Key key) {
        super.setContainer(container, key);
//        this.add(_roomSpot);
    }

// * * * * * * * *  S P O T S  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    private void tryAddToManager(Spot newSpot ) {
        if(newSpot instanceof PathSpot)
        {
            this.getContainerFloor().addPathSpot((PathSpot) newSpot);
        }
        else if( newSpot instanceof MarkerSpot)
        {
            this.getContainerFloor().addMarker((MarkerSpot) newSpot);
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
//    public void addMarker(MarkerSpot marker) {
//        this.add(marker);
//    }
//


    public RoomSpot getRoomSpot() {

        if(autogenRoomSpot == true && _roomSpot_needs_refresh )
        {
            generateRoomSpot();
        }
        return _roomSpot;
    }
    private void generateRoomSpot() {
        if(this._vertices.size() != 0)
        {
            float x = 0f;
            float y = 0f;
            Vertex v;
            int pointCount = _vertices.size();
            for (int i = 0; i < pointCount; i++)
            {
                v = _vertices.get(i);
                x += v.getX();
                y += v.getY();
            }

            x = x/pointCount;
            y = y/pointCount;

            _roomSpot.setPosition(x, y);
        }

        _roomSpot_needs_refresh = false;
    }


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





    // * * * * * * * * *  VERTICES - ( PAINTING: WALLS, DOORS, APERTURE ) * * * * * * * * * * * * *
    private void addVertex(Vertex vertex){
        this._vertices.add(vertex);
        if(this.autogenRoomSpot == true ) this._roomSpot_needs_refresh = true;
    }
    public void pushVertex(Vertex newVertex){
        addVertex(newVertex);
    }
    public void pushAperture(PointF newVertexCoord) {
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

            for (int i = 1; i < nVertices; i++)
            {
                oldVertex = vertex;
                vertex = _vertices.get(i);
                if (vertex.getType() == Vertex.Type.APERTURE)
                {
                    canvas.drawLine(oldVertex.getX() * PPM, oldVertex.getY() * PPM,
                            vertex.getX() * PPM, vertex.getY() * PPM, this.wallsPaint);
                }
                switch (oldVertex.getType())
                {
                    case DOOR:
                        // PRIMA RIMUOVO IL MURO E POI INSERISCO LA PORTA..
                        // se non è necessario eliminare il disegno della apertura
                        canvas.drawLine(oldVertex.getX() * PPM, oldVertex.getY() * PPM,
                                vertex.getX() * PPM, vertex.getY() * PPM, this.aperturePaint);
                        canvas.drawLine(oldVertex.getX() * PPM, oldVertex.getY() * PPM,
                                vertex.getX() * PPM, vertex.getY() * PPM, this.doorPaint);
                        break;

                    case APERTURE:
                        canvas.drawLine(oldVertex.getX() * PPM, oldVertex.getY() * PPM,
                                vertex.getX() * PPM, vertex.getY() * PPM, this.aperturePaint);
                        break;

                }
            }
        }

    }






}