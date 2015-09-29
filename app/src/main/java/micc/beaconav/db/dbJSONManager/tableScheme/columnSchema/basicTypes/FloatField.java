package micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes;

import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.ColumnField;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.ColumnSchema;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class FloatField extends ColumnField<Float> {

    public FloatField(ColumnSchema<Float> schema) {
        super(schema);
    }

    @Override
    protected Float generateNewInitValue() {
        return new Float(0);
    }

    @Override
    protected Float parseString(String newStringToParse) {
        return Float.parseFloat(newStringToParse);
    }

    @Override
    public Float generateDeepCopy(Float deepCopyThis) {
        return new Float(deepCopyThis);
    }

}
