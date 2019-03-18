package cn.com.starshopping.dao.shangpinguanli;

import cn.com.starshopping.util.Mydb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Wong
 * Date: 2019/01/07
 * Description:
 * Version: V1.0
 */
public class DeleteDao {
    /**
     * 将片段拼装sql执行查询
     * @param idArray
     * @return 返回true说明没有上架中商品，反之有
     */
    public boolean select(String idArray){
        boolean action = false;
        String sql = "select id from shangpinxinxi where id in "+idArray+" and ShangPinZhuangTai = 1";
        try {
            PreparedStatement preparedStatement = Mydb.myConnection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()){
                action = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return action;
    }

    /**
     * 将片段拼装sql执行删除
     * @param idArray
     */
    public void delete(String idArray){
        String sql = "delete from shangpinxinxi where id in " + idArray;
        try {
            PreparedStatement preparedStatemen = Mydb.myConnection.prepareStatement(sql);
            preparedStatemen.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 遍历集合组成sql片段
     * @param arrayList
     * @return
     */
    public String newsql(ArrayList<String> arrayList) {
        String newsql = "(";
        for (String a:arrayList){
            newsql = newsql + a + ",";
        }
        int index = newsql.lastIndexOf(",");
        newsql = newsql.substring(0,index);
        newsql = newsql + ")";
        return newsql;
    }
}
