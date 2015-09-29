package micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.basicTypes;

import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.ColumnField;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.ColumnSchema;

/**
 * Created by nagash on 02/02/15.
 */
public class BooleanSchema extends ColumnSchema<Boolean> {


    public BooleanSchema(String name) {
        super(name);
    }

    @Override
    public BooleanField newField() {
        return new BooleanField(this);
    }

}
