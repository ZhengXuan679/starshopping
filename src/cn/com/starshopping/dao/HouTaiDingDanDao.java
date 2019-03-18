package cn.com.starshopping.dao;

import cn.com.starshopping.pojo.DinDan;
import cn.com.starshopping.pojo.ShangPinXinXi;
import cn.com.starshopping.util.Mydb;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yanmin
 * Date: 2019/01/05
 * Description: 后台订单数据操作层
 * Version: V1.0
 */
public class HouTaiDingDanDao {

    /**
     * 查询订单方法
     * 这个方法通过用户输入的时间范围，用户名，商品名称，订单编号，订单金额范围，页码进行订单查询
     * sql通过循环拼接的方法进行查询
     * 因为需要分页，所以此方法访问两次数据库，第一次查询到数据的总条数，第二次才查询到结果
     * 通过数据库的查询结果组装成一个JSONObject存入JSONArray中返回到service层
     * @param dinDan   订单表pojo对象
     * @param endShiJian  时间范围结尾时间
     * @param endDingDanZongJia   订单金额范围结尾的订单金额
     * @param yeMa   limit分页开始下标
     * @return       查询成功返回结果JSONArray，失败返回JSONArray 为空
     */
    public JSONArray chaXun(DinDan dinDan, String endShiJian, int endDingDanZongJia, int yeMa) {
        JSONArray jsonArray = new JSONArray();
        String sql = "select id,DingDanBianHao,YongHuMing,ShangPinXinXi,WuLiuDanHao,DingDanZongE,DingDanShiJian,DingDanZhuangTai,ShouHuoDiZhi from dindan";  // 查询所有订单细信息
        String[] dingDanBianHao = null;                                       // 定义一个字符串来组装订单编号
        String sqlFenYe = "select count(id) from dindan";
        if (dinDan.getDingDanHao() != null && dinDan.getDingDanHao().size() != 0) {  // 如果订单编号不等于空
            dingDanBianHao = new String[dinDan.getDingDanHao().size()];
            for (int i = 0; i < dinDan.getDingDanHao().size(); i++) {  // 循环取出元素，组装为一个{‘a’,'b','c'} 类型的数组
                dingDanBianHao[i] = dinDan.getDingDanHao().get(i);   // 将取出的订单编号存入数组
            }
        }
        List<String> list = new ArrayList<>();
        list.add(dinDan.getDingDanShiJian());          // 开始时间存入list
        list.add(endShiJian);                                 // 存入结束时间
        list.add(dinDan.getYongHuMing());               // 存入用户名
        list.add(dinDan.getShouHuoDiZhi());         // 存入收货地址
        list.add(String.valueOf(dinDan.getDingDanZongE()));            // 存入订单总额,订单总额由int数据类型转换为String类型
        list.add(String.valueOf(endDingDanZongJia));            // 存入结束订单总额，由int数据类型转换为String类型
        String[] sqls = {"DingDanShiJian >= ?", "DingDanShiJian <= ?", "YongHuMing like ?",
                "ShouHuoDiZhi like ?", "DingDanZongE >= ?", "DingDanZongE <= ?"};  // 注意数组的元素要和list的元素对应
        String sqlJ = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null && !list.get(i).equals("") && !list.get(i).equals("-1")) {  // 如果对应的数据不为空,并且订单总额不为-1
                if (sqlJ.equals("")) {    // 如果以上条件第一次满足，sqlJ第一次拼接，那么添加一个where
                    sqlJ = " where " + sqls[i];
                } else {                // 如果sqlJ不是第一次拼接，那么加上and
                    sqlJ += " and " + sqls[i];
                }
            }
        }
        if (dingDanBianHao != null) {   // 如果订单编号组不为空
            String zhanWeiFu = "?";    // 定义一个字符串装占位符,订单编号不为空，至少有一个占位符
            for (int i = 1; i < dingDanBianHao.length; i++) {
                zhanWeiFu += ",?";
            }
            if (sqlJ.equals("")) {  //
                sqlJ += " where DingDanBianHao in(" + zhanWeiFu + ")";  // 占位符放里面
            } else {
                sqlJ += " and DingDanBianHao in(" + zhanWeiFu + ")";
            }
        }
        sql = sql + sqlJ;               // 拼接
        sqlFenYe += sqlJ;
        sql +=
                " ORDER BY DingDanShiJian desc" + " limit ?,10";
//        JDBC jdbc = new JDBC();
//        Connection conn = jdbc.getConnection();
        try {
            PreparedStatement pre = Mydb.myConnection.prepareStatement(sqlFenYe);  // 查询总条数
            int count = 0;  // 占位符位数
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) != null && !list.get(i).equals("") && !list.get(i).equals("-1")) {  // 如果对应的数据不为空,并且订单总额不为-1
                    if (i != 2 && i != 3) {     // 用户名和收货地址用模糊查询
                        pre.setString(count + 1, list.get(i));
                    } else {
                        pre.setString(count + 1, "%" + list.get(i) + "%");
                    }
                    count++;                 // 满足条件占位符个数加1
                }
            }
            if (dingDanBianHao != null) { // 如果订单编号组不为空
                for (int i = 0; i < dingDanBianHao.length; i++) {
                    pre.setString(count + 1, dingDanBianHao[i]);  // 订单编号组合成的字符串放入占位符
                    count++;
                }
            }
            ResultSet rs = pre.executeQuery(); // 结果集
            if (rs.next()) {  // 如果查询出结果，取出总条数
                int zongTiaoShu = rs.getInt(1);
                dinDan.setZongTiaoShu(zongTiaoShu);  // 总条数存入pojo
            }
            rs.close();
            pre = Mydb.myConnection.prepareStatement(sql);   // 查询结果
            count = 0;  // 占位符位数
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) != null && !list.get(i).equals("") && !list.get(i).equals("-1")) {  // 如果对应的数据不为空,并且订单总额不为-1
                    if (i != 2 && i != 3) {     // 用户名和收货地址用模糊查询
                        pre.setString(count + 1, list.get(i));
                    } else {
                        pre.setString(count + 1, "%" + list.get(i) + "%");
                    }
                    count++;                 // 满足条件占位符个数加1
                }
            }
            if (dingDanBianHao != null) { // 如果订单编号组不为空
                for (int i = 0; i < dingDanBianHao.length; i++) {
                    pre.setString(count + 1, dingDanBianHao[i]);  // 订单编号组合成的字符串放入占位符
                    count++;
                }
            }
            pre.setInt(count + 1, yeMa);  // 订单编号组合成的字符串放入占位符
            rs = pre.executeQuery();
            while (rs.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", rs.getInt("id"));  // 得到订单ID
                jsonObject.put("DingDanBianHao", rs.getString("DingDanBianHao"));  // 得到订单编号
                jsonObject.put("YongHuMing", rs.getString("YongHuMing"));  // 得到用户名
                jsonObject.put("ShangPinXinXi", new JSONArray(rs.getString("ShangPinXinXi")));  // 得到商品信息
                jsonObject.put("WuLiuDanHao", rs.getString("WuLiuDanHao"));  // 得到物流单号
                jsonObject.put("DingDanZongE", rs.getString("DingDanZongE"));  // 得到订单总额
                String shiJian = rs.getString("DingDanShiJian");  // 得到订单时间
                jsonObject.put("DingDanShiJian", shiJian.substring(0, shiJian.lastIndexOf(".")));  // 存入去了毫秒的订单时间
                jsonObject.put("DingDanZhuangTai", rs.getString("DingDanZhuangTai"));  // 得到订单状态
                jsonObject.put("ShouHuoDiZhi", rs.getString("ShouHuoDiZhi"));  // 得到收货地址
                jsonArray.put(jsonObject);       // 将这个JSONObject存入JSONArray中
            }
            rs.close();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }


    /**
     * 查询商品组编号
     * 此方法通过商品名称查询商品组编号
     * 查询出商品组编号组装为一个list
     * 此方法暂时未被使用
     * @param shangPinXinXi   商品信息pojo对象
     * @return  成功返回list，失败返回list为空
     */
    public List<String> chaXunZuBian(ShangPinXinXi shangPinXinXi) {
        List<String> list = new ArrayList<>();
//        JDBC jdbc = new JDBC();
//        Connection conn = jdbc.getConnection();
        String sql = "select distinct ShangPinZuBianHao from shangpinxinxi where ShangPinMingChen like ?;";
        try {
            PreparedStatement pre = Mydb.myConnection.prepareStatement(sql);
            pre.setString(1, "%" + shangPinXinXi.getShangPinMingChen() + "%");
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {   // 通过名称模糊查询到的组编号可能不止一个，这里sql语句进行去重后得到结果循环取出存入list
                list.add(rs.getString(1)); // 存入list
            }
            rs.close();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 查询订单编号
     * 通过用户输入的商品名查询到订单编号
     * 此方法为商品名称查询订单信息做基础
     * 此方法查询到的订单编号可能不止一条，所以查询结果组装为一个list
     * @param shangPinMingChen 商品名称
     * @return 查询成功返回订单编号有值的list，失败返回null
     */
    public List<String> chaXunDingDanBianHao(String shangPinMingChen) {
        if (shangPinMingChen == null || shangPinMingChen.equals("")) {   // 如果传入的shangPinMingChen为空，那么直接return一个null回去
            return null;
        }
        List<String> listJieGuo = new ArrayList<>();
        String sql = "select distinct DingDanBianHao from dindan where ShangPinXinXi like ?;";
        try {
            PreparedStatement pre = Mydb.myConnection.prepareStatement(sql);
            // 因为商品名称存储在商品信息JSON中，所以直接通过模糊查询字符串的形式去查询
            pre.setString(1, "%" + shangPinMingChen + "%");
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {   // 通过名称模糊查询到的商品编号不止一个，循环取出并存入list
                listJieGuo.add(rs.getString(1)); // 存入list
            }
            rs.close();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listJieGuo;
    }

    /**
     * 查询商品名称，商品规格
     * 此方法通过商品编号来查询商品名称和商品规格
     * 此方法将查询结果组装为一个String类型的数组
     * @param shangPinXinXi  商品信息pojo对象
     * @return     成功返回一个长度为3的数组，失败返回null
     */
    public String[] chaXunShangPinMingCheng(ShangPinXinXi shangPinXinXi) {
        String[] action = null;
        String sql = "select ShangPinMingChen,ShangPinGuiGe1,ShangPinGuiGe2 from shangpinxinxi where ShangPinBianHao = ?";
        try {
            PreparedStatement pre = Mydb.myConnection.prepareStatement(sql);
            pre.setString(1, shangPinXinXi.getShangPinBianHao());
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {   // 如果能查到结果,一个商品编号对应一条信息
                String[] s = new String[3];
                s[0] = rs.getString(1); // 得到查询到的商品名称
                s[1] = rs.getString(2); // 得到查询到的商品规格1
                s[2] = rs.getString(3); // 得到查询到的商品规格2
                action = s;
            }
            pre.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return action;
    }

    /**
     * 查询订单信息
     * 这个方法通过订单编号查询订单的所有信息
     * 这个方法用来显示订单详情
     * @param dinDan   订单pojo对象
     * @return  成功返回一个有值的JSONObject 失败返回一个空值的JSONObject
     */
    public JSONObject chaXunDingDan(DinDan dinDan) {
        JSONObject jsonObject = new JSONObject();
        // 因为订单编号是封装的一个list，这个方法因为页面传入的list是一个订单编号，所以只需要取第一个元素
        List<String> list = dinDan.getDingDanHao();
        String dingDanBianHao = list.get(0);   // 得到订单编号
        String sql = "select * from dindan where DingDanBianHao = ?";
        try {
            PreparedStatement pre = Mydb.myConnection.prepareStatement(sql);
            pre.setString(1, dingDanBianHao);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {   // 如果能查到结果,一个订单编号对应一条信息
                jsonObject.put("id", rs.getInt("id"));  // 得到订单ID
                jsonObject.put("DingDanBianHao", rs.getString("DingDanBianHao"));  // 得到订单编号
                jsonObject.put("YongHuMing", rs.getString("YongHuMing"));  // 得到用户名
                jsonObject.put("ShangPinXinXi", new JSONArray(rs.getString("ShangPinXinXi")));  // 得到商品信息
                jsonObject.put("WuLiuDanHao", rs.getString("WuLiuDanHao"));  // 得到物流单号
                jsonObject.put("DingDanZongE", rs.getString("DingDanZongE"));  // 得到订单总额
                String shiJian = rs.getString("DingDanShiJian");  // 得到订单时间
                jsonObject.put("DingDanShiJian", shiJian.substring(0, shiJian.lastIndexOf(".")));  // 存入去了毫秒的订单时间
                jsonObject.put("DingDanZhuangTai", rs.getString("DingDanZhuangTai"));  // 得到订单状态
                jsonObject.put("ShouHuoDiZhi", rs.getString("ShouHuoDiZhi"));  // 得到收货地址
            }
            rs.close();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 根据订单id修改物流单号
     * 这个方法用来发货
     * @param dinDan 订单pojo对象
     * @return 修改成功返回true，失败返回false
     */
    public boolean xiuGaiWuLiu(DinDan dinDan) {
        boolean action = false;
        String sql = "update dindan set WuLiuDanHao=? where id=?";
        try {
            PreparedStatement pre = Mydb.myConnection.prepareStatement(sql);
            pre.setString(1, dinDan.getWuLiuDanHao());
            pre.setInt(2, dinDan.getId());
            int i = pre.executeUpdate();  // 得到受影响条数
            if (i == 1) {
                action = true;
            }
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return action;
    }

    /**
     * 查询统计订单信息
     * 这个方法用来查询统计所有订单以表格的方式展示
     * 这个方法用过用户输入的订单编号或者时间范围来查询订单信息
     * @param dinDan 订单pojo对象
     * @param endShiJian  时间范围结尾时间
     * @param fenYeZuo  limit分页开始下标
     * @return 成功返回一个JSONArray有值，失败返回JSONArray为空值
     */
    public JSONArray tongJi(DinDan dinDan, String endShiJian, int fenYeZuo) {
        JSONArray jsonArray = new JSONArray();
        String sql = "select id,DingDanBianHao,DingDanShiJian,ShangPinXinXi,YongHuMing,DingDanZhuangTai,ShouHuoDiZhi,DingDanZongE from dindan";
        String sqlTiaoShu = "select count(id) from dindan";
        List<String> list = new ArrayList<>();
        String[] dingDanBianHao = null;
        if (dinDan.getDingDanHao() != null && dinDan.getDingDanHao().size() != 0) {  // 如果订单编号不为空
            dingDanBianHao = new String[dinDan.getDingDanHao().size()];
            for (int i = 0; i < dinDan.getDingDanHao().size(); i++) {  // 循环取出元素，组装为一个{‘a’,'b','c'} 类型的数组
                dingDanBianHao[i] = dinDan.getDingDanHao().get(i);   // 将取出的订单编号存入数组
            }
        }
        list.add(dinDan.getDingDanShiJian());  // 获取开始订单时间存入list
        list.add(endShiJian);      // 存入结尾时间
        String[] sqlz = {" DingDanShiJian >= ?", " DingDanShiJian <= ?"};  // sql语句组合成数组
        String sqlJ = "";  // 需要组合的sql
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null && !list.get(i).equals("")) {  // 如果list的值不为空
                if (sqlJ.equals("")) {  // 如果sqlJ为第一次拼接
                    sqlJ = " where " + sqlz[i];           // 拼接
                } else {                // 如果sqlJ不为第一次拼接
                    sqlJ += " and " + sqlz[i];           // 拼接
                }
            }
        }
        if (dingDanBianHao != null) {   // 如果订单编号组不为空
            String zhanWeiFu = "?";    // 定义一个字符串装占位符,订单编号不为空，至少有一个占位符
            for (int i = 1; i < dingDanBianHao.length; i++) {
                zhanWeiFu += ",?";
            }
            if (sqlJ.equals("")) {  //
                sqlJ += " where DingDanBianHao in(" + zhanWeiFu + ")";  // 占位符放里面
            } else {
                sqlJ += " and DingDanBianHao in(" + zhanWeiFu + ")";
            }
        }
        String sqlFenYe = " limit ?,10";   // 分页sql
        sql = sql + sqlJ + " ORDER BY DingDanShiJian desc " + sqlFenYe  ;    // sql拼接 + 按时间降序排序
        sqlTiaoShu += sqlJ;      // 查询条数sql拼接
        try {
            PreparedStatement pre = Mydb.myConnection.prepareStatement(sqlTiaoShu);
            int count = 0;  // 统计占位符个数
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) != null && !list.get(i).equals("")) {  // 如果list的值不为空
                    pre.setString(count + 1, list.get(i));
                    count++;  // 占位符个数+1
                }
            }
            if (dingDanBianHao != null) { // 如果订单编号组不为空
                for (int i = 0; i < dingDanBianHao.length; i++) {
                    pre.setString(count + 1, dingDanBianHao[i]);  // 订单编号组合成的字符串放入占位符
                    count++;
                }
            }
            ResultSet rs = pre.executeQuery();
            rs.next();
            dinDan.setZongTiaoShu(rs.getInt(1)); // 得到条数传入订单pojo对象
            rs.close();                                // 得到结果关闭结果集
            pre = Mydb.myConnection.prepareStatement(sql);
            count = 0;  // 统计占位符个数
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) != null && !list.get(i).equals("")) {  // 如果list的值不为空
                    pre.setString(count + 1, list.get(i));
                    count++;  // 占位符个数+1
                }
            }
            if (dingDanBianHao != null) { // 如果订单编号组不为空
                for (int i = 0; i < dingDanBianHao.length; i++) {
                    pre.setString(count + 1, dingDanBianHao[i]);  // 订单编号组合成的字符串放入占位符
                    count++;
                }
            }
            pre.setInt(count + 1, fenYeZuo);  // 传入分页左边的开始数字
            rs = pre.executeQuery();                            // 开启结果集
            while (rs.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", rs.getInt("id"));   // 订单id存入JSON
                jsonObject.put("DingDanBianHao", rs.getString("DingDanBianHao"));   // 订单编号存入JSON
                String shiJian = rs.getString("DingDanShiJian");  // 得到订单时间
                jsonObject.put("DingDanShiJian", shiJian.substring(0, shiJian.lastIndexOf(".")));  // 存入去了毫秒的订单时间
                JSONArray jsonArr = new JSONArray(rs.getString("ShangPinXinXi"));  // 的达到商品信息转换为JSONArray
                jsonObject.put("ShangPinShuLiang", jsonArr.length());   // 商品信息的长度就是商品的数量
                String shangpin = "";
                for (int i = 0; i < jsonArr.length(); i++) {   // 循环取出商品具体信息
                    shangpin += jsonArr.getJSONObject(i).getString("shangpinmingcheng") +
                            "个数" + jsonArr.getJSONObject(i).getInt("shangpinshuliang") +
                            "总价" + jsonArr.getJSONObject(i).getInt("shangpinzongjia");   // 装入jsonObject一个key里面，有商品名称，数量，总价
                }
                jsonObject.put("shangpin", shangpin);
                jsonObject.put("YongHuMing", rs.getString("YongHuMing"));   // 下单用户存入JSON
                if (rs.getString("DingDanBianHao") != null) {
                    jsonObject.put("DingDanZhuangTai", "已完成");   // 订单状态存入JSON
                } else {
                    jsonObject.put("DingDanZhuangTai", "未完成");   // 订单状态存入JSON
                }
                jsonObject.put("DingDanZongE", rs.getString("DingDanZongE"));   //订单金额存入JSON
                jsonObject.put("ShouHuoDiZhi", rs.getString("ShouHuoDiZhi"));   //收货地址存入JSON
                jsonArray.put(jsonObject);       // JSONObject 存入JSONArray
            }
            rs.close();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    /**
     * 这个方法用来导出所有的订单信息
     *
     * @return 成功返回结果JSONArray 失败返回JSONArray == []
     */
    public JSONArray quanDaoChu() {
        JSONArray jsonArray = new JSONArray();
        String sql = "select id,DingDanBianHao,DingDanShiJian,ShangPinXinXi,YongHuMing,DingDanZhuangTai,ShouHuoDiZhi,DingDanZongE from dindan";
        try {
            PreparedStatement pre = Mydb.myConnection.prepareStatement(sql);
            ResultSet rs = pre.executeQuery();                            // 开启结果集
            while (rs.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", rs.getInt("id"));   // 订单id存入JSON
                jsonObject.put("DingDanBianHao", rs.getString("DingDanBianHao"));   // 订单编号存入JSON
                String shiJian = rs.getString("DingDanShiJian");  // 得到订单时间
                jsonObject.put("DingDanShiJian", shiJian.substring(0, shiJian.lastIndexOf(".")));  // 存入去了毫秒的订单时间
                JSONArray jsonArr = new JSONArray(rs.getString("ShangPinXinXi"));  // 的达到商品信息转换为JSONArray
                jsonObject.put("ShangPinShuLiang", jsonArr.length());   // 商品信息的长度就是商品的数量
                String shangpin = "";
                for (int i = 0; i < jsonArr.length(); i++) {   // 循环取出商品具体信息
                    shangpin += jsonArr.getJSONObject(i).getString("shangpinmingcheng") +
                            "个数" + jsonArr.getJSONObject(i).getInt("shangpinshuliang") +
                            "总价" + jsonArr.getJSONObject(i).getInt("shangpinzongjia");   // 装入jsonObject一个key里面，有商品名称，数量，总价
                }
                jsonObject.put("shangpin", shangpin);
                jsonObject.put("YongHuMing", rs.getString("YongHuMing"));   // 下单用户存入JSON
                if (rs.getString("DingDanBianHao") != null) {
                    jsonObject.put("DingDanZhuangTai", "已完成");   // 订单状态存入JSON
                } else {
                    jsonObject.put("DingDanZhuangTai", "未完成");   // 订单状态存入JSON
                }
                jsonObject.put("DingDanZongE", rs.getString("DingDanZongE"));   //订单金额存入JSON
                jsonObject.put("ShouHuoDiZhi", rs.getString("ShouHuoDiZhi"));   //收货地址存入JSON
                jsonArray.put(jsonObject);       // JSONObject 存入JSONArray
            }
            rs.close();
            pre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }



    public static void main(String[] args) {
        HouTaiDingDanDao houTaiDingDanDao = new HouTaiDingDanDao();
        DinDan dinDan = new DinDan();
//        dinDan.setYongHuMing("测试");
//        houTaiDingDanDao.chaXun(dinDan, "", -1);
    }
}
