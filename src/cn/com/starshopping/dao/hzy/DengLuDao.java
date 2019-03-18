package cn.com.starshopping.dao.hzy;


import cn.com.starshopping.pojo.KeHuDengLu;
import cn.com.starshopping.util.JDBC;
import cn.com.starshopping.util.Mydb;

import java.sql.*;

public class DengLuDao {
    private  final JDBC jdbc = new JDBC();
    private Connection connection;
    private  int count = 0;

    /**
     * 根据登录名查询用户名
     * @param dengluming
     * @return 返回yonghuming字段的值
     */
    public String dengLuMingChaYongHuMing(String dengluming){
        String yonghuming = "";
        String sql = "select yonghuming from kehudenglu where shoujihaoma = ? or yonghuming = ? or youxiang = ?;";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,dengluming);
            ps.setString(2,dengluming);
            ps.setString(3,dengluming);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                yonghuming = rs.getString("yonghuming");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return yonghuming;
    }

    /**
     * 查询手机号码 是否已经注册
     * @param shoujihaoma
     * @return 判断手机号码是否已经注册，1为已经注册，0为未注册（只可能为1或0）
     */
    public  int checkPhoneIsExist(String shoujihaoma){
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
     * 查询相应登录名的登陆成功次数
     * @param dengluming
     * @return 返回denglucishu字段的值
     */
    private  int checkDengLuCiShu(String dengluming){
        String sql = "select denglucishu from kehudenglu where shoujihaoma = ? or yonghuming = ? or youxiang = ?";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,dengluming);
            ps.setString(2,dengluming);
            ps.setString(3,dengluming);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                count = rs.getInt("denglucishu");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 登录时 更新登陆次数和最后一次登陆时间
     * @param keHuDengLuPojo
     */
    public  void dengLu(KeHuDengLu keHuDengLuPojo){
        String dengluming = keHuDengLuPojo.getShouJiHaoMa();
        int denglucishu = checkDengLuCiShu(dengluming)+1;
        String sql = "UPDATE kehudenglu SET denglucishu = ?,zuihouyicidenglushijian = ? WHERE " +
                "yonghuming = ? OR shoujihaoma = ? OR youxiang = ?;";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setInt(1,denglucishu);
            ps.setTimestamp(2,keHuDengLuPojo.getZuiHouYiCiDengLuShiJian());
            ps.setString(3,dengluming);
            ps.setString(4,dengluming);
            ps.setString(5,dengluming);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *查询登录名 是否已经注册
     * @param dengluming
     * @return 判断登录名是否已经注册，1为已经注册，0为未注册（只可能为1或0）
     */
    public  int checkDengLuMingIsExist(String dengluming){
        String sql = "select count(id) from kehudenglu where shoujihaoma = ? or yonghuming = ? or youxiang = ?";
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
     * 查询与登录名相对应的密码
     * @param dengluming
     * @return 返回mima字段的值
     */
    public  String checkMiMaIsTrue(String dengluming){
        String sql = "select mima from kehudenglu where shoujihaoma = ? or yonghuming = ? or youxiang = ? ;";
        String returnmima = "";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,dengluming);
            ps.setString(2,dengluming);
            ps.setString(3,dengluming);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                returnmima = rs.getString("mima");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnmima;
    }

    /**
     * 查询与登录名相应的错误次数
     * @param dengluming
     * @return 返回cuowucishu（int型）字段的值
     */
    public  int checkCuoWuCiShu(String dengluming){
        String sql = "select cuowucishu from kehudenglu where shoujihaoma = ? or yonghuming = ? or youxiang = ?";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,dengluming);
            ps.setString(2,dengluming);
            ps.setString(3,dengluming);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                count = rs.getInt("cuowucishu");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 登陆成功或失败时(账号密码)，刷新用户的cuowucishu
     * @param dengluming
     */
    public  void dengLuShiBai(String dengluming,int cuowucishu){
        String sql = "UPDATE kehudenglu SET cuowucishu = ? WHERE " +
                "yonghuming = ? OR shoujihaoma = ? OR youxiang = ?;";
        try {
            Mydb.myConnection.setAutoCommit(false);
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setInt(1,cuowucishu);
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

    /**
     *查询用户是否可以登录
     * @param dengluming
     * @return 返回kehudenglu表中状态字段的值，1为可以登录，0为不可登录
     */
    public  int  zhuangTai (String dengluming){
        String sql = "select zhuangtai from kehudenglu where yonghuming = ? OR shoujihaoma = ? OR youxiang = ?;";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,dengluming);
            ps.setString(2,dengluming);
            ps.setString(3,dengluming);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                count = rs.getInt("zhuangtai");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  count;
    }

    /**
     * 把kehudenglu表的状态改为0（不可登录，账号冻结s）,并写入相应的冻结时间
     * @param dengluming
     * @param zhuangtai
     * @param dongjieshijian
     */
    public void caoZuoZhuangTai(String dengluming, int zhuangtai, Timestamp dongjieshijian){
        String sql = "UPDATE kehudenglu SET zhuangtai = ?,dongjieshijian = ? WHERE " +
                "yonghuming = ? OR shoujihaoma = ? OR youxiang = ?;";
        try {
            Mydb.myConnection.setAutoCommit(false);
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setInt(1,zhuangtai);
            ps.setTimestamp(2,dongjieshijian);
            ps.setString(3,dengluming);
            ps.setString(4,dengluming);
            ps.setString(5,dengluming);
            ps.execute();
            Mydb.myConnection.commit();
            Mydb.myConnection.setAutoCommit(true);
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
