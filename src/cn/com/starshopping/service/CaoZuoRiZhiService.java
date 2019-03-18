package cn.com.starshopping.service;

import cn.com.starshopping.dao.CaoZuoJiLuDao;
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
 * Date: 2019/01/15
 * Description: 操作日志
 * Version: V1.0
 */
public class CaoZuoRiZhiService {
    public String kongZhiTai(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        String json = "";
        String xingMing = (String) request.getSession().getAttribute(SessionKey.KEFU); // 得到客服登录成功后传入的客服姓名
        if (xingMing != null && action != null && !action.trim().equals("")) {  // 如果状态不为空
            if (action.equals("daochu")) {
                CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();   // 操作记录Dao
                caoZuoJiLuDao.chaRuCaoZuoJiLu(xingMing, "导出订单到excel");  // 调用操作记录方法，记录操作
                daoChu(request, response);  // 调用查询方法
            } else if (action.equals("rizhi")) {  // 查看日志
                CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();   // 操作记录Dao
                caoZuoJiLuDao.chaRuCaoZuoJiLu(xingMing, "查看日志");  // 调用操作记录方法，记录操作
                json = houTaiRiZhi(request);   // 调用日志方法
            }
        } else {
            json = "{\"returncode\":-100}";
        }
        return json;
    }


    /**
     * 后台日志查询
     *
     * @param request
     * @return 成功返回结果json
     */
    public String houTaiRiZhi(HttpServletRequest request) {
        String returncode = "{\"returncode\":-1}";
        String xingMing = request.getParameter("xingMing"); // 得到用户名
        String yeMa = request.getParameter("yema");           // 得到页码
        String duankou = request.getParameter("duankou");           // 得到前端或者后端查询
        CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao(); // 操作日志dao对象
        if (yeMa != null && !yeMa.trim().equals("") && duankou != null && !duankou.equals("")) {  // 如果姓名和action不为空
            int fenYeIndex = (Integer.parseInt(yeMa) - 1) * 10;  // 得到分页开始下标
            // 如果姓名不为空，这个判断主要是姓名调用trim方法是避免空指针
            JSONObject jsonObject = null;  // 这个jsonObject用来接收查询结果
            if (xingMing != null) {
                jsonObject = caoZuoJiLuDao.chaXunRiZhi(xingMing.trim(), duankou, fenYeIndex);  // 调用操作记录查询方法
            } else {
                jsonObject = caoZuoJiLuDao.chaXunRiZhi(xingMing, duankou, fenYeIndex);  // 调用操作记录查询方法
            }
            if (jsonObject != null) {     // 如果查询成功无异常
                jsonObject.put("returncode", 200); // 返回状态码200
                jsonObject.put("xingMing", (String) request.getSession().getAttribute(SessionKey.KEFU));   // 存入姓名
                returncode = jsonObject.toString(); // 返回值为jsonObject
            } else {
                returncode = "{\"returncode\":-3}";  // 服务器出错，返回码-3
            }

        }
        return returncode;
    }

    /**
     * @param request
     * @return 成功返回true false
     */
    public boolean daoChu(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean zhuangtai = false;
        CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao(); // 操作日志dao对象
        String arr = request.getParameter("arr");
        String duankou = request.getParameter("duankou");  // 得到是前台还是是后台
        JSONArray jsonArray = null;
        if (arr.equals("quandaochu")) {
            jsonArray = caoZuoJiLuDao.daoChu(duankou, null);   // 调用查询方法
        } else if (arr != null && !arr.equals("")) {
            String[] array = arr.split(",");
            List<String> list = new ArrayList<>();
            for (int i = 0; i < array.length; i++) {
                list.add(array[i]);     // 订单编号存入
            }
            jsonArray = caoZuoJiLuDao.daoChu(duankou, list);   // 调用查询方法
        }
        POIUtil poiUtil = new POIUtil();
        List<List<String>> lists = new ArrayList<>();
        List<String> only_list = new ArrayList<>();
        only_list.add("序号"); // 表头
        only_list.add("姓名"); // 存入数据到excel
        only_list.add("时间");
        only_list.add("操作");
        lists.add(only_list);
        for (int i = 0; i < jsonArray.length(); i++) {
            only_list = new ArrayList<>();
            only_list.add("" + (i + 1)); // 存入数据到excel,序号
            only_list.add(jsonArray.getJSONObject(i).getString("YongHuMing")); // 存入数据到excel
            only_list.add(jsonArray.getJSONObject(i).getString("ShiJian"));
            only_list.add(jsonArray.getJSONObject(i).getString("CaoZuoJiLu"));
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
