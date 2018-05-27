package ensp.reseau.wiatalk.localstorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ensp.reseau.wiatalk.model.Message;

public class MessageDAO {
    private SQLiteDatabase database;
    private DatabaseHandler handler;

    public MessageDAO(Context context){
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

    private ContentValues messagesContentValues(Message message){
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.DB_MESSAGE__ID, message.get_id());
        values.put(DatabaseHandler.DB_MESSAGE__SENDER, message.getSenderId());
        values.put(DatabaseHandler.DB_MESSAGE__SEND_TIMESTAMP, String.valueOf(message.getSend_timestamp()));
        values.put(DatabaseHandler.DB_MESSAGE__SERVER_RECEPTION_TIMESTAMP, String.valueOf(message.getSend_timestamp()));
        values.put(DatabaseHandler.DB_MESSAGE__MY_RECEPTION_TIMESTAMP, String.valueOf(message.getMyReceptionTimestamp()));
        values.put(DatabaseHandler.DB_MESSAGE__GROUP_ID, String.valueOf(message.getGroupId()));
        values.put(DatabaseHandler.DB_MESSAGE__STATUS, message.getStatus());
        values.put(DatabaseHandler.DB_MESSAGE__TEXT, message.getText());
        values.put(DatabaseHandler.DB_MESSAGE__MESSAGE_FILE_ID, String.valueOf(message.getFileId()));
        values.put(DatabaseHandler.DB_MESSAGE__VOICENOTE_URL, String.valueOf(message.getVoicenote()));
        values.put(DatabaseHandler.DB_MESSAGE__VOICENOTE_LOCAL_PATH, String.valueOf(message.getVoiceNotePath()));
        values.put(DatabaseHandler.DB_MESSAGE__ISNEW, message.isNew()?1:0);
        return values;
    }

    private ArrayList<Message> cursorToMessage(Cursor cursor){
        if (cursor.getCount()==0) return null;
        ArrayList<Message> messages = new ArrayList<>();
        int i = 0;
        while (cursor.moveToPosition(i)){
            Message message = new Message();
            message.set_id(cursor.getString(0));
            message.setSenderId(cursor.getString(1));
            message.setSend_timestamp(Long.valueOf(cursor.getString(2)));
            message.setServer_reception_timestamp(Long.valueOf(cursor.getString(3)));
            message.setMyReceptionTimestamp(Long.valueOf(cursor.getString(4)));
            message.setGroupId(cursor.getString(5));
            message.setStatus(cursor.getInt(6));
            message.setText(cursor.getString(7));
            message.setFileId(cursor.getString(8));
            message.setVoicenote(cursor.getString(9));
            message.setVoiceNotePath(cursor.getString(10));
            message.setNew(cursor.getInt(11)==1?true:false);
            messages.add(message);
        }
        return messages;
    }

    public long storeMessage(Message message){
        return database.insert(DatabaseHandler.DB_MESSAGE, null, messagesContentValues(message));
    }

    public int updateMessage(Message message){
        return database.update(DatabaseHandler.DB_MESSAGE, messagesContentValues(message),DatabaseHandler.DB_MESSAGE__ID + " = '" + message.get_id() + "'", null);
    }

    public int deleteMessage(String messageId){
        return database.delete(DatabaseHandler.DB_MESSAGE, DatabaseHandler.DB_MESSAGE__ID + " = '" + messageId + "'", null);
    }

    public ArrayList<Message> getAllMessage(){
        Cursor cursor = database.query(DatabaseHandler.DB_MESSAGE, DatabaseHandler.DB_MESSAGE_COLUMNS, null, null, null, null, DatabaseHandler.DB_MESSAGE__MY_RECEPTION_TIMESTAMP);
        return cursorToMessage(cursor);
    }

    public ArrayList<Message> getGroupsMessages(String groupId){
        Cursor cursor = database.query(DatabaseHandler.DB_MESSAGE, DatabaseHandler.DB_MESSAGE_COLUMNS, DatabaseHandler.DB_MESSAGE__GROUP_ID + " = '"+groupId+"'", null, null, null, DatabaseHandler.DB_MESSAGE__MY_RECEPTION_TIMESTAMP + " DESC");
        return cursorToMessage(cursor);
    }

    public Message getMessagesById(String messageId){
        Cursor cursor = database.query(DatabaseHandler.DB_MESSAGE, DatabaseHandler.DB_MESSAGE_COLUMNS, DatabaseHandler.DB_MESSAGE__ID + " = '" + messageId+ "'", null, null, null, DatabaseHandler.DB_MESSAGE__MY_RECEPTION_TIMESTAMP + " DESC");
        ArrayList<Message> res = cursorToMessage(cursor);
        return res==null?null:res.get(0);
    }

    public Message getLastGroupMessage(String groupId){
        Cursor cursor = database.query(DatabaseHandler.DB_MESSAGE, DatabaseHandler.DB_MESSAGE_COLUMNS, DatabaseHandler.DB_MESSAGE__GROUP_ID + " = '"+groupId+"'", null, null, null, DatabaseHandler.DB_MESSAGE__MY_RECEPTION_TIMESTAMP + " DESC");
        ArrayList<Message> res = cursorToMessage(cursor);
        return res==null?null:res.get(0);
    }

    public ArrayList<Message> getGroupNewMessages(String groupId){
        Cursor cursor = database.query(DatabaseHandler.DB_MESSAGE, DatabaseHandler.DB_MESSAGE_COLUMNS, DatabaseHandler.DB_MESSAGE__GROUP_ID + " = '"+groupId+"' AND " +
                DatabaseHandler.DB_MESSAGE__ISNEW + " = 1", null, null, null, DatabaseHandler.DB_MESSAGE__MY_RECEPTION_TIMESTAMP + " DESC");
        return cursorToMessage(cursor);
    }
}
