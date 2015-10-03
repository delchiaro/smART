package micc.beaconav.indoorEngine.spot.__old.path;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

import micc.beaconav.indoorEngine.dijkstraSolver.DijkstraNodeAdapter;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class DoorSpot extends PathSpot {

    public enum Visibility {
        VISIBLE, HIDDEN
    }

    private static final float arrowCosin = 1;
    private static final int arrowColor = Color.RED;
    private static final int arrowWidth = 5;

    private static Paint arrowPaint = null;
    private static void initPaint() {
        arrowPaint = new Paint();
        arrowPaint.setStyle(Paint.Style.STROKE);
        arrowPaint.setColor(arrowColor);
        arrowPaint.setStrokeWidth(arrowWidth);
    }

    private Visibility _visibleInDijkstraPath = Visibility.HIDDEN;
    private DoorSpot _linkedDoor;

    public DoorSpot(float x, float y, DoorSpot linkedDoor, Visibility visibleInPath ) {
        super(x, y);
        this._visibleInDijkstraPath = visibleInPath;
        if(linkedDoor != null)
        {
            if (linkedDoor != null && linkedDoor._linkedDoor == null)
            {
                _linkedDoor = linkedDoor;
                linkedDoor._linkedDoor = this;
            }
            else
            {
                //TODO:  exception
            }
        }
        else this._linkedDoor = null;

        if(arrowPaint != null) {
            initPaint();
        }
    }

    public DoorSpot(float x, float y, DoorSpot linkedDoor) {
        this(x, y, linkedDoor, Visibility.HIDDEN);
    }


    public boolean isVisibleInDijkstraPath() {
        return (this._visibleInDijkstraPath == Visibility.VISIBLE);
    }


    public void setVisibleInDijkstraPath( Visibility visibility ) {
        this._visibleInDijkstraPath = visibility;
    }

    @Override
    public List<? extends DijkstraNodeAdapter> getAdjacent() {
        ArrayList<DijkstraNodeAdapter> retList = new ArrayList<>(super.getAdjacent());
        retList.add(_linkedDoor);
        return retList;
    }



}
