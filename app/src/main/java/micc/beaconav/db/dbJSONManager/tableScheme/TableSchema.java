package micc.beaconav.db.dbJSONManager.tableScheme;

import java.util.HashMap;

import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.ColumnSchema;

/**
 * Created by nagash on 21/01/15.
 */
public abstract class TableSchema<TR extends  TableRow>
{

    private final String name;
    private final HashMap<String, ColumnSchema> columns;


    public TableSchema()
    {
        this.name = generateTableName();
        final ColumnSchema[] columns = generateTableColumns();

        HashMap<String, ColumnSchema> initColumns = new HashMap<>(columns.length);
        for(int i = 0; i <columns.length; i++)
        {
            initColumns.put(columns[i].name(), columns[i]);
        }
        this.columns = initColumns;
    }

    protected abstract String         generateTableName();
    protected abstract ColumnSchema[] generateTableColumns();
    protected abstract TR       generateRow();


    public TR newRow(){
        return generateRow();
    }




    public final String getName(){
        return this.name;
    }

    public final HashMap<String, ColumnSchema> getColumnsMap() {
        return columns;
    }
    public final ColumnSchema[] getColumns() {
        return columns.values().toArray(new ColumnSchema[columns.size()]);
    }
    public final ColumnSchema getColumn(String colName) {
        return columns.get(colName);
    }
    public final ColumnSchema getcolumn(int index) {
        return getColumns()[index];
    }

    public final int size() {
        return columns.size();
    }


}
