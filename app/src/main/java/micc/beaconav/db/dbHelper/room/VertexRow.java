package micc.beaconav.db.dbHelper.room;

import micc.beaconav.db.dbJSONManager.tableScheme.TableRow;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.FloatField;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes.LongField;
import micc.beaconav.indoorEngine.building.Vertex;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class VertexRow extends TableRow<VertexSchema>
{
    static final VertexSchema schema = new VertexSchema();

    public final LongField ID          = (LongField) field(schema.ID);
    public final FloatField  x           = (FloatField) field(schema.x);
    public final FloatField  y           = (FloatField) field(schema.y);


    public VertexRow(VertexSchema tableSchema) {
        super(tableSchema);
    }

    public final Vertex toVertex() {
        return new Vertex(x.getValue(), y.getValue());
    }

}

