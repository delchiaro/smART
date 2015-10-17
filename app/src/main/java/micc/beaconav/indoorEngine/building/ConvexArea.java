package micc.beaconav.indoorEngine.building;

import java.util.ArrayList;

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
    private ArrayList<Position> _positions = new ArrayList<>();

    private Marker _markerSelected = null;

    public ConvexArea() {
        super();
    }
    public ConvexArea(ArrayList<Vertex> _vertices) {
        super();
        this._vertices = _vertices;
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



}
