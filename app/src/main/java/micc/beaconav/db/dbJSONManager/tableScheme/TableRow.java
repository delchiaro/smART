package micc.beaconav.db.dbJSONManager.tableScheme;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.ColumnField;
import micc.beaconav.db.dbJSONManager.tableScheme.columnSchema.ColumnSchema;

/**
 * Created by nagash on 21/01/15.
 */
public class TableRow<TS extends TableSchema>
{
    private final TS schema;
    private final HashMap<String, ColumnField> fields;

    public TableRow(TS schema, ColumnField... extistingfields)
    {
        this.schema = schema;
        this.fields = new HashMap<>();


        Iterator<ColumnSchema> iter = schema.getColumnsMap().values().iterator();

        while(iter.hasNext())
        {
            boolean existingFieldInserted = false;
            ColumnSchema columnSchema = iter.next();
            for(int i = 0; i < extistingfields.length && !existingFieldInserted; i++)
            {
                if(columnSchema == extistingfields[i].getColumnSchema()) {
                    this.fields.put(columnSchema.name(), extistingfields[i]);
                    existingFieldInserted = true;
                }
            }
            if(!existingFieldInserted)
                fields.put(columnSchema.name(), columnSchema.newField());
        }

    }
    public TableRow(TS schema)
    {
        this.schema = schema;
        fields = new HashMap<>(schema.getColumnsMap().values().size());

        Iterator<ColumnSchema> iter = schema.getColumnsMap().values().iterator();

        while(iter.hasNext())
        {
            ColumnSchema columnSchema = iter.next();
            fields.put(columnSchema.name(), columnSchema.newField());
        }
    }



    public TS getSchema(){
        return schema;
    }

    public int size(){
        return fields.values().size();
    }


    public ColumnField[] fields(){
        return fields.values().toArray(new ColumnField[fields.size()]);
    }

    public ColumnField field(String name){
       // schema.getColumn(name).getClass().cast(fields.get(name));
        return fields.get(name);
    }
    public ColumnField field(ColumnSchema colSchema){

        if(schema.getColumn(colSchema.name()).getClass() == colSchema.getClass())
        {
            return fields.get(colSchema.name());
        }
        else return null;
    }
    public ColumnField field(int index){
        return fields()[index];
    }





}
