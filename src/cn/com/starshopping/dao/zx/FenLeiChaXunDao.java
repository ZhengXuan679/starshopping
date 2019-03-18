package cn.com.starshopping.dao.zx;

import cn.com.starshopping.pojo.PageInfo;
import cn.com.starshopping.util.JDBC;
import cn.com.starshopping.util.Mydb;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FenLeiChaXunDao {
    /**
     * 分类查询商品，并且进行分页，每页显示6条数据
     * @param fenlei
     * @param pageInfo
     * @return
     */

    public JSONObject selectfenleishangpin_fenye(ArrayList<String> fenlei, PageInfo pageInfo){
        JDBC jdbc = new JDBC();
        String sql = "select ShangPinBianHao,ShangPinZuBianHao,ShangPinMingChen,ShangPinLeiXin,ShangPinGuiGe1,ShangPinGuiGe2,PinPai,ShangPinJiaGe,ShangPinMiaoSu,YueXiaoLiang,ZongXiaoLiang from  shangpinxinxi  where ShangPinLeiXin in('"+fenlei.get(0)+" ' ";
        for (int i=1;i<fenlei.size();i++){
            sql+=", '"+fenlei.get(i)+"'";
        }
        sql=sql+") limit ?,?";
        try{
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setInt(1,(pageInfo.getCurrentPage()-1)*6);
            pstm.setInt(2,pageInfo.getPageSize());
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
            jsonObject.put("total",pageInfo.getTotalNum());
            jsonObject.put("success",jsonArray);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * 分页显示每条的信息
     * @param leixin
     * @param pageInfo
     * @return 返回显示的JSONObject
     */
    public JSONObject selectxianshixinxi(String  leixin,PageInfo pageInfo){
        JDBC jdbc = new JDBC();
        String sql = "select ShangPinBianHao,ShangPinZuBianHao,ShangPinMingChen,ShangPinLeiXin,ShangPinGuiGe1,ShangPinGuiGe2,PinPai,ShangPinJiaGe,ShangPinMiaoSu,YueXiaoLiang,ZongXiaoLiang from  shangpinxinxi  where ShangPinLeiXin=? limit ?,?";
        try{
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1,leixin);
            pstm.setInt(2, (pageInfo.getCurrentPage()-1)*6);
            pstm.setInt(3,pageInfo.getPageSize());
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
            jsonObject.put("total",pageInfo.getTotalNum());
            jsonObject.put("success",jsonArray);

            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 000
     * @param leixin
     * @param pageInfo
     * @return
     */
    public PageInfo selectTotalDaLei(String  leixin,PageInfo pageInfo){
        JDBC jdbc = new JDBC();
        String sql = "select count(id) from  shangpinxinxi  where LeiXin=?";
        try{
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1,leixin);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()){
                pageInfo.setTotalNum(rs.getInt(1));
                //pageInfo.setTotalPages(((rs.getInt(1)-1)/6+1));
            }

            return pageInfo;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 000
     * 根据类型查询该分类下的所有数据条数
     * @param leixin
     * @param pageInfo
     * @return
     */
    public PageInfo selectTotal(String  leixin,PageInfo pageInfo){
        JDBC jdbc = new JDBC();
        String sql = "select count(id) from  shangpinxinxi  where ShangPinLeiXin=?";
        try{
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1,leixin);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()){
                pageInfo.setTotalNum(rs.getInt(1));
              //  pageInfo.setTotalNum(((rs.getInt(1)-1)/6+1));
            }

            return pageInfo;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }





    /**
     * 000
     * 根据大类型查询里面的小类型
     * @param daLeiXin
     * @return 返回一个list集合
     */
    public ArrayList selectFuShi(String  daLeiXin){
        JDBC jdbc = new JDBC();
        String sql="select LeiXing from leixingguigexinxibiao where DaLeiXin=? ";
        try {
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1,daLeiXin);
            ResultSet rs = pstm.executeQuery();
            ArrayList list = new ArrayList<String>();
            while (rs.next()){
                list.add(rs.getString(1));

            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
