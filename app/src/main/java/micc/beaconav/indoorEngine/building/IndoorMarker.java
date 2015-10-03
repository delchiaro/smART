package micc.beaconav.indoorEngine.building;

import micc.beaconav.indoorEngine.spot.marker.Marker;

/**
 * Created by Nagash on 10/3/2015.
 */
public abstract class IndoorMarker extends Marker {
    private Room _room = null;

    public IndoorMarker(float x, float y) {
        super(x, y);
    }

    void setRoom(Room room){
        if(_room == null)
            _room = room;
    }

    public Room containerRoom(){
        return this._room;
    }

}
