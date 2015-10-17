package micc.beaconav.indoorEngine;

import micc.beaconav.indoorEngine.building.Position;
import micc.beaconav.indoorEngine.spot.marker.IMarkerContainer;
import micc.beaconav.indoorEngine.spot.marker.Marker;

/**
 * Created by Nagash on 15/10/2015.
 */
public class ArtworkPosition extends Position {

    private final ArtworkMarker _markerSpot;

    private ArtworkRow _artworkRow;


    public ArtworkPosition(float x, float y, ArtworkRow artworkRow) {
        super(new ArtworkMarker(x, y));
        _markerSpot = (ArtworkMarker)super._spot;

        try {
            _markerSpot.setContainer(this);
        } catch (IMarkerContainer.IrreplaceableMarkerContainerException e) {
            e.printStackTrace();
        }

        this._artworkRow = artworkRow;
        this._artworkRow.setPosition(this);
    }


    public ArtworkRow getArtworkRow()
    {
        return _artworkRow;
    }

    public ArtworkMarker getMarker() {
        return _markerSpot; // markerSpot è super.getSpot() giá castato a ArtworkMarker
    }
}
