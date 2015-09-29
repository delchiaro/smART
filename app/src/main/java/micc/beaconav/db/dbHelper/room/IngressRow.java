package micc.beaconav.db.dbHelper.room;

import micc.beaconav.db.dbJSONManager.tableScheme.TableRow;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.FloatField;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.IntegerField;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.LongField;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.StringField;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class IngressRow extends TableRow<IngressSchema>
{
    static final IngressSchema schema = new IngressSchema();

    public final LongField  ID          = (LongField) field(schema.ID);
    public final LongField  ID_area_a   = (LongField) field(schema.ID_area_a);
    public final LongField  ID_area_b   = (LongField) field(schema.ID_area_b);
    public final FloatField x           = (FloatField) field(schema.x);
    public final FloatField y           = (FloatField) field(schema.y);
    public final LongField  dock_a_x    = (LongField) field(schema.dock_a_x);
    public final LongField  dock_a_y    = (LongField) field(schema.dock_a_y);
    public final LongField  dock_b_x    = (LongField) field(schema.dock_b_x);
    public final LongField  dock_b_y    = (LongField) field(schema.dock_b_y);




    public IngressRow() {
        super(schema);
    }


}

