package micc.beaconav.db.dbHelper.artworkImage;

import micc.beaconav.db.dbJSONManager.tableScheme.TableSchema;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.ColumnSchema;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.DoubleSchema;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.FloatSchema;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.LongSchema;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.StringSchema;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class ArtworkImageSchema extends TableSchema<ArtworkImageRow>
{
    static final String          tableName  = "ArtworkImage";

    static final LongSchema    ID           = new LongSchema("ID");
    static final StringSchema  link         = new StringSchema("link");
    static final LongSchema    ID_museum    = new LongSchema("ID_museum");



    private static final ColumnSchema[] columns = new ColumnSchema[]{ ID, link, ID_museum};

    @Override
    protected String generateTableName() {
        return tableName;
    }

    @Override
    protected ColumnSchema[] generateTableColumns() {
        return columns;
    }

    @Override
    protected ArtworkImageRow generateRow() {
        return new ArtworkImageRow();
    }

}
