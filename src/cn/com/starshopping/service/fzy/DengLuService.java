package cn.com.starshopping.service.fzy;



import cn.com.starshopping.dao.hzy.CaoZuoJiLuDao;
import cn.com.starshopping.dao.hzy.DengLuDao;
import cn.com.starshopping.pojo.KeHuDengLu;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;

import static cn.com.starshopping.util.fzy.cookie.CookieKey.StartShopping_DengLuMing;
import static cn.com.starshopping.util.fzy.sms.SendSMS.sendSMS;



public class DengLuService implements CaoZuoJiLuInterface {
        private final CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();
    /**
     * 通过 switch case 判断前端ajax所有请求的操作函数
     * @param request
     * @param response
     * @return 返回一个JSONObject，用于Servlet将其返回前端。
     * @throws IOException
     */
    public JSONObject editorAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader br = request.getReader();//只会读一行，前端传过来的json一行显示
        String jsonstr = br.readLine();
        DengLuDao dengLuDao = new DengLuDao();
        JSONObject jsonObject = new JSONObject(jsonstr);
        String option = jsonObject.getString("option");
        String shoujihaoma = jsonObject.getString("shoujihaoma");
        String dengluming = jsonObject.getString("dengluming");
        zhiXingChaRu(option,dengluming);
        String mima = jsonObject.getString("mima");
        KeHuDengLu keHuDengLu = new KeHuDengLu();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String dengluchenggong_url = "/html/index.html";//登陆成功的跳转地址
        String yhm = "";
        int count = 0;

        HttpSession session =request.getSession();

        switch (option){
           case "1"://查询 是否是已经注册的手机号码
               jsonObject.put("panduancount",dengLuDao.checkPhoneIsExist(shoujihaoma));
               break;
           case "2"://获取验证码
               String yanzhengma = sendSMS(shoujihaoma);
               jsonObject.put("shoujiyanzhengmahuidiao",yanzhengma);
               break;
           case "3"://手机验证 登录按钮
               keHuDengLu.setShouJiHaoMa(shoujihaoma);
               keHuDengLu.setZuiHouYiCiDengLuShiJian(timestamp);
               dengLuDao.dengLu(keHuDengLu);//dao中的登录方法
               yhm = dengLuDao.dengLuMingChaYongHuMing(dengluming);
               session.setAttribute("yonghuming",yhm);
               jsonObject.put("url",dengluchenggong_url+"?yonghuming="+yhm+"");
               break;
           case "4"://查看登录名（用户名，手机号码，邮箱）是否已经注册
               count = dengLuDao.checkDengLuMingIsExist(dengluming);
               jsonObject.put("panduancount",count);
               break;
           case "5"://账号密码 登录按钮
               ServletContext servletContext = request.getServletContext();
               long jiezhishijian = 0L;//每次冻结的截止时间
               if (dengLuDao.zhuangTai(dengluming) == 1) {//用户状态为可登录时 zhuangtai = 1
                   if (dengLuDao.checkMiMaIsTrue(dengluming).equals(mima)) {//账号密码对应
                       keHuDengLu.setShouJiHaoMa(dengluming);
                       keHuDengLu.setZuiHouYiCiDengLuShiJian(timestamp);
                       dengLuDao.dengLu(keHuDengLu);//dao中的登录方法
                       Cookie cookie = new Cookie(StartShopping_DengLuMing, dengluming);
                       response.addCookie(cookie);
                       dengLuDao.dengLuShiBai(dengluming,0);
                       jsonObject.put("zhanghaomima", "duiyin");//返回前端用于判断是否让其登录
                       yhm = dengLuDao.dengLuMingChaYongHuMing(dengluming);
                       session.setAttribute("yonghuming",yhm);
                       jsonObject.put("url",dengluchenggong_url+"?yonghuming="+yhm+"");
                   } else {//账号与密码不对应
                       Timestamp dongjieshijian = null;
                       long longshijian =  System.currentTimeMillis();//long型当前时间
                       int cuowucishu = dengLuDao.checkCuoWuCiShu(dengluming);
                       if (0 <= cuowucishu && cuowucishu < 2) {//第一次和第二次输入错误进来
                           dengLuDao.dengLuShiBai(dengluming, ++cuowucishu);
                       } else if (cuowucishu == 2) {//第三次输入错误进来,冻结五分钟
                           dengLuDao.dengLuShiBai(dengluming, ++cuowucishu);
                            jiezhishijian = longshijian+5*60*1000;
                           dongjieshijian = new Timestamp(jiezhishijian);//把long转换为Timestamp类型 5*60*1000
                           dengLuDao.caoZuoZhuangTai(dengluming,0,dongjieshijian);
                           servletContext.setAttribute("dongjieshijian",jiezhishijian);
                       }
                       jsonObject.put("cuowucishu", cuowucishu);
                       jsonObject.put("zhanghaomima", "buduiyin");//返回前端用于判断是否让其登录
                   }
                   jsonObject.put("zhuangtai", "1");//返回前端用于判断此用户是否记入黑名单,1位可登陆
               } else {//用户状态为不可登录时 zhuangtai = 0
                   Object dongjieshijian_Object = servletContext.getAttribute("dongjieshijian");
                   if( dongjieshijian_Object != null){
                       long dongjieshijian_long = Long.valueOf(dongjieshijian_Object.toString());
                        if( timestamp.getTime() >= dongjieshijian_long){//超过冻结时间
                            if (dengLuDao.checkMiMaIsTrue(dengluming) == mima) {//账号密码对应
                                keHuDengLu.setShouJiHaoMa(dengluming);
                                keHuDengLu.setZuiHouYiCiDengLuShiJian(timestamp);
                                dengLuDao.dengLu(keHuDengLu);//dao中的登录方法
                                Cookie cookie = new Cookie(StartShopping_DengLuMing, dengluming);
                                response.addCookie(cookie);
                                dengLuDao.dengLuShiBai(dengluming,0);
                                jsonObject.put("zhanghaomima", "duiyin");//返回前端用于判断是否让其登录
                                yhm = dengLuDao.dengLuMingChaYongHuMing(dengluming);
                                session.setAttribute("yonghuming",yhm);
                                jsonObject.put("url",dengluchenggong_url+"?yonghuming="+yhm+"");
                                //登录成功，该用户解冻（状态字段重置为1）
                                dengLuDao.caoZuoZhuangTai(dengluming,1,timestamp);
                                jsonObject.put("zhuangtai", "1");//返回前端用于判断此用户是否记入黑名单
                            } else {//账号与密码不对应
                                Timestamp dongjieshijian = null;
                                long longshijian =  System.currentTimeMillis();//long型当前时间
                                int cuowucishu = dengLuDao.checkCuoWuCiShu(dengluming);
                                if(3 <= cuowucishu && cuowucishu < 5){//第四次和第五次输入错误进来
                                    dengLuDao.dengLuShiBai(dengluming, ++cuowucishu);
                                }else if(cuowucishu == 5){//第六次输入错误进来,冻结三十分钟
                                    dengLuDao.dengLuShiBai(dengluming, ++cuowucishu);
                                    jiezhishijian = longshijian+30*60*1000;
                                    dongjieshijian = new Timestamp(jiezhishijian);//把long转换为Timestamp类型
                                    dengLuDao.caoZuoZhuangTai(dengluming,0,dongjieshijian);
                                    servletContext.setAttribute("dongjieshijian",jiezhishijian);
                                }else if(6 <= cuowucishu && cuowucishu < 8){
                                    dengLuDao.dengLuShiBai(dengluming, ++cuowucishu);
                                }else if(cuowucishu == 8){//第九次输入错误进来,冻结二十四小时
                                    dengLuDao.dengLuShiBai(dengluming, ++cuowucishu);
                                    jiezhishijian = longshijian+24*60*60*1000;
                                    dongjieshijian = new Timestamp(jiezhishijian);//把long转换为Timestamp类型
                                    dengLuDao.caoZuoZhuangTai(dengluming,0,dongjieshijian);
                                    servletContext.setAttribute("dongjieshijian",jiezhishijian);
                                }else if(cuowucishu == 9){//冻结二十四小时后，第一次登录,如果再次输错，就永久冻结
                                    dengLuDao.dengLuShiBai(dengluming, ++cuowucishu);//错误次数为10时，冻结10年
                                    jiezhishijian = longshijian+10*365*24*60*60*1000;
                                    dongjieshijian = new Timestamp(jiezhishijian);//把long转换为Timestamp类型
                                    dengLuDao.caoZuoZhuangTai(dengluming,0,dongjieshijian);
                                    servletContext.setAttribute("dongjieshijian",jiezhishijian);
                                }
                                jsonObject.put("cuowucishu", cuowucishu);
                                jsonObject.put("zhuangtai", "1");//返回前端用于判断此用户是否记入黑名单
                                jsonObject.put("zhanghaomima", "buduiyin");//返回前端用于判断是否让其登录
                            }
                        }else {//未超过冻结时间
                            jsonObject.put("zhuangtai", "0");//返回前端用于判断此用户是否记入黑名单，0为冻结
                        }
                   }else {
                       jsonObject.put("zhuangtai", "0");//返回前端用于判断此用户是否记入黑名单，0为冻结
                   }
               }
               break;
       }

        return jsonObject;
    }

    /**
     *  对参数进行判断,然后调用并传参Dao层的chaRuCaoZuoJiLu（）方法
     * @param option  switch 对应的操作请求参数
     * @param yonghuming 执行此操作用户对应的用户名
     */
    public void zhiXingChaRu(String option,String yonghuming){
        String caozuojilu = "";
        if(option.equals("2")){
            caozuojilu = "登录 获取验证码";
        }else if(option.equals("3")){
            caozuojilu = "手机验证 登录";
        }else if(option.equals("5")){
            caozuojilu = "账号密码 登录";
        }
        caoZuoJiLuDao.chaRuCaoZuoJiLu(yonghuming,caozuojilu);
    }
}
