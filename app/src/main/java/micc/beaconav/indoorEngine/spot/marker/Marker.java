package micc.beaconav.indoorEngine.spot.marker;

import micc.beaconav.indoorEngine.spot.drawable.DrawableSpot;
import micc.beaconav.indoorEngine.spot.marker.collidable.Collidable;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 *
 */

public abstract class Marker extends DrawableSpot {



    protected boolean _selected = false;

    public Marker(float x, float y) {
        super(x, y);
        this._selected = false;
    }

    public void select() {
        if(this.isSelected())
            onReselected();
        else
        {
            this._selected = true;
            onSelected();
        }
    }
    public void deselect() {
        this._selected = false;
        onDeselected();
    }

    public final boolean isSelected() {
        return this._selected;
    }



    protected abstract void onReselected();
    protected abstract void onSelected();
    protected abstract void onDeselected();

    protected abstract Collidable generateCollidable(float x, float y);


    public boolean checkCollision(float x, float y) {
        return generateCollidable(x_for_drawing(), y_for_drawing()) . checkCollision(x, y);
    }





}
