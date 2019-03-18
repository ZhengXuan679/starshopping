package cn.com.starshopping.dao;


import cn.com.starshopping.util.Mydb;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: huxiaoyi
 * Date: 2019/01/07
 * Description:
 * Version: V1.0
 */
public class KeFuXinXiDao {
    /**
     * 查看客服信息
     * @param xingming  传入的客服姓名
     * @return  返回客服的个人信息
     */
    public JSONObject chakan(String xingming){
        String cha_sql = "select xingming,shoujihaoma,shenfenzheng,qiandaocishu,youxiang,"+
                "xingbie,zuihouqiandao from kefudenglu where xingming = ?";
        PreparedStatement preparedStatement = null;
        JSONObject json = new JSONObject();
        try {
            preparedStatement = Mydb.myConnection.prepareStatement(cha_sql);
            preparedStatement.setString(1,xingming);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                json.put("xingming",rs.getString("xingming"));
                json.put("shoujihaoma",rs.getString("shoujihaoma"));
                json.put("shenfenzheng",rs.getString("shenfenzheng"));
                json.put("qiandaocishu",rs.getInt("qiandaocishu"));
                json.put("youxiang",rs.getString("youxiang"));
                json.put("xingbie",rs.getString("xingbie"));
                //得到上次签到的时间
                String qiandaotime = rs.getString("zuihouqiandao");
                json.put("zuihouqiandao",qiandaotime);
                //得到上次签到时间的年   月   日
                int year = Integer.valueOf(qiandaotime.substring(0,4));
                int month = Integer.valueOf(qiandaotime.substring(5,7));
                int day = Integer.valueOf(qiandaotime.substring(8,10));
                //得到上次签到月份有多少天
                Calendar a =Calendar.getInstance();
                a.set(Calendar.YEAR,year);
                a.set(Calendar.MONTH, month- 1);
                a.set(Calendar.DATE, 1);
                a.roll(Calendar.DATE,-1);
                int maxday =a.get(Calendar.DATE);
                //得到可以签到的时间
                String yi = "-";
                String er = "-";
                if(day+1>maxday){
                    day = 1;
                    er = er + 0;
                    month = month+1;
                    if(month>12){
                        month = 1;
                        yi = yi + 0;
                        year = year+1;
                    }
                }
                String qiandao = ""+year + yi + month + er + (day+1) + " 00:00:00.0";
                Timestamp nowtime = new Timestamp(System.currentTimeMillis());
                //当前
                if(Timestamp.valueOf(qiandao).before(nowtime)){
                    json.put("qiandao",true);
                }
                else{
                    json.put("qiandao",false);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 签到功能
     *
     * @param xingming  前台传入的客服姓名
     * @return  action 返回是否成功的状态
     */
    public boolean qiandao(String xingming){
        boolean action = false;
        String cha_sql = "select qiandaocishu,zuihouqiandao from kefudenglu where xingming = ?";
        String qiandao_sql = "update kefudenglu set QianDaoCiShu = ?,zuihouqiandao = ?"+
                "where xingming = ? ";
        int qiandaocishu;
        try {
            PreparedStatement preparedStatement = Mydb.myConnection.prepareStatement(cha_sql);
            preparedStatement.setString(1,xingming);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                qiandaocishu = rs.getInt("qiandaocishu")+1;
                //记录当前时间，值保留年月日
                String nowtime = (new Timestamp(System.currentTimeMillis())).toString().substring(0,10);
                preparedStatement = Mydb.myConnection.prepareStatement(qiandao_sql);
                preparedStatement.setInt(1,qiandaocishu);
                preparedStatement.setString(2,nowtime);
                preparedStatement.setString(3,xingming);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                action = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return action;
    }

}
