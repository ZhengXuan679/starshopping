package cn.com.starshopping.service.fzy;


import cn.com.starshopping.dao.hzy.CaoZuoJiLuDao;
import cn.com.starshopping.dao.hzy.ZhuCeDao;
import cn.com.starshopping.pojo.KeHuDengLu;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;

import static cn.com.starshopping.util.fzy.sessionkey.sessionkey.sessionkey_mima;
import static cn.com.starshopping.util.fzy.sessionkey.sessionkey.sessionkey_shoujihaoma;
import static cn.com.starshopping.util.fzy.sms.SendSMS.sendSMS;

public class ZhuCeService {
    private final CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();

    /**
     * 通过 switch case 判断前端ajax所有请求的操作函数
     * @param request
     * @param response
     * @return 返回一个JSONObject，用于Servlet将其返回前端。
     * @throws IOException
     */
    public JSONObject editorAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader br = request.getReader();//只会读一行，传过来的json一行显示。
        String jsonstr = br.readLine();
        ZhuCeDao zhuCeDao = new ZhuCeDao();
        JSONObject jsonObject = new JSONObject(jsonstr);
        String option  = jsonObject.getString("option");//switch case 对应的操作指令
        String shoujihaoma = jsonObject.getString("shoujihaoma");
        zhiXingChaRu(option,shoujihaoma);//操作日志记录
        HttpSession session = request.getSession();
        switch (option){
            case "1"://查看手机号是否已经存在
                jsonObject.put("panduancount",zhuCeDao.checkPhoneIsExist(shoujihaoma));
                break;
            case "2"://获取验证码
                String yanzhengma = sendSMS(shoujihaoma);
                jsonObject.put("shoujiyanzhengma",yanzhengma);
                break;
            case "3"://点击 立即注册
                 String mima = jsonObject.getString("mima");
                 session.setAttribute(sessionkey_shoujihaoma,shoujihaoma);
                 session.setAttribute(sessionkey_mima,mima);
                 //url   页面跳转地址
                 jsonObject.put("url","/html/zhanghaobuquan.html");
                break;
            case "4"://默认调用一次 让账号补全页面拿到session中的手机号码和密码
                jsonObject.put("shoujihaoma",session.getAttribute(sessionkey_shoujihaoma));
                jsonObject.put("mima",session.getAttribute(sessionkey_mima));
                break;
            case "5"://查看邮箱是否已经存在
                jsonObject.put("panduancount",zhuCeDao.checkMailIsExist(jsonObject.getString("youxiang")));
                break;
            case "6"://查看用户名是否已经存在
                jsonObject.put("panduancount",zhuCeDao.checkMingIsExist(jsonObject.getString("yonghuming")));
                break;
            case "7"://确认提交 --- 注册  （账号信息写入数据库）
                Timestamp now = new Timestamp(System.currentTimeMillis());//此时时间
                KeHuDengLu keHuDengLu = new KeHuDengLu();
                 keHuDengLu.setZhuCeShiJian(now);
                 keHuDengLu.setShouJiHaoMa(shoujihaoma);
                 keHuDengLu.setMiMa(jsonObject.getString("mima"));
                 keHuDengLu.setYongHuMing(jsonObject.getString("yonghuming"));
                 keHuDengLu.setYouXiang(jsonObject.getString("youxiang"));
                zhuCeDao.insertInto(keHuDengLu);//kehudenglu表
                zhuCeDao.chaRuYongHu(keHuDengLu);//yonghu表
                jsonObject.put("url","/html/denglu.html");
                break;


        }//switch

        return jsonObject;
    }//editorAll方法

    /**
     *  对参数进行判断,然后调用并传参Dao层的chaRuCaoZuoJiLu（）方法
     * @param option  switch 对应的操作请求参数
     * @param yonghuming 执行此操作用户对应的用户名
     */
    public void zhiXingChaRu(String option,String yonghuming){
        String caozuojilu = "";
        if(option.equals("7")){
            caozuojilu = "注册成功";
        }
        caoZuoJiLuDao.chaRuCaoZuoJiLu(yonghuming,caozuojilu);
    }
}//类
