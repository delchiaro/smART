package micc.beaconav.indoorEngine.databaseLite;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

import micc.beaconav.indoorEngine.building.Door;

/**
 * Created by Nagash on 25/10/2015.
 */
final public class CursorAdapterNullValue  {


    private final Cursor cursor;

    public CursorAdapterNullValue(Cursor cursor) {
        this.cursor = cursor;
    }


    final public int getCount() {
        return cursor.getCount();
    }

    
    final public int getPosition() {
        return cursor.getPosition();
    }

    
    final public boolean move(int i) {
        return cursor.move(i);
    }

    
    final public boolean moveToPosition(int i) {
        return cursor.moveToPosition(i);
    }

    
    final public boolean moveToFirst() {
        return cursor.moveToFirst();
    }

    
    final public boolean moveToLast() {
        return cursor.moveToLast();
    }

    
    final public boolean moveToNext() {
        return cursor.moveToNext();
    }

    
    final public boolean moveToPrevious() {
        return cursor.moveToPrevious();
    }

    
    final public boolean isFirst() {
        return cursor.isFirst();
    }

    
    final public boolean isLast() {
        return cursor.isLast();
    }

    
    final public boolean isBeforeFirst() {
        return cursor.isBeforeFirst();
    }

    
    final public boolean isAfterLast() {
        return cursor.isAfterLast();
    }

    
    final public int getColumnIndex(String s) {
        return cursor.getColumnIndex(s);
    }

    
    final public int getColumnIndexOrThrow(String s) throws IllegalArgumentException {
        return cursor.getColumnIndexOrThrow(s);
    }

    
    final public String getColumnName(int i) {
        return cursor.getColumnName(i);
    }

    
    final public String[] getColumnNames() {
        return cursor.getColumnNames();
    }

    
    final public int getColumnCount() {
        return cursor.getColumnCount();
    }

    
    final public byte[] getBlob(int i) {
        return cursor.getBlob(i);
    }


    final public String getString(int i) {
        return cursor.getString(i);
    }

    
    final public void copyStringToBuffer(int i, CharArrayBuffer charArrayBuffer) {
        cursor.copyStringToBuffer(i, charArrayBuffer);
    }

    
    final public Short getShort(int i) {
        if(cursor.isNull(i))
            return null;
        else return cursor.getShort(i);
    }

    
    final public Integer getInt(int i) {
        if(cursor.isNull(i))
            return null;
        else return cursor.getInt(i);
    }

    
    final public Long getLong(int i) {
        if(cursor.isNull(i))
            return null;
        else return cursor.getLong(i);
    }

    
    final public Float getFloat(int i) {
        if(cursor.isNull(i))
            return null;
        else return cursor.getFloat(i);
    }

    
    final public Double getDouble(int i) {
        if(cursor.isNull(i))
            return null;
        else return cursor.getDouble(i);
    }

    
    final public int getType(int i) {
        return cursor.getType(i);
    }

    
    final public boolean isNull(int i) {
        return cursor.isNull(i);
    }

    @Deprecated
    final public void deactivate() {
        cursor.deactivate();
    }

    @Deprecated
    final public boolean requery() {
        return cursor.requery();
    }

    
    final public void close() {
        cursor.close();
    }

    
    final public boolean isClosed() {
        return cursor.isClosed();
    }

    
    final public void registerContentObserver(ContentObserver contentObserver) {
        cursor.registerContentObserver(contentObserver);
    }

    
    final public void unregisterContentObserver(ContentObserver contentObserver) {
        cursor.unregisterContentObserver(contentObserver);
    }

    
    final public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        cursor.registerDataSetObserver(dataSetObserver);
    }

    
    final public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        cursor.unregisterDataSetObserver(dataSetObserver);
    }

    
    final public void setNotificationUri(ContentResolver contentResolver, Uri uri) {
        cursor.setNotificationUri(contentResolver,uri);
    }

    
    final public Uri getNotificationUri() {
        return cursor.getNotificationUri();
    }

    
    final public boolean getWantsAllOnMoveCalls() {
        return cursor.getWantsAllOnMoveCalls();
    }

    
    final public Bundle getExtras() {
        return cursor.getExtras();
    }

    
    final public Bundle respond(Bundle bundle) {
        return cursor.respond(bundle);
    }
}
