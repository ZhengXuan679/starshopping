package cn.com.starshopping.dao;

import cn.com.starshopping.pojo.KeFuDengLu;
import cn.com.starshopping.util.Mydb;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: yanmin
 * Date: 2019/01/14
 * Description: 客服管理Dao
 * Version: V1.0
 */
public class KeFuDao {

    /**
     * 客服信息查询表
     *
     * @param keFuDengLu 客服登录pojo对象
     * @return 查询结果组装为一个JSONArray返回
     */
    public JSONArray chaXunKeFu(KeFuDengLu keFuDengLu, int fenYeIndex) {
        JSONArray jsonArray = new JSONArray();  // 创建一个JSONArray存放查询结果
        String xingMing = keFuDengLu.getXingMing();  // 得到姓名
        String sql = "select id,XingMing,ShouJiHaoMa,ZuiHouQianDao,ZhuangTai from kefudenglu"; // 查询得到姓名，手机号码，最后一次登录时间，状态
        String sqlz = "select count(id) from kefudenglu";    // 查询总条数，以便分页
        if (xingMing != null && !xingMing.equals("")) {  // 如果用户传递了姓名进来
            sql += " where XingMing=?";   // 那么就将sql语句加上条件
        }
        sql += " limit ?,10";
        try {
            PreparedStatement pre = Mydb.myConnection.prepareStatement(sqlz);
            ResultSet rs = pre.executeQuery();
            rs.next();
            keFuDengLu.setZongTiaoShu(rs.getInt(1)); // 得到总条数书存入对象
            rs.close();
            pre = Mydb.myConnection.prepareStatement(sql);
            if (xingMing != null && !xingMing.equals("")) {  // 如果用户传递了姓名进来
                pre.setString(1, "%" + xingMing + "%"); // 传入姓名查询
                pre.setInt(2, fenYeIndex); // 传入分页下标
            } else {   // 如果没有传递
                pre.setInt(1, fenYeIndex); // 传入分页下标
            }
            rs = pre.executeQuery();
            while (rs.next()) {
                JSONObject jsonObject = new JSONObject();  // 创建一个JSONObject对象来存放查询结果
                jsonObject.put("id", rs.getInt("id"));                 // 得到id
                jsonObject.put("XingMing", rs.getString("XingMing"));  // 得到姓名
                jsonObject.put("ShouJiHaoMa", rs.getString("ShouJiHaoMa")); // 得到手机号码
                jsonObject.put("ZuiHouQianDao", rs.getString("ZuiHouQianDao")); // 得到最后一次登录时间
                jsonObject.put("ZhuangTai", rs.getInt("ZhuangTai"));  // 得到状态
                jsonArray.put(jsonObject);    // 每条结果存入jsonArray中
            }
            rs.close();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    /**
     * 修改状态方法
     * 通过id修改状态
     *
     * @param keFuDengLu 客服登录pojo
     */
    public void xiuGaiZhuangTai(KeFuDengLu keFuDengLu) {
        // 查询客服姓名，使用手机号码+密码或者身份证号码+密码的方式
        String sql = "update kefudenglu set ZhuangTai=?,MiMa=111111  where id=?;";
        try {
            PreparedStatement pre = Mydb.myConnection.prepareStatement(sql);
            pre.setInt(1, keFuDengLu.getZhuangTai());  // 状态值
            pre.setInt(2, keFuDengLu.getId());  // id
            pre.executeUpdate();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增客服
     * 通过大老板输入的姓名，手机号码，身份证号码新增客服
     * @param keFuDengLu 客服登录pojo表
     */
    public void xinZengKeFu(KeFuDengLu keFuDengLu) {
        // 查询客服姓名，使用手机号码+密码或者身份证号码+密码的方式
        String sql = "insert into kefudenglu(XingMing,ShouJiHaoMa,ShenFenZheng) values(?,?,?)";
        try {
            PreparedStatement pre = Mydb.myConnection.prepareStatement(sql);
            pre.setString(1, keFuDengLu.getXingMing());  // 姓名
            pre.setString(2, keFuDengLu.getShouJiHaoMa());  // 手机号码
            pre.setString(3, keFuDengLu.getShenFenZheng());  // 身份证号码
            pre.executeUpdate();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
