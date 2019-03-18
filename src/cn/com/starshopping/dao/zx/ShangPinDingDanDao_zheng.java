package cn.com.starshopping.dao.zx;

import cn.com.starshopping.util.JDBC;
import cn.com.starshopping.util.Mydb;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShangPinDingDanDao_zheng {
    public JSONObject insertShangPinDingDan(String[] ShangPinBianHaoArr , String YongHuMing) {
        String sql = "select YongHuMing, ShangPinBianHao, ShangPinShuLiang, ShangPinMiaoShu, ShangPinGuiGe1, ShangPinGuiGe2, ShangPinJiaGe,imgurl,ShangPinMingChen from gouwuche where YongHuMing=? and ShangPinBianHao in("+ShangPinBianHaoArr[0]+" ";
        for (int i=1;i<ShangPinBianHaoArr.length;i++){
            sql+=","+ShangPinBianHaoArr[i];
        }
        sql=sql+")";
        try{
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1,YongHuMing);
            ResultSet rs = pstm.executeQuery();
            JSONArray jsonArray = new JSONArray();
            while (rs.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("YongHuMing", rs.getString(1));
                jsonObject.put("ShangPinBianHao", rs.getString(2));
                jsonObject.put("ShangPinShuLiang", rs.getString(3));
                jsonObject.put("ShangPinMiaoShu", rs.getString(4));
                jsonObject.put("ShangPinGuiGe1", rs.getString(5));
                jsonObject.put("ShangPinGuiGe2", rs.getString(6));
                jsonObject.put("ShangPinJiaGe", rs.getString(7));
                jsonObject.put("imgurl", rs.getString(8));
                jsonObject.put("ShangPinMingChen", rs.getString(9));
                jsonArray.put(jsonObject);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("jiesuan", jsonArray);

            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
