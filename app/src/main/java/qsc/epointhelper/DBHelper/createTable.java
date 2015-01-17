package qsc.epointhelper.DBHelper;

/**
 * Created by 士川 on 2014-12-1.
 */
public class createTable {
    public String getStrSql() {

        String sql = "CREATE TABLE IF NOT EXISTS  UserList" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT " +
                ", UserName nvarchar(20) not null" +
                ", UserGuid nvarchar(50) not null" +
                ", OUName nvarchar(20) not null);";
        String sql2 = "CREATE TABLE IF NOT EXISTS  Address" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT " +
                ", Name nvarchar(100) not null" +
                ", Street nvarchar(100) not null);";
        return sql + sql2;
    }

}
