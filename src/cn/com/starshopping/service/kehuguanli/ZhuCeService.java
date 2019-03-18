package cn.com.starshopping.service.kehuguanli;

import cn.com.starshopping.dao.CaoZuoJiLuDao;
import cn.com.starshopping.dao.KeHuDao;
import cn.com.starshopping.pojo.KeHuDengLu;
import cn.com.starshopping.util.SessionKey;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: huxiaoyi
 * Date: 2019/01/10
 * Description:查看注册用户service层
 * Version: V1.0
 */
public class ZhuCeService {

    public JSONObject zhuce(HttpServletRequest request) throws IOException {
        //记录操作
        String kefu = (String) request.getSession().getAttribute(SessionKey.KEFU);
        CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();
        caoZuoJiLuDao.chaRuCaoZuoJiLu(kefu, "查看注册用户");
        //得到前台传入的搜索条件
        BufferedReader bufferedReader = request.getReader();
        String json = bufferedReader.readLine();
        KeHuDao keHuDao = new KeHuDao();
        KeHuDengLu keHuDengLu = new KeHuDengLu();
        JSONObject jsonObject = new JSONObject(json);
        JSONObject sousuojsonObject = jsonObject.getJSONObject("data");
        keHuDengLu.setYongHuMing(sousuojsonObject.getString("yonghuming"));
        keHuDengLu.setShouJiHaoMa(sousuojsonObject.getString("shoujihaoma"));
        keHuDengLu.setZuiHouYiCiDengLuShiJian(Timestamp.valueOf(sousuojsonObject.getString("dengludatemin")));
        int page = sousuojsonObject.getInt("page");
        int limit = sousuojsonObject.getInt("limit");
        if (limit == 0) {
            limit = 10;   //每页显示条数，默认为10
        }
        String action = jsonObject.getString("action");
        //获取当前时间 ，年-月-日格式
        long time = System.currentTimeMillis();
        String nowtime = (new Timestamp(time)).toString().substring(0, 10);
        //查看最近活跃用户
        JSONObject jsonObject1;
        String zhucedatemin = "";
        String zhucedatemax = "";
        if (action.equals("zuijin")) {
            int day = Integer.valueOf(sousuojsonObject.getString("day"));
            zhucedatemin = (new Timestamp(time - 1000 * 60 * 60 * 24 * day)).toString().substring(0, 10);
        }
        //查看当天活跃用户
        else if (action.equals("day")) {
            zhucedatemin = nowtime;
        }
        //查看本周活跃用户
        else if (action.equals("week")) {
            //得到本周周一的日期
            Calendar cal = Calendar.getInstance();
            //amount在这里为往后推迟几天或者往前移几天0代表本周，-7代表往前移七天指上周
            cal.add(Calendar.DATE, 0);
            //想周几，这里就传几Calendar.MONDAY（TUESDAY...）
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            zhucedatemin = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        }
        //查看本月活跃用户
        else if (action.equals("month")) {
            //获取本月第一天的日期
            zhucedatemin = nowtime.substring(0, 8) + "01";
        }
        //查看本年活跃用户
        else if (action.equals("year")) {
            //获取本年第一天的日期
            zhucedatemin = nowtime.substring(0, 5) + "01-01";
        } else if (action.equals("sweek")) {
            //得到上周周一的日期
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -7);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            zhucedatemin = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
            //得到上周末的日期,因为idea默认星期是从星期天开始，因此amount=0
            Calendar cal1 = Calendar.getInstance();
            cal1.add(Calendar.DATE, 0);
            cal1.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            zhucedatemax = new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime());

        } else if (action.equals("smonth")) {
            //得到上个月的月数，如果小于等于0则改为12
            int year = Integer.valueOf(nowtime.substring(0, 4));
            int month = Integer.valueOf(nowtime.substring(5, 7)) - 1;
            if (month <= 0) {
                year--;
                month = 12;
            }
            //得到month月份中有多少天
            Calendar a = Calendar.getInstance();
            a.set(Calendar.YEAR, year);
            a.set(Calendar.MONTH, month - 1);
            a.set(Calendar.DATE, 1);
            a.roll(Calendar.DATE, -1);
            int maxday = a.get(Calendar.DATE);
            zhucedatemin = "" + year + "-" + month + "-01";
            zhucedatemax = "" + year + "-" + month + "-" + maxday;
        } else if (action.equals("syear")) {
            int year = Integer.valueOf(nowtime.substring(0, 4));
            zhucedatemin = (year - 1) + "-01-01";
            zhucedatemax = (year - 1) + "-12-31";
        }
        keHuDengLu.setZuiHouYiCiDengLuShiJian(Timestamp.valueOf(zhucedatemin));
        jsonObject1 = keHuDao.souSuo(keHuDengLu, sousuojsonObject.getString("dengludatemax"),
                zhucedatemax, page, limit);
        return jsonObject1;
    }
}
