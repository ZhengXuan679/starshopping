package cn.com.starshopping.service.fzy;


import cn.com.starshopping.dao.hzy.CaoZuoJiLuDao;
import cn.com.starshopping.dao.hzy.WangJiMiMaDao;
import cn.com.starshopping.util.fzy.wangjimimamail.MyJavaMail;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static cn.com.starshopping.util.fzy.sms.SendSMS.sendSMS;


public class WangJiMiMaService {
    private final CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();
    /**
     * 对用户的请求指令做出判断并执行
     * @param request
     * @param response
     */
    public JSONObject editorAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader br = null;//只读一行,前端传递过来的json对象一行显示
        JSONObject jsonObject = null;
        String option = "";
        String dengluming = "";
        br = request.getReader();
        String jsonstr = br.readLine();
        if(jsonstr != null && jsonstr != ""){
            jsonObject = new JSONObject(jsonstr);
            option = jsonObject.getString("option");
            dengluming = jsonObject.getString("dengluming");
        }else {
            option = request.getParameter("option");
            dengluming = request.getParameter("dengluming");
        }
        zhiXingChaRu(option,dengluming);
        WangJiMiMaDao wangJiMiMaDao = new WangJiMiMaDao();
        String tiaozhuanurl = "/html/denglu.html";
        int panduancount = 0;
        switch (option){
            case "1"://查询登录名是否已经注册
                 jsonObject.put("panduancount",wangJiMiMaDao.checkGengLumIngIsExist(dengluming));
                break;
            case "2"://查询手机号码与登录名是否匹配
                String return_shoujihaoma = wangJiMiMaDao.piPeiShouJi(dengluming);
                if(jsonObject.getString("shoujihaoma").equals(return_shoujihaoma)){
                    panduancount = 1;
                }
                jsonObject.put("panduancount",panduancount);
                break;
            case "3"://手机获取验证码
                String shoujhaoma = jsonObject.getString("shoujihaoma");
                jsonObject.put("sjyanzhengma",sendSMS(shoujhaoma));
                break;
            case "4"://查询手机号码与登录名是否匹配
                String return_youxiang = wangJiMiMaDao.piPeiYouXiang(dengluming);
                if(jsonObject.getString("youxiang").equals(return_youxiang)){
                    panduancount = 1;
                }
                jsonObject.put("panduancount",panduancount);
                break;
            case "5"://用户点击获取邮件，后台发送邮件
                String youxiang = jsonObject.getString("youxiang");
                String xmm = jsonObject.getString("xinmima");
                MyJavaMail myJavaMail = new MyJavaMail(youxiang,dengluming,xmm);
                new Thread(myJavaMail).start();//发送邮件
                break;
            case "6"://手机验证更改 登录
                String mima = jsonObject.getString("mima");
                wangJiMiMaDao.gengGaiChengGong(dengluming,mima);
                jsonObject.put("tiaozhuanurl",tiaozhuanurl);
                break;
            case "7"://邮件超链接
                String xinmima = request.getParameter("xinmima");
                wangJiMiMaDao.gengGaiChengGong(dengluming,xinmima);
                response.sendRedirect(tiaozhuanurl);
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
        if(option.equals("3")){
            caozuojilu = "忘记密码，手机获取验证码";
        }else if(option.equals("5")){
            caozuojilu = "忘记密码，邮箱获取验证邮件";
        }
        caoZuoJiLuDao.chaRuCaoZuoJiLu(yonghuming,caozuojilu);
    }
}
