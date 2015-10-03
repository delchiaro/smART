package micc.beaconav.db.dbHelper.room;

import micc.beaconav.db.dbJSONManager.tableScheme.TableRow;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.FloatField;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.LongField;
import micc.beaconav.indoorEngine.spot.Spot;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class SpotRow extends TableRow<SpotSchema>
{
    static final SpotSchema schema = new SpotSchema();

    public final LongField ID          = (LongField) field(schema.ID);
    public final FloatField  x           = (FloatField) field(schema.x);
    public final FloatField  y           = (FloatField) field(schema.y);
    public final LongField ID_convexArea          = (LongField) field(schema.ID_convexArea);


    public SpotRow(SpotSchema tableSchema) {
        super(tableSchema);
    }

    public final Spot toSpot() {
        return new Spot(x.getValue(), y.getValue());
    }

}

