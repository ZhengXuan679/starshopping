package cn.com.starshopping.service;

import cn.com.starshopping.dao.CaoZuoJiLuDao;
import cn.com.starshopping.dao.HouTaiDengLuDao;
import cn.com.starshopping.pojo.KeFuDengLu;
import cn.com.starshopping.util.SessionKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: yanmin
 * Date: 2019/01/14
 * Description: 客服登录service
 * Version: V1.0
 */
public class HouTaiDengLuService {

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    public String kongZhiTai(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        String json = "";
        if (action != null && !action.trim().equals("")) {  // 如果状态不为空
            if (action.equals("denglu")) {              // 如果前端是查询订单列表
                json = dengLu(request);
            } else if (action.equals("xiugai")) {  //  如果前端是查询订单详情
                String xinMing = (String) request.getSession().getAttribute(SessionKey.KEFU); // 得到登录名
                if (xinMing != null) {
                    json = xiuGai(request);  // 调用查询方法
                    CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();   // 操作记录Dao
                    caoZuoJiLuDao.chaRuCaoZuoJiLu(xinMing,"修改密码");  // 调用操作记录方法，记录操作
                } else {
                    json = "{\"returncode\":-100}";  // 没有登录
                }
            }
        }
        return json;
    }


    /**
     * 登录验证方法
     *
     * @param request
     * @return
     */
    public String dengLu(HttpServletRequest request) {
        String action = "{\"returncode\":-1}";   // 初始状态码为-1,验证成功赋值200，验证失败直接返回
        String yongHuMing = request.getParameter("yonghuming");
        String miMa = request.getParameter("mima");
        if (yongHuMing != null && miMa != null && !yongHuMing.trim().equals("") && !miMa.trim().equals("")) { // 如果用户名密码不为空
            KeFuDengLu keFuDengLu = new KeFuDengLu(); // 客服登录pojo表
            if (yongHuMing.trim().length() == 18) {         // 如果用户名长度为18，那么用户输入的是身份证号码
                keFuDengLu.setShenFenZheng(yongHuMing);  // 身份证号码存入pojo对象
            } else {   // 否则就默认用户输入的是手机号码
                keFuDengLu.setShouJiHaoMa(yongHuMing);   // 手机号码存入pojo对象
            }
            keFuDengLu.setMiMa(miMa);   // 密码存入
            HouTaiDengLuDao houTaiDengLuDao = new HouTaiDengLuDao(); // 后台登录dao对象
            KeFuDengLu returnZhuangTai = houTaiDengLuDao.chaXunZhuangTai(keFuDengLu);  // 调用查询状态方法，查看该账号情况
            if (returnZhuangTai == null) {   // 如果状态查询结果为空，那么说明账号不存在，直接return
                return "{\"returncode\":-1}";
            }
            int zhangTai = returnZhuangTai.getZhuangTai();   // 得到状态
            if (zhangTai != -1) {   // 如果状态不为-1
                KeFuDengLu returnKeFuDengLu = houTaiDengLuDao.dengLu(keFuDengLu);   // 调用登录方法进行验证，用一个对象来接收
                if (returnKeFuDengLu != null) {
                    // 如果登录成功传出客服姓名到session中，并且返回状态码200
                    keFuDengLu.setCuoWuCiShu(0);   // 登录成功，错误次数初始化为0
                    houTaiDengLuDao.cuoWu(keFuDengLu);  // 调用修改错误次数方法
                    request.getSession().setAttribute(SessionKey.KEFU, returnKeFuDengLu.getXingMing());    // 传出姓名
                    request.getSession().setAttribute(SessionKey.SFZ, returnKeFuDengLu.getShenFenZheng()); // 传出身份证号码
                    CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();   // 操作记录Dao
                    caoZuoJiLuDao.chaRuCaoZuoJiLu(returnKeFuDengLu.getXingMing(),"登录");  // 调用操作记录方法，记录操作
                    String returnMiMa = houTaiDengLuDao.miMaChaXun(keFuDengLu);   // 调用查询密码方法,如果密码为初始密码，直接跳转密码修改页面
                    houTaiDengLuDao.xiuGaiShiJian(keFuDengLu);  // 调用登录时间记录方法
                    if (zhangTai == 110) {  // 如果是大老板
                        action = "{\"returncode\":110}";    // 大老板专用状态码110
                    } else {   // 不是大老板
                        if (returnMiMa != null && returnMiMa.equals("111111")) {  // 如果密码为初始密码
                            action = "{\"returncode\":199}";    // 状态码为199
                        } else {
                            action = "{\"returncode\":200}";
                        }
                    }
                } else {
                    if (zhangTai == 110) {    // 如果是大老板账号
                        action = "{\"returncode\":-3}";   // 状态码-3，说明老板的密码错了，老板号不封禁
                    } else {                    // 如果是普通账号
                        KeFuDengLu returnCuoWuCiShu = houTaiDengLuDao.chaXunCuoWu(keFuDengLu);   // 调用查询错误次数方法
                        int cuoWuCiShu = returnCuoWuCiShu.getCuoWuCiShu(); // 得到错误次数
                        keFuDengLu.setCuoWuCiShu(cuoWuCiShu + 1);   // 错误次数+1传入pojo对象
                        houTaiDengLuDao.cuoWu(keFuDengLu);  // 调用修改错误次数方法
                        if (cuoWuCiShu == 3) {              // 如果错误次数为3的时候
                            keFuDengLu.setZhuangTai(-1);    // 状态修改为-1，冻结账户
                            houTaiDengLuDao.dongJie(keFuDengLu);   // 调用冻结方法冻结此账号
                            action = "{\"returncode\":-2}";   // 账号已被封禁，找老板解锁
                        }
                    }
                }
            } else {
                action = "{\"returncode\":-2}";   // 账号已被封禁，找老板解锁
            }
        }
        return action;
    }

    /**
     * 修改密码
     *
     * @param request
     * @return
     */
    public String xiuGai(HttpServletRequest request) {
        String action = "{\"returncode\":-1}";   // 初始状态码为-1,验证成功赋值200，修改失败直接返回
        String miMa = request.getParameter("mima");  // 得到密码
        String miMa2 = request.getParameter("mima2");  // 得到密码2
        if (miMa != null && miMa2 != null && miMa.trim().equals(miMa2.trim())) {  // 如果密码不为空并且两次输入的密码相同
            KeFuDengLu keFuDengLu = new KeFuDengLu(); // 客服登录pojo表
            String shenFenZheng = (String) request.getSession().getAttribute(SessionKey.SFZ);  // 得到身份证号码
            keFuDengLu.setMiMa(miMa);  // 传入密码
            keFuDengLu.setShenFenZheng(shenFenZheng); // 传入身份证号码
            HouTaiDengLuDao houTaiDengLuDao = new HouTaiDengLuDao(); // 后台登录dao对象
            if (houTaiDengLuDao.xiuGaiMiMa(keFuDengLu)) { // 调用修改密码方法，因为方法返回值为boolean，如果修改成功
                action = "{\"returncode\":200}";  // 如果修改成功
            } else {
                action = "{\"returncode\":-3}";  // 如果修改失败，代表服务器出问题
            }
        } else {
            action = "{\"returncode\":-2}";  // 状态码-2，代表两次密码输入不一致
        }
        return action;
    }
}
