package micc.beaconav.db.dbHelper.museum;

import micc.beaconav.db.dbJSONManager.tableScheme.TableRow;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.ColumnSchema;
import micc.beaconav.db.dbJSONManager.tableScheme.TableSchema;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.Type;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.DoubleSchema;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.FloatSchema;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.LongField;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.LongSchema;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.StringSchema;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class MuseumSchema extends TableSchema<MuseumRow>
{
    static final String          tableName  = "Museum";

    static final LongSchema    ID            = new LongSchema("ID");
    static final StringSchema    name        = new StringSchema("name");
    static final StringSchema    descr       = new StringSchema("descr");
    static final DoubleSchema    latitude    = new DoubleSchema("latitude");
    static final DoubleSchema    longitude   = new DoubleSchema("longitude");
    static final FloatSchema     width       = new FloatSchema("width");
    static final FloatSchema     height      = new FloatSchema("height");



    private static final ColumnSchema[] columns = new ColumnSchema[]{ ID, name, descr, latitude, longitude, width, height};

    @Override
    protected String generateTableName() {
        return tableName;
    }

    @Override
    protected ColumnSchema[] generateTableColumns() {
        return columns;
    }

    @Override
    protected MuseumRow generateRow() {
        return new MuseumRow();
    }

}
