package cn.com.starshopping.service;

import cn.com.starshopping.dao.CaoZuoJiLuDao;
import cn.com.starshopping.dao.HouTaiDingDanDao;
import cn.com.starshopping.pojo.DinDan;
import cn.com.starshopping.pojo.ShangPinXinXi;
import cn.com.starshopping.util.SessionKey;
import cn.com.starshopping.util.poi.POIUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yanmin
 * Date: 2019/01/05
 * Description:
 * Version: V1.0
 */
public class HouTaiDingDanService {


    /**
     * @param request
     * @param response
     * @throws IOException
     */
    public String kongZhiTai(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        String json = "";
        String xingMing = (String) request.getSession().getAttribute(SessionKey.KEFU); // 得到客服登录成功后传入的客服姓名
        if (xingMing != null && action != null && !action.trim().equals("")) {  // 如果状态不为空
            if (action.equals("chaxun")) {              // 如果前端是查询订单列表
                CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();   // 操作记录Dao
                caoZuoJiLuDao.chaRuCaoZuoJiLu(xingMing, "查询订单列表");  // 调用操作记录方法，记录操作
                json = chaXun(request);
            } else if (action.equals("xiangqing")) {  //  如果前端是查询订单详情
                CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();   // 操作记录Dao
                caoZuoJiLuDao.chaRuCaoZuoJiLu(xingMing, "查询订单详情");  // 调用操作记录方法，记录操作
                json = chaXunDingDan(request);  // 调用查询方法
            } else if (action.equals("fahuo")) {
                CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();   // 操作记录Dao
                caoZuoJiLuDao.chaRuCaoZuoJiLu(xingMing, "发货");  // 调用操作记录方法，记录操作
                json = xiuGaiWuLiu(request);  // 调用查询方法
            } else if (action.equals("tongji")) {
                CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();   // 操作记录Dao
                caoZuoJiLuDao.chaRuCaoZuoJiLu(xingMing, "统计订单");  // 调用操作记录方法，记录操作
                json = tongJi(request);  // 调用查询方法
            } else if (action.equals("daochu")) {
                CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();   // 操作记录Dao
                caoZuoJiLuDao.chaRuCaoZuoJiLu(xingMing, "导出订单到excel");  // 调用操作记录方法，记录操作
                daoChu(request, response);  // 调用查询方法
            }
        } else {
            json = "{\"returncode\":-100}";
        }
        return json;
    }

    /**
     * 这个方法用来查询所有的订单信息，等待修改
     *
     * @return
     */
    public String chaXun(HttpServletRequest request) {
        String json = "{\"returncode\":-1}";              // 返回码-1代表查询没有结果
        DinDan dinDan = new DinDan();             // 订单pojo
        ShangPinXinXi shangPinXinXi = new ShangPinXinXi(); // 商品信息pojo
        int yeMa = 0;
        if (request.getParameter("yema") != null && !request.getParameter("yema").trim().equals("")) {  // 获取到当前页码
            int dangQianYeMa = Integer.parseInt(request.getParameter("yema"));   // 得到需要传入数据库的页码（当前页码 - 1）* 每页显示多少条
            yeMa = (dangQianYeMa - 1) * 10;   // 获取到从第几开始分页
        }
        dinDan.setDingDanShiJian(request.getParameter("DingDanShiJian").trim());  // 订单时间传入订单pojo对象
        String dingDanBianHao = request.getParameter("DingDanBianHao");  // 得到订单编号
        dinDan.setYongHuMing(request.getParameter("YongHuMing").trim());                   // 用户名称传入订单pojo对象
        dinDan.setShouHuoDiZhi(request.getParameter("ShouHuoDiZhi").trim());            // 收货地址传入订单pojo对象
        String dingDanZongE = request.getParameter("DingDanZongE");
        if (dingDanZongE != null && !dingDanZongE.trim().equals("")) {        //如果订单总额不为空，那么才转为int类型数值
            int dingDanZongJia = Integer.parseInt(dingDanZongE);
            dinDan.setDingDanZongE(dingDanZongJia);                                  // 订单总额传入订单pojo对象
        }
        dinDan.setWuLiuDanHao(request.getParameter("WuLiuDanHao").trim());            //物流单号传入订单pojo对象
        String endShiJian = request.getParameter("endShiJian");
        String endDingDanZongE = request.getParameter("endDingDanZongE");
        int endDingDanZongJia = -1;                                             // 初始化结尾价格为-1
        if (endDingDanZongE != null && !endDingDanZongE.trim().equals("")) {        //如果结尾价格不为空，那么才转为int类型数值
            endDingDanZongJia = Integer.parseInt(endDingDanZongE);
        }
        HouTaiDingDanDao houTaiDingDanDao = new HouTaiDingDanDao();
        String shangPinMingChen = request.getParameter("ShangPinMingChen");
        List<String> dingDanList = new ArrayList<>();
        if (shangPinMingChen != null && !shangPinMingChen.trim().equals("")) { // 如果商品名称不为空
            dingDanList = houTaiDingDanDao.chaXunDingDanBianHao(shangPinMingChen); // 调用查询订单编号方法，查询订单编号，得到订单编号list
            if (dingDanList == null || dingDanList.size() == 0) {  // 查询结果得到list为空或者长度为0
                // 如果通过商品名称查询没有结果，直接返回返回码为-1，代表查询无结果，后续代码不运行
                return "{\"returncode\":-1}";
            }
        }
        if (dingDanBianHao != null && !dingDanBianHao.trim().equals("")) {  // 如果从前台得到的订单编号不为空
            dingDanList.add(dingDanBianHao);                                // 将订单编号存入list
        }
        dinDan.setDingDanHao(dingDanList);            // 将订单编号list存入pojo对象
        JSONArray jsonArray = houTaiDingDanDao.chaXun(dinDan, endShiJian, endDingDanZongJia, yeMa);   // 调用查询方法,传入订单对象，结束时间，结束总价
        JSONObject jsonObject = new JSONObject();
        if (jsonArray != null && jsonArray.length() != 0) {  // 如果查询结果不为null
            for (int i = 0; i < jsonArray.length(); i++) {
                // 将查询得到的商品信息取出，存入JSONArray中,因查询得到的订单信息可能不止一个，所以需要循环取出
                JSONArray jsa = jsonArray.getJSONObject(i).getJSONArray("ShangPinXinXi");
                // 将取出的商品信息中再取出其中的商品编号，同样编号可能不止一个，需要循环取出
                for (int j = 0; j < jsa.length(); j++) {
                    JSONObject jsonspxx = jsa.getJSONObject(j);                  // 将每个商品信息的JSON存入jsonsoxx中
                    String shangpinbianhao = jsonspxx.getString("shangpinbianhao");  // 取出商品编号
                    ShangPinXinXi sp = new ShangPinXinXi(); // 新建商品信息pojo
                    sp.setShangPinBianHao(shangpinbianhao);   // 传入商品编号到pojo对象
                    String[] shangPinXx = houTaiDingDanDao.chaXunShangPinMingCheng(sp);   // 调用查询商品名称方法得到商品名称,商品规格
                    // 得到的商品名称存入相应的位置，也就是JSONArray第i个JSONObject中的JSONArray中的第j个JSONObject中
                    jsonArray.getJSONObject(i).getJSONArray("ShangPinXinXi").getJSONObject(j).put("shangpinmingcheng", shangPinXx[0]); // 商品名称存入
                    jsonArray.getJSONObject(i).getJSONArray("ShangPinXinXi").getJSONObject(j).put("shangguige01", shangPinXx[1]);      // 商品规格存入
                    jsonArray.getJSONObject(i).getJSONArray("ShangPinXinXi").getJSONObject(j).put("shangguige02", shangPinXx[2]);       // 商品规格2 存入
                }
                jsonObject.put("zongtiaoshu", dinDan.getZongTiaoShu());    // 总条数存入jsonObject
            }

            jsonObject.put("returncode", 200);  // 查询成功返回码为200
            jsonObject.put("data", jsonArray);   // 将newJSONArray存入jsonObject中，再将这个jsonObject以字符串的形式返回
            jsonObject.put("xingMing", (String) request.getSession().getAttribute(SessionKey.KEFU));  // 登录人姓名存入
            json = jsonObject.toString();
        }
        return json;
    }

    /**
     * 这个方法用来查询订单详细信息，用来传出JSONObject到订单详情页面
     *
     * @param request
     * @return 查询成功返回JSONObjcet状态码为200，查询失败返回状态嘛-1
     */
    public String chaXunDingDan(HttpServletRequest request) {
        String action = "{\"returncode\":-1}";   // 返回码为-1代表查询失败
        DinDan dinDan = new DinDan();        // 创建订单pojo对象
        String dingDanBianHao = request.getParameter("DingDanBianHao");
        if (dingDanBianHao != null && !dingDanBianHao.trim().equals("")) {  // 如果订单编号不为空也不为空字符串
            List<String> list = new ArrayList<>();  // 因为业务需求将订单编号list改为list集合，所以讲前端传入到的订单编号存入list中在传入pojo对象
            list.add(dingDanBianHao.trim());
            dinDan.setDingDanHao(list);  // 传入pojo对象
            HouTaiDingDanDao houTaiDingDanDao = new HouTaiDingDanDao();
            JSONObject jsonObject = houTaiDingDanDao.chaXunDingDan(dinDan);    // 调用查询订单信息方法
            JSONArray jsonArray = jsonObject.getJSONArray("ShangPinXinXi");
            if (jsonArray != null && jsonArray.length() != 0) {  // 如果得到的商品信息jsonarray不为空，放置用户自行输入参数查询
                for (int i = 0; i < jsonArray.length(); i++) {  // 循环取出商品编号
                    ShangPinXinXi shangPinXinXi = new ShangPinXinXi();// 商品信息pojo对象
                    // 得到商品编号传入商品信息pojo对象中
                    shangPinXinXi.setShangPinBianHao(jsonArray.getJSONObject(i).getString("shangpinbianhao"));
                    String[] shangPin = houTaiDingDanDao.chaXunShangPinMingCheng(shangPinXinXi); // 查询得到商品名称和规格
                    // 得到的商品名称存入相应的位置，也就是JSONArray第i个JSONObject中的JSONArray中的第j个JSONObject中
                    // 因为取到的商品名称和规格是一个长度为3的数组，直接通过下标取值
                    jsonObject.getJSONArray("ShangPinXinXi").getJSONObject(i).put("shangpinmingcheng", shangPin[0]);
                    jsonObject.getJSONArray("ShangPinXinXi").getJSONObject(i).put("shangguige01", shangPin[1]);
                    jsonObject.getJSONArray("ShangPinXinXi").getJSONObject(i).put("shangguige02", shangPin[2]);
                }
            } else {
                return "{\"returncode\":-1}";
            }
            jsonObject.put("returncode", 200);  // 查询成功状态码加入成功jsonobject
            jsonObject.put("xingMing", (String) request.getSession().getAttribute(SessionKey.KEFU));  // 登录人姓名存入
            action = jsonObject.toString();    // jsonObject以字符串的方式赋值给action
        }
        return action;
    }

    /**
     * 修改物流编号
     *
     * @param request
     * @return
     */
    public String xiuGaiWuLiu(HttpServletRequest request) {
        String wuLiuDanHao = request.getParameter("WuLiuDanHao");
        String id = request.getParameter("id");   // 得到订单id
        if (wuLiuDanHao != null && !wuLiuDanHao.trim().equals("") && id != null && !id.trim().equals("")) {  // 如果物流编号和id不为空
            DinDan dinDan = new DinDan();        // 创建订单pojo对象
            dinDan.setId(Integer.parseInt(id));                      // 传入id 到pojo对象
            dinDan.setWuLiuDanHao(wuLiuDanHao);                         // 传入物流单号到pojo对象
            HouTaiDingDanDao houTaiDingDanDao = new HouTaiDingDanDao(); // 后台订单Dao对象
            boolean b = houTaiDingDanDao.xiuGaiWuLiu(dinDan);   // 调用修改物流单号方法，得到返回值
            if (b) {
                return "{\"returncode\":200}";
            }
        }
        return "{\"returncode\":-1}";
    }

    /**
     * 统计订单情况
     *
     * @param request
     * @return
     */
    public String tongJi(HttpServletRequest request) {
        String action = "{\"returncode\":-1}";   // 查询失败返回状态码-1
        String dingDanShiJian = request.getParameter("DingDanShiJian");
        String endShiJian = request.getParameter("endShiJian");
        String DingDanBianHao = request.getParameter("DingDanBianHao");
        String yeMa = request.getParameter("yema");
        // 页码不等于空，因为前段传递页码是由初始值得，所以页码正常状态下不会为空，如果为空，那么用户存在非法操作
        // 所以不为空才进行下步操作
        if (yeMa != null && !yeMa.trim().equals("")) {
            DinDan dinDan = new DinDan();  // 订单表pojo对象
            List<String> list = new ArrayList<>();
            if (DingDanBianHao != null && !DingDanBianHao.trim().equals("")) { // 如果订单编号不为空
                list.add(DingDanBianHao);  // 订单编号存入订单编号list
            }
            dinDan.setDingDanHao(list);  // 订单编号存入pojo对象
            dinDan.setDingDanShiJian(dingDanShiJian);
            int fenYeZuo = (Integer.parseInt(yeMa) - 1) * 10;   // 得到分页开始数字
            HouTaiDingDanDao houTaiDingDanDao = new HouTaiDingDanDao(); // 后台订单Dao对象
            JSONArray jsonArray = houTaiDingDanDao.tongJi(dinDan, endShiJian, fenYeZuo);  // 调用统计方法得到结果JSONArray

            JSONObject jsonObject = new JSONObject();
            if (jsonArray != null && jsonArray.length() != 0) {  // 查询结果的JSONArray不为空
                jsonObject.put("returncode", "200");   // 状态码200
                jsonObject.put("data", jsonArray);    // 存入
                jsonObject.put("zongtiaoshu", dinDan.getZongTiaoShu());  // 得到总条数
                jsonObject.put("xingMing", (String) request.getSession().getAttribute(SessionKey.KEFU));  // 登录人姓名存入
                action = jsonObject.toString();
            }
        }
        return action;
    }

    /**
     * @param request
     * @return 成功返回true false
     */
    public boolean daoChu(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean zhuangtai = false;
        HouTaiDingDanDao houTaiDingDanDao = new HouTaiDingDanDao(); // 后台订单Dao对象
        String arr = request.getParameter("arr");
        JSONArray jsonArray = null;
        if (arr.equals("quandaochu")) {
            jsonArray = houTaiDingDanDao.quanDaoChu();   // 调用全查方法,导出全部
        } else if (arr != null && !arr.equals("")) {
            String[] array = arr.split(",");
            List<String> list = new ArrayList<>();
            for (int i = 0; i < array.length; i++) {
                list.add(array[i]);     // 订单编号存入
            }
            DinDan dinDan = new DinDan();
            dinDan.setDingDanHao(list);  // 订单编号存入订单pojo对象
            jsonArray = houTaiDingDanDao.tongJi(dinDan, "", 0);   // 调用子查方法
        }
        POIUtil poiUtil = new POIUtil();
        List<List<String>> lists = new ArrayList<>();
        List<String> only_list = new ArrayList<>();
        only_list.add("序号"); // 表头
        only_list.add("订单编号"); // 存入数据到excel
        only_list.add("订单时间");
        only_list.add("商品数量");
        only_list.add("商品情况");
        only_list.add("下单用户");
        only_list.add("订单状态");
        only_list.add("订单总金额");
        only_list.add("收货地址");
        lists.add(only_list);
        for (int i = 0; i < jsonArray.length(); i++) {
            only_list = new ArrayList<>();
            only_list.add("" + (i + 1)); // 存入数据到excel,序号
            only_list.add(jsonArray.getJSONObject(i).getString("DingDanBianHao")); // 存入数据到excel
            only_list.add(jsonArray.getJSONObject(i).getString("DingDanShiJian"));
            only_list.add("" + jsonArray.getJSONObject(i).getInt("ShangPinShuLiang"));
            only_list.add(jsonArray.getJSONObject(i).getString("shangpin"));
            only_list.add(jsonArray.getJSONObject(i).getString("YongHuMing"));
            only_list.add(jsonArray.getJSONObject(i).getString("DingDanZhuangTai"));
            only_list.add(jsonArray.getJSONObject(i).getString("DingDanZongE"));
            only_list.add(jsonArray.getJSONObject(i).getString("ShouHuoDiZhi"));
            lists.add(only_list);
        }
        Workbook workbook = poiUtil.creatExcelForPOI(lists, "第一页");
        if (workbook != null) {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + System.currentTimeMillis() + ".xls");  // 导出的文件名是当前时间为文件名
            //获取响应报文输出流对象
            ServletOutputStream out = response.getOutputStream();
            //输出
            workbook.write(out);
            out.flush();
            out.close();
        } else {
            response.setCharacterEncoding("utf-8");
            response.getWriter().print("系统服务出错!");
        }

        zhuangtai = true;
        return zhuangtai;
    }
}
