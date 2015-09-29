package micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes;

import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.ColumnField;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.ColumnSchema;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class LongField extends ColumnField<Long> {

    public LongField(ColumnSchema<Long> schema) {
        super(schema);
    }

    @Override
    protected Long generateNewInitValue() {
        return new Long(0);
    }

    @Override
    protected Long parseString(String newStringToParse) {
        return Long.parseLong(newStringToParse);
    }

    @Override
    public Long generateDeepCopy(Long deepCopyThis) {
        return new Long(deepCopyThis);
    }

}
