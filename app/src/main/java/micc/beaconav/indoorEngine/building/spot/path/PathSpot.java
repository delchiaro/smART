package micc.beaconav.indoorEngine.building.spot.path;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import micc.beaconav.FragmentHelper;
import micc.beaconav.R;
import micc.beaconav.indoorEngine.building.Room;
import micc.beaconav.indoorEngine.building.spot.draw.DrawableSpot;
import micc.beaconav.indoorEngine.dijkstraSolver.DijkstraNodeAdapter;
import micc.beaconav.indoorEngine.dijkstraSolver.DijkstraStatistics;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class PathSpot extends DrawableSpot implements DijkstraNodeAdapter<PathSpot>
{

    public static int STEP_NUMBER_GOAL = -100;
    public static int STEP_NUMBER_MY_POSITION = -1000;
    public static int STEP_MY_LAST_POSITION = -2000;

    protected int stepNumber = -1;
    private DijkstraStatistics _dijkstraStatistick = new DijkstraStatistics();
    private List<PathSpot> _linkedSpots = new ArrayList<PathSpot>();


    public PathSpot(float x, float y, Room room_container) {
        super(x, y, room_container);
        initDrawable();
    }
    public PathSpot(float x, float y) {
        super(x, y);
        initDrawable();
    }







    // * * * * * * * * * * * * DIJKSTRA * * * * * * * * * * * * * * * * * * * * * * * * * * *


    public void addLinkBidirectional(PathSpot linkedSpot) {
        this._linkedSpots.add(linkedSpot);
        linkedSpot._linkedSpots.add(this);
    }
    public void addLinkBidirectionalSequence(PathSpot... linkedSequenceSpot) {
        int sequenceLenght = linkedSequenceSpot.length;
        if (sequenceLenght > 0)
        {
            this.addLinkBidirectional(linkedSequenceSpot[0]);
            for (int i = 1; i < sequenceLenght; i++)
            {
                linkedSequenceSpot[i-1].addLinkBidirectional(linkedSequenceSpot[i]);
            }
        }
    }
    public void allLinkBidirectionalToAll(PathSpot... linkedSpots) {
        int sequenceLenght = linkedSpots.length;
        for (int i = 0; i < sequenceLenght; i++)
        {
            this.addLinkBidirectional(linkedSpots[i]);
        }

    }
    public void addLinkNet(PathSpot... netLinkedSpot) {

        int netSize = netLinkedSpot.length;

        for(int i = 0; i < netSize; i++ )
        {
            this.addLinkBidirectional(netLinkedSpot[i]);

            for(int j = i; j < netSize; j++ )
            {
                netLinkedSpot[i].addLinkBidirectional(netLinkedSpot[j]);
            }
        }
    }


    @Override
    public double getArchWeight(PathSpot adjacentNodeAdapter) {
        float adjX = adjacentNodeAdapter.x();
        float adjY = adjacentNodeAdapter.y();
        float x = this.x();
        float y = this.y();
        double distance = Math.sqrt((adjX - x)*(adjX - x) + (adjY - y)*(adjY - y));
        return distance;
    }

    @Override
    public List<? extends DijkstraNodeAdapter> getAdjacent() {
        //return Arrays.asList(((DijkstraNodeAdapter[])_linkedSpots.toArray(new PathSpot[_linkedSpots.size()])));
        return _linkedSpots;
    }

    @Override
    public void setPathIndex(int index) {
        this.setStepNumber(index);
    }


    @Override
    public DijkstraStatistics getDijkstraStatistic() {
        return _dijkstraStatistick;
    }



    // * * * * * * * * * * * * DRAWABLE  * * * * * * * * * * * * * * * * * * * * * * * * * * *




    private static Paint textNumberPaint = null;
    private static Paint textPaint = null;

    private static Paint borderPaint = null;
    private static Paint fillPaint = null;

    private static Paint redBallPaint = null;

    private final static int radiusBall = FragmentHelper.dpToPx(4);
    private final static int radius = FragmentHelper.dpToPx(10);


//    @Override
//    public Drawable drawable()
//    {
//        if( stepNumber >= 0 || stepNumber == STEP_NUMBER_GOAL)
//            return super.drawable();
//
//        else return null;
//    }

    protected void initDrawable() {
        if(borderPaint == null)
        {

            redBallPaint = new Paint();
            redBallPaint.setColor(FragmentHelper.instance().getMainActivity().getResources().getColor(R.color.material_red));
            redBallPaint.setStyle(Paint.Style.FILL);
            redBallPaint.setAntiAlias(true);

            textPaint = new Paint();
            textPaint.setColor(Color.BLACK);
            textPaint.setStyle(Paint.Style.STROKE);
            textPaint.setStrokeWidth(FragmentHelper.dpToPx(1)*2/3);
            textPaint.setAntiAlias(true);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(28);

            textNumberPaint = new Paint();
            textNumberPaint.setColor(Color.BLACK);
            textNumberPaint.setStyle(Paint.Style.STROKE);
            textNumberPaint.setStrokeWidth(FragmentHelper.dpToPx(1));
            textNumberPaint.setAntiAlias(true);
            textNumberPaint.setTextAlign(Paint.Align.CENTER);
            textNumberPaint.setTextSize(36);

            borderPaint = new Paint();
            borderPaint.setColor(Color.RED);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(FragmentHelper.dpToPx(4));
            borderPaint.setAntiAlias(true);

            fillPaint = new Paint();
            fillPaint.setColor(Color.WHITE);
            fillPaint.setStyle(Paint.Style.FILL);
            fillPaint.setAntiAlias(true);
        }
    }




    @Override
    protected Drawable generateDrawable() {
        return new PathDrawable();
    }


    public void hide() {
        resetStepNumber();
    }
    public void resetStepNumber() {
        this.stepNumber = -1;
    }
    public void setStepNumber( int stepNumber) {
        this.stepNumber = stepNumber;
    }
    public void removeStepNumber() {
        this.stepNumber = -1;
    }








    public class PathDrawable extends Drawable {

        private final Rect textBounds = new Rect(); //don't new this up in a draw method

        public void drawTextCentred(Canvas canvas, Paint paint, String text, float cx, float cy){
            paint.getTextBounds(text, 0, text.length(), textBounds);
            canvas.drawText(text, cx , cy - textBounds.exactCenterY(), paint);
           // canvas.drawText(text, cx - textBounds.exactCenterX() , cy - textBounds.exactCenterY(), paint);

        }

        @Override
        public void draw(Canvas canvas) {
            // drawConPallini(canvas);
            drawConLinee(canvas);

        }

        public void drawConLinee(Canvas canvas) {
            canvas.drawCircle(x_for_drawing(), y_for_drawing(), getScaleFactor() * radiusBall,  redBallPaint);
        }

        public void drawConPallini(Canvas canvas) {
            if (stepNumber >= 0 || stepNumber == STEP_NUMBER_GOAL )
            {
                canvas.drawCircle(x_for_drawing(), y_for_drawing(), getScaleFactor() * radius,  borderPaint);
                canvas.drawCircle(x_for_drawing(), y_for_drawing(), getScaleFactor() * radius,  fillPaint);

                //canvas.drawText(Integer.toString(stepNumber), x_for_drawing(), y_for_drawing(), textPaint);

                if(stepNumber == 0)
                    drawTextCentred(canvas, textPaint, "Start", x_for_drawing(), y_for_drawing());

                else if(stepNumber == STEP_NUMBER_GOAL)
                    drawTextCentred(canvas, textPaint, "Goal", x_for_drawing(), y_for_drawing());

                else
                    drawTextCentred(canvas, textNumberPaint, Integer.toString(stepNumber), x_for_drawing(), y_for_drawing());
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
    }


}
