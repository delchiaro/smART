package micc.beaconav.db.dbHelper.artwork;

import micc.beaconav.db.dbHelper.IArtRow;

/**
 * Created by Nagash on 10/7/2015.
 */
public interface IArtworkRow extends IArtRow {

    String getName();
    String getDescription();
    String getArtistDescr();
    int getImageId();
    long getID();
    String getCreationYear();
    String getArtistName();
    String getLocation();
    String getType();
    String getDimensions();


}
