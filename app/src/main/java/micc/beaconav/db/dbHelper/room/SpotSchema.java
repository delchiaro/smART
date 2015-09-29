package micc.beaconav.db.dbHelper.room;

import micc.beaconav.db.dbJSONManager.tableScheme.TableSchema;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.ColumnSchema;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.FloatSchema;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.LongSchema;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class SpotSchema extends TableSchema<SpotRow> {


    public static final String          tableName  = "Spot";

    public static final LongSchema     ID  = new LongSchema("ID");
    public static final FloatSchema    x   = new FloatSchema("x");
    public static final FloatSchema    y   = new FloatSchema("y");
    public static final LongSchema     ID_convexArea  = new LongSchema("ID_convexArea");


    private static final ColumnSchema[] columns = new ColumnSchema[]{ ID, x, y};


    @Override
    protected String generateTableName() {
        return tableName;
    }

    @Override
    protected ColumnSchema[] generateTableColumns() {
        return columns;
    }

    @Override
    protected SpotRow generateRow() {
        return new SpotRow(this);
    }
}
