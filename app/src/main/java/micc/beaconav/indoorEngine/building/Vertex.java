package micc.beaconav.indoorEngine.building;

import android.graphics.PointF;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class Vertex
{

    public enum Type {
        WALL, DOOR, APERTURE
    }

    private final Type _type;
    private final PointF _floorOffsetPosition;


    public Vertex(PointF position, Type nextLineType) {
        this._floorOffsetPosition = position;
        this._type = nextLineType;
    }
    public Vertex(PointF position) {
        this(position,  Type.WALL);
    }
    public Vertex(float x, float y, Type nextLineType) {
        this(new PointF(x, y), nextLineType);
    }
    public Vertex(float x, float y) {
        this(new PointF(x, y));
    }


    public float getX() {
        return _floorOffsetPosition.x;
    }
    public float getY() {
        return _floorOffsetPosition.y;
    }

    public Type getType() {
        return _type;
    }

}
