package cn.com.starshopping.dao.zx;

import cn.com.starshopping.util.JDBC;
import cn.com.starshopping.util.Mydb;
import jdk.nashorn.internal.scripts.JD;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShangPinShouYeDao {
    /**
     * 商品首页查询所有商品
     * @return JSONObject对象
     */
    public JSONObject selectAllShangping(){
        String sql = "select any_value(ShangPinBianHao),ShangPinZuBianHao,any_value(ShangPinMingChen),any_value(ShangPinLeiXin),any_value(ShangPinGuiGe1),any_value(ShangPinGuiGe2),any_value(PinPai),any_value(ShangPinJiaGe),any_value(ShangPinMiaoSu),any_value(YueXiaoLiang),any_value(ZongXiaoLiang) from  shangpinxinxi GROUP BY ShangPinZuBianHao";
        try {
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            JSONArray jsonArray = new JSONArray();
            while (rs.next()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ShangPinBianHao",rs.getString(1));
                jsonObject.put("ShangPinZuBianHao",rs.getString(2));
                jsonObject.put("ShangPinMingChen",rs.getString(3));
                jsonObject.put("ShangPinLeiXin",rs.getString(4));
                jsonObject.put("ShangPinGuiGe1",rs.getString(5));
                jsonObject.put("ShangPinGuiGe2",rs.getString(6));
                jsonObject.put("PinPai",rs.getString(7));
                jsonObject.put("ShangPinJiaGe",rs.getString(8));
                jsonObject.put("ShangPinMiaoSu",rs.getString(9));
                jsonObject.put("YueXiaoLiang",rs.getString(10));
                jsonObject.put("ZongXiaoLiang",rs.getString(11));
                jsonArray.put(jsonObject);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success",jsonArray);

            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
