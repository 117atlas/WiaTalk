package ensp.reseau.wiatalk.localstorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ensp.reseau.wiatalk.model.IPCall;

public class IPCallsDAO {
    private SQLiteDatabase database;
    private DatabaseHandler handler;

    public IPCallsDAO(Context context){
        handler = new DatabaseHandler(context);
    }
    public void open(){
        database = handler.getWritableDatabase();
    }
    public void close(){
        database.close();
    }
    public SQLiteDatabase getDatabase(){
        return database;
    }

    private ContentValues callsContentValues(IPCall call){
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.DB_IPCALLS__ID, call.get_id());
        values.put(DatabaseHandler.DB_IPCALLS__CALLER, call.getCallerId());
        values.put(DatabaseHandler.DB_IPCALLS__CALLED, String.valueOf(call.getCalledId()));
        values.put(DatabaseHandler.DB_IPCALLS__CALL_TIMESTAMP, String.valueOf(call.getCall_timestamp()));
        values.put(DatabaseHandler.DB_IPCALLS__DURATION, String.valueOf(call.getDuration()));
        values.put(DatabaseHandler.DB_IPCALLS__TYPE, String.valueOf(call.getType()));
        values.put(DatabaseHandler.DB_IPCALLS__MODE, String.valueOf(call.getMode()));
        return values;
    }

    private ArrayList<IPCall> cursorToIPCall(Cursor cursor){
        if (cursor.getCount()==0) return null;
        ArrayList<IPCall> calls = new ArrayList<>();
        int i = 0;
        while (cursor.moveToPosition(i)){
            IPCall ipcall = new IPCall();
            ipcall.set_id(cursor.getString(0));
            ipcall.setCallerId(cursor.getString(1));
            ipcall.setCalledId(cursor.getString(2));
            ipcall.setCall_timestamp(Long.valueOf(cursor.getString(3)));
            ipcall.setDuration(cursor.getInt(4));
            ipcall.setType(cursor.getInt(5));
            ipcall.setMode(cursor.getInt(6));
            calls.add(ipcall);
        }
        return calls;
    }

    public long storeIPCall(IPCall call){
        return database.insert(DatabaseHandler.DB_IPCALLS, null, callsContentValues(call));
    }

    public int updateIPCall(IPCall call){
        return database.update(DatabaseHandler.DB_IPCALLS, callsContentValues(call),DatabaseHandler.DB_IPCALLS__ID + " = '" + call.get_id() + "'", null);
    }

    public int deleteIPCall(String ipcallId){
        return database.delete(DatabaseHandler.DB_IPCALLS, DatabaseHandler.DB_IPCALLS__ID + " = '" + ipcallId + "'", null);
    }

    public ArrayList<IPCall> getAllIPCall(){
        Cursor cursor = database.query(DatabaseHandler.DB_IPCALLS, DatabaseHandler.DB_IPCALLS_COLUMNS, null, null, null, null, DatabaseHandler.DB_IPCALLS__CALL_TIMESTAMP);
        return cursorToIPCall(cursor);
    }

    public IPCall getIPCallsById(String ipcallId){
        Cursor cursor = database.query(DatabaseHandler.DB_IPCALLS, DatabaseHandler.DB_IPCALLS_COLUMNS, DatabaseHandler.DB_IPCALLS__ID + " = '" + ipcallId+ "'", null, null, null, DatabaseHandler.DB_IPCALLS__CALL_TIMESTAMP);
        ArrayList<IPCall> res = cursorToIPCall(cursor);
        return res==null?null:res.get(0);
    }
}
