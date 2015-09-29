package micc.beaconav.db.dbJSONManager.tableScheme.columnSchema;

/**
 * Created by nagash on 21/01/15.
 */
public abstract class ColumnSchema<T>
{

    private final String name;


    public ColumnSchema(String name) {
        this.name = name;
    }

    public abstract ColumnField<T> newField();

    public final String name(){
        return this.name;
    }

}
