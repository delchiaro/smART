package micc.beaconav.__test.canvasTest;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class canvasTestActivity extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        setContentView(new DrawSurfaceViewCanvas(this));

    }



    // Draw with separate task!! Best performance if used with threads
    public class DrawSurfaceViewCanvas extends SurfaceView implements SurfaceHolder.Callback
    {

        public DrawSurfaceViewCanvas(Context context)
        {
            super(context);

            SurfaceHolder sh = getHolder();
            //sh.addCallback();
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);

            int x = getWidth();
            int y = getHeight();
            int radius;
            radius = 100;
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            // Use Color.parseColor to define HTML colors
            paint.setColor(Color.parseColor("#CD5C5C"));
            canvas.drawCircle(x / 2, y / 2, radius, paint);

            paint.setStrokeWidth(10);
            canvas.drawLine(0, 0, 100, 100, paint);
            canvas.save();
            canvas.translate(2, 2);
            canvas.scale(0.02f, 0.02f);
            canvas.restore();
        }


        @Override
        public void surfaceCreated(SurfaceHolder holder)
        {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }







    // bad performance
    public class DrawViewCanvas extends View
    {

        public DrawViewCanvas(Context context)
        {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);

            int x = getWidth();
            int y = getHeight();
            int radius;
            radius = 100;
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            // Use Color.parseColor to define HTML colors
            paint.setColor(Color.parseColor("#CD5C5C"));
            canvas.drawCircle(x / 2, y / 2, radius, paint);

            paint.setStrokeWidth(10);
            canvas.drawLine(0, 0, 100, 100, paint);
            canvas.save();
            canvas.translate(2, 2);
            canvas.scale(0.02f, 0.02f);
            canvas.restore();
        }


    }




    // better performance
    public class DrawViewCanvasOnBitmap extends View
    {

        Bitmap b = null;

        public DrawViewCanvasOnBitmap(Context context)
        {
            super(context);

            b = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(b);


            int x = getWidth();
            int y = getHeight();
            int radius;
            radius = 100;
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            // Use Color.parseColor to define HTML colors
            paint.setColor(Color.parseColor("#CD5C5C"));
            canvas.drawCircle(x / 2, y / 2, radius, paint);

            paint.setStrokeWidth(10);
            canvas.drawLine(0, 0, 100, 100, paint);
            canvas.save();
            canvas.translate(2, 2);
            canvas.scale(0.02f, 0.02f);
            canvas.restore();

        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            canvas.drawBitmap(b,500,500,null);
        }


    }


}
