package micc.beaconav.indoorEngine.building.spot.marker;

import micc.beaconav.indoorEngine.building.Room;
import micc.beaconav.indoorEngine.building.spot.draw.DrawableSpot;
import micc.beaconav.indoorEngine.building.spot.path.PathSpot;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 *
 * Rappresenta un  PathSpot cliccabile che interagisce quindi con l'utente come un pulsante.
 */

public abstract class MarkerSpot extends DrawableSpot {

    // TODO: meccanismo per renderlo cliccabile


    private PathSpot _nearestPathSpot = null;
    // per punto di arrivo o punto di partenza del cammino verso/da il marker
    // se è null usiamo come punto di partenza il RoomSpot



    protected boolean _selected = false;

    protected MarkerSpot(float x, float y) {
        super(x, y);
    }
    public MarkerSpot(float x, float y, Room roomContainer){
        super(x, y, roomContainer);
       // _generatedPathSpot = new PathSpot(x, y);
    }


    public void setNearestPathSpot(PathSpot nearestPathSpot) {
        if(nearestPathSpot != null)
          this._nearestPathSpot = nearestPathSpot;
    }

    public PathSpot getNearestPathSpot() {
        if(_nearestPathSpot != null)
            return _nearestPathSpot;
        else return getContainerRoom().getRoomSpot();
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






    //    public MarkerSpot(float x, float y, Room room_container) {
//        super(x, y, room_container);
//        _generatedPathSpot = new PathSpot(x, y);
//    }
//
//    @Override
//    public void x(float x) {
//        super.x(x);
//        _generatedPathSpot.x(x);
//    }
//
//    @Override
//    public void y(float y) {
//        super.y(y);
//        _generatedPathSpot.y(y);
//    }

//    public PathSpot generateAssociatedPathSpot() {
//        return _generatedPathSpot;
//    }
//


}
