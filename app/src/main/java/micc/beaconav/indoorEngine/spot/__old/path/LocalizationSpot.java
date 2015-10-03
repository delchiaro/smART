package micc.beaconav.indoorEngine.spot.__old.path;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import micc.beaconav.FragmentHelper;
import micc.beaconav.indoorEngine.building.Room;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class LocalizationSpot extends PathSpot {


    boolean isCurrentlyLocalized = false;
    PathSpot associatedPathSpot;


    public LocalizationSpot(PathSpot associatedPathSpot)
    {
        super(associatedPathSpot.x(), associatedPathSpot.y());
        this.associatedPathSpot = associatedPathSpot;
    }

    public LocalizationSpot(PathSpot associatedPath, float x, float y, Room room_container) {
        super(x, y);
        this.associatedPathSpot = associatedPath;
        initDrawable();
    }

    public LocalizationSpot(PathSpot associatedPath, float x, float y) {
        super(x, y);
        this.associatedPathSpot = associatedPath;
        initDrawable();
    }


    public void setCurrentlyLocalized(boolean value){
        this.isCurrentlyLocalized = value;
    }

    public boolean isCurrentlyLocalized() {
        return this.isCurrentlyLocalized;
    }
    public PathSpot getAssociatedPathSpot() {
        return associatedPathSpot;
    }
    @Override
    public void initDrawable() {

        if( myLocationWhiteCircle == null ) {
            myLocationWhiteCircle = new Paint();
            myLocationWhiteCircle.setColor(Color.WHITE);
            myLocationWhiteCircle.setStyle(Paint.Style.FILL);
            myLocationWhiteCircle.setAntiAlias(true);

            myLocationPaint = new Paint();
            myLocationPaint.setColor(Color.rgb(33, 150, 243));
            myLocationPaint.setStyle(Paint.Style.FILL);
            myLocationPaint.setAntiAlias(true);

            myLastLocationPaint = new Paint();
            myLastLocationPaint.setColor(Color.rgb(158, 158, 158));
        }


    }


    private static Paint myLocationWhiteCircle = null;
    private static Paint myLocationPaint = null;
    private static Paint myLastLocationPaint = null;
    private static int loc_radius = FragmentHelper.dpToPx(12);
    private static int loc_inner_radius = FragmentHelper.dpToPx(10);




    @Override
    protected Drawable generateDrawable() {
        return new Drawable() {
            @Override
            public void draw(Canvas canvas) {


                if(isCurrentlyLocalized)
                {
                    canvas.drawCircle(x_for_drawing(), y_for_drawing(), getScaleFactor() * loc_radius, myLocationWhiteCircle);
                    canvas.drawCircle(x_for_drawing(), y_for_drawing(), getScaleFactor() * loc_inner_radius, myLocationPaint);
                }
                else
                {
                    canvas.drawCircle(x_for_drawing(), y_for_drawing(), getScaleFactor() * loc_radius, myLocationWhiteCircle);
                    canvas.drawCircle(x_for_drawing(), y_for_drawing(), getScaleFactor() * loc_inner_radius, myLastLocationPaint);

                }
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter cf) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };
    }
}
