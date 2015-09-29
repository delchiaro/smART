package micc.beaconav.db.dbJSONManager.tableScheme.columnSchema;

/**
 * Created by Nagash on 20/01/2015.
 */

public abstract class ColumnField<T>
{
    private ColumnSchema<T> schema = null;
    private T value;
    private T immutableValue = null;



    public ColumnField(ColumnSchema<T> schema) {
        this.schema = schema;
        initValue();
    }

    private void initValue(){
        this.value = generateNewInitValue();
    }

    protected abstract T generateNewInitValue();
    protected abstract T parseString(String newStringToParse);
    public abstract T generateDeepCopy(T deepCopyThis);


    public void set(T newVal) {
        if(!isImmutable()) this.value = newVal;
    }
    public void setParsing(String newValue) {
        if(!isImmutable()) this.value = parseString(newValue);
    }


    public final String columnName(){
        return this.schema.name();
    }
    public final ColumnSchema<T> getColumnSchema() {
        return this.schema;
    }
    public final T getValue() {
        if(!isImmutable()) return value;
        else return immutableValue;
    }

    public final T getValueCopy(){
        return generateDeepCopy(this.value);
    }

    public final void makeImmutable(){
        this.immutableValue = getValueCopy();
    }

    public final boolean isImmutable(){
        return (this.immutableValue != null);
    }


}
