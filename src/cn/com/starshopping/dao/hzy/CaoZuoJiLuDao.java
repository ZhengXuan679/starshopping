package cn.com.starshopping.dao.hzy;


import cn.com.starshopping.util.JDBC;
import cn.com.starshopping.util.Mydb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CaoZuoJiLuDao {
   private final JDBC jdbc = new JDBC();
   private Connection connection;

    /**
     * 记录用户的操作记录
     * @param yonghuming 执行此操作用户对应的用户名
     * @param caozuojilu 具体的操作指令
     */
    public void chaRuCaoZuoJiLu(String yonghuming,String caozuojilu){
        if(!caozuojilu.equals("")){
            String sql = "INSERT INTO caozuorizhi(yonghuming,caozuojilu,shijian) VALUES (?,?,NOW());";
            try {
                PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
                ps.setString(1,yonghuming);
                ps.setString(2,caozuojilu);
                ps.execute();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
