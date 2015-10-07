package micc.beaconav.indoorEngine;

import micc.beaconav.db.dbHelper.artwork.IArtworkRow;

/**
 * Created by Nagash on 10/7/2015.
 */
public class ArtworkRow implements IArtworkRow {



    private long _ID;
    private String _title;
    private String _descr;
    private String _creationYear;
    private long _ID_artworkImage;
    private String _dimensions;
    private String _artistName;
    private String _artistBiography;
    private String _type;



    public ArtworkRow(long ID, String title, String descr, String creationYear, long ID_image, String dimensions,
               String artistName, String artistBiography, String artworkType)
    {
        this._ID = ID;
        this._title = title;
        this._descr = descr;
        this._creationYear = creationYear;
        this._ID_artworkImage = ID_image;
        this._dimensions = dimensions;
        this._artistName = artistName;
        this._artistBiography = artistBiography;
        this._type = artworkType;
    }


    private ArtMarker _marker = null;

    boolean setMarker(ArtMarker marker)
    {
        if(_marker == null) {
            _marker = marker;
            return true;
        }
        else return false;
    }
    public ArtMarker getMarker()
    {
        return this._marker;
    }


    @Override
    public String getName() {
        return _title;
    }
    @Override
    public String getDescription() {
        return _descr;
    }

    public String getArtistDescr()
    {
        return _artistBiography;
    }

    @Override
    public int getImageId() {
        // TODO: Image per artworks
        //return context.getResources().getIdentifier(ID_artworkImage.getValue() , "drawable", context.getPackageName());
        //qui va presa la stringa per l'id di ogni opera
        return 0;

    }

    @Override
    public long getID() {
        return _ID;
    }

    public String getCreationYear()
    {
        return _creationYear;
    }

    public String getArtistName()
    {
        return _artistName;
    }

    public String getLocation()
    {
        return "Piano x, Stanza y";
    }

    public String getType()
    {
        return _type;
    }

    public String getDimensions()
    {
        return _dimensions;
    }

}
