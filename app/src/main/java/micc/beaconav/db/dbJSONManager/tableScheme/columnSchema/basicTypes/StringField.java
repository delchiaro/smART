package micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes;

import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.ColumnField;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.ColumnSchema;

/**
 * Created by Riccardo Del Chiaro & Franco Yang (25/02/2015)
 */
public class StringField extends ColumnField<String> {

    public StringField(ColumnSchema<String> schema) {
        super(schema);
    }

    @Override
    protected String generateNewInitValue() {
        return new String("");
    }

    @Override
    protected String parseString(String newStringToParse) {
        return newStringToParse;
    }

    @Override
    public String generateDeepCopy(String deepCopyThis) {
        return new String(deepCopyThis);
    }


}
