package cn.com.starshopping.dao.shangpinguanli;

import cn.com.starshopping.util.Mydb;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Wong
 * Date: 2019/01/05
 * Description:
 * Version: V1.0
 */
public class SelectDao {
    private int page;
    private int limit;

    /**
     * 传入页码进行封装
     * @param page
     */
    public SelectDao(int page,int limit){
        this.page = (page - 1) * limit;
        this.limit = limit;
    }
    public SelectDao(){

    }

    /**
     * 将集合最后的sql拼接进sql，并依次将值放入占位符
     * @param new_keys
     * @return
     */
    public JSONArray select(ArrayList<String> new_keys){
        int max = new_keys.size();
        JSONArray jsonArray = new JSONArray();
        String sql = "select * from shangpinxinxi" + new_keys.get(max - 1) + " limit ?,?";
//        String sql = "select * from shangpinxinxi" + new_keys.get(max - 1);
        try {
            PreparedStatement preparedStatement = Mydb.myConnection.prepareStatement(sql);
            for (int i = 0;i < new_keys.size() - 1;i++){
                preparedStatement.setString(i + 1,new_keys.get(i));
            }
            preparedStatement.setInt(max,page);
            preparedStatement.setInt(max + 1,limit);
            ResultSet resultSet = preparedStatement.executeQuery();
            JSONObject jsonObject = null;
            while (resultSet.next()){
                jsonObject = new JSONObject();
                jsonObject.put("id",resultSet.getInt("id"));
                jsonObject.put("shangPinBianHao",resultSet.getString("ShangPinBianHao"));
                jsonObject.put("shangPinZuBianHao",resultSet.getString("ShangPinZuBianHao"));
                jsonObject.put("shangPinMingCheng",resultSet.getString("ShangPinMingChen"));
                jsonObject.put("shangPinLeiXin",resultSet.getString("ShangPinLeiXin"));
                jsonObject.put("shangPinGuiGe1",resultSet.getString("ShangPinGuiGe1"));
                jsonObject.put("shangPinGuiGe2",resultSet.getString("ShangPinGuiGe2"));
                jsonObject.put("pinPai",resultSet.getString("PinPai"));
                jsonObject.put("shangPinJiaGe",resultSet.getString("ShangPinJiaGe"));
                jsonObject.put("shangPinChengBen",resultSet.getString("ShangPinChengBen"));
                jsonObject.put("yueXiaoLiang",resultSet.getInt("YueXiaoLiang"));
                jsonObject.put("zongXiaoLiang",resultSet.getInt("ZongXiaoLiang"));
                jsonObject.put("shangPinZhuangTai",resultSet.getString("ShangPinZhuangTai").equals("1")?"已上架":"下架中");
                jsonObject.put("caoZuoShiJian",resultSet.getString("CaoZuoShiJian"));
                jsonArray.put(jsonObject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    /**
     * 将前端的字段值依次查询并放入集合，再将组成的sql放入集合最后
     * @param request
     * @return
     */
    public ArrayList<String> newSql(HttpServletRequest request){
        String[] keys = {"ShangPinBianHao","ShangPinZuBianHao","ShangPinMingCheng","ShangPinLeiXin","ShangPinGuiGe1","ShangPinGuiGe2","PinPai","ShangPinJiaGe","ShangPinChengBen","YueXiaoLiang","ZongXiaoLiang","ShangPinZhuangTai","CaoZuoShiJian"};
        String newsql = " where ";
        ArrayList<String> new_keys = new ArrayList<>();
        for (String key:keys){
            String value = request.getParameter(key);
            if (value != null){
                if (!value.equals("")){
                    newsql = newsql + key + " = ? and ";
                    new_keys.add(value);
                }
            }
        }
        int index = newsql.lastIndexOf(" and");
        if (index == -1){
            newsql = "";
        }
        else {
            newsql = newsql.substring(0, index);
        }
        new_keys.add(newsql);
        return new_keys;
    }

    /**
     * 为了配合layUI先搜索总信息数
     * @param new_keys
     * @return
     */
    public int count(ArrayList<String> new_keys){
        int max = new_keys.size();
        int j = 0;
        JSONArray jsonArray = new JSONArray();
        String sql = "select count(id) from shangpinxinxi" + new_keys.get(max - 1);
        try {
            PreparedStatement preparedStatement = Mydb.myConnection.prepareStatement(sql);
            for (int i = 0; i < new_keys.size() - 1; i++) {
                preparedStatement.setString(i + 1, new_keys.get(i));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            j = resultSet.getInt(1);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return j;
    }
}
