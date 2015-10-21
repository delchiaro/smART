package micc.beaconav.indoorEngine.dijkstraSolver;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;

import java.util.Iterator;
import java.util.List;

import micc.beaconav.FragmentHelper;
import micc.beaconav.R;
import micc.beaconav.indoorEngine.DoorSpot;
import micc.beaconav.indoorEngine.LocalizationSpot;
import micc.beaconav.indoorEngine.spot.drawable.DrawableSpotManager;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class PathSpotManager<PS extends PathSpot> extends DrawableSpotManager<PS> {

    public PathSpotManager() {
        initPaint();
    }

    public PathSpotManager(List initList) {
        super(initList);
        initPaint();
    }



    private static Paint paintLine = null;
    private  static int lineStroke = FragmentHelper.dpToPx(2);

    public static void initPaint() {
        if(paintLine == null )
        {
            paintLine = new Paint();
            paintLine.setColor(FragmentHelper.instance().getMainActivity().getResources().getColor(R.color.material_red));
            paintLine.setStyle(Paint.Style.STROKE);
            paintLine.setStrokeWidth(lineStroke);
        }
    }

    @Override
    protected  Drawable generateWrapperDrawable()
    {
        return new Drawable() {
            @Override
            public void draw(Canvas canvas)
            {
                Iterator<PS> spotIter = iterator();
                int i = 0;

                PS spot;
                PointF[] path =  new PointF[size()];
                boolean drawLine = false;

                while(spotIter.hasNext())
                {
                    spot = spotIter.next();

                    if (spot instanceof DoorSpot)
                    {
                        if (((DoorSpot) spot).isVisibleInDijkstraPath())
                        {
                            if (spotIter.hasNext() == false)
                                spot.setStepNumber(-100); // -100 = punto di arrivo
                            else spot.setStepNumber(i); // 0 = punto di partenza
                            spot.drawable().draw(canvas);
                            path[i] = new PointF(spot.x_for_drawing(), spot.y_for_drawing());
                            i++;
                        }
                    }
                    // TODO
//                    else if(spot instanceof LocalizationSpot)
//                    {
//                        spot.setStepNumber(0);
//                        spot.drawable().draw(canvas);
//                    }
                    else
                    {
                        if (spotIter.hasNext() == false)
                            spot.setStepNumber(-100); // -100 = punto di arrivo
                        else spot.setStepNumber(i); // 0 = punto di partenza
                        spot.drawable().draw(canvas);
                        path[i] = new PointF(spot.x_for_drawing(), spot.y_for_drawing());
                        i++;
                    }
                }

                if(path != null && i > 0)
                {
                    Path pathLine = new Path();
                    pathLine.moveTo(path[0].x, path[0].y);

                    int j=1;
                    for ( j = 1; j < i; j++)
                        pathLine.lineTo(path[j].x, path[j].y);


                    canvas.drawPath(pathLine, paintLine);
                }
            }



//            public final boolean drawConLinee(Canvas canvas, iterator<PS> spotIter, Integer index)
//            {
//
//            }

            public final void drawConPallini(Canvas canvas) {
                Iterator<PS> spotIter = iterator();
                int i = 0;
                while(spotIter.hasNext())
                {
                    PS spot = spotIter.next();
                    if(spot instanceof DoorSpot)
                    {
                        if( ((DoorSpot) spot).isVisibleInDijkstraPath() ) {

                            if (spotIter.hasNext() == false)
                                spot.setStepNumber(-100); // -100 = punto di arrivo
                            else spot.setStepNumber(i); // 0 = punto di partenza
                            spot.drawable().draw(canvas);
                            i++;
                        }
//                        PS nextSpot = spotIter.next();
//                        if(nextSpot instanceof DoorSpot)
//                        {
//                            // disegna Spot --> nextSpot nei rispettivi punti di aggancio della porta
//                        }
//                        else nextSpot.drawable().draw(canvas);
                    }
                    else
                    {
                        if( spotIter.hasNext() == false )
                            spot.setStepNumber(-100); // -100 = punto di arrivo
                        else spot.setStepNumber(i); // 0 = punto di partenza
                        spot.drawable().draw(canvas);


                        i++;
                    }

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
