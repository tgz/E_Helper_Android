package qsc.epointhelper.DBHelper;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlHelperUserList extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "EpointHelperUserList.db";

    public SqlHelperUserList(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE IF NOT EXISTS  UserList" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT " +
                ", UserName nvarchar(20) not null" +
                ", UserGuid nvarchar(50) not null" +
                ", OUName nvarchar(20) not null);";

      //  String sql = new createTable().getStrSql();
        db.execSQL(sql);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
//    	 db.execSQL(SQL_DELETE_ENTRIES);
//         onCreate(db);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //onUpgrade(db, oldVersion, newVersion);
        onCreate(db);
    }


}