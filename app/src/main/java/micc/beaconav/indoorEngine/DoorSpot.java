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


//  DEPRECATED DEPRECATED DEPRECATED DEPRECATED DEPRECATED DEPRECATED DEPRECATED DEPRECATED DEPRECATED
//   DEPRECATED DEPRECATED DEPRECATED DEPRECATED DEPRECATED DEPRECATED DEPRECATED DEPRECATED DEPRECATED
public class DoorSpot extends PathSpot {


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


    public DoorSpot(float x, float y ) {
       this(new  Spot(x, y));
    }

    public DoorSpot(Spot spot ) {
        super(spot);


        if(arrowPaint != null) {
            initPaint();
        }
    }


//    @Override
//    public List<? extends DijkstraNodeAdapter> getAdjacent() {
//        ArrayList<DijkstraNodeAdapter> retList = new ArrayList<>(super.getAdjacent());
//        //retList.add(_linkedDoor);
//        return retList;
//    }



}
