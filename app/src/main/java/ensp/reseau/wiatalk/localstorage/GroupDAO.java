package ensp.reseau.wiatalk.localstorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.ArrayList;

import ensp.reseau.wiatalk.model.Group;
import ensp.reseau.wiatalk.model.Group;

public class GroupDAO {
    private SQLiteDatabase database;
    private DatabaseHandler handler;

    public GroupDAO(Context context){
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

    private ContentValues groupContentValues(Group group){
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.DB_GROUPS__ID, group.get_id());
        values.put(DatabaseHandler.DB_GROUPS__NAME, group.getName());
        values.put(DatabaseHandler.DB_GROUPS__TYPE, group.getType());
        values.put(DatabaseHandler.DB_GROUPS__PP, group.getPp());
        if (group.getPpPath()!=null && !group.getPpPath().isEmpty()) values.put(DatabaseHandler.DB_GROUPS__PP_PATH, group.getPpPath());
        values.put(DatabaseHandler.DB_GROUPS__CREATOR_ID, group.getCreatorId());
        values.put(DatabaseHandler.DB_GROUPS__CREATION_TIMESTAMP, String.valueOf(group.getCreation_date()));
        values.put(DatabaseHandler.DB_GROUPS__PP_TIMESTAMP, group.getPp_change_timestamp());
        values.put(DatabaseHandler.DB_GROUPS__OLD_PP_TIMESTAMP, ""+group.getOld_pp_change_timestamp());
        return values;
    }

    private ArrayList<Group> cursorToGroups(Cursor cursor){
        if (cursor.getCount()==0) return null;
        ArrayList<Group> groups = new ArrayList<>();
        int i = 0;
        while (cursor.moveToPosition(i)){
            Group group = new Group();
            group.set_id(cursor.getString(0));
            group.setName(cursor.getString(1));
            group.setType(cursor.getInt(2));
            group.setPp(cursor.getString(3));
            group.setPpPath(cursor.getString(4));
            group.setCreatorId(cursor.getString(5));
            group.setCreation_date(Long.valueOf(cursor.getString(6)));
            group.setPp_change_timestamp(Long.valueOf(cursor.getString(7)));
            group.setOld_pp_change_timestamp(Long.valueOf(cursor.getString(8)));
            groups.add(group);
            i++;
        }
        return groups;
    }

    public long storeGroup(Group group){
        return database.insert(DatabaseHandler.DB_GROUPS, null, groupContentValues(group));
    }

    public int updateGroup(Group group){
        return database.update(DatabaseHandler.DB_GROUPS, groupContentValues(group), DatabaseHandler.DB_GROUPS__ID + " = '"+group.get_id()+"'", null);
    }

    public int deleteGroup(String groupId){
        return database.delete(DatabaseHandler.DB_GROUPS, DatabaseHandler.DB_GROUPS__ID + " = '" + groupId + "'", null);
    }

    public ArrayList<Group> getAllGroups(){
        Cursor cursor = database.query(DatabaseHandler.DB_GROUPS, DatabaseHandler.DB_GROUPS_COLUMNS, null, null, null, null, DatabaseHandler.DB_GROUPS__NAME);
        return cursorToGroups(cursor);
    }

    public Group getGroupById(String groupId){
        Cursor cursor = database.query(DatabaseHandler.DB_GROUPS, DatabaseHandler.DB_GROUPS_COLUMNS, DatabaseHandler.DB_GROUPS__ID + " = '" + groupId + "'", null, null, null, DatabaseHandler.DB_GROUPS__NAME);
        ArrayList<Group> res = cursorToGroups(cursor);
        return res==null?null:res.get(0);
    }

    public String getIb(String me, String userId){
        Cursor cursor_me = database.query(DatabaseHandler.DB_USERS_GROUPS, new String[]{DatabaseHandler.DB_USERS_GROUPS__GROUP}, DatabaseHandler.DB_USERS_GROUPS__USER + " = '"+me+"'", null, null, null, null);
        String in = "";
        int i=0;
        if (cursor_me.getCount()>0){
            in = in + "(";
            while (cursor_me.moveToPosition(i)){
                in = in + "'" + cursor_me.getString(0) + "', ";
                i++;
            }
            in = in.substring(0, in.length()-2);
            in = in + ")";
        }
        if (in.isEmpty()) return null;
        Cursor cursor_ib = database.query(DatabaseHandler.DB_USERS_GROUPS, new String[]{DatabaseHandler.DB_USERS_GROUPS__GROUP}, DatabaseHandler.DB_USERS_GROUPS__USER +" = '"+userId+"' AND " + DatabaseHandler.DB_USERS_GROUPS__GROUP + " IN " + in, null, null, null, null);
        i=0;
        if (cursor_ib.getCount()==0) return null;
        else {
            cursor_ib.moveToPosition(0);
            return cursor_ib.getString(0);
        }
    }

    public ArrayList<String> getCommonGroups(String memberId){
        Cursor cursor = database.query(DatabaseHandler.DB_USERS_GROUPS, new String[]{DatabaseHandler.DB_USERS_GROUPS__GROUP}, DatabaseHandler.DB_USERS_GROUPS__USER + " = '" + memberId + "'", null, null, null, null);
        if (cursor.getCount()==0) return null;
        ArrayList<String> groupsIds = new ArrayList<>();
        int i=0;
        while (cursor.moveToPosition(i)){
            groupsIds.add(cursor.getString(0));
            i++;
        }
        return groupsIds;
    }
}
