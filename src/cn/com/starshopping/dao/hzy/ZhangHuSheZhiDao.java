package cn.com.starshopping.dao.hzy;


import cn.com.starshopping.util.JDBC;
import cn.com.starshopping.util.Mydb;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ZhangHuSheZhiDao {
    private JDBC jdbc = new JDBC();
    private Connection connection;
    private JSONObject jsonObject = new JSONObject();
    private int count = 0;

    /**
     * 根据用户名查询kehudenglu表中的相关数据
     * @param yonghuming
     * @return 在方法中拼装并返回一个JSONObject
     */
    public JSONObject selectAll(String yonghuming){
        String sql = "select shoujihaoma,youxiang,mima from kehudenglu where yonghuming = ?";
        connection = jdbc.getConnection();
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,yonghuming);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                jsonObject.put("shoujihaoma",rs.getString("shoujihaoma"));
                jsonObject.put("youxiang",rs.getString("youxiang"));
                jsonObject.put("mima",rs.getString("mima"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  jsonObject;
    }

    /**
     *
     * 查询用户名与手机号是否匹配
     * @param yonghuming
     * @param shoujihaoma
     * @return 匹配返回 1，不匹配返回0
     */
    public int piPeiShouJi(String yonghuming,String shoujihaoma){
        String sql = "select count(id) from kehudenglu where yonghuming = ? and shoujihaoma = ?";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,yonghuming);
            ps.setString(2,shoujihaoma);
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
     * 更改对应用户名的密码
     * @param yonghuming
     * @param mima
     */
    public void gengGaiMiMa(String yonghuming,String mima){
        String sql = "UPDATE kehudenglu SET mima = ? where yonghuming = ?";
        try {
            Mydb.myConnection.setAutoCommit(false);
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,mima);
            ps.setString(2,yonghuming);
            ps.execute();
            Mydb.myConnection.commit();
            Mydb.myConnection.setAutoCommit(true);
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询手机号码是否已经被绑定
     * @param shoujihaoma
     * @return 返回count（int） 1为已被绑定，0位未被绑定
     */
    public int selectPhoneIsBangDing(String shoujihaoma){
        String sql = "select count(id) from kehudenglu where shoujihaoma = ?";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,shoujihaoma);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                count = rs.getInt("count(id)");
            }
            rs.close();;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 查询邮箱与用户名是否匹配
     * @param yonghuming
     * @param youxiang
     * @return 匹配返回1，不匹配返回0
     */
    public int piPeiYouXiang(String yonghuming,String youxiang){
        String sql = "select count(id) from kehudenglu where yonghuming = ? and youxiang = ?";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,yonghuming);
            ps.setString(2,youxiang);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                count = rs.getInt("count(id)");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  count;
    }

    /**
     * 根据对应的用户名设置新的手机号码
     * @param yonghuming
     * @param xinshoujihaoma
     */
    public void updateNewPhone(String yonghuming,String xinshoujihaoma){
        String sql = "UPDATE kehudenglu SET shoujihaoma = ? where yonghuming = ?";
        try {
            Mydb.myConnection.setAutoCommit(false);
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,xinshoujihaoma);
            ps.setString(2,yonghuming);
            ps.execute();
            Mydb.myConnection.commit();
            Mydb.myConnection.setAutoCommit(true);
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询邮箱号是否已经被绑定
     * @param youxiang
     * @return 返回count（int） 1为已被绑定，0位未被绑定
     */
    public  int selectMailIsBangDing(String youxiang){
        String sql = "select count(id) from kehudenglu where youxiang = ?";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,youxiang);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                count = rs.getInt("count(id)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 更改指定用户的邮箱
     * @param youxiang
     */
    public void gengGaiYouXiang(String yonghuming,String youxiang){
        String sql = "UPDATE kehudenglu SET youxiang = ? where yonghuming = ?";
        try {
            Mydb.myConnection.setAutoCommit(false);
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,youxiang);
            ps.setString(2,yonghuming);
            ps.execute();
            Mydb.myConnection.commit();
            Mydb.myConnection.setAutoCommit(true);
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
