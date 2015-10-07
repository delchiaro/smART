//package micc.beaconav.db.dbHelper.artwork;
//
//import micc.beaconav.db.dbJSONManager.tableScheme.TableSchema;
//import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.ColumnSchema;
//import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.DoubleSchema;
//import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.FloatSchema;
//import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.LongSchema;
//import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.StringSchema;
//
///**
// * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
// */
//public class ArtworkSchema extends TableSchema<ArtworkRow>
//{
//    static final String          tableName  = "artwork";
//
//    static final LongSchema    ID          = new LongSchema("ID");
//    static final StringSchema    title       = new StringSchema("title");
//    static final StringSchema    descr       = new StringSchema("descr");
//    static final FloatSchema     x           = new FloatSchema("x");
//    static final FloatSchema     y           = new FloatSchema("y");
//    static final StringSchema    creationYear = new StringSchema("creationYear");
//    static final StringSchema    ID_artworkImage     = new StringSchema("ID_artworkImage");
//    static final StringSchema    dimensions  = new StringSchema("dimensions");
//    static final StringSchema    name  = new StringSchema("name");
//    static final StringSchema    biography  = new StringSchema("biography");
//    static final StringSchema    type  = new StringSchema("type");
//
//
//
//    private static final ColumnSchema[] columns = new ColumnSchema[]{ ID, title, descr, x, y, creationYear, ID_artworkImage, dimensions,
//                                                                        name, biography, type };
//
//    @Override
//    protected String generateTableName() {
//        return tableName;
//    }
//
//    @Override
//    protected ColumnSchema[] generateTableColumns() {
//        return columns;
//    }
//
//    @Override
//    protected ArtworkRow generateRow() {
//        return new ArtworkRow();
//    }
//
//}
