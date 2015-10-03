package micc.beaconav.indoorEngine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import micc.beaconav.FragmentHelper;
import micc.beaconav.R;
import micc.beaconav.db.dbHelper.IArtRow;
import micc.beaconav.db.dbHelper.artwork.ArtworkRow;
import micc.beaconav.indoorEngine.building.IndoorMarker;
import micc.beaconav.indoorEngine.building.Room;
import micc.beaconav.indoorEngine.spot.marker.collidable.Collidable;
import micc.beaconav.indoorEngine.spot.marker.collidable.CollidableCircle;
import micc.beaconav.indoorEngine.spot.marker.Marker;


/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class ArtMarker extends IndoorMarker implements IArtRow
{



    private static Paint borderPaint = null;
    private static Paint fillPaint = null;

    private static Paint borderPaintSelected = null;
    private static Paint borderPaintSelected2 = null;
    private static Paint fillPaintSelected = null;

    private static final float bluePaintStroke = 4;
    private final static int radius_DP           = 5;
    private final static int radius_selected_DP  = 8;
    private final static int radius_collision_DP = 25;

    private final static int radius         = FragmentHelper.dpToPx(radius_DP);
    private final static int radius_selected = FragmentHelper.dpToPx(radius_selected_DP);
    private final static int radius_collision = FragmentHelper.dpToPx(radius_collision_DP);

    private static Paint bmpPaint = null;
    private static Bitmap bmpSelected = null;
    private static Bitmap bmp = null;
    private static int bmp_x_offset = -1;
    private static int bmp_y_offset = -1;



    public ArtMarker(float x, float y, String name, String descr, int imageID, int ID) {
        super(x, y);

        this._name = name;
        this._descr = descr;
        this._imageID = imageID;
        this._ID = ID;

        initDrawable();
        if(borderPaint == null)
        {
            borderPaint = new Paint();
            borderPaint.setColor(Color.BLUE);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(FragmentHelper.dpToPx(4));
            borderPaint.setAntiAlias(true);

            fillPaint = new Paint();
            fillPaint.setColor(Color.WHITE);
            fillPaint.setStyle(Paint.Style.FILL);
            fillPaint.setAntiAlias(true);


            borderPaintSelected = new Paint();
            borderPaintSelected.setColor(Color.BLUE);
            borderPaintSelected.setStyle(Paint.Style.STROKE);
            borderPaintSelected.setStrokeWidth(FragmentHelper.dpToPx(7));
            borderPaintSelected.setAntiAlias(true);

            borderPaintSelected2 = new Paint();
            borderPaintSelected2.setColor(Color.WHITE);
            borderPaintSelected2.setStyle(Paint.Style.STROKE);
            borderPaintSelected2.setStrokeWidth(FragmentHelper.dpToPx(4));
            borderPaintSelected2.setAntiAlias(true);

            fillPaintSelected = new Paint();
            fillPaintSelected.setColor(Color.BLUE);
            fillPaintSelected.setStyle(Paint.Style.FILL);
            fillPaintSelected.setAntiAlias(true);

        }
    }







    public static void flushDrawable() {
        if(bmp != null) {
            bmp.recycle();
            bmpSelected.recycle();
            bmp = null;
            bmpSelected = null;
        }
    }
    public static void initDrawable(){
        if(bmp == null)
        {
            bmp = BitmapFactory.
                    decodeResource(FragmentHelper.instance().getMainActivity().getResources(),
                            R.mipmap.artspot);

            bmpSelected = BitmapFactory.
                    decodeResource(FragmentHelper.instance().getMainActivity().getResources(),
                            R.mipmap.artwork_spot_green);
            bmp_x_offset = bmp.getWidth()/2;
            bmp_y_offset = bmp.getHeight()/2;

            bmpPaint = new Paint();
        }
    }

    @Override
    protected Drawable generateDrawable() {

        return new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                if(isSelected()){
//                    canvas.drawCircle(x_for_drawing(), y_for_drawing(), radius_selected, borderPaintSelected);
//                    canvas.drawCircle(x_for_drawing(), y_for_drawing(), radius_selected, borderPaintSelected2);
//                    canvas.drawCircle(x_for_drawing(), y_for_drawing(), radius_selected, fillPaintSelected);
                    canvas.drawBitmap(bmpSelected, x_for_drawing() - bmp_x_offset, y_for_drawing() - bmp_y_offset, bmpPaint );
                }
                else
                {
//                    canvas.drawCircle(x_for_drawing(), y_for_drawing(), radius, borderPaint);
//                    canvas.drawCircle(x_for_drawing(), y_for_drawing(), radius, fillPaint);
                    canvas.drawBitmap(bmp, x_for_drawing() - bmp_x_offset, y_for_drawing() - bmp_y_offset, bmpPaint);
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





    @Override
    protected void onReselected() {

    }

    @Override
    protected void onSelected() {
        //drawable().invalidateSelf();
    }

    @Override
    protected void onDeselected() {
        //drawable().invalidateSelf();
    }

    @Override
    protected Collidable generateCollidable(float x, float y) {
        return new CollidableCircle(x, y, radius_collision);
    }







    // ROBA DI: IArtRow
    private String _name;
    private String _descr;
    private int _imageID;
    private int _ID;



    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public int getImageId() {
        return 0;
    }

    @Override
    public long getID() {
        return 0;
    }
}
