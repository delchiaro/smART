package micc.beaconav.indoorEngine.building.spot.custom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import micc.beaconav.FragmentHelper;
import micc.beaconav.R;
import micc.beaconav.db.dbHelper.artwork.ArtworkRow;
import micc.beaconav.indoorEngine.building.Room;
import micc.beaconav.indoorEngine.building.spot.marker.Collidable;
import micc.beaconav.indoorEngine.building.spot.marker.CollidableCircle;
import micc.beaconav.indoorEngine.building.spot.marker.MarkerSpot;


/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class ArtSpot extends MarkerSpot
{
    private  ArtworkRow artworkRow;

    public ArtSpot() { this( 0, 0); }
    public ArtSpot(float x, float y) {
        this(x, y, null);
    }
    public ArtSpot(float x, float y, Room room_container) {
        super(x, y, room_container);
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


    public void setArtworkRow(ArtworkRow row) {
        this.artworkRow = row;
        if(row.getLinkedArtSpot() != this)
        {
            row.setLinkArtSpot(this);
        }
    }
    public ArtworkRow getArtworkRow() {
        return this.artworkRow;
    }


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


}
