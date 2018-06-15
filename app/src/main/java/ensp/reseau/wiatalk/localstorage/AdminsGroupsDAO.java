package ensp.reseau.wiatalk.localstorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ensp.reseau.wiatalk.model.AdminsGroups;
import ensp.reseau.wiatalk.model.Group;

public class AdminsGroupsDAO {
    private SQLiteDatabase database;
    private DatabaseHandler handler;

    public AdminsGroupsDAO(Context context){
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

    private ContentValues adminsgroupsContentValues(AdminsGroups adminsGroups){
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.DB_ADMINS_GROUPS__USER, adminsGroups.getMemberId());
        values.put(DatabaseHandler.DB_ADMINS_GROUPS__GROUP, adminsGroups.getGroupId());
        values.put(DatabaseHandler.DB_ADMINS_GROUPS__NOMINATION_DATE, String.valueOf(adminsGroups.getNomination_date()));
        return values;
    }

    private ArrayList<AdminsGroups> cursorToAdminsGroups(Cursor cursor){
        if (cursor.getCount()==0) return null;
        ArrayList<AdminsGroups> adminsGroups = new ArrayList<>();
        int i = 0;
        while (cursor.moveToPosition(i)){
            AdminsGroups ag = new AdminsGroups();
            ag.setAdminId(cursor.getString(0));
            ag.setGroupId(cursor.getString(1));
            ag.setNomination_date(Long.valueOf(cursor.getString(2)));
            adminsGroups.add(ag);
            i++;
        }
        return adminsGroups;
    }

    public long storeAdminGroup(AdminsGroups adminsGroups){
        return database.insert(DatabaseHandler.DB_ADMINS_GROUPS, null, adminsgroupsContentValues(adminsGroups));
    }

    public int deleteAdminGroup(String userId, String groupId){
        return database.delete(DatabaseHandler.DB_ADMINS_GROUPS, DatabaseHandler.DB_ADMINS_GROUPS__USER + " = '" + userId + "' AND " +
                DatabaseHandler.DB_ADMINS_GROUPS__GROUP + " = '"+groupId+"'", null);
    }

    public ArrayList<AdminsGroups> getAllGroups(){
        Cursor cursor = database.query(DatabaseHandler.DB_ADMINS_GROUPS, DatabaseHandler.DB_ADMINS_GROUPS_COLUMNS, null, null, null, null, null);
        return cursorToAdminsGroups(cursor);
    }

    public ArrayList<AdminsGroups> getGroupAdmins(String groupId){
        Cursor cursor = database.query(DatabaseHandler.DB_ADMINS_GROUPS, DatabaseHandler.DB_ADMINS_GROUPS_COLUMNS, DatabaseHandler.DB_ADMINS_GROUPS__GROUP + " = '" + groupId + "'", null, null, null, null);
        return cursorToAdminsGroups(cursor);
    }

    public AdminsGroups getAdminsGroupsByIds(String userId, String groupId){
        Cursor cursor = database.query(DatabaseHandler.DB_ADMINS_GROUPS, DatabaseHandler.DB_ADMINS_GROUPS_COLUMNS, DatabaseHandler.DB_ADMINS_GROUPS__USER + " = '" + userId + "' AND " +
                DatabaseHandler.DB_ADMINS_GROUPS__GROUP + " = '"+groupId+"'", null, null, null, null);
        ArrayList<AdminsGroups> res = cursorToAdminsGroups(cursor);
        return res==null?null:res.get(0);
    }

    public void deleteAllForGroup(String groupId){
        database.delete(DatabaseHandler.DB_ADMINS_GROUPS, DatabaseHandler.DB_ADMINS_GROUPS__GROUP + " = '" + groupId + "'", null);
    }
}
