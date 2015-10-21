package micc.beaconav.indoorEngine;

import micc.beaconav.indoorEngine.building.Position;
import micc.beaconav.indoorEngine.dijkstraSolver.PathSpot;
import micc.beaconav.indoorEngine.spot.Spot;

/**
 * Created by Nagash on 21/10/2015.
 */
public class MyPosition {


    private Position position;
    private LocalizationSpot localizationSpot;

    public MyPosition(Position myPosition) {
        this();
        setPosition(myPosition);
    }


    public MyPosition() {
        this.position = null;
        localizationSpot =  new LocalizationSpot( 0, 0 );
    }

    public void setPosition(Position myNewPosition)
    {
        this.position = myNewPosition;
        this.localizationSpot.x(myNewPosition.getSpot().x());
        this.localizationSpot.y(myNewPosition.getSpot().y());
        localizationSpot.setVisible(true);
    }

    public Position getPosition() {
        return position;
    }

    public void realtimeLocalied() {
        localizationSpot.setCurrentlyLocalized(true);
    }
    public void notRealtimeLocalized() {
        localizationSpot.setCurrentlyLocalized(false);
    }

    public boolean isRealTimeLocalized() {
        return localizationSpot.isCurrentlyLocalized();
    }

    public LocalizationSpot getLocalizationSpot() {
        return this.localizationSpot;
    }
}
