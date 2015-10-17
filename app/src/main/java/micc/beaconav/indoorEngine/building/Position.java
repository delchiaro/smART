package micc.beaconav.indoorEngine.building;

import micc.beaconav.indoorEngine.spot.Spot;
import micc.beaconav.indoorEngine.spot.marker.IMarkerContainer;
import micc.beaconav.indoorEngine.spot.marker.Marker;
import micc.beaconav.util.containerContained.Contained;
import micc.beaconav.util.containerContained.Container;

/**
 * Created by Nagash on 10/3/2015.
 */
public class Position extends Contained<ConvexArea> implements IMarkerContainer {


    // private Room _room;
    protected final Spot _spot;



    public Position(float x, float y) {
        super();
        this._spot = new Spot(x, y);
    }

    public Position(Spot spot) {
        super();
        this._spot = spot;

//
    }

    @Override
    public void setContainer(ConvexArea container, Container.Key key) {
        super.setContainer(container, key);

        if(this._spot instanceof Marker)
        {
            getConvexArea().getContainerFloor().addMarker((Marker) _spot);
        }
        else
        {
            getConvexArea().getContainerFloor().addSpot(_spot);
        }
    }

    public final ConvexArea getConvexArea()
    {
        //** @Return: getContainer() alias
        //*
        return getContainer();
    }

    public Spot getSpot() {
        return this._spot;
    }

}
