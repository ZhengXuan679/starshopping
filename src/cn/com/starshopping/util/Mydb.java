package cn.com.starshopping.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Jxhun
 * Date: 2018/12/14
 * Description:
 * Version: V1.0
 */
public class Mydb {

    public static Connection myConnection;
    public static Connection jdbc(String driver_url, String db_url, String name, String password) throws SQLException, ClassNotFoundException {
        Class.forName(driver_url);
        myConnection = DriverManager.getConnection(db_url,name,password);
        return myConnection;
    }
}
