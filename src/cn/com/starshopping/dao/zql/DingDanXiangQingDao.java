package cn.com.starshopping.dao.zql;

import cn.com.starshopping.util.Mydb;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: ZouQinLun
 * Date: 2019/01/09
 * Description:
 * Version: V1.0
 */
public class DingDanXiangQingDao {
    /**
     * 此方法通过传入订单编号，以JSONObject的形式返回该订单的所有信息
     *
     * @param s
     * @return
     */
    public JSONObject dingDanXiangQingDao(String s) {
        JSONObject jsonObject = new JSONObject();
        String shangPinBianHao = "";
        String sql = "select ShangPinXinXi,WuLiuDanHao,DingDanZongE,DingDanShiJian,ShouHuoDiZhi from dindan where DingDanBianHao = ?";//通过搜索订单在订单表搜索订单详情
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1, s);
            ResultSet rs = ps.executeQuery();
            rs.next();
            JSONArray jsonArray = new JSONArray(rs.getString("ShangPinXinXi"));//以JSONArray的形式得到同zx订单中的各个商品信息
            JSONArray jsonArray2 = new JSONArray();
            for (int i = 0; i < jsonArray.length(); i++) {                                           //循环此订单中的各个商品，分别得倒每样商品的信息
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);                               //以JSONObject形式得到第i样商品的信息
                shangPinBianHao = jsonObject1.getString("shangpinbianhao");            //得到当前商品的商品编号
                String sql1 = "select ShangPinMingChen,ShangPinLeiXin,ShangPinGuiGe1,ShangPinGuiGe2 from shangpinxinxi where ShangPinBianHao=?";//通过搜索出的商品编号到商品信息表搜索商品详情
                PreparedStatement ps1 = Mydb.myConnection.prepareStatement(sql1);
                ps1.setString(1, shangPinBianHao);
                ResultSet resultSet = ps1.executeQuery();
                resultSet.next();
                String shangPinMingChen = resultSet.getString("ShangPinMingChen");                 //得到商品名称


                String shangPinLeiXin = resultSet.getString("ShangPinLeiXin");                      //得到商品类型
                String sql2 = "select GuiGe1,GuiGe2 from leixingguigexinxibiao where LeiXing=?";    //通过商品类型在类型关系信息表搜索此类型的商品的规格名
                PreparedStatement ps2 = Mydb.myConnection.prepareStatement(sql2);
                ps2.setString(1, shangPinLeiXin);
                ResultSet resultSet1 = ps2.executeQuery();
                resultSet1.next();
                String guiGe1 = resultSet1.getString("GuiGe1");                                  //得到规格1名称
                String guiGe2 = resultSet1.getString("GuiGe2");                                  //得到规格2名称
                JSONObject jsonObject2 = new JSONObject();          //创建JSONObject对象，用于封装商品信息
                JSONObject jsonObject3 = new JSONObject();          //创建JSONObject对象，用于封装规格1信息
                JSONObject jsonObject4 = new JSONObject();          //创建JSONObject对象，用于封装规格2信息

                jsonObject2.put("shangPinMingChen", shangPinMingChen);       //将商品名称封装到JSONObject
                jsonObject2.put("shangPinShuLiang", jsonObject1.getInt("shangpinshuliang")); //将此种商品数量封装到JSONObject
                jsonObject2.put("shangPinDanJia", jsonObject1.getDouble("shangpindanjia"));  //将此种商品单价封装到JSONObject
                jsonObject2.put("shangPinZongJia", jsonObject1.getDouble("shangpinzongjia"));//将此种商品总价封装到JSONObject
                if (resultSet.getString("ShangPinGuiGe1") == null) {        //如果商品规格1为null，则封装为空在JSONObject
                    jsonObject3.put(guiGe1, "");
                } else {
                    jsonObject3.put(guiGe1, resultSet.getString("ShangPinGuiGe1"));//如果商品规格1不为null，则将规格1的名称和内容封装到JSONObject
                }
                if (resultSet.getString("ShangPinGuiGe2") == null) {        //如果商品规格2为null，则封装为空在JSONObject
                    jsonObject4.put(guiGe2, "");
                } else {
                    jsonObject4.put(guiGe2, resultSet.getString("ShangPinGuiGe2"));//如果商品规格2不为null，则将规格1的名称和内容封装到JSONObject
                }
                jsonObject2.put("guiGe1", jsonObject3);  //将规格1封装到JSONObject
                jsonObject2.put("guiGe2", jsonObject4);  //将规格2封装到JSONObject
                jsonArray2.put(jsonObject2);            //将此种商品的信息以JSONObject形式封装到JSONArray
                resultSet.close();
                ps1.close();
            }
            jsonObject.put("shangPinXinXi", jsonArray2); //将重新组装的商品信息的JSONArray封装入JSONObject
            jsonObject.put("wuLiuDanHao", rs.getString("WuLiuDanHao")); //将物流单号封装入JSONObject
            jsonObject.put("dingDanZongE", rs.getDouble("DingDanZongE"));//将订单总额封装入JSONObject
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//规定日期格式
            String shiJian = df.format(rs.getTimestamp("DingDanShiJian"));    //将从数据库得到的订单时间转化为规格格式
            jsonObject.put("dingDanShiJian", shiJian);                            //将格式化后的订单时间装入JSONObject
            jsonObject.put("shouHuoDiZhi", rs.getString("ShouHuoDiZhi"));      //将收货地址封装入JSONObject
            jsonObject.put("ShangPinZuBianHao", shangPinBianHao.substring(0, shangPinBianHao.indexOf("-")));      //将收货地址封装入JSONObject
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonObject;              //返回JSONObject
    }
}
