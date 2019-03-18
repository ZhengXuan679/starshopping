package cn.com.starshopping.dao;

import cn.com.starshopping.util.Mydb;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class XiaoLiangTongJiDao {
    /**
     * 统计查询
     * 该方法用于查询某时间段内的商品销售统计
     *
     * @param startime 查询的起始时间
     * @param endtime  查询的终止时间
     * @return 返回一个满足layui所需格式的JsonObject
     */
    public JSONArray xiaoLiang(String startime, String endtime) {
        JSONArray xiaoLiangTongJi = new JSONArray();
        JSONObject jsonObject = null;
        String bianhao = "";
        int number = 0;
        //sql语句查询订单信息表中商品信息（jsonArrary）中的商品编号和商品数量
        String sql = "SELECT  ShangPinXinXi->'$[*].shangpinbianhao'as bianhao,ShangPinXinXi->'$[*].shangpinshuliang'as number FROM dindan where  DingDanShiJian BETWEEN ? AND ?;";
        try {
            PreparedStatement ps = null;
            ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1, startime);
            ps.setString(2, endtime);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String bianhao1 = rs.getString("bianhao");
                JSONArray bianhaoJA = new JSONArray(bianhao1);//查询出的商品编号数组放入JsonArray
                JSONArray numberJA = new JSONArray(rs.getString("number"));//查询出的商品编号对应数量的数组放入JsonArray

                for (int i = 0; i < bianhaoJA.length(); i++) {
                    bianhao = bianhaoJA.getString(i); //遍历获取编号JSONArray中的值
                    number = numberJA.getInt(i);//遍历获取数量JSONArray中的值
                    //如果销量统计（JSONArray）的长度为0，则将获取的编号和数量放入一个JsonObject，并将该JsonObject放入销量统计JsonArray
                    if (xiaoLiangTongJi.length() == 0) {
                        jsonObject = new JSONObject();
                        jsonObject.put("bianhao", bianhao);
                        jsonObject.put("number", number);
                        xiaoLiangTongJi.put(jsonObject);
                    }
                    //如果销量统计（JSONArray）的长度不为0，则对销量统计进行遍历
                    else {
                        for (int j = 0; j < xiaoLiangTongJi.length(); j++) {
                            String bh = xiaoLiangTongJi.getJSONObject(j).getString("bianhao");
                            if (bh.equals(bianhao)) {//如果查询出的编号在销量统计中存在，则更新其数量为2者总和
                                number = number + xiaoLiangTongJi.getJSONObject(j).getInt("number");
                                xiaoLiangTongJi.getJSONObject(j).put("number", number);
                                break;

                            } else {//如果查询出的编号在销量统计中不存在
                                if (j == xiaoLiangTongJi.length() - 1) {//并且已经遍历到销量统计的最后个JsonObject，则将获取的编号和数量放入一个JsonObject，并将该JsonObject放入销量统计JsonArray
                                    jsonObject = new JSONObject();
                                    jsonObject.put("bianhao", bianhao);
                                    jsonObject.put("number", number);
                                    xiaoLiangTongJi.put(jsonObject);
                                    System.out.println(xiaoLiangTongJi);
                                    break;
                                } else {
                                    continue;
                                }
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return xiaoLiangTongJi;
    }

    /**
     * 分页封装
     * 该方法用于对xiaoLiangTongJi得到的数据进行真分页，并封装成layui所需的json
     *
     * @param xiaoLiangTongJi 包含商品编号和对应数量的JSONArray数组
     * @param startime        查询的起始时间
     * @param endtime         查询的结束时间
     * @param page            页码
     * @param limit           每页最大条数
     * @return layui所需的json格式
     */
    public JSONObject tongJi(JSONArray xiaoLiangTongJi, String startime, String endtime, int page, int limit) {
        int count = xiaoLiangTongJi.length();//总条数=销量统计（JsonArray）的长度
        int pageindex = (page - 1) * limit;//根据页码数获取查询信息的起始下标
        int pagemax = page * limit;//分页中获取到的信息最大下标
        String bianhao = "";
        String mingchen = "";
        int number = 0;
        JSONArray jsa = new JSONArray();
        JSONObject jso = new JSONObject();
        JSONObject json = null;
        if (pagemax > count) {//如果page*limit>总条数，则令pagemax=count
            pagemax = count;
        }
        for (int k = pageindex; k < pagemax; k++) {//获取分页所需的编号和数量
            bianhao = xiaoLiangTongJi.getJSONObject(k).getString("bianhao");
            number = xiaoLiangTongJi.getJSONObject(k).getInt("number");
            //通过获得的商品编号查询商品名称，成本价，价格
            String sql2 = "select ShangPinMingChen,ShangPinChengBen,ShangPinJiaGe from shangpinxinxi where ShangPinBianHao = ?";
            PreparedStatement ps = null;
            try {
                ps = Mydb.myConnection.prepareStatement(sql2);

                ps.setString(1, bianhao);
                ResultSet rs2 = ps.executeQuery();
                rs2.next();
                json = new JSONObject();
                mingchen = rs2.getString("ShangPinMingChen");
                double chengbenjia = rs2.getDouble("ShangPinChengBen");
                double jiage = rs2.getInt("ShangPinJiaGe");
                double chengben = chengbenjia * number;
                double xiaoshoue = jiage * number;
                json.put("bianhao", bianhao);
                json.put("number", number);
                json.put("mingchen", mingchen);
                json.put("chengbenjia", chengbenjia);
                json.put("jiage", jiage);
                json.put("shiduan", startime + "---" + endtime);
                json.put("chengben", chengben);
                json.put("xiaoshoue", xiaoshoue);
                json.put("lirun", xiaoshoue - chengben);
                jsa.put(json);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        //组装layui所需的Json
        jso.put("code", 0);
        jso.put("msg", "");
        jso.put("count", count);
        jso.put("data", jsa);
        return jso;
    }

    /**
     * 图标数据
     * 该方法用于向前台传递图表所需的数据
     *
     * @param xiaoLiangTongJi 包含商品编号和对应数量的JSONArray数组
     * @return 一个包含商品名，和对应成本总价和利润的JsonObject
     */
    public JSONObject tubiao(JSONArray xiaoLiangTongJi) {
        String bianhao = "";
        int number = 0;
        JSONObject json = null;
        String mingchen = "";
        JSONArray jsa = new JSONArray();
        for (int k = 0; k < xiaoLiangTongJi.length(); k++) {
            bianhao = xiaoLiangTongJi.getJSONObject(k).getString("bianhao");
            number = xiaoLiangTongJi.getJSONObject(k).getInt("number");
            //通过获得的商品编号查询商品名称，成本价，价格
            String sql3 = "select ShangPinMingChen,ShangPinChengBen,ShangPinJiaGe from shangpinxinxi where ShangPinBianHao = ?";
            PreparedStatement ps = null;
            try {
                ps = Mydb.myConnection.prepareStatement(sql3);
                ps.setString(1, bianhao);
                ResultSet rs3 = ps.executeQuery();
                rs3.next();
                json = new JSONObject();
                mingchen = rs3.getString("ShangPinMingChen");
                double chengbenjia = rs3.getDouble("ShangPinChengBen");
                double jiage = rs3.getInt("ShangPinJiaGe");
                double chengben = chengbenjia * number;
                double xiaoshoue = jiage * number;
//                json.put("number",number);
                json.put("mingchen", mingchen);
//                json.put("chengbenjia",chengbenjia);
//                json.put("jiage",jiage);
                json.put("chengben", chengben);
//                json.put("xiaoshoue",xiaoshoue);
                json.put("lirun", xiaoshoue - chengben);
                jsa.put(json);
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
        //组装所需的Json
        JSONObject jso = new JSONObject();
        jso.put("data", jsa);
        return jso;
    }

    /**
     * 操作记录
     * 该方法用于向sql插入用户名，监听到的用户操作，操作时间
     *
     * @param actions
     * @param yongHuMing
     */
    public void jilu(String actions, String yongHuMing) {
        String sql = "insert into houtaicaozuorizhi(yonghuming, caozuojilu, shijian) values(?,?,now()) ";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1, yongHuMing);
            ps.setString(2, actions);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当日时间
     * 该方法用于获取当日0点和第二日0点（当日24点）的时间（ yyyy-MM-dd HH：mm：ss）
     *
     * @return 一个包含起始时间和终止时间的数组
     */
    public String[] todaydate() {
        LocalDate today = LocalDate.now();
        String startime = today.toString() + "  00：00：00";//今天0点
        String endtime = today.plusDays(1).toString() + "  00：00：00";//第二天0点
        String[] todaydate = {startime, endtime};
        return todaydate;
    }

    /**
     * 本周时间
     * 该方法用于获取本周一0点和下周一0点（本周日24点）的时间（ yyyy-MM-dd HH：mm：ss）
     *
     * @return 一个包含起始时间和终止时间的数组
     */
    public String[] weekdate() {
        LocalDate today = LocalDate.now();
        LocalDate star = today.with(DayOfWeek.MONDAY);
        LocalDate end = star.plusDays(7);
        String startime = star.toString() + "  00：00：00";
        String endtime = end.toString() + "  00：00：00";
        String[] weekdate = {startime, endtime};
        return weekdate;
    }

    /**
     * 本周时间
     * 该方法用于获取本月1号0点和下月11号0点（本月最后一天24点）的时间（ yyyy-MM-dd HH：mm：ss）
     *
     * @return 一个包含起始时间和终止时间的数组
     */
    public String[] monthdate() {
        LocalDate today = LocalDate.now();
        String startime = today.with(TemporalAdjusters.firstDayOfMonth()).toString() + "  00：00：00";
        String endtime = today.with(TemporalAdjusters.firstDayOfNextMonth()).toString() + "  00：00：00";
        String[] monthdate = {startime, endtime};
        return monthdate;
    }

    /**
     * 本季时间
     * 该方法用于获取本季度第一天0点和下季度第一天0点（本季度最后一天24点）的时间（ yyyy-MM-dd HH：mm：ss）
     *
     * @return 一个包含起始时间和终止时间的数组
     */
    public String[] quarterdate() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        String startime = "";
        String endtime = "";
        if (month >= 1 && month <= 3) {
            startime = "" + year + "-01-01" + "  00：00：00";
            endtime = "" + year + "-04-01" + "  00：00：00";
        } else if (month >= 4 && month <= 6) {
            startime = "" + year + "-04-01" + "  00：00：00";
            endtime = "" + year + "-07-01" + "  00：00：00";
        } else if (month >= 7 && month <= 9) {
            startime = "" + year + "-07-01" + "  00：00：00";
            endtime = "" + year + "-10-01" + "  00：00：00";
        } else if (month >= 10 && month <= 12) {
            year = year + 1;
            startime = "" + year + "-10-01" + "  00：00：00";
            endtime = "" + year + "-01-01" + "  00：00：00";
        }
        String[] quarterdate = {startime, endtime};
        return quarterdate;
    }

    /**
     * 年时间
     * 该方法用于获取今年1月1号0点和明年1月1号0点（今年12月31号24点）的时间（ yyyy-MM-dd HH：mm：ss）
     *
     * @return 一个包含起始时间和终止时间的数组
     */
    public String[] yeardate() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        String startime = "" + year + "-01-01" + "  00：00：00";
        year = year + 1;
        String endtime = "" + year + "-01-01" + "  00：00：00";
        String[] quarterdate = {startime, endtime};
        return quarterdate;
    }

    public void monthdelete() {
        String sql = "update shangpinxinxi set YueXiaoLiang = YueXiaoLiang+1 where id=1";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
