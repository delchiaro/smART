package micc.beaconav.outdoorEngine;


import micc.beaconav.db.dbHelper.museum.MuseumRow;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public interface MuseumMarkerManager {

    abstract void onClickMuseumMarker(MuseumRow museumRow);
    abstract void onDeselectMuseumMarker();

}
