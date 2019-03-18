package cn.com.starshopping.dao.hzy;


import cn.com.starshopping.util.JDBC;
import cn.com.starshopping.util.Mydb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WangJiMiMaDao {
    private final JDBC jdbc = new JDBC();
    private Connection connection;

    /**
     * 查询登录名是否已注册
     * @param dengluming
     * @return 若登录名已注册则返回1，未注册则返回0。
     */
    public int checkGengLumIngIsExist(String dengluming){
        int count = 0;
        String sql = "select count(id) from kehudenglu where yonghuming = ? or shoujihaoma = ? or youxiang = ?";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,dengluming);
            ps.setString(2,dengluming);
            ps.setString(3,dengluming);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                count = rs.getInt("count(id)");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 查询与登录名相应的手机号码
     * @param dengluming
     * @return 返回shoujihaoma字段的值
     */
    public String piPeiShouJi(String dengluming){
        String return_shoujihaoma = "";
        String sql = "select shoujihaoma from kehudenglu where yonghuming = ? or shoujihaoma = ? or youxiang = ?";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,dengluming);
            ps.setString(2,dengluming);
            ps.setString(3,dengluming);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return_shoujihaoma = rs.getString("shoujihaoma");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return return_shoujihaoma;
    }

    /**
     *  查询与登录名相应的邮箱号
     * @param dengluming
     * @return 返回youxiang字段的值
     */
    public String piPeiYouXiang(String dengluming){
        String return_youxiang = "";
        String sql = "select youxiang from kehudenglu where yonghuming = ? or shoujihaoma = ? or youxiang = ?";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,dengluming);
            ps.setString(2,dengluming);
            ps.setString(3,dengluming);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return_youxiang = rs.getString("youxiang");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return return_youxiang;
    }

    /**
     * 根据登录名，修改新密码
     * @param dengluming
     * @param mima
     */
    public void gengGaiChengGong(String dengluming,String mima){
        String sql = "UPDATE kehudenglu SET mima = ?  where yonghuming = ? or shoujihaoma = ? or youxiang = ?";
        try {
            Mydb.myConnection.setAutoCommit(false);
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,mima);
            ps.setString(2,dengluming);
            ps.setString(3,dengluming);
            ps.setString(4,dengluming);
            ps.execute();
            Mydb.myConnection.commit();
            Mydb.myConnection.setAutoCommit(true);
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
