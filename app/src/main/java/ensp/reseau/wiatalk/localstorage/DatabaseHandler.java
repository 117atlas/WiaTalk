package ensp.reseau.wiatalk.localstorage;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sim'S on 17/05/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;

    public static final String DB_NAME = "wiatalk_db";

    //Table USERS
    public static final String DB_USERS = "users";
    public static final String DB_USERS__ID = "_id";
    public static final String DB_USERS__MOBILE = "mobile";
    public static final String DB_USERS__PSEUDO = "pseudo";
    public static final String DB_USERS__PP = "pp";
    public static final String DB_USERS__PP_PATH = "pp_path";
    public static final String DB_USERS__ACTIVE = "active";
    public static final String DB_USERS__ISME = "isme";
    public static final String DB_USERS__PP_TIMESTAMP = "pp_change_timestamp";
    public static final String[] DB_USERS_COLUMN = {DB_USERS__ID, DB_USERS__MOBILE, DB_USERS__PSEUDO, DB_USERS__PP, DB_USERS__PP_PATH, DB_USERS__ACTIVE, DB_USERS__PP_TIMESTAMP};

    //Table GROUP
    public static final String DB_GROUPS = "groups";
    public static final String DB_GROUPS__ID = "_id";
    public static final String DB_GROUPS__NAME = "name";
    public static final String DB_GROUPS__TYPE = "type";
    public static final String DB_GROUPS__PP = "pp";
    public static final String DB_GROUPS__PP_PATH = "pp_path";
    public static final String DB_GROUPS__CREATOR_ID = "creator_id";
    public static final String DB_GROUPS__CREATION_TIMESTAMP = "creation_timestamp";
    public static final String DB_GROUPS__PP_TIMESTAMP = "pp_change_timestamp";
    public static final String[] DB_GROUPS_COLUMNS = {DB_GROUPS__ID, DB_GROUPS__NAME, DB_GROUPS__TYPE, DB_GROUPS__PP, DB_GROUPS__PP_PATH, DB_GROUPS__CREATOR_ID, DB_GROUPS__CREATION_TIMESTAMP, DB_GROUPS__PP_TIMESTAMP};

    //Table MESSAGES
    public static final String DB_MESSAGE = "messages";
    public static final String DB_MESSAGE__ID = "_id";
    public static final String DB_MESSAGE__SENDER = "sender";
    public static final String DB_MESSAGE__SEND_TIMESTAMP = "send_timestamp";
    public static final String DB_MESSAGE__SERVER_RECEPTION_TIMESTAMP = "server_reception_timestamp";
    public static final String DB_MESSAGE__MY_RECEPTION_TIMESTAMP = "my_reception_timestamp";
    public static final String DB_MESSAGE__GROUP_ID = "group_id";
    public static final String DB_MESSAGE__STATUS = "status";
    public static final String DB_MESSAGE__TEXT = "text";
    public static final String DB_MESSAGE__MESSAGE_FILE_ID = "file_id";
    public static final String DB_MESSAGE__VOICENOTE_URL = "voicenote";
    public static final String DB_MESSAGE__VOICENOTE_LOCAL_PATH = "voicenote_path";
    public static final String DB_MESSAGE__RECEIVED = "received";
    public static final String DB_MESSAGE__READ = "read";
    public static final String DB_MESSAGE__ISNEW = "isnew";
    public static final String[] DB_MESSAGE_COLUMNS = {DB_MESSAGE__ID, DB_MESSAGE__SENDER, DB_MESSAGE__SEND_TIMESTAMP, DB_MESSAGE__SERVER_RECEPTION_TIMESTAMP,
            DB_MESSAGE__MY_RECEPTION_TIMESTAMP, DB_MESSAGE__GROUP_ID, DB_MESSAGE__STATUS, DB_MESSAGE__TEXT, DB_MESSAGE__MESSAGE_FILE_ID,
            DB_MESSAGE__VOICENOTE_URL, DB_MESSAGE__VOICENOTE_LOCAL_PATH, DB_MESSAGE__RECEIVED, DB_MESSAGE__READ, DB_MESSAGE__ISNEW};

    //Table Users-Groups
    public static final String DB_USERS_GROUPS = "users_groups";
    public static final String DB_USERS_GROUPS__USER = "member_id";
    public static final String DB_USERS_GROUPS__GROUP = "group_id";
    public static final String DB_USERS_GROUPS__ENTRANCE_DATE = "entrance_date";
    public static final String DB_USERS_GROUPS__EXIT_DATE = "exit_date";
    public static final String DB_USERS_GROUPS__ISINGROUP = "is_in_group";
    public static final String[] DB_USERS_GROUPS_COLUMNS = {DB_USERS_GROUPS__USER, DB_USERS_GROUPS__GROUP, DB_USERS_GROUPS__ENTRANCE_DATE,
            DB_USERS_GROUPS__EXIT_DATE, DB_USERS_GROUPS__ISINGROUP};

    //Table Admins-Groups
    public static final String DB_ADMINS_GROUPS = "admins_groups";
    public static final String DB_ADMINS_GROUPS__USER = "admin_id";
    public static final String DB_ADMINS_GROUPS__GROUP = "group_id";
    public static final String DB_ADMINS_GROUPS__NOMINATION_DATE = "nomination_date";
    public static final String[] DB_ADMINS_GROUPS_COLUMNS = {DB_ADMINS_GROUPS__USER, DB_ADMINS_GROUPS__GROUP, DB_ADMINS_GROUPS__NOMINATION_DATE};

    //Table Fichiers
    public static final String DB_MESSAGE_FILES = "messages_files";
    public static final String DB_MESSAGE_FILES__ID = "_id";
    public static final String DB_MESSAGE_FILES__TYPE = "type";
    public static final String DB_MESSAGE_FILES__URL = "url";
    public static final String DB_MESSAGE_FILES__LOCAL_PATH = "path";
    public static final String[] DB_MESSAGE_FILES_COLUMNS = {DB_MESSAGE_FILES__ID, DB_MESSAGE_FILES__TYPE, DB_MESSAGE_FILES__URL, DB_MESSAGE_FILES__LOCAL_PATH};

    //Table Calls
    public static final String DB_IPCALLS = "calls";
    public static final String DB_IPCALLS__ID = "_id";
    public static final String DB_IPCALLS__CALLER = "caller";
    public static final String DB_IPCALLS__CALLED = "called";
    public static final String DB_IPCALLS__CALL_TIMESTAMP = "call_timestamp";
    public static final String DB_IPCALLS__DURATION = "duration";
    public static final String DB_IPCALLS__TYPE = "type";
    public static final String DB_IPCALLS__MODE = "mode";
    public static final String[] DB_IPCALLS_COLUMNS = {DB_IPCALLS__ID, DB_IPCALLS__CALLER, DB_IPCALLS__CALLED, DB_IPCALLS__CALL_TIMESTAMP,
            DB_IPCALLS__DURATION, DB_IPCALLS__TYPE, DB_IPCALLS__MODE};


    //Create Tables scripts
    public static final String CREATE_USERS  = "CREATE TABLE IF NOT EXISTS " + DB_USERS + " (" +
            DB_USERS__ID + " TEXT, " +
            DB_USERS__MOBILE + " TEXT, " +
            DB_USERS__PSEUDO + " TEXT, " +
            DB_USERS__PP + " TEXT, " +
            DB_USERS__PP_PATH + " TEXT, " +
            DB_USERS__ACTIVE + " INT, " +
            DB_USERS__ISME + " INT, " +
            DB_USERS__PP_TIMESTAMP + " TEXT" +
            ")";

    public static final String CREATE_GROUPS = "CREATE TABLE IF NOT EXISTS " + DB_GROUPS + " (" +
            DB_GROUPS__ID + " TEXT, " +
            DB_GROUPS__NAME + " TEXT, " +
            DB_GROUPS__TYPE + " INT, " +
            DB_GROUPS__PP + " TEXT, " +
            DB_GROUPS__PP_PATH + " TEXT, " +
            DB_GROUPS__CREATOR_ID + " TEXT, " +
            DB_GROUPS__CREATION_TIMESTAMP + " TEXT, " +
            DB_USERS__PP_TIMESTAMP + " TEXT" +
            ")";

    public static final String CREATE_MESSAGES = "CREATE TABLE IF NOT EXISTS " + DB_MESSAGE + " (" +
            DB_MESSAGE__ID + " TEXT, " +
            DB_MESSAGE__SENDER + " TEXT, " +
            DB_MESSAGE__SEND_TIMESTAMP + " TEXT, " +
            DB_MESSAGE__SERVER_RECEPTION_TIMESTAMP + " TEXT, " +
            DB_MESSAGE__MY_RECEPTION_TIMESTAMP + " TEXT, " +
            DB_MESSAGE__GROUP_ID + " TEXT, " +
            DB_MESSAGE__STATUS + " INT, " +
            DB_MESSAGE__TEXT + " TEXT, " +
            DB_MESSAGE__MESSAGE_FILE_ID + " TEXT, " +
            DB_MESSAGE__VOICENOTE_URL + " TEXT, " +
            DB_MESSAGE__VOICENOTE_LOCAL_PATH + " TEXT, " +
            DB_MESSAGE__RECEIVED + " TEXT, " +
            DB_MESSAGE__READ + " TEXT, " +
            DB_MESSAGE__ISNEW + " INT" +
            ")";

    public static final String CREATE_USERS_GROUPS = "CREATE TABLE IF NOT EXISTS " + DB_USERS_GROUPS + " (" +
            DB_USERS_GROUPS__GROUP + " TEXT, " +
            DB_USERS_GROUPS__USER + " TEXT, " +
            DB_USERS_GROUPS__ENTRANCE_DATE + " TEXT, " +
            DB_USERS_GROUPS__EXIT_DATE + " TEXT, " +
            DB_USERS_GROUPS__ISINGROUP + " INT" +
            ")";

    public static final String CREATE_ADMINS_GROUPS = "CREATE TABLE IF NOT EXISTS " + DB_ADMINS_GROUPS + " (" +
            DB_ADMINS_GROUPS__GROUP + " TEXT, " +
            DB_ADMINS_GROUPS__USER + " TEXT, " +
            DB_ADMINS_GROUPS__NOMINATION_DATE + " TEXT" +
            ")";

    public static final String CREATE_MESSAGE_FILES = "CREATE TABLE IF NOT EXISTS " + DB_MESSAGE_FILES + " (" +
            DB_MESSAGE_FILES__ID + " TEXT, " +
            DB_MESSAGE_FILES__TYPE + " INT, " +
            DB_MESSAGE_FILES__URL + " TEXT, " +
            DB_MESSAGE_FILES__LOCAL_PATH + " TEXT" +
            ")";

    public static final String CREATE_IPCALLS = "CREATE TABLE IF NOT EXISTS " + DB_IPCALLS + " (" +
            DB_IPCALLS__ID + " TEXT, " +
            DB_IPCALLS__CALLER + " TEXT, " +
            DB_IPCALLS__CALLED + " TEXT, " +
            DB_IPCALLS__CALL_TIMESTAMP + " TEXT, " +
            DB_IPCALLS__DURATION + " INT, " +
            DB_IPCALLS__TYPE + " INT, " +
            DB_IPCALLS__MODE + " INT" +
            ")";

    public DatabaseHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USERS);
        sqLiteDatabase.execSQL(CREATE_GROUPS);
        sqLiteDatabase.execSQL(CREATE_USERS_GROUPS);
        sqLiteDatabase.execSQL(CREATE_ADMINS_GROUPS);
        sqLiteDatabase.execSQL(CREATE_MESSAGES);
        sqLiteDatabase.execSQL(CREATE_MESSAGE_FILES);
        sqLiteDatabase.execSQL(CREATE_IPCALLS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_USERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_GROUPS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_USERS_GROUPS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_ADMINS_GROUPS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_MESSAGE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_MESSAGE_FILES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_IPCALLS);
        onCreate(sqLiteDatabase);
    }
}
