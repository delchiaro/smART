package micc.beaconav.indoorEngine.building.painting;

import android.graphics.Color;
import android.graphics.Paint;

import micc.beaconav.indoorEngine.ProportionsHelper;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class MapPaint
{


    private static int PPM = ProportionsHelper.PPM;

    private static int          WALL_DEFAULT_COLOR = Color.rgb(0, 0, 0);
    private static float        WALL_DEFAULT_WIDTH = 0.25f;
    private static Paint.Style  WALL_STYLE = Paint.Style.STROKE;

    private static int          DOOR_DEFAULT_COLOR = Color.rgb(255, 230, 171);
    private static float        DOOR_DEFAULT_WIDTH = 0.25f;
    private static Paint.Style  DOOR_DEFAULT_STYLE = Paint.Style.STROKE;

    private static int          FLOOR_DEFAULT_COLOR = Color.rgb(237, 231, 246); //Color.rgb(255, 255, 255);
    private static Paint.Style  FLOOR_DEFAULT_STYLE = Paint.Style.FILL;





    public static MapPaint wall_default_25 = new MapPaint(WALL_DEFAULT_COLOR, WALL_DEFAULT_WIDTH, WALL_STYLE, false);
    public static MapPaint door_default_25 = new MapPaint(DOOR_DEFAULT_COLOR, DOOR_DEFAULT_WIDTH, DOOR_DEFAULT_STYLE, false);
    public static MapPaint floor_default = new MapPaint(FLOOR_DEFAULT_COLOR, 0, FLOOR_DEFAULT_STYLE, false);
    public static MapPaint aperture_default = new MapPaint(FLOOR_DEFAULT_COLOR, WALL_DEFAULT_WIDTH, WALL_STYLE, false);




    private final Paint wallPaint;

    private MapPaint(int color, float stroke_in_meters, Paint.Style paintStyle, boolean antiAliasing){
        wallPaint = new Paint();
        wallPaint.setColor(color);
        wallPaint.setStrokeWidth((PPM*stroke_in_meters));
        wallPaint.setStyle(paintStyle);
        wallPaint.setAntiAlias(antiAliasing);
    }


    public Paint getPaint() {
        return wallPaint;
    }
//
//    public void setWidth(float width_in_meters){
//        wallPaint.setStrokeWidth(width_in_meters*PPM);
//    }
//    public void setColor(int color) {
//        wallPaint.setColor(color);
//    }

    public class Builder{
    }

}
