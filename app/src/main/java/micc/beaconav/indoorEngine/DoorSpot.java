package micc.beaconav.indoorEngine;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import micc.beaconav.indoorEngine.building.Position;
import micc.beaconav.indoorEngine.dijkstraSolver.DijkstraNodeAdapter;
import micc.beaconav.indoorEngine.dijkstraSolver.PathSpot;
import micc.beaconav.indoorEngine.spot.Spot;

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

    public DoorSpot(float x, float y, Visibility visibleInPath ) {
       this(new  Spot(x, y), visibleInPath);
    }

    public DoorSpot(Spot spot, Visibility visibleInPath ) {
        super(spot);
        this._visibleInDijkstraPath = visibleInPath;


        if(arrowPaint != null) {
            initPaint();
        }
    }

    public DoorSpot(Spot spot) {
        this(spot, Visibility.HIDDEN);
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
        //retList.add(_linkedDoor);
        return retList;
    }



}
