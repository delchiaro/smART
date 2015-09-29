//package micc.beaconav.db.dbHelper.room;
//
//import micc.beaconav.db.dbJSONManager.tableScheme.TableRow;
//import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.LongField;
//import micc.beaconav.indoorEngine.building.ConvexArea;
//
///**
// * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
// */
//public class ConvexAreaRow extends TableRow<ConvexAreaSchema>
//{
//    static final ConvexAreaSchema schema = new ConvexAreaSchema();
//
//    public final LongField ID          = (LongField) field(schema.ID);
//    public final LongField ID_room        = (LongField) field(schema.ID_room);
//
//    public ConvexAreaRow() {
//        super(schema);
//    }
//
//    public ConvexArea toConvexArea() {
//        return new ConvexArea();
//    }
//}
//
