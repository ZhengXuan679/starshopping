package cn.com.starshopping.dao.zql;

import cn.com.starshopping.pojo.CaoZuoRiZhi;
import cn.com.starshopping.pojo.DingDanSouSuo;
import cn.com.starshopping.util.Mydb;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ZouQinLun
 * Date: 2019/01/05
 * Description:
 * Version: V1.0
 */
public class DingDanDao {
    /**
     * 此方法通过传入用户名，在数据库里查询该用户名下，所有订单状态为0的订单的数量
     *
     * @param yongHuMing
     * @return
     */
    public int count(String yongHuMing) {
        int count = 0;
        String sql = "select count(id) from dindan where DingDanZhuangTai =0 and YongHuMing=?";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1, yongHuMing);
            ResultSet rs = ps.executeQuery();
            rs.next();
            count = rs.getInt("count(id)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 该方法通过传入用户名，每页条数，以及起始下标，分页查找该用户名下，订单状态为0的订单想详情，并将每单订单以JSONObject形式按时间倒序的顺序封装入JSONArray，返回此JSONArray
     *
     * @param yongHuMing
     * @param meiyetiaoshu
     * @param qishixiabiao
     * @return
     */
    public JSONArray dingDanDao(String yongHuMing, int meiyetiaoshu, int qishixiabiao) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        JSONArray jsonArray1 = null;
        JSONArray jsonArray2 = null;
        String shangPinBianHao = "";
        String sql = "select id,DingDanBianHao,YongHuMing,ShangPinXinXi,WuLiuDanHao,DingDanZongE,DingDanShiJian,DingDanZhuangTai,ShouHuoDiZhi from dindan where DingDanZhuangTai=0 and YongHuMing=? order by DingDanShiJian desc limit ?,?";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);

            ps.setString(1, yongHuMing);                 //将用户名传入sql语句的第zx个问号
            ps.setInt(2, qishixiabiao);                  //将起始下标传入sql语句的第二个问号
            ps.setInt(3, meiyetiaoshu);                  //将每页条数传入sql语句的第三个问号

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jsonObject = new JSONObject();              //创建新的JSONObject
                jsonObject.put("id", rs.getInt("id"));   //将id封装到JSONObject
                jsonObject.put("dingDanBianHao", rs.getString("DingDanBianHao"));//将订单编号封装到JSONObject
                jsonObject.put("yongHuMing", rs.getString("YongHuMing"));         //将用户名封装到JSONObject
                jsonArray1 = new JSONArray(rs.getString("ShangPinXinXi"));       //得到商品信息的JSONArray1
                jsonArray2 = new JSONArray();                                       //创建新的JSONArray，用于存放拼装的商品信息
                for (int i = 0; i < jsonArray1.length(); i++) {                       //循环获取商品信息
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);           //以JSONObject的格式得到第i种商品的商品信息
                    shangPinBianHao = jsonObject1.getString("shangpinbianhao");//从商品信息的JSONObjet中得到商品编号
                    String sql1 = "select ShangPinMingChen,ShangPinGuiGe1,ShangPinGuiGe2 from shangpinxinxi where ShangPinBianHao=?";//通过商品编号在商品信息表搜索商品名称，规格1，规格2
                    PreparedStatement ps1 = Mydb.myConnection.prepareStatement(sql1);
                    ps1.setString(1, shangPinBianHao);
                    ResultSet resultSet = ps1.executeQuery();
                    resultSet.next();
                    String shangPinMingChen = resultSet.getString("ShangPinMingChen");//得到商品名称
                    JSONObject jsonObject2 = new JSONObject();                      //创建JSONObject，用于封装组装的该种商品信息
                    jsonObject2.put("shangPinMingChen", shangPinMingChen);           //将商品名称，商品数量，商品单价，商品总价等商品信息封装进JSONObject
                    jsonObject2.put("shangPinShuLiang", jsonObject1.getInt("shangpinshuliang"));
                    jsonObject2.put("shangPinDanJia", jsonObject1.getDouble("shangpindanjia"));
                    jsonObject2.put("shangPinZongJia", jsonObject1.getDouble("shangpinzongjia"));
                    if (resultSet.getString("ShangPinGuiGe1") == null) {                //如果商品规格1为null，则将规格1内容封装为空
                        jsonObject2.put("shangPinGuiGe1", "");
                    } else {
                        jsonObject2.put("shangPinGuiGe1", resultSet.getString("ShangPinGuiGe1")); //如果商品规格1不为null，则将规格1封装进JSONObject
                    }
                    if (resultSet.getString("ShangPinGuiGe2") == null) {                 //如果商品规格2为null，则将规格2内容封装为空
                        jsonObject2.put("shangPinGuiGe2", "");
                    } else {
                        jsonObject2.put("shangPinGuiGe2", resultSet.getString("ShangPinGuiGe2"));//如果商品规格2不为null，则将规格2封装进JSONObject
                    }

                    jsonArray2.put(jsonObject2);        //将拼装的JSONObject封装进JSONArray
                    resultSet.close();
                    ps1.close();
                }
                jsonObject.put("shangPinXinXi", jsonArray2); //将装有商品信息的JSONArray封装进JSONObject
                jsonObject.put("wuLiuDanHao", rs.getString("WuLiuDanHao"));//将物流单号封装进JSONObject
                jsonObject.put("dingDanZongE", rs.getDouble("DingDanZongE"));//将订单总额封装进JSONObject
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//格式化时间
                String shiJian = df.format(rs.getTimestamp("DingDanShiJian"));
                jsonObject.put("dingDanShiJian", shiJian);                      //将格式化后的时间封装进JSONObject
                jsonObject.put("dingDanZhuangTai", rs.getInt("DingDanZhuangTai"));//将订单状态封装进JSONObject
                jsonObject.put("shouHuoDiZhi", rs.getString("ShouHuoDiZhi"));//将收货地址封装进JSONObject
                jsonObject.put("shangPinZuBianHao", shangPinBianHao.substring(0, shangPinBianHao.indexOf("-")));//将商品组编号封装进JSONObject
                jsonArray.put(jsonObject);                  //将含有此项订单全部信息的JSONObject封装进JSONArray
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jsonArray;           //返回JSONArray
    }

    /**
     * //根据传递过来的订单号，修改订单状态为1，返回boolean值，true表示修改成功，false表示修改失败
     *
     * @param dingDanHao
     * @return
     */
    public boolean shanChu(String dingDanHao) {
        boolean action = false;
        String sql = "update dindan set DingDanZhuangTai=1 where DingDanBianHao =?";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1, dingDanHao);
            ps.execute();
            action = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return action;
    }

    /**
     * 此方法用于响应订单管理前端的非订单号搜索，通过传入DingDanSouSuo的对象（封装了搜索条件），
     * 每页显示条数，起始下标，用户名，进行分页查询
     *
     * @param dingDanSouSuo
     * @param meiyetiaoshu
     * @param qishixiabiao
     * @param yongHuMing
     * @return
     */
    public JSONArray souSuo(DingDanSouSuo dingDanSouSuo, int meiyetiaoshu, int qishixiabiao, String yongHuMing) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        JSONArray jsonArray1 = null;
        JSONArray jsonArray2 = null;
        String shangPinBianHao = "";
        String sql = "select id,DingDanBianHao,YongHuMing,ShangPinXinXi,WuLiuDanHao,DingDanZongE,DingDanShiJian,DingDanZhuangTai,ShouHuoDiZhi from dindan where DingDanZhuangTai=0 and YongHuMing=?";
        String sqlJ = sousuo(dingDanSouSuo);                                                  //调用sousuo方法，返回拼装的字符串
        sql = sql + sqlJ + " order by DingDanShiJian DESC limit " + qishixiabiao + "," + meiyetiaoshu;  //拼装sql语句
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1, yongHuMing);                             //将用户名传入sql语句的第zx个问号
            List<String> list = list(dingDanSouSuo);                   //调用list方法，返回zx个String的list
            int count = 2;  // 占位符位数
            for (int i = 0; i < list.size() - 1; i++) {                  //循环list，到list.size()-1位（最后zx个关键词已经拼装入sql语句了）
                if (list.get(i) != null && !list.get(i).equals("")) {   //如果list(i)不为null且不为空，则将list(i)拼装到第count个问号，count++
                    ps.setString(count, list.get(i));
                    count++;
                }
            }
            ResultSet rs = ps.executeQuery();   //执行查询语句，返回结果集
            while (rs.next()) {
                jsonObject = new JSONObject();  //创建新的JSONObject
                jsonObject.put("id", rs.getInt("id"));   //将id封装到JSONObject
                jsonObject.put("dingDanBianHao", rs.getString("DingDanBianHao"));    //将订单编号封装到JSONObject
                jsonObject.put("yongHuMing", rs.getString("YongHuMing"));            //将用户名封装到JSONObject
                jsonArray1 = new JSONArray(rs.getString("ShangPinXinXi"));          //得到商品信息的JSONArray1
                jsonArray2 = new JSONArray();                                          //创建新的JSONArray，用于存放拼装的商品信息
                for (int i = 0; i < jsonArray1.length(); i++) {                          //循环商品信息
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);              //以JSONObject的格式得到第i种商品的商品信息
                    shangPinBianHao = jsonObject1.getString("shangpinbianhao");//从商品信息的JSONObjet中得到商品编号
                    String sql1 = "select ShangPinMingChen,ShangPinGuiGe1,ShangPinGuiGe2 from shangpinxinxi where ShangPinBianHao=?";//通过商品编号在商品信息表搜索商品名称，规格1，规格2
                    PreparedStatement ps1 = Mydb.myConnection.prepareStatement(sql1);
                    ps1.setString(1, shangPinBianHao);
                    ResultSet resultSet = ps1.executeQuery();
                    resultSet.next();
                    String shangPinMingChen = resultSet.getString("ShangPinMingChen"); //得到商品名称
                    JSONObject jsonObject2 = new JSONObject();                          //创建JSONObject，用于封装组装的该种商品信息
                    jsonObject2.put("shangPinMingChen", shangPinMingChen);                //将商品名称，商品数量，商品单价，商品总价等商品信息封装进JSONObject
                    jsonObject2.put("shangPinShuLiang", jsonObject1.getInt("shangpinshuliang"));
                    jsonObject2.put("shangPinDanJia", jsonObject1.getDouble("shangpindanjia"));
                    jsonObject2.put("shangPinZongJia", jsonObject1.getDouble("shangpinzongjia"));
                    if (resultSet.getString("ShangPinGuiGe1") == null) {            //如果商品规格1为null，则将规格1内容封装为空
                        jsonObject2.put("shangPinGuiGe1", "");
                    } else {
                        jsonObject2.put("shangPinGuiGe1", resultSet.getString("ShangPinGuiGe1"));    //如果商品规格1不为null，则将规格1封装进JSONObject
                    }
                    if (resultSet.getString("ShangPinGuiGe2") == null) {            //如果商品规格2为null，则将规格2内容封装为空
                        jsonObject2.put("shangPinGuiGe2", "");
                    } else {
                        jsonObject2.put("shangPinGuiGe2", resultSet.getString("ShangPinGuiGe2"));  //如果商品规格2不为null，则将规格2封装进JSONObject
                    }

                    jsonArray2.put(jsonObject2);    //将拼装的JSONObject封装进JSONArray
                    resultSet.close();
                    ps1.close();
                }
                jsonObject.put("shangPinXinXi", jsonArray2); //将装有商品信息的JSONArray封装进JSONObject
                jsonObject.put("wuLiuDanHao", rs.getString("WuLiuDanHao"));//将物流单号封装进JSONObject
                jsonObject.put("dingDanZongE", rs.getDouble("DingDanZongE"));//将订单总额封装进JSONObject
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//格式化时间
                String shiJian = df.format(rs.getTimestamp("DingDanShiJian"));
                jsonObject.put("dingDanShiJian", shiJian);                   //将格式化后的时间封装进JSONObject
                jsonObject.put("dingDanZhuangTai", rs.getInt("DingDanZhuangTai"));//将订单状态封装进JSONObject
                jsonObject.put("shouHuoDiZhi", rs.getString("ShouHuoDiZhi"));//将收货地址封装进JSONObject
                jsonObject.put("shangPinZuBianHao", shangPinBianHao.substring(0, shangPinBianHao.indexOf("-")));//将商品组编号封装进JSONObject
                jsonArray.put(jsonObject);  //将含有此项订单全部信息的JSONObject封装进JSONArray
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonArray;                   //返回JSONArray
    }

    public int souSuoCount(DingDanSouSuo dingDanSouSuo, String yongHuMing) {       //此方法得到输入搜索信息后得到的信息总条数
        int count = -1;
        String sql = "select count(id) from dindan where DingDanZhuangTai=0 and YongHuMing=?";
        String sqlJ = sousuo(dingDanSouSuo);        //传入DingDanSouSuo对象调用sousuo方法，组装字符串
        sql = sql + sqlJ;                //组装sql语句

        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1, yongHuMing);     //将用户名set进sql语句的第zx个问号
            List<String> list = list(dingDanSouSuo);        //调用list方法返回zx个String list;
            int c = 2;  // 占位符位数
            for (int i = 0; i < list.size() - 1; i++) {       //循环list
                if (list.get(i) != null && !list.get(i).equals("")) {   //如果list(i)不等于null，且不等于空，则在SQL语句第c个问号set进list(i)
                    ps.setString(c, list.get(i));
                    c++;                    //每zx次成功给sql语句的？set值后,c自增
                }
            }

            ResultSet rs = ps.executeQuery();   //执行查询
            rs.next();
            count = rs.getInt("count(id)");//基于搜索条件，得到该用户搜索出的订单信息条数
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;                  //返回搜索出的订单信息总条数

    }

    /**
     * 此方法根据传入的DingDanSouSuo对象（封装有搜索条件），调用list方法返回list，根据返回的list拼装字符串
     *
     * @param dingDanSouSuo
     * @return
     */
    public String sousuo(DingDanSouSuo dingDanSouSuo) {
        List<String> list = list(dingDanSouSuo);    //调用list方法，通过传入DingDanSouSuo对象，返回zx个list
        String[] sqlT = {"DingDanShiJian>=?", "DingDanShiJian<=?", "DingDanZongE >=?", "DingDanZongE <=?", "ShangPinXinXi like '%" + dingDanSouSuo.getGuanJianCi() + "%'"};
        String sqlJ = "";
        for (int i = 0; i < list.size(); i++) { //循环list的长度的次数，依次拼装字符串
            if (list.get(i) != null && !list.get(i).equals("")) {   //如果list的第i个元素不为nul且不为0，则将sqlT的对应字符拼装进字符串
                sqlJ += " and " + sqlT[i];
            }
        }


        return sqlJ;
    }

    /**
     * 此方法通过传入DingDanSouSuo对象，将DingDanSouSuo中封装的每样搜索条件加入list，最后返回此list
     *
     * @param dingDanSouSuo
     * @return
     */
    public List list(DingDanSouSuo dingDanSouSuo) {
        List<String> list = new ArrayList<>();
        list.add(dingDanSouSuo.getQiShiRiQi());
        if (dingDanSouSuo.getZhongZhiRiQi() != null && !dingDanSouSuo.getZhongZhiRiQi().equals("")) {
            list.add(dingDanSouSuo.getZhongZhiRiQi() + 1);
        } else {
            list.add(dingDanSouSuo.getZhongZhiRiQi());
        }
        list.add(dingDanSouSuo.getJiaGeXiaXian());
        list.add(dingDanSouSuo.getJiaGeShangXian());
        list.add(dingDanSouSuo.getGuanJianCi());
        return list;
    }

    /**
     * 此方法传入用户名，订单号，得到此订单的所有详细信息，封装成zx个JSONObject，装入JSONArray中返回
     *
     * @param yongHuMing
     * @param dingDanHao
     * @return
     */
    public JSONArray dingDanHaoSouSuo(String yongHuMing, String dingDanHao) {
        String shangPinBianHao = "";
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        String sql = "select id,DingDanBianHao,YongHuMing,ShangPinXinXi,WuLiuDanHao,DingDanZongE,DingDanShiJian," +
                "DingDanZhuangTai,ShouHuoDiZhi from dindan where YongHuMing=? and DingDanZhuangTai=0 and  DingDanBianHao =?";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1, yongHuMing);
            ps.setString(2, dingDanHao);
            ResultSet rs = ps.executeQuery();
            rs.next();
            jsonObject.put("id", rs.getInt("id"));
            jsonObject.put("dingDanBianHao", rs.getString("DingDanBianHao"));
            jsonObject.put("yongHuMing", rs.getString("YongHuMing"));
            JSONArray jsonArray1 = new JSONArray(rs.getString("ShangPinXinXi"));
            JSONArray jsonArray2 = new JSONArray();
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                shangPinBianHao = jsonObject1.getString("shangpinbianhao");
                String sql1 = "select ShangPinMingChen,ShangPinGuiGe1,ShangPinGuiGe2 from shangpinxinxi where ShangPinBianHao=?";
                PreparedStatement ps1 = Mydb.myConnection.prepareStatement(sql1);
                ps1.setString(1, shangPinBianHao);
                ResultSet resultSet = ps1.executeQuery();
                resultSet.next();
                String shangPinMingChen = resultSet.getString("ShangPinMingChen");
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("shangPinMingChen", shangPinMingChen);
                jsonObject2.put("shangPinShuLiang", jsonObject1.getInt("shangpinshuliang"));
                jsonObject2.put("shangPinDanJia", jsonObject1.getDouble("shangpindanjia"));
                jsonObject2.put("shangPinZongJia", jsonObject1.getDouble("shangpinzongjia"));
                if (resultSet.getString("ShangPinGuiGe1") == null) {
                    jsonObject2.put("shangPinGuiGe1", "");
                } else {
                    jsonObject2.put("shangPinGuiGe1", resultSet.getString("ShangPinGuiGe1"));
                }
                if (resultSet.getString("ShangPinGuiGe2") == null) {
                    jsonObject2.put("shangPinGuiGe2", "");
                } else {
                    jsonObject2.put("shangPinGuiGe2", resultSet.getString("ShangPinGuiGe2"));
                }

                jsonArray2.put(jsonObject2);
                resultSet.close();
                ps1.close();
            }
            jsonObject.put("shangPinXinXi", jsonArray2);
            jsonObject.put("wuLiuDanHao", rs.getString("WuLiuDanHao"));
            jsonObject.put("dingDanZongE", rs.getDouble("DingDanZongE"));
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String shiJian = df.format(rs.getTimestamp("DingDanShiJian"));
            jsonObject.put("dingDanShiJian", shiJian);
            jsonObject.put("dingDanZhuangTai", rs.getInt("DingDanZhuangTai"));
            jsonObject.put("shouHuoDiZhi", rs.getString("ShouHuoDiZhi"));
            jsonObject.put("shangPinZuBianHao", shangPinBianHao.substring(0, shangPinBianHao.indexOf("-")));
            jsonArray.put(jsonObject);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    /**
     * 传入CaoZuoRiZhi对象，向操作日志表插入操作日志，包括用户名，操作记录，操作时间成功返回1，失败返回0
     *
     * @param caoZuoRiZhi
     * @return
     */
    public int caoZuoRiZhi(CaoZuoRiZhi caoZuoRiZhi) {
        int count = 0;
        String sql = "insert into caozuorizhi(yonghuming, caozuojilu, shijian) values (?,?,?)";
        try {
            PreparedStatement ps = Mydb.myConnection.prepareStatement(sql);
            ps.setString(1, caoZuoRiZhi.getYongHuMing());
            ps.setString(2, caoZuoRiZhi.getCaoZuoJiLu());
            ps.setTimestamp(3, caoZuoRiZhi.getShiJian());
            count = ps.executeUpdate();
            ps.close();
            ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}

