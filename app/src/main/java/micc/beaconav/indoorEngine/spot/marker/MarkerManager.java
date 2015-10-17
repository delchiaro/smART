package micc.beaconav.indoorEngine.spot.marker;

import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;
import java.util.Iterator;

import micc.beaconav.indoorEngine.spot.Spot;
import micc.beaconav.indoorEngine.spot.drawable.DrawableSpot;
import micc.beaconav.indoorEngine.spot.drawable.DrawableSpotManager;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 *
 */
public class MarkerManager<M extends Marker> extends DrawableSpotManager<M> implements View.OnTouchListener
{
    private static final int MAX_CLICK_DURATION = 150;
    private long startClickTime;

    M selectedMarker = null;
    OnMarkerSelectedListener<M> listener = null;




    @Override
    public boolean add(M spot) {
        boolean superResult = super.add(spot);
        return superResult;
    }

    public void setOnMarkerSpotSelectedListener( OnMarkerSelectedListener<M> listener) {
        this.listener = listener;
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
            {
                if(event.getPointerCount() == 1)
                    startClickTime = Calendar.getInstance().getTimeInMillis();
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
            {
                if(event.getPointerCount() == 1)
                {
                    long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                    if (clickDuration < MAX_CLICK_DURATION)
                    {
                        M selectedMarker = tapEvent(event);
                        if(this.listener != null)
                        {
                            if (selectedMarker != null)
                                listener.onMarkerSpotSelected(selectedMarker);
                            else
                                listener.onNullMarkerSpotSelected();
                        }

                    }

                }
            }


        }
        return true;
    }

    public M tapEvent(MotionEvent event) {

        MotionEvent.PointerCoords pointerCoords = new MotionEvent.PointerCoords();
        event.getPointerCoords(0, pointerCoords);

        M collidedMarker = getCollidedMarker(pointerCoords.x, pointerCoords.y);

        if(collidedMarker != null)
        {
            if (this.selectedMarker != collidedMarker)
            {
                if (selectedMarker != null)
                    selectedMarker.deselect();
                selectedMarker = collidedMarker;
                selectedMarker.select();
            }
            else
            {
                // scaturirá l'evento "onReselected" senza scaturire onSelected e onDeselected
                selectedMarker.select();
            }
        }
        return collidedMarker;
    }

    public M getCollidedMarker(float x, float y) {

        M collidedMarker = null;

        Iterator<M> markerIter = iterator();
        boolean collided = false;

        while( !collided && markerIter.hasNext())
        {
            M marker = markerIter.next();
            if(marker.checkCollision(x, y))
            {
                collided = true;
                collidedMarker = marker;
            }
        }

        return collidedMarker;
    }

}
