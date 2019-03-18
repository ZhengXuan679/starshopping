package cn.com.starshopping.dao.hzy;


import cn.com.starshopping.pojo.ShouHuoDiZhi;
import cn.com.starshopping.pojo.YongHu;
import cn.com.starshopping.util.JDBC;
import cn.com.starshopping.util.Mydb;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GeRenZiLiaoDao {
    private final JDBC jdbc = new JDBC();
    private Connection connection;

    /**
     * 根据yonghuming查询出yonghu表中所有字段的值,并set进一个JSONObject对象中
     * @param yonghuming
     * @return JSONObject
     */
    public JSONObject selectYongHu(String yonghuming){
        JSONObject jsonObjectYongHu = new JSONObject();
        String sql = "select yonghuming,nicheng,xingming,xingbie,ShengRi,xingzuo,juzhudi,touxiang,jiaxiang " +
                "from yonghu where yonghuming = ?;";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,yonghuming);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                jsonObjectYongHu.put("yonghuming",rs.getString("yonghuming"));
                jsonObjectYongHu.put("nicheng",rs.getString("nicheng"));
                jsonObjectYongHu.put("xingming",rs.getString("xingming"));
                jsonObjectYongHu.put("xingbie",rs.getString("xingbie"));
                jsonObjectYongHu.put("chushengriqi",rs.getDate("ShengRi"));
                jsonObjectYongHu.put("xingzuo",rs.getString("xingzuo"));
                jsonObjectYongHu.put("jvzhudi",rs.getString("juzhudi"));
                jsonObjectYongHu.put("touxiang",rs.getString("touxiang"));
                jsonObjectYongHu.put("jiaxiang",rs.getString("jiaxiang"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonObjectYongHu;
    }

    /**
     * 根据yonghuming查询出shouhuodizhi表中所有字段的值,并set进一个JSONArray对象中
     * @param yonghuming
     * @return 返回一个JSONArray对象
     */
    public JSONArray selectShouHuoDiZhi(String yonghuming){
        JSONArray jsonArray = new JSONArray();
        String sql = "SELECT id,shouhuoren,suozaidiqu,xiangxidizhi,youbian,lianxidianhua,zhuangtai FROM shouhuodizhi WHERE yonghuming = ?;";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,yonghuming);
            ResultSet rs = ps.executeQuery();
            JSONObject jsonObjectShouHuoDiZhi = null;
            while (rs.next()){
                jsonObjectShouHuoDiZhi = new JSONObject();
                jsonObjectShouHuoDiZhi.put("id",rs.getInt("id"));
                jsonObjectShouHuoDiZhi.put("yonghuming",yonghuming);
                jsonObjectShouHuoDiZhi.put("shouhuoren",rs.getString("shouhuoren"));
                jsonObjectShouHuoDiZhi.put("suozaidiqu",rs.getString("suozaidiqu"));
                jsonObjectShouHuoDiZhi.put("xiangxidizhi",rs.getString("xiangxidizhi"));
                jsonObjectShouHuoDiZhi.put("youbian",rs.getString("youbian"));
                jsonObjectShouHuoDiZhi.put("lianxidianhua",rs.getString("lianxidianhua"));
                jsonObjectShouHuoDiZhi.put("zhuangtai",rs.getInt("zhuangtai"));
                jsonArray.put(jsonObjectShouHuoDiZhi);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    /**
     * 根据yonghuming查询出kehudenglu表中所有字段的值,并set进一个JSONObject对象中
     * @param yonghuming
     * @return 返回一个JSONObject对象
     */
    public JSONObject selectKeHuDengLu(String yonghuming){
        JSONObject jsonObjectKeHuDengLu = new JSONObject();
        String sql = "SELECT yonghuming,shoujihaoma,youxiang FROM kehudenglu WHERE yonghuming = ?;";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,yonghuming);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                jsonObjectKeHuDengLu.put("yonghuming",rs.getString("yonghuming"));
                jsonObjectKeHuDengLu.put("shoujihaoma",rs.getString("shoujihaoma"));
                jsonObjectKeHuDengLu.put("youxiang",rs.getString("youxiang"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  jsonObjectKeHuDengLu;
    }

    /**
     *  将头像地址写入数据库对应yonghuming的yonghu表中
     * @param yonghuming
     * @param touxiang
     */
    public void chaRuTouXiangDiZhi(String yonghuming,String touxiang){
        String sql = "UPDATE yonghu SET touxiang = ? where yonghuming = ?";
        try {
            Mydb.myConnection.setAutoCommit(false);
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,touxiang);
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
     * yonghu提交按钮
     * @param yongHuPojo
     */
    public void yongHuTiJiao(YongHu yongHuPojo){
        String sql = "UPDATE yonghu SET nicheng = ?,xingming = ?,xingbie = ?,ShengRi = ?," +
                "xingzuo = ?,JuZhuDi = ?,jiaxiang = ? where yonghuming = ?";
        try {
            Mydb.myConnection.setAutoCommit(false);
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,yongHuPojo.getNiCheng());
            ps.setString(2,yongHuPojo.getXingMing());
            ps.setString(3,yongHuPojo.getXingBie());
            ps.setString(4,yongHuPojo.getChuShengRiQi());
            ps.setString(5,yongHuPojo.getXingZuo());
            ps.setString(6,yongHuPojo.getJuZhuDi());
            ps.setString(7,yongHuPojo.getJiaXiang());
            ps.setString(8,yongHuPojo.getYongHuMing());
            ps.execute();
            Mydb.myConnection.commit();
            Mydb.myConnection.setAutoCommit(true);
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 收货信息新增
     * @param shouHuoDiZhiPojo
     */
    public void insertIntoShouHuoDiZhi(ShouHuoDiZhi shouHuoDiZhiPojo){
        String sql = "INSERT INTO shouhuodizhi(yonghuming,shouhuoren,suozaidiqu,xiangxidizhi,youbian,lianxidianhua,zhuangtai)" +
                " VALUES (?,?,?,?,?,?,?);";
        connection = jdbc.getConnection();
        try {
            Mydb.myConnection.setAutoCommit(false);
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,shouHuoDiZhiPojo.getYongHuMing());
            ps.setString(2,shouHuoDiZhiPojo.getShouHuoRen());
            ps.setString(3,shouHuoDiZhiPojo.getSuoZaiDiQu());
            ps.setString(4,shouHuoDiZhiPojo.getXiangXiDiZhi());
            ps.setString(5,shouHuoDiZhiPojo.getYouBian());
            ps.setString(6,shouHuoDiZhiPojo.getLianXiDianHua());
            ps.setInt(7,shouHuoDiZhiPojo.getZhuangTai());
            ps.execute();
            Mydb.myConnection.commit();
            Mydb.myConnection.setAutoCommit(true);
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 收货信息修改
     * @param shouHuoDiZhiPojo
     */
    public void updateShouHuoDiZhi(ShouHuoDiZhi shouHuoDiZhiPojo){
        String sql = "UPDATE shouhuodizhi SET shouhuoren = ?,suozaidiqu = ?,xiangxidizhi = ?,youbian = ?," +
                "lianxidianhua = ? where yonghuming = ? and id = ?";
        try {
            Mydb.myConnection.setAutoCommit(false);
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,shouHuoDiZhiPojo.getShouHuoRen());
            ps.setString(2,shouHuoDiZhiPojo.getSuoZaiDiQu());
            ps.setString(3,shouHuoDiZhiPojo.getXiangXiDiZhi());
            ps.setString(4,shouHuoDiZhiPojo.getYouBian());
            ps.setString(5,shouHuoDiZhiPojo.getLianXiDianHua());
            ps.setString(6,shouHuoDiZhiPojo.getYongHuMing());
            ps.setInt(7,shouHuoDiZhiPojo.getId());
            ps.execute();
            Mydb.myConnection.commit();
            Mydb.myConnection.setAutoCommit(true);
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 收货信息删除
     * @param shouHuoDiZhiPojo
     */
    public void deleteShouHuoDiZhi(ShouHuoDiZhi shouHuoDiZhiPojo){
        String sql = "DELETE FROM shouhuodizhi WHERE id = ? AND yonghuming = ?;";
        try {
            Mydb.myConnection.setAutoCommit(false);
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setInt(1,shouHuoDiZhiPojo.getId());
            ps.setString(2,shouHuoDiZhiPojo.getYongHuMing());
            ps.execute();
            Mydb.myConnection.commit();
            Mydb.myConnection.setAutoCommit(true);
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置默认地址
     * @param shouHuoDiZhiPojo
     */
    public void updateMoRen(ShouHuoDiZhi shouHuoDiZhiPojo){
        updateNoMoRen(shouHuoDiZhiPojo);
        String sql = "UPDATE shouhuodizhi SET zhuangtai = 1 where yonghuming = ? and id = ?";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,shouHuoDiZhiPojo.getYongHuMing());
            ps.setInt(2,shouHuoDiZhiPojo.getId());
            ps.execute();
            Mydb.myConnection.commit();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据用户名设置无默认地址
     * @param shouHuoDiZhiPojo
     */
    public void updateNoMoRen(ShouHuoDiZhi shouHuoDiZhiPojo){
        String sql = "UPDATE shouhuodizhi SET zhuangtai = 0 where yonghuming = ?";
        try {
            Mydb.myConnection.setAutoCommit(false);
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1,shouHuoDiZhiPojo.getYongHuMing());
            ps.execute();
            Mydb.myConnection.commit();
            Mydb.myConnection.setAutoCommit(false);
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
