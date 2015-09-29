package micc.beaconav.db.dbHelper.room;

import micc.beaconav.db.dbJSONManager.tableScheme.TableSchema;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.ColumnSchema;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.DoubleSchema;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.FloatSchema;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.LongField;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.LongSchema;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.StringSchema;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class VertexSchema extends TableSchema<VertexRow> {


    public static final String          tableName  = "Vertex";

    public static final LongSchema     ID  = new LongSchema("ID");
    public static final FloatSchema    x   = new FloatSchema("x");
    public static final FloatSchema    y   = new FloatSchema("y");

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
    protected VertexRow generateRow() {
        return new VertexRow(this);
    }
}
