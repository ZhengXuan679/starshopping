package cn.com.starshopping.dao.hzy;


import cn.com.starshopping.pojo.KeHuDengLu;
import cn.com.starshopping.util.JDBC;
import cn.com.starshopping.util.Mydb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ZhuCeDao {
    private  final JDBC jdbc = new JDBC();
    private Connection connection;

    /**
     * 查询数据库中是否已经存在该号码
     * @param shoujihaoma
     * @return 返回int，1为存在，0为不存在（只可能为1或0）
     */
    public  int checkPhoneIsExist(String shoujihaoma){
        int count = 0;//0为数据库中不存在该号码，1为数据库中已经存在该号码(只可能为1或0)
        String sql = "select count(id) from kehudenglu where shoujihaoma = ?";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,shoujihaoma);
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
     *  查询数据库中是否已经存在该邮箱号
     * @param youxiang
     * @return 返回int，1为存在，0为不存在（只可能为1或0）
     */
    public   int checkMailIsExist(String youxiang){
        int count = 0;//0为数据库中不存在该邮箱号，1为数据库中已经存在该邮箱号(只可能为1或0)
        String sql = "select count(id) from kehudenglu where youxiang = ?";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,youxiang);
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
     *   查询数据库中是否已经存在该用户名
     * @param yonghuming
     * @return 返回int，1为存在，0为不存在（只可能为1或0）
     */
    public  int checkMingIsExist(String yonghuming){
        int count = 0;//0为数据库中不存在该邮箱号，1为数据库中已经存在该邮箱号(只可能为1或0)
        String sql = "select count(id) from kehudenglu where yonghuming = ?";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,yonghuming);
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
     * 将用户账号信息写入数据库
     * @param keHuDengLuPojo
     */
    public  void insertInto(KeHuDengLu keHuDengLuPojo){
        String sql = "INSERT INTO kehudenglu(yonghuming,shoujihaoma,youxiang,mima,zhuceshijian,denglucishu" +
                ",cuowucishu,zhuangtai) VALUES (?,?,?,?,?,?,?,?);";
        try {
            Mydb.myConnection.setAutoCommit(false);
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,keHuDengLuPojo.getYongHuMing());
            ps.setString(2,keHuDengLuPojo.getShouJiHaoMa());
            ps.setString(3,keHuDengLuPojo.getYouXiang());
            ps.setString(4,keHuDengLuPojo.getMiMa());
            ps.setTimestamp(5,keHuDengLuPojo.getZhuCeShiJian());
            ps.setInt(6,0);
            ps.setInt(7,0);
            ps.setInt(8,1);
            ps.execute();
            Mydb.myConnection.commit();
            Mydb.myConnection.setAutoCommit(true);
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向yonghu表中插入yonghuming
     * @param keHuDengLuPojo
     */
    public void chaRuYongHu(KeHuDengLu keHuDengLuPojo){
        String sql = "INSERT INTO yonghu(yonghuming) VALUES (?);";
        try {
            Mydb.myConnection.setAutoCommit(false);
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,keHuDengLuPojo.getYongHuMing());
            ps.execute();
            Mydb.myConnection.commit();
            Mydb.myConnection.setAutoCommit(true);
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向shouhuodizhi表中插入yonghuming
     * @param keHuDengLuPojo
     */
    public void chaRuShouHuoDiZhi(KeHuDengLu keHuDengLuPojo){
        String sql = "INSERT INTO shouhuodizhi(yonghuming) VALUES (?);";
        try {
            Mydb.myConnection.setAutoCommit(false);
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,keHuDengLuPojo.getYongHuMing());
            ps.execute();
            Mydb.myConnection.commit();
            Mydb.myConnection.setAutoCommit(true);
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
