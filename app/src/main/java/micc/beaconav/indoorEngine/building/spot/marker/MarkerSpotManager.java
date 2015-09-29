package micc.beaconav.indoorEngine.building.spot.marker;

import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;
import java.util.Iterator;

import micc.beaconav.indoorEngine.building.spot.draw.DrawableSpotManager;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 *
 */
public class MarkerSpotManager<MS extends  MarkerSpot> extends DrawableSpotManager<MS>  implements View.OnTouchListener
{

    // TODO: implementare metodi per touch, ritornare il marker toccato tra quelli nel manager

    MS selectedMarker = null;
    private static final int MAX_CLICK_DURATION = 150;
    private long startClickTime;

    OnSpotMarkerSelectedListener<MS> listener = null;


    public void setOnMarkerSpotSelectedListener( OnSpotMarkerSelectedListener<MS> listener) {
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
                        MS selectedMarker = tapEvent(event);
                        if(selectedMarker != null && this.listener != null )
                        {
                            listener.onMarkerSpotSelected(selectedMarker);
                        }
                        else if(selectedMarker == null && this.listener != null)
                        {
                            listener.onNullMarkerSpotSelected();
                        }

                    }

                }
            }


        }
        return true;
    }

    public MS tapEvent(MotionEvent event) {

        MotionEvent.PointerCoords pointerCoords = new MotionEvent.PointerCoords();
        event.getPointerCoords(0, pointerCoords);

        MS collidedMarker = getCollidedMarker(pointerCoords.x, pointerCoords.y);

        if(collidedMarker != null)
        {
            if (this.selectedMarker != collidedMarker)
            {
                if (selectedMarker != null)
                    selectedMarker.deselect();
                collidedMarker.select();
                selectedMarker = collidedMarker;
            }
            else
            {
                // scaturirá l'evento "onReselected" senza scaturire onSelected e onDeselected
                selectedMarker.select();
            }
        }
        return collidedMarker;
    }

    public MS getCollidedMarker(float x, float y) {

        MS collidedMarker = null;

        Iterator<MS> markerIter = iterator();
        boolean collided = false;

        while( !collided && markerIter.hasNext())
        {
            MS marker = markerIter.next();
            if(marker.checkCollision(x, y))
            {
                collided = true;
                collidedMarker = marker;
            }
        }

        return collidedMarker;
    }

}
