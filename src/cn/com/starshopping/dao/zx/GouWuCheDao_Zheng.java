package cn.com.starshopping.dao.zx;

import cn.com.starshopping.pojo.DinDan;
import cn.com.starshopping.pojo.GouWuChe;
import cn.com.starshopping.pojo.ShangPinXinXi;
import cn.com.starshopping.util.JDBC;
import cn.com.starshopping.util.Mydb;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

/**
 * 购物车管理dao 包括购物车的修改，删除，结算
 */
public class GouWuCheDao_Zheng {


    /**
     * 查询订单中的商品信息
     *
     * @param ShangPinBianHaoArr 商品编号组成的数组
     * @param YongHuMing
     * @return
     */
    public JSONArray selectDinDanShangPingXinXi(String[] ShangPinBianHaoArr, String YongHuMing) {
        JDBC jdbc = new JDBC();
        String sql = "select  ShangPinBianHao, ShangPinShuLiang, ShangPinJiaGe,ShangPinMingChen from gouwuche where YongHuMing=? and ShangPinBianHao in(?";
        for (int i = 1; i < ShangPinBianHaoArr.length; i++) {
            sql += ",?";
        }
        sql = sql + ")";
        try {
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1, YongHuMing);
            for (int i = 0; i < ShangPinBianHaoArr.length; i++) {
                pstm.setString(i + 2, ShangPinBianHaoArr[i]);
            }
            ResultSet rs = pstm.executeQuery();
            JSONArray jsonArray = new JSONArray();
            while (rs.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("shangpinbianhao", rs.getString(1));
                jsonObject.put("shangpinshuliang", rs.getString(2));
                jsonObject.put("shangpindanjia", rs.getString(3));
                jsonObject.put("shangpinmingcheng", rs.getString(4));
                jsonObject.put("shangpinzongjia", Integer.parseInt(rs.getString(2)) * Double.parseDouble(rs.getString(3)));
                jsonArray.put(jsonObject);
            }
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询订单中的商品信息
     *
     * @param ShangPinBianHaoArr 商品编号组成的数组
     * @return
     */
    public JSONArray selectShangPinXinXi(String[] ShangPinBianHaoArr, int num) {
        num = 1;
        JDBC jdbc = new JDBC();
        String sql = "select  ShangPinBianHao,ShangPinJiaGe,ShangPinMingChen from shangpinxinxi where ShangPinBianHao in(?";
        for (int i = 1; i < ShangPinBianHaoArr.length; i++) {
            sql += ",?";
        }
        sql = sql + ")";
        try {
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            for (int i = 0; i < ShangPinBianHaoArr.length; i++) {
                pstm.setString(i + 1, ShangPinBianHaoArr[i]);
            }
            ResultSet rs = pstm.executeQuery();
            JSONArray jsonArray = new JSONArray();
            while (rs.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("shangpinbianhao", rs.getString(1));
                jsonObject.put("shangpindanjia", rs.getString(2));
                jsonObject.put("shangpinmingcheng", rs.getString(3));
                jsonObject.put("shangpinzongjia", num * Double.parseDouble(rs.getString(2))); // 商品总价为商品数量*商品单价
                jsonObject.put("shangpinshuliang", num);  // 得到商品数量
                jsonArray.put(jsonObject);
            }
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询所有需要购买的商品的详情
     *
     * @param ShangPinBianHaoArr 参数是一个由商品编号组成的数组
     * @return 返回一个JSONObject
     */
    public JSONObject selectAllDingDan(String[] ShangPinBianHaoArr, String YongHuMing) {
        JDBC jdbc = new JDBC();
        String sql = "select YongHuMing, ShangPinBianHao, ShangPinShuLiang, ShangPinMiaoShu, ShangPinGuiGe1, ShangPinGuiGe2, ShangPinJiaGe,imgurl,ShangPinMingChen from gouwuche where YongHuMing=? and ShangPinBianHao in (?";
        for (int i = 1; i < ShangPinBianHaoArr.length; i++) {
            sql += ",?";
        }
        sql = sql + ")";
        try {
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1, YongHuMing);
            for (int i = 0; i < ShangPinBianHaoArr.length; i++) {
                pstm.setString(i + 2, ShangPinBianHaoArr[i]);
            }
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

    /**
     * 查询购物车的所有商品
     *
     * @param yonghuming 用户名
     * @return 返回一个ShangPinJiaGe
     */
    public JSONObject selectAllGouWuChe(String yonghuming) {
        JDBC jdbc = new JDBC();
        String sql = "select YongHuMing, ShangPinBianHao, ShangPinShuLiang, ShangPinMiaoShu, ShangPinGuiGe1, ShangPinGuiGe2, ShangPinJiaGe,imgurl,ShangPinMingChen from gouwuche where YongHuMing=?";
        try {
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1, yonghuming);
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
            jsonObject.put("car", jsonArray);

            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把购物车信息插入数据库
     *
     * @param shangPinXinXi    商品信息的POJO
     * @param YongHuMing       用户名
     * @param ShangPinShuLiang 商品的数量
     * @return true 代表插入成功
     */

    public boolean insertGouWuChe(ShangPinXinXi shangPinXinXi, String YongHuMing, int ShangPinShuLiang, String imgurl) {
        JDBC jdbc = new JDBC();
        String sql = "insert into gouwuche(yonghuming, shangpinbianhao,ShangPinShuLiang,ShangPinMiaoShu,ShangPinGuiGe1,ShangPinGuiGe2,ShangPinJiaGe,imgurl,ShangPinMingChen) values (?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1, YongHuMing);
            pstm.setString(2, shangPinXinXi.getShangPinBianHao());
            pstm.setInt(3, ShangPinShuLiang);
            pstm.setString(4, shangPinXinXi.getShangPinMiaoSu());
            pstm.setString(5, shangPinXinXi.getShangPinGuiGe1());
            pstm.setString(6, shangPinXinXi.getShangPinGuiGe2());
            pstm.setDouble(7, shangPinXinXi.getShangPinJiaGe());
            pstm.setString(8, imgurl);
            pstm.setString(9, shangPinXinXi.getShangPinMingChen());
            pstm.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 根据组编号，规格1，规格2,查询该商品的编号
     *
     * @param shangPinXinXi 商品信息的POJO对象
     * @return 返回商品信息对象
     */
    public ShangPinXinXi select_shangpinbianhao(ShangPinXinXi shangPinXinXi) {
        JDBC jdbc = new JDBC();
        String sql = "select ShangPinBianHao from shangpinxinxi where ShangPinZuBianHao=? and ShangPinGuiGe1=? and ShangPinGuiGe2=?";
        try {
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1, shangPinXinXi.getShangPinZuBianHao());
            pstm.setString(2, shangPinXinXi.getShangPinGuiGe1());//红色
            pstm.setString(3, shangPinXinXi.getShangPinGuiGe2());
            ResultSet rs = pstm.executeQuery();
            rs.next();
            shangPinXinXi.setShangPinBianHao(rs.getString(1));

            return shangPinXinXi;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 结算成功，插入订单表订单信息
     *
     * @param dinDan 订单的POJO信息
     * @return true代表插入信息成功
     */
    public boolean insertDingDanXinXi(DinDan dinDan) {
        String sql = "insert into dindan(dingdanbianhao, yonghuming, shangpinxinxi, wuliudanhao, dingdanzonge, dingdanshijian,DingDanZhuangTai,ShouHuoDiZhi) values (?,?,?,?,?,now(),?,?)";
        try {
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1, dinDan.getDingDanBianHao());
            pstm.setString(2, dinDan.getYongHuMing());
            pstm.setString(3, dinDan.getShangPinXinXi());
            pstm.setString(4, dinDan.getWuLiuDanHao());
            pstm.setDouble(5, dinDan.getDingDanZongE());
            pstm.setInt(6, dinDan.getDingDanZhuangTai());
            pstm.setString(7, dinDan.getShouHuoDiZhi());
            pstm.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改购物车商品的数量
     *
     * @param gouWuChe
     * @return true 代表修改成功
     */
    public boolean updateGouWuChe(GouWuChe gouWuChe) {
        String sql = "update gouwuche set ShangPinShuLiang=? where YongHuMing=? and ShangPinBianHao=?";
        try {
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setInt(1, gouWuChe.getShangPinShuLiang());
            pstm.setString(2, gouWuChe.getYongHuMing());
            pstm.setString(3, gouWuChe.getShangPinBianHao());
            pstm.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 根据用户名和商品编号，查询购物车中商品数量
     *
     * @param gouWuChe
     * @return
     */
    public int selectGouWuCheShuLiang(GouWuChe gouWuChe) {
        String sql = "select  ShangPinShuLiang from gouwuche where YongHuMing=? and ShangPinBianHao=?";
        try {
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1, gouWuChe.getYongHuMing());
            pstm.setString(2, gouWuChe.getShangPinBianHao());
            ResultSet rs = pstm.executeQuery();
            rs.next();
            int shuliang = rs.getInt(1);

            return shuliang;
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        return -1;
    }


    /**
     * 删除购物车
     *
     * @param ShangPinBianHaoArr 在前端通过选择多选框获得商品编号组成的String[]数组
     * @param YongHuMing         用户名
     * @return true 代表删除购物车成功 false代表删除购物车失败
     */
    public boolean deleteGouWuCheDao(String[] ShangPinBianHaoArr, String YongHuMing) {
        String sql = "delete from gouwuche where YongHuMing=? and  ShangPinBianHao in(?";
        for (int i = 1; i < ShangPinBianHaoArr.length; i++) {
            sql += ",?";
        }
        sql = sql + ")";
        try {
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1, YongHuMing);
            for (int i = 0; i < ShangPinBianHaoArr.length; i++) {
                pstm.setString(i + 2, ShangPinBianHaoArr[i]);
            }
            pstm.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
