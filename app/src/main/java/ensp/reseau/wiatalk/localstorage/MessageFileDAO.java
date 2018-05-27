package ensp.reseau.wiatalk.localstorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ensp.reseau.wiatalk.model.MessageFile;

public class MessageFileDAO {
    private SQLiteDatabase database;
    private DatabaseHandler handler;

    public MessageFileDAO(Context context){
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

    private ContentValues messageFilesContentValues(MessageFile messageFile){
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.DB_MESSAGE_FILES__ID, messageFile.get_id());
        values.put(DatabaseHandler.DB_MESSAGE_FILES__TYPE, messageFile.getType());
        values.put(DatabaseHandler.DB_MESSAGE_FILES__URL, String.valueOf(messageFile.getUrl()));
        values.put(DatabaseHandler.DB_MESSAGE_FILES__LOCAL_PATH, String.valueOf(messageFile.getLocalPath()));
        return values;
    }

    private ArrayList<MessageFile> cursorToMessageFile(Cursor cursor){
        if (cursor.getCount()==0) return null;
        ArrayList<MessageFile> messageFiles = new ArrayList<>();
        int i = 0;
        while (cursor.moveToPosition(i)){
            MessageFile mf = new MessageFile();
            mf.set_id(cursor.getString(0));
            mf.setType(cursor.getInt(1));
            mf.setUrl(cursor.getString(2));
            mf.setLocalPath(cursor.getString(3));
            messageFiles.add(mf);
        }
        return messageFiles;
    }

    public long storeMessageFile(MessageFile messageFile){
        return database.insert(DatabaseHandler.DB_MESSAGE_FILES, null, messageFilesContentValues(messageFile));
    }

    public int updateMessageFile(MessageFile messageFile){
        return database.update(DatabaseHandler.DB_MESSAGE_FILES, messageFilesContentValues(messageFile),DatabaseHandler.DB_MESSAGE_FILES__ID + " = '" + messageFile.get_id() + "'", null);
    }

    public int deleteMessageFile(String mfId){
        return database.delete(DatabaseHandler.DB_MESSAGE_FILES, DatabaseHandler.DB_MESSAGE_FILES__ID + " = '" + mfId + "'", null);
    }

    public ArrayList<MessageFile> getAllMessageFile(){
        Cursor cursor = database.query(DatabaseHandler.DB_MESSAGE_FILES, DatabaseHandler.DB_MESSAGE_FILES_COLUMNS, null, null, null, null, null);
        return cursorToMessageFile(cursor);
    }

    public MessageFile getMessageFileById(String mfId){
        Cursor cursor = database.query(DatabaseHandler.DB_MESSAGE_FILES, DatabaseHandler.DB_MESSAGE_FILES_COLUMNS, DatabaseHandler.DB_MESSAGE_FILES__ID + " = '" + mfId+ "'", null, null, null, null);
        ArrayList<MessageFile> res = cursorToMessageFile(cursor);
        return res==null?null:res.get(0);
    }
}
