package cn.com.starshopping.dao.zx;

import cn.com.starshopping.pojo.ShangPinXinXi;
import cn.com.starshopping.pojo.ShouHuoDiZhi;
import cn.com.starshopping.util.JDBC;
import cn.com.starshopping.util.Mydb;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SouHuoDiZhiDao_zheng {


    /**
     * 删除收货地址
     * @param id 删除地址的ID数组
     * @param YongHuMing 用户名
     * @return true代表删除成功
     */
    public boolean deleteShouHuoDiZhiDao(String[] id,String YongHuMing){
        String sql = "delete from shouhuodizhi where YongHuMing=? and  id in("+id[0]+" ";
        for (int i=1;i<id.length;i++){
            sql+=","+id[i];
        }
        sql=sql+")";
        try {
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1,YongHuMing);
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  false;
    }



    /**
     * 增加收货地址
     * @param shouHuoDiZhi 收货地址POJO
     * @return true 插入地址成功
     */
    public boolean insertShouHuoDiZhi(ShouHuoDiZhi shouHuoDiZhi){
        String sql = "insert into shouhuodizhi(YongHuMing, ShouHuoRen, SuoZaiDiQu, XiangXiDiZhi, LianXiDianHua) values (?,?,?,?,?)";
        try {
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1,shouHuoDiZhi.getYongHuMing());
            pstm.setString(2,shouHuoDiZhi.getShouHuoRen());
            pstm.setString(3,shouHuoDiZhi.getSuoZaiDiQu());
            pstm.setString(4,shouHuoDiZhi.getXiangXiDiZhi());
            pstm.setString(5,shouHuoDiZhi.getLianXiDianHua());
            pstm.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  false;
    }

    /**
     * 根据用户名查询所有收货地址
     * @param YongHuMing 用户名
     * @return 返回一个地址的JSONObject
     */
    public JSONObject selectAllDiZhi(String YongHuMing){
        String sql="select YONGHUMING, SHOUHUOREN, SUOZAIDIQU, XIANGXIDIZHI, YOUBIAN, LIANXIDIANHUA, ZHUANGTAI,id from shouhuodizhi where YongHuMing=?";
        try {
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1,YongHuMing);
            ResultSet rs = pstm.executeQuery();
            JSONArray jsonArray = new JSONArray();
            while (rs.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("YongHuMing", rs.getString(1));
                jsonObject.put("ShouHuoRen", rs.getString(2));
                jsonObject.put("SuoZaiDiQu", rs.getString(3));
                jsonObject.put("XiangXiDiZhi", rs.getString(4));
                jsonObject.put("YouBian", rs.getString(5));
                jsonObject.put("LianXiDianHua", rs.getString(6));
                jsonObject.put("zhuangtai", rs.getString(7));
                jsonObject.put("id", rs.getString(8));
                jsonArray.put(jsonObject);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 0);
            jsonObject.put("msg", "");
            jsonObject.put("count", 20);
            jsonObject.put("dizhi", jsonArray);

            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改地址
     * @param shouHuoDiZhi 收货地址的POJO对象
     * @return true代表收货成功
     */
    public boolean updateShouHuoDiZhi(ShouHuoDiZhi shouHuoDiZhi){
        String sql = "update shouhuodizhi set ShouHuoRen=? , SuoZaiDiQu=? , XiangXiDiZhi=? , LianXiDianHua=? where id=?";
        try {
            PreparedStatement pstm = Mydb.myConnection.prepareStatement(sql);
            pstm.setString(1,shouHuoDiZhi.getShouHuoRen());
            pstm.setString(2,shouHuoDiZhi.getSuoZaiDiQu());
            pstm.setString(3,shouHuoDiZhi.getXiangXiDiZhi());
            pstm.setString(4,shouHuoDiZhi.getLianXiDianHua());
            pstm.setInt(5,shouHuoDiZhi.getId());
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  false;
    }

}
