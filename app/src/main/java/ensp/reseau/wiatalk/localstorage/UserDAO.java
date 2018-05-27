package ensp.reseau.wiatalk.localstorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ensp.reseau.wiatalk.model.User;

/**
 * Created by Sim'S on 17/05/2018.
 */

public class UserDAO {
    private SQLiteDatabase database;
    private DatabaseHandler handler;

    public UserDAO(Context context){
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

    private ContentValues userContentValues(User user, boolean isMe){
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.DB_USERS__ID, user.get_Id());
        values.put(DatabaseHandler.DB_USERS__MOBILE, user.getMobile());
        values.put(DatabaseHandler.DB_USERS__PSEUDO, user.getPseudo());
        values.put(DatabaseHandler.DB_USERS__PP, user.getPp());
        values.put(DatabaseHandler.DB_USERS__PP_PATH, user.getPpPath());
        values.put(DatabaseHandler.DB_USERS__ACTIVE, user.isActive()?1:0);
        values.put(DatabaseHandler.DB_USERS__PP_TIMESTAMP, user.getPp_change_timestamp());
        if (isMe) values.put(DatabaseHandler.DB_USERS__ISME, 1);
        return values;
    }

    private ArrayList<User> cursorToUsers(Cursor cursor){
        if (cursor.getCount()==0) return null;
        ArrayList<User> users = new ArrayList<>();
        int i = 0;
        while (cursor.moveToPosition(i)){
            User user = new User();
            user.setId(cursor.getString(0));
            user.setMobile(cursor.getString(1));
            user.setPseudo(cursor.getString(2));
            user.setPp(cursor.getString(3));
            user.setPpPath(cursor.getString(4));
            user.setActive(cursor.getInt(5)==0?false:true);
            user.setPp_change_timestamp(Long.valueOf(cursor.getString(6)));
            users.add(user);
            i++;
        }
        return users;
    }

    public long storeUser(User user, boolean isMe){
        return database.insert(DatabaseHandler.DB_USERS, null, userContentValues(user, isMe));
    }

    public int updateUser(User user){
        return database.update(DatabaseHandler.DB_USERS, userContentValues(user, false), DatabaseHandler.DB_USERS__ID + " = '"+user.get_Id()+"'", null);
    }

    public int deleteUser(String userId){
        return database.delete(DatabaseHandler.DB_USERS, DatabaseHandler.DB_USERS__ID + " = '" + userId + "'", null);
    }

    public ArrayList<User> getAllUsers(){
        Cursor cursor = database.query(DatabaseHandler.DB_USERS, DatabaseHandler.DB_USERS_COLUMN, null, null, null, null, DatabaseHandler.DB_USERS__PSEUDO);
        return cursorToUsers(cursor);
    }

    public User getUserById(String userId){
        Cursor cursor = database.query(DatabaseHandler.DB_USERS, DatabaseHandler.DB_USERS_COLUMN, DatabaseHandler.DB_USERS__ID + " = '" + userId + "'", null, null, null, DatabaseHandler.DB_USERS__PSEUDO);
        ArrayList<User> res = cursorToUsers(cursor);
        return res==null?null:res.get(0);
    }

    public User getMe(){
        Cursor cursor = database.query(DatabaseHandler.DB_USERS, DatabaseHandler.DB_USERS_COLUMN, DatabaseHandler.DB_USERS__ISME + " = 1", null, null, null, DatabaseHandler.DB_USERS__PSEUDO);
        ArrayList<User> res = cursorToUsers(cursor);
        return res==null?null:res.get(0);
    }
}
