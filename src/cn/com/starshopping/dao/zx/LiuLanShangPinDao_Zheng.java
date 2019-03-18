package cn.com.starshopping.dao.zx;

import cn.com.starshopping.pojo.ShangPinXinXi;
import cn.com.starshopping.util.JDBC;
import cn.com.starshopping.util.Mydb;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LiuLanShangPinDao_Zheng {

    public int fileNum(String ShangPinZuBianHao, String zimulumingcheng, HttpServletRequest request) {
        ServletContext servletContext = request.getServletContext();
        File file = new File(servletContext.getRealPath(File.separator + ShangPinZuBianHao + File.separator + zimulumingcheng));
        String[] list = file.list();
        return list.length;

    }

    /**
     * 根据用户的商品类型查询对应商品的规格2（规格）
     *
     * @param shangPinXinXi 商品信息的POJO对象
     * @return 返回JSONObject代表执行成功
     */
    public JSONObject selectShangGuiGe2(ShangPinXinXi shangPinXinXi) {
        String sql = "select ShangPinGuiGe2 from  shangpinxinxi where ShangPinZuBianHao=? group by ShangPinGuiGe2";
        try {
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1, shangPinXinXi.getShangPinZuBianHao());
            ResultSet rs = pstm.executeQuery();
            JSONArray jsonArray = new JSONArray();
            while (rs.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("filed2", rs.getString(1));
                jsonArray.put(jsonObject);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("filed2", jsonArray);

            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     * 根据用户的商品类型查询对应商品的颜色（规格）
     *
     * @param shangPinXinXi 商品信息的POJO对象
     * @return 返回JSONObject代表执行成功
     */
    public JSONObject selectShangGuiGe1(ShangPinXinXi shangPinXinXi) {
        String sql = "select ShangPinGuiGe1 from  shangpinxinxi where ShangPinZuBianHao=? group by ShangPinGuiGe1";
        try {
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1, shangPinXinXi.getShangPinZuBianHao());
            ResultSet rs = pstm.executeQuery();
            JSONArray jsonArray = new JSONArray();
            while (rs.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("filed1", rs.getString(1));
                jsonArray.put(jsonObject);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("filed1", jsonArray);

            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     * 根据用户的商品类型查询对应商品的属性（规格）
     *
     * @param shangPinXinXi 商品信息的POJO对象
     * @return 返回JSONObject代表执行成功
     */
    public JSONObject selectShangPinLeiXing(ShangPinXinXi shangPinXinXi) {
        String sql = "select leixingguigexinxibiao.GuiGe1,leixingguigexinxibiao.GuiGe2 from leixingguigexinxibiao,shangpinxinxi  where shangpinxinxi.ShangPinLeiXin=? and leixingguigexinxibiao.LeiXing=shangpinxinxi.ShangPinLeiXin ";
        try {
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1, shangPinXinXi.getShangPinLeiXin());
            ResultSet rs = pstm.executeQuery();
            JSONArray jsonArray = new JSONArray();
            rs.next();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("GuiGe1", rs.getString(1));
            jsonObject.put("GuiGe2", rs.getString(2));
            jsonArray.put(jsonObject);

            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("GuiGe", jsonArray);

            return jsonObject2;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     * 查询商品详情，商品详情中商品状态为1时代表已经上架
     *
     * @param shangPinXinXi 商品信息的POJO对象
     * @return 返回查询到的商品信息的JSONObject对象
     */
    public JSONObject selectShangPinXiangQingDao(ShangPinXinXi shangPinXinXi) {
        String sql = "select ShangPinBianHao,ShangPinZuBianHao,ShangPinMingChen,ShangPinLeiXin,ShangPinGuiGe1,ShangPinGuiGe2,PinPai,ShangPinJiaGe,ShangPinMiaoSu,YueXiaoLiang,ZongXiaoLiang from  shangpinxinxi where ShangPinZuBianHao=? ";
        try {
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1, shangPinXinXi.getShangPinZuBianHao());
            ResultSet rs = pstm.executeQuery();
            JSONArray jsonArray = new JSONArray();
            while (rs.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ShangPinBianHao", rs.getString(1));
                jsonObject.put("ShangPinZuBianHao", rs.getString(2));
                jsonObject.put("ShangPinMingChen", rs.getString(3));
                jsonObject.put("ShangPinLeiXin", rs.getString(4));
                jsonObject.put("ShangPinGuiGe1", rs.getString(5));
                jsonObject.put("ShangPinGuiGe2", rs.getString(6));
                jsonObject.put("PinPai", rs.getString(7));
                jsonObject.put("ShangPinJiaGe", rs.getString(8));
                jsonObject.put("ShangPinMiaoSu", rs.getString(9));
                jsonObject.put("YueXiaoLiang", rs.getString(10));
                jsonObject.put("ZongXiaoLiang", rs.getString(11));
                jsonArray.put(jsonObject);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success", jsonArray);

            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
    }

}
