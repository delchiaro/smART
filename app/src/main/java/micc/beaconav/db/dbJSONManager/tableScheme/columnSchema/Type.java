package micc.beaconav.db.dbJSONManager.tableScheme.columnSchema;

/**
 * Created by nagash on 21/01/15.
 */
public enum Type
{
    INT, LONG,  FLOAT, DOUBLE,  STRING, BOOL;




    public Object getInitValue(){
        return Type.getInitValue(this);
    }

    public Object fromString(String value){
        return stringToValue(this, value);
    }

    public static Object stringToValue(Type type, String value)
    {
        switch(type)
        {
            case INT: return Integer.parseInt(value);
            case LONG: return Long.parseLong(value);
            case FLOAT: return Float.parseFloat(value);
            case DOUBLE: return Double.parseDouble(value);
            case BOOL: return Boolean.parseBoolean(value);
            case STRING: return value;
        }
        return null;
    }


    public static Object getInitValue(Type type ){
        switch(type)
        {
            case STRING:    return new String();
            case INT:       return new Integer(0);
            case LONG:      return new Long(0);
            case FLOAT:     return new Float(0);
            case DOUBLE:    return new Double(0);
            case BOOL:      return new Boolean(false);
        }
        return null;
    }

}

