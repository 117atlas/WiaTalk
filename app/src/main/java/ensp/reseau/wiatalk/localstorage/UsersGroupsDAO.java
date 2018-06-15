package ensp.reseau.wiatalk.localstorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ensp.reseau.wiatalk.model.UsersGroups;

public class UsersGroupsDAO {
    private SQLiteDatabase database;
    private DatabaseHandler handler;

    public UsersGroupsDAO(Context context){
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

    private ContentValues usersgroupsContentValues(UsersGroups usersGroups){
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.DB_USERS_GROUPS__USER, usersGroups.getMemberId());
        values.put(DatabaseHandler.DB_USERS_GROUPS__GROUP, usersGroups.getGroupId());
        values.put(DatabaseHandler.DB_USERS_GROUPS__ENTRANCE_DATE, String.valueOf(usersGroups.getEntrance_date()));
        values.put(DatabaseHandler.DB_USERS_GROUPS__EXIT_DATE, String.valueOf(usersGroups.getExit_date()));
        values.put(DatabaseHandler.DB_USERS_GROUPS__ISINGROUP, usersGroups.isIs_in_group()?1:0);
        return values;
    }

    private ArrayList<UsersGroups> cursorToUsersGroups(Cursor cursor){
        if (cursor.getCount()==0) return null;
        ArrayList<UsersGroups> usersGroups = new ArrayList<>();
        int i = 0;
        while (cursor.moveToPosition(i)){
            UsersGroups ug = new UsersGroups();
            ug.setMemberId(cursor.getString(0));
            ug.setGroupId(cursor.getString(1));
            ug.setEntrance_date(Long.valueOf(cursor.getString(2)));
            ug.setExit_date(Long.valueOf(cursor.getString(3)));
            ug.setIs_in_group(cursor.getInt(4)==1?true:false);
            usersGroups.add(ug);
            i++;
        }
        return usersGroups;
    }

    public long storeUserGroup(UsersGroups usersGroups){
        return database.insert(DatabaseHandler.DB_USERS_GROUPS, null, usersgroupsContentValues(usersGroups));
    }

    public int updateUserGroup(UsersGroups usersGroups){
        return database.update(DatabaseHandler.DB_USERS_GROUPS, usersgroupsContentValues(usersGroups),DatabaseHandler.DB_USERS_GROUPS__USER + " = '" + usersGroups.getMemberId() + "' AND " +
                DatabaseHandler.DB_USERS_GROUPS__GROUP + " = '"+usersGroups.getGroupId()+"'", null);
    }

    public int deleteUserGroup(String userId, String groupId){
        return database.delete(DatabaseHandler.DB_USERS_GROUPS, DatabaseHandler.DB_USERS_GROUPS__USER + " = '" + userId + "' AND " +
                DatabaseHandler.DB_USERS_GROUPS__GROUP + " = '"+groupId+"'", null);
    }

    public ArrayList<UsersGroups> getAllUsersGroups(){
        Cursor cursor = database.query(DatabaseHandler.DB_USERS_GROUPS, DatabaseHandler.DB_USERS_GROUPS_COLUMNS, null, null, null, null, null);
        return cursorToUsersGroups(cursor);
    }

    public ArrayList<UsersGroups> getGroupMembersIds(String groupId){
        Cursor cursor = database.query(DatabaseHandler.DB_USERS_GROUPS, DatabaseHandler.DB_USERS_GROUPS_COLUMNS, DatabaseHandler.DB_USERS_GROUPS__GROUP + " = '" + groupId + "'", null, null, null, null);
        return cursorToUsersGroups(cursor);
    }

    public ArrayList<UsersGroups> getUserGroupsIds(String userId){
        Cursor cursor = database.query(DatabaseHandler.DB_USERS_GROUPS, DatabaseHandler.DB_USERS_GROUPS_COLUMNS, DatabaseHandler.DB_USERS_GROUPS__USER + " = '" + userId + "'", null, null, null, null);
        return cursorToUsersGroups(cursor);
    }

    public UsersGroups getUsersGroupsByIds(String userId, String groupId){
        Cursor cursor = database.query(DatabaseHandler.DB_USERS_GROUPS, DatabaseHandler.DB_USERS_GROUPS_COLUMNS, DatabaseHandler.DB_USERS_GROUPS__USER + " = '" + userId + "' AND " +
                DatabaseHandler.DB_USERS_GROUPS__GROUP + " = '"+groupId+"'", null, null, null, null);
        ArrayList<UsersGroups> res = cursorToUsersGroups(cursor);
        return res==null?null:res.get(0);
    }

    public void deleteAllForGroup(String groupId){
        database.delete(DatabaseHandler.DB_USERS_GROUPS, DatabaseHandler.DB_USERS_GROUPS__GROUP + " = '" + groupId + "'", null);
    }
}
