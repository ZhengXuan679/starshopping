package cn.com.starshopping.service.fzy;


import cn.com.starshopping.dao.hzy.CaoZuoJiLuDao;
import cn.com.starshopping.dao.hzy.ZhangHuSheZhiDao;
import cn.com.starshopping.util.fzy.zhanghushezhimail.MyJavaMail;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static cn.com.starshopping.util.fzy.sms.SendSMS.sendSMS;

public class ZhangHuSheZhiService {
    private final CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();
    /**
     *对用户的请求指令做出判断并执行
     * @param request
     * @param response
     * @return 返回前端一个JSONObject
     */
    public JSONObject editorAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader br = request.getReader();//只读一行，前端传过来的json一行显示
        String jsonstr = br.readLine();
        JSONObject jsonObject = null;
        String option = "";
        String yonghuming = "";
        if(jsonstr == null || jsonstr == ""){
           option = request.getParameter("option");
           yonghuming = request.getParameter("yonghuming");
        }else {
            jsonObject = new JSONObject(jsonstr);
            option = jsonObject.getString("option");
            yonghuming = jsonObject.getString("yonghuming");
        }
        zhiXingChaRu(option,yonghuming);
        ZhangHuSheZhiDao zhangHuSheZhiDao = new ZhangHuSheZhiDao();
        switch (option){
            case "1"://根据用户名查询kehudenglu表中的相关数据
                jsonObject = zhangHuSheZhiDao.selectAll(yonghuming);
                break;
            case "2"://根据用户名查询手机号码是否对应
                String pipei_shoujihaoma = jsonObject.getString("shoujihaoma");
                jsonObject.put("returncount",zhangHuSheZhiDao.piPeiShouJi(yonghuming,pipei_shoujihaoma));
                break;
            case "3"://手机获取验证码
                String ganggaimima_shoujihaoma = jsonObject.getString("shoujihaoma");
                String ggmm_yanzhengma = sendSMS(ganggaimima_shoujihaoma);
                jsonObject.put("yanzhengma",ggmm_yanzhengma);
                break;
            case "4"://更改密码
                 zhangHuSheZhiDao.gengGaiMiMa(yonghuming,jsonObject.getString("mima"));
                break;
            case "5"://查询手机号码 是否已经被绑定
                jsonObject.put("panduancount",zhangHuSheZhiDao.selectPhoneIsBangDing(jsonObject.getString("shoujihaoma")));
                break;
            case "6"://查询邮箱与用户名是否对应
                jsonObject.put("panduancount",zhangHuSheZhiDao.piPeiYouXiang(yonghuming,jsonObject.getString("youxiang")));
                break;
            case "7"://更改手机号获取邮件
                String thisyouxiang = jsonObject.getString("youxiang");
                String thisyonghuming = jsonObject.getString("yonghuming");
                String thisxinshoujihaoma = jsonObject.getString("shoujihaoma");
                MyJavaMail myJavaMail = new MyJavaMail(thisyouxiang,thisyonghuming,thisxinshoujihaoma);
                new Thread(myJavaMail).start();//发送邮件
                break;
            case "8"://邮件超链接更改手机号码 请求
                zhangHuSheZhiDao.updateNewPhone(yonghuming,request.getParameter("xinshoujihaoma"));
                response.sendRedirect("/html/shezhi.html?yonghuming="+yonghuming+"");
                break;
            case "9"://查询邮箱是否已经被绑定
                jsonObject.put("panduancount",zhangHuSheZhiDao.selectMailIsBangDing(jsonObject.getString("youxiang")));
                break;
            case "10"://更改邮箱
                zhangHuSheZhiDao.gengGaiYouXiang(yonghuming,jsonObject.getString("youxiang"));
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
            caozuojilu = "账户设置，手机获取验证码";
        }else if(option.equals("4")){
            caozuojilu = "账户设置，更改了密码";
        }else if(option.equals("7")){
            caozuojilu = "账户设置，获取邮件(更改手机号)";
        }else if(option.equals("10")){
            caozuojilu = "账户设置，更改了邮箱";
        }
        caoZuoJiLuDao.chaRuCaoZuoJiLu(yonghuming,caozuojilu);
    }
}
