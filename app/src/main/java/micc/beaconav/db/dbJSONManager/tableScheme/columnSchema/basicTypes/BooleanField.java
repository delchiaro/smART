package micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes;

import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.ColumnField;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.ColumnSchema;

/**
 * Created by nagash on 01/02/15.
 */
public class BooleanField extends ColumnField<Boolean> {

    public BooleanField(ColumnSchema<Boolean> schema) {
        super(schema);
    }

    @Override
    protected Boolean generateNewInitValue() {
        return new Boolean(false);
    }

    @Override
    protected Boolean parseString(String newStringToParse) {
        return Boolean.parseBoolean(newStringToParse);
    }

    @Override
    public Boolean generateDeepCopy(Boolean deepCopyThis) {
        return new Boolean(deepCopyThis);
    }

}
