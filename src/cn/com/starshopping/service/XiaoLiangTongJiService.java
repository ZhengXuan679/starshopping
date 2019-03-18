package cn.com.starshopping.service;

import cn.com.starshopping.dao.CaoZuoJiLuDao;
import cn.com.starshopping.dao.XiaoLiangTongJiDao;
import cn.com.starshopping.util.SessionKey;
import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

public class XiaoLiangTongJiService {
    public JSONObject xiaoLiang(HttpServletRequest request, HttpServletResponse response) {
        String yongHuMing = (String) request.getSession().getAttribute(SessionKey.KEFU); // 得到登录用户名;
//        String yongHuMing ="lzy";
        JSONObject jso = new JSONObject();
        String ac = request.getParameter("ac");
        String tu = request.getParameter("tu");
        XiaoLiangTongJiDao xiaoLiangTongJiDao = new XiaoLiangTongJiDao();
        if (ac != null) {
            jso.put("yongHuMing", yongHuMing);
        }
        else if (tu != null) {
           JSONArray xiaoLiangTongJi = (JSONArray) request.getSession().getAttribute("xiaoLiangTongJi");
            jso = xiaoLiangTongJiDao.tubiao(xiaoLiangTongJi);
        }
        else {
            String action = request.getParameter("action");//获取状态码
            String page = request.getParameter("page");//获取页码
            String limit = request.getParameter("limit");//获取每页条数
            int pages = Integer.parseInt(page);//页码转为int型
            int limits = Integer.parseInt(limit);//每页条数转化为int型

            CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();
            String actions = "";//操作记录容器
            //初始状态码设为4（月统计）
            if (action == null) {
                action = "4";
            }
            String startime = "";//起始时间
            String endtime = "";//终止时间
            //状态码1为具体时间段统计，获取起始和终止时间,并传入操作动作和用户名到操作记录方法
            if (action.equals("1")) {
                startime = request.getParameter("startime") + "  00:00:00";//获取起始日期并拼接0点的时分秒
                endtime = request.getParameter("endtime");//获取结束日期
                endtime = LocalDate.parse(endtime).plusDays(1).toString() + "  00:00:00";//结束日期+1并拼接0点的时分秒，即为到终止日期为24点
                actions = "详细查询:" + startime + "——" + endtime;//拼接操作功能
                caoZuoJiLuDao.chaRuCaoZuoJiLu(actions, yongHuMing);//调用操作记录方法，传入操作的方法和用户名
            }
            //状态码2为当日统计，获取当周起始和结束时间,并传入操作动作和用户名到操作记录方法
            else if (action.equals("2")) {
                String[] day = xiaoLiangTongJiDao.todaydate();
                startime = day[0];
                endtime = day[1];
                actions = "日查询";
                caoZuoJiLuDao.chaRuCaoZuoJiLu(actions, yongHuMing);//调用操作记录方法，传入操作的方法和用户名
            }
            //状态码3为当周统计，获取当周起始和结束时间,并传入操作动作和用户名到操作记录方法
            else if (action.equals("3")) {
                String[] week = xiaoLiangTongJiDao.weekdate();
                startime = week[0];
                endtime = week[1];
                actions = "周查询";
                caoZuoJiLuDao.chaRuCaoZuoJiLu(actions, yongHuMing);//调用操作记录方法，传入操作的方法和用户名
            }
            //状态码4为当月统计，获取当月起始和结束时间,并传入操作动作和用户名到操作记录方法
            else if (action.equals("4")) {
                String[] month = xiaoLiangTongJiDao.monthdate();
                startime = month[0];
                endtime = month[1];
                actions = "月查询";
                caoZuoJiLuDao.chaRuCaoZuoJiLu(actions, yongHuMing);

            }
            //状态码5为当季统计，获取当前季度起始和结束时间,并传入操作动作和用户名到操作记录方法
            else if (action.equals("5")) {

                String[] quarter = xiaoLiangTongJiDao.quarterdate();
                startime = quarter[0];
                endtime = quarter[1];
                actions = "季查询";
                caoZuoJiLuDao.chaRuCaoZuoJiLu(yongHuMing,actions);
            }
            //状态码6为年统计，获取今年起始和结束时间,并传入操作动作和用户名到操作记录方法
            else if (action.equals("6")) {
                String[] year = xiaoLiangTongJiDao.yeardate();
                startime = year[0];
                endtime = year[1];
                actions = "年查询";
                caoZuoJiLuDao.chaRuCaoZuoJiLu(actions, yongHuMing);
            }

            JSONArray xiaoLiangTongJi = xiaoLiangTongJiDao.xiaoLiang(startime, endtime);//根据起始和终止时间，页码和每页条数进行查询
            request.getSession().setAttribute("xiaoLiangTongJi",xiaoLiangTongJi);
            jso = xiaoLiangTongJiDao.tongJi(xiaoLiangTongJi,startime,endtime,pages,limits);

        }
        return jso;
    }
}
