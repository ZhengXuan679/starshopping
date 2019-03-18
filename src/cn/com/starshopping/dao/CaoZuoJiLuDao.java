package cn.com.starshopping.dao;

import cn.com.starshopping.util.Mydb;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CaoZuoJiLuDao {

    /**
     * 记录用户的操作记录
     *
     * @param yonghuming 执行此操作用户对应的用户名
     * @param caozuojilu 具体的操作指令
     */
    public void chaRuCaoZuoJiLu(String yonghuming, String caozuojilu) {
        if (caozuojilu != null && !caozuojilu.equals("")) {
            String sql = "INSERT INTO houtaicaozuorizhi(YongHuMing,CaoZuoJiLu,ShiJian) VALUES (?,?,NOW());";
            try {
                PreparedStatement pre = Mydb.myConnection.prepareStatement(sql);
                pre.setString(1, yonghuming);  //  用户名
                pre.setString(2, caozuojilu);  // 操作记录
                pre.executeUpdate();
                pre.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询操作日志
     * 这个方法用来查询操作日志，通过用户输入的用户名或者直接查询全部
     *
     * @param yongHuMing 用户名
     * @param duankou    这个duankou用来判断是查询前台客户操作还是后台客服操作
     * @param fenYeIndex 分页开始下标
     * @return 成功返回JSONObject，失败返回null
     */
    public JSONObject chaXunRiZhi(String yongHuMing, String duankou, int fenYeIndex) {
        JSONObject retrunJSONObject = new JSONObject(); // 这个jsonObject用来存放最终结果，格式{data:[], zongtiaoshu:x}
        String sqlh = "select id,YongHuMing,CaoZuoJiLu,ShiJian from houtaicaozuorizhi"; // 后台操作日志查询，查询得到id，用户名，操作记录，时间
        String sqlq = "select id,YongHuMing,CaoZuoJiLu,ShiJian from caozuorizhi"; // 前台操作日志查询，查询得到id，用户名，操作记录，时间
        String sqlzh = "select count(id) from houtaicaozuorizhi";    // 查询总条数，以便分页
        String sqlzq = "select count(id) from caozuorizhi";    // 查询总条数，以便分页
        if (yongHuMing != null && !yongHuMing.equals("")) {  // 如果用户传递了姓名进来
            sqlh += " where YongHuMing like ?";   // 那么就将查询后台操作日志sql语句加上条件
            sqlq += " where YongHuMing like ?";   // 那么就将查询前台操作日志sql语句加上条件
            sqlzh += " where YongHuMing like ?";   // 那么就将查询后台总条数sql语句加上条件
            sqlzq += " where YongHuMing like ?";   // 那么就将查询前台总条数sql语句加上条件
        }
        sqlh += " limit ?,10";    // 分页加上
        sqlq += " limit ?,10";   // 分页加上
        PreparedStatement pre = null;
        try {
            if (duankou.equals("qiantairizhi")) {
                pre = Mydb.myConnection.prepareStatement(sqlzq);
            } else {
                pre = Mydb.myConnection.prepareStatement(sqlzh);
            }
            if (yongHuMing != null && !yongHuMing.equals("")) {  // 如果用户传递了姓名进来
                pre.setString(1, "%" + yongHuMing + "%"); // 传入姓名查询
            }
            ResultSet rs = pre.executeQuery();
            rs.next();
            retrunJSONObject.put("zongtiaoshu", rs.getInt(1));  // 总条数存入JSONObject
            rs.close();
            if (duankou.equals("qiantairizhi")) {
                pre = Mydb.myConnection.prepareStatement(sqlq);
            } else {
                pre = Mydb.myConnection.prepareStatement(sqlh);
            }
            if (yongHuMing != null && !yongHuMing.equals("")) {  // 如果用户传递了姓名进来
                pre.setString(1, "%" + yongHuMing + "%"); // 传入姓名查询
                pre.setInt(2, fenYeIndex); // 传入分页下标
            } else {   // 如果没有传递
                pre.setInt(1, fenYeIndex); // 传入分页下标
            }
            rs = pre.executeQuery();
            JSONArray jsonArray = new JSONArray();  // 创建一个JSONArray存放查询结果
            while (rs.next()) {
                JSONObject jsonObject = new JSONObject();  // 创建一个JSONObject对象来存放查询结果
                jsonObject.put("id", rs.getInt("id"));                 // 得到id
                jsonObject.put("YongHuMing", rs.getString("YongHuMing"));  // 得到用户名
                jsonObject.put("CaoZuoJiLu", rs.getString("CaoZuoJiLu")); // 得到操作记录
                jsonObject.put("ShiJian", rs.getString("ShiJian")); // 得到操作时间
                jsonArray.put(jsonObject);    // 每条结果存入jsonArray中
            }
            retrunJSONObject.put("data", jsonArray); // 存入查询结果
            rs.close();
            pre.close();
        } catch (SQLException e) {
            retrunJSONObject = null;
            e.printStackTrace();
        }
        return retrunJSONObject;
    }

    /**
     * 导出所有操作日志
     *
     * @return 成功返回结果JSONArray 失败返回JSONArray == []
     */
    public JSONArray daoChu(String duankou, List<String> list) {
        JSONArray jsonArray = new JSONArray();
        String sqlh = "select id,YongHuMing,CaoZuoJiLu,ShiJian from houtaicaozuorizhi"; // 后台操作日志查询，查询得到id，用户名，操作记录，时间
        String sqlq = "select id,YongHuMing,CaoZuoJiLu,ShiJian from caozuorizhi"; // 前台操作日志查询，查询得到id，用户名，操作记录，时间
        String sqlJ = "";
        if (list != null) {  // 如果list不为空，那么说明不是全导出
            sqlJ = " where id in(?";   // 至少有一个占位符
            for (int i = 1; i < list.size(); i++) {  // 初始已加入了一个占位符
                sqlJ += ",?";
            }
            sqlJ += ")";
        }
        sqlh += sqlJ;  // 拼接
        sqlq += sqlJ;  // 拼接
        PreparedStatement pre = null;
        try {
            if (duankou.equals("qiantai")) {
                pre = Mydb.myConnection.prepareStatement(sqlq);
            } else {
                pre = Mydb.myConnection.prepareStatement(sqlh);
            }
            if (list != null) {  // 如果list不为空
                for (int i = 0; i < list.size(); i++) {
                    pre.setString(i + 1, list.get(i));  // 多少个元素就是多少个占位符
                }
            }
            ResultSet rs = pre.executeQuery();                            // 开启结果集
            while (rs.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", rs.getInt("id"));   // 订单id存入JSON
                jsonObject.put("YongHuMing", rs.getString("YongHuMing"));   // 订单用户名
                jsonObject.put("ShiJian", rs.getString("ShiJian"));  // 得到时间
                jsonObject.put("CaoZuoJiLu", rs.getString("CaoZuoJiLu"));   // 操作记录
                jsonArray.put(jsonObject);       // JSONObject 存入JSONArray
            }
            rs.close();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
