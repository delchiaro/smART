//package micc.beaconav.db.dbHelper.room;
//
//import micc.beaconav.db.dbJSONManager.tableScheme.TableSchema;
//import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.ColumnSchema;
//import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.FloatSchema;
//import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.IntegerSchema;
//import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.LongSchema;
//import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.StringSchema;
//
///**
// * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
// */
//public class ConvexAreaSchema extends TableSchema<ConvexAreaRow>
//{
//    public static final String          tableName  = "ConvexArea";
//
//    static final LongSchema    ID          = new LongSchema("ID");
//    static final LongSchema    ID_room        = new LongSchema("ID_room");
//
//    private static final ColumnSchema[] columns = new ColumnSchema[]{ ID, ID_room};
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
//    protected ConvexAreaRow generateRow() {
//        return new ConvexAreaRow();
//    }
//
//}
