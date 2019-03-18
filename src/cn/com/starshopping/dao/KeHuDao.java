package cn.com.starshopping.dao;

import cn.com.starshopping.pojo.KeHuDengLu;
import cn.com.starshopping.util.Mydb;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: huxiaoyi
 * Date: 2019/01/05
 * Description: 客户管理Dao层
 * Version: V1.0
 */
public class KeHuDao {
    /**
     * @param keHuDengLu              客户登录POJO，储存了前台传入的搜索条件
     * @param final_zuihouyicishijian 最后一次登录时间的搜索范围
     * @param final_zhuceshijian      注册时间的搜索范围
     * @return jsonArray               将客户信息封装在jsonArray中
     */
    public JSONObject souSuo(KeHuDengLu keHuDengLu, String final_zuihouyicishijian, String final_zhuceshijian, int page, int limit) {
        JSONArray jsonArray = new JSONArray();
        int count = 0;
        String sql_count = "select count(id) from kehudenglu";
        String sql_1 = "select id,yonghuming,shoujihaoma,youxiang,denglucishu,zuihouyicidenglushijian," +
                "zhuangtai,zhuceshijian,dongjieshijian from kehudenglu";
        String[] sql_2 = {"yonghuming like ?", "shoujihaoma like ?", "zuihouyicidenglushijian >= ?",
                "zuihouyicidenlushijian <= ?", "zhuceshijian >= ?", "zhuceshijian <= ?"};
        List<String> list = new ArrayList<>();
        if (keHuDengLu.getYongHuMing() != null && !keHuDengLu.getYongHuMing().trim().equals("")) {
            list.add("%" + keHuDengLu.getYongHuMing() + "%");
        } else {
            list.add(keHuDengLu.getYongHuMing());
        }
        if (keHuDengLu.getShouJiHaoMa() != null && !keHuDengLu.getShouJiHaoMa().trim().equals("")) {
            list.add("%" + keHuDengLu.getShouJiHaoMa() + "%");
        } else {
            list.add(keHuDengLu.getShouJiHaoMa());
        }
        String zuiHouYiCiDengLuShiJian = String.valueOf(keHuDengLu.getZuiHouYiCiDengLuShiJian());
        if (zuiHouYiCiDengLuShiJian.equals("null")) {  // String.valueOf会将null转换为字符串
            zuiHouYiCiDengLuShiJian = null;
        }
        list.add(zuiHouYiCiDengLuShiJian);
        list.add(final_zuihouyicishijian);
        String zhuCeShiJian = String.valueOf(keHuDengLu.getZhuCeShiJian()); // 得到注册时间
        if (zhuCeShiJian.equals("null")) {  // String.valueOf会将null转换为字符串
            zhuCeShiJian = null;
        }
        list.add(zhuCeShiJian);
        list.add(final_zhuceshijian);
        String sql_3 = "";    // 拼接sql语句中的条件语句
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null && !list.get(i).trim().equals("")) {  //判断，当list中的值不为空或不为null时，才进行下面的操作
                if (sql_3.equals("")) {
                    sql_3 = sql_3 + " where " + sql_2[i];  //第一次拼接时加where
                } else {
                    sql_3 = sql_3 + " and " + sql_2[i];  //非第一次拼接时加and
                }
            }
        }
        String sql_4 = sql_3 + " limit " + (page - 1) * limit + "," + limit;
        try {
            PreparedStatement preparedStatement = Mydb.myConnection.prepareStatement(sql_1 + sql_4);
            int index = 1;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) != null && !list.get(i).trim().equals("")) {
                    preparedStatement.setString(index, list.get(i).trim());
                    index++;
                }
            }
            ResultSet rs = preparedStatement.executeQuery();
            JSONObject jsonObject;
            while (rs.next()) {
                jsonObject = new JSONObject();
                jsonObject.put("id", rs.getInt("id"));
                jsonObject.put("yonghuming", rs.getString("yonghuming"));
                jsonObject.put("shoujihaoma", rs.getString("shoujihaoma"));
                jsonObject.put("youxiang", rs.getString("youxiang"));
                jsonObject.put("denglucishu", rs.getInt("denglucishu"));
                jsonObject.put("zuihouyicidenglushijian", rs.getString("zuihouyicidenglushijian").substring(0, 10));
                jsonObject.put("zhuceshijian", rs.getString("zhuceshijian").substring(0, 10));
                jsonObject.put("dongjieshijian", rs.getTimestamp("dongjieshijian"));
                int zhuangtai = rs.getInt("zhuangtai");
                if (zhuangtai == 1) {
                    jsonObject.put("zhuangtai", "已启用");
                } else {
                    jsonObject.put("zhuangtai", "已停用");
                }
                jsonArray.put(jsonObject);
            }
            rs.close();
            preparedStatement = Mydb.myConnection.prepareStatement(sql_count + sql_3);
            index = 1;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) != null && !list.get(i).trim().equals("")) {
                    preparedStatement.setString(index, list.get(i).trim());
                    index++;
                }
            }
            rs = preparedStatement.executeQuery();
            rs.next();
            count = count = rs.getInt("count(id)");
            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        json.put("code", 0);     //返回的code状态码
        json.put("count", count); //满足条件的数据总条数
        json.put("data", jsonArray); //数据内容
        return json;
    }

    /**
     * 解冻或冻结
     * 根据前台传入的用户名，将客户的冻结或解冻
     *
     * @param zhuangtai 设定用户冻结或者激活状态
     * @param id        前台传入的用户的id
     * @return action 返回成功与否的状态
     */
    public int bianJi(int zhuangtai, int id) {
        String sql = "update kehudenglu set zhuangtai = ? where id = ?";
        int action = 0;
        try {
            PreparedStatement preparedStatement = Mydb.myConnection.prepareStatement(sql);
            preparedStatement.setInt(1, zhuangtai);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            action = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return action;
    }

    /**
     * 查看
     * 查看用户的一些个人信息和订单数以及订单总额
     *
     * @param yonghuming 前台传入要查看的用户名
     * @return jsonObject 将用户信息装在jsonObject中
     */
    public JSONObject chaKan(String yonghuming) {
        String yonghu_sql = "select id,yonghuming,nicheng,xingming,xingbie,shengri,xingzuo,juzhudi," +
                "touxiang,jiaxiang from yonghu where yonghuming = ?";
        String dindan_sql = "select count(id),sum(dingdanzonge) from dindan where yonghuming = ?";
        JSONObject jsonObject = new JSONObject();
        try {
            PreparedStatement preparedStatement1 = Mydb.myConnection.prepareStatement(yonghu_sql);
            preparedStatement1.setString(1, yonghuming);
            ResultSet rs = preparedStatement1.executeQuery();
            PreparedStatement preparedStatement2 = Mydb.myConnection.prepareStatement(dindan_sql);
            preparedStatement2.setString(1, yonghuming);
            ResultSet rs2 = preparedStatement2.executeQuery();
            if (rs.next()) {
                rs2.next();
                jsonObject.put("id", rs.getInt("id"));
                jsonObject.put("yonghuming", yonghuming);
                jsonObject.put("nicheng", rs.getString("nicheng"));
                jsonObject.put("xingming", rs.getString("xingming"));
                jsonObject.put("xingbie", rs.getString("xingbie"));
                jsonObject.put("shengri", rs.getTimestamp("shengri"));
                jsonObject.put("xingzuo", rs.getString("xingzuo"));
                jsonObject.put("juzhudi", rs.getString("juzhudi"));
                jsonObject.put("touxiang", rs.getString("touxiang"));
                jsonObject.put("jiaxiang", rs.getString("jiaxiang"));
                jsonObject.put("dingdanshu", rs2.getInt("count(id)"));
                jsonObject.put("dingdanzonge", rs2.getInt("sum(dingdanzonge)"));
            }
            rs2.close();
            rs.close();
            preparedStatement2.close();
            preparedStatement1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }
}
