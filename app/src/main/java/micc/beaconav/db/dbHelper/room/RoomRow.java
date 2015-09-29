package micc.beaconav.db.dbHelper.room;

import micc.beaconav.db.dbJSONManager.tableScheme.TableRow;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.FloatField;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.IntegerField;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.LongField;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.StringField;
import micc.beaconav.indoorEngine.building.Room;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class RoomRow extends TableRow<RoomSchema>
{
    static final RoomSchema schema = new RoomSchema();

    public final LongField ID          = (LongField) field(schema.ID);
    public final StringField name        = (StringField) field(schema.name);
    public final StringField descr       = (StringField) field(schema.descr);
    public final FloatField  x           = (FloatField) field(schema.x);
    public final FloatField  y           = (FloatField) field(schema.y);
    public final IntegerField floorIndex = (IntegerField) field(schema.floorIndex);


    public RoomRow() {
        super(schema);
    }


    public Room toRoom() {

        Room ret = new Room();

        return ret;
    }
}

