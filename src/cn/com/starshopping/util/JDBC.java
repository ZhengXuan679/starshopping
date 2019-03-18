package cn.com.starshopping.util;

/**
 *
 * 连接数据库的工具包
 * @author         zhengxuan
 * @version        V1.0
 */


import java.sql.*;

public class JDBC {
    private String url;
    private String username;
    private String password;
    private String classDriver;
    public JDBC() {
        url="jdbc:mysql://127.0.0.1:3306/starshopping"
                + "?useSSL=true&rewriteBatchedStatements=true"
                + "&useUnicode=true&characterEncoding=utf-8";
        username="root";
        password="1234";
        classDriver="com.mysql.jdbc.Driver";
    }
    /**
     * 此方法实现数据库的连接，返回一个连接
     * @return  返回Connection对象
     */

    public Connection getConnection(){
        Connection conn=null;
        try {
            Class.forName(classDriver);
            conn = DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 关闭连接 连接对象
     * @param conn  数据库连接
     */
    public void close(Connection conn){
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     *  关闭连接
     * @param conn  连接对象
     * @param ps   预处理集对象
     */
    public void close(Connection conn,PreparedStatement ps){
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(ps!=null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     *
     * @param conn  连接对象
     * @param ps   预处理集对象
     * @param rs     结果集对象
     */
    public void close(Connection conn,PreparedStatement ps,ResultSet rs){
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(ps!=null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
