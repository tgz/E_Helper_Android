package qsc.epointhelper.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlHelper extends SQLiteOpenHelper {
	 // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "EpointHelperAddress.db";
    
    public SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
    	String sql = "CREATE TABLE IF NOT EXISTS  Address" +
    			"(_id INTEGER PRIMARY KEY AUTOINCREMENT " +
   			", Name nvarchar(100) not null" +
   			", Street nvarchar(100) not null);";

        //String sql = new createTable().getStrSql();
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
