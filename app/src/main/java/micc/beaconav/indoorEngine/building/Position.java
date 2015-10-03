package micc.beaconav.indoorEngine.building;

import micc.beaconav.indoorEngine.spot.Spot;

/**
 * Created by Nagash on 10/3/2015.
 */
public class Position extends Spot {

    private Room _room;
    private ConvexArea convexArea;

    public Position(float x, float y, Room room) {
        super(x, y);
        this._room = room;
    }

}
