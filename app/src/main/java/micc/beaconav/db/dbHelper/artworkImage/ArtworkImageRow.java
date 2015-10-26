package micc.beaconav.db.dbHelper.artworkImage;

import micc.beaconav.db.dbHelper.IArtRow;
import micc.beaconav.db.dbJSONManager.tableScheme.TableRow;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.DoubleField;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.FloatField;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.LongField;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.StringField;
import micc.beaconav.outdoorEngine.localization.outdoorProximity.ProximityObject;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class ArtworkImageRow extends TableRow<ArtworkImageSchema>
{
    static ArtworkImageSchema schema = new ArtworkImageSchema();

    public final LongField ID          = (LongField) field(schema.ID);
    public final StringField link        = (StringField) field(schema.link);
    public final LongField ID_museum       = (LongField) field(schema.ID_museum);


    public ArtworkImageRow() {
        super(schema);
    }


    public final long getID(){
         return ID.getValue();
    }
    public final String getLink()
    {
        return link.getValue();
    }
    public final long getID_museum()
    {
        return ID_museum.getValue();
    }

}

