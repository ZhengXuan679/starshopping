package cn.com.starshopping.service;

import cn.com.starshopping.dao.CaoZuoJiLuDao;
import cn.com.starshopping.dao.KeFuDao;
import cn.com.starshopping.pojo.KeFuDengLu;
import cn.com.starshopping.util.SessionKey;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: yanmin
 * Date: 2019/01/14
 * Description:
 * Version: V1.0
 */
public class KeFuService {

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    public String kongZhiTai(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        String json = "";
        String xinMing = (String) request.getSession().getAttribute(SessionKey.KEFU); // 得到登录名
        if (xinMing != null) {  // 如果登录名不为空，进行以下操作，否则直接返回状态码-100，代表没有登录
            if (action != null && !action.trim().equals("")) {  // 如果状态不为空
                CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();   // 操作记录Dao
                if (action.equals("yuangong")) {              // 如果前端是查询员工列表
                    caoZuoJiLuDao.chaRuCaoZuoJiLu(xinMing, "查询员工列表");  // 调用操作记录方法，记录操作
                    json = chaXunKeFu(request);
                } else if (action.equals("zhuangtaixiugai")) {  //  如果修改客服状态
                    caoZuoJiLuDao.chaRuCaoZuoJiLu(xinMing, "修改客服状态");  // 调用操作记录方法，记录操作
                    json = xiuGaiZhuangTai(request);
                } else if(action.equals("keFuXinZeng")){
                    caoZuoJiLuDao.chaRuCaoZuoJiLu(xinMing, "新增客服");  // 调用操作记录方法，记录操作
                    json = xinZengKeFu(request);  // 调用新增方法
                }
            }
        } else {
            json = "{\"returncode\":-100}";  // 没有登录
        }

        return json;
    }

    /**
     * 客服查询方法
     *
     * @param request
     * @return
     */
    public String chaXunKeFu(HttpServletRequest request) {
        String action = "{\"returncode\":-1}";
        String keFu = (String) request.getSession().getAttribute(SessionKey.KEFU);  // 得到登录的客服姓名
//        if (keFu != null && keFu.equals("大老板")) {  // 如果登录账号是大老板的
        String xingMing = request.getParameter("xingMing");   // 得到姓名
        String yeMa = request.getParameter("yema");   // 得到页码
        int fenYeIndex = 0;               // 开始分页下标,默认从0开始
        if (yeMa != null) {
            fenYeIndex = (Integer.parseInt(yeMa) - 1) * 10;   // 得到开始分页下标
        }
        KeFuDengLu keFuDengLu = new KeFuDengLu();   // 客服登录pojo对象
        keFuDengLu.setXingMing(xingMing.trim());  // 姓名传入pojo对象
        KeFuDao keFuDao = new KeFuDao();         // 客服管理dao对象
        JSONArray jsonArray = keFuDao.chaXunKeFu(keFuDengLu, fenYeIndex);         // 调用查询客服方法，用一个JSONArray接收
        JSONObject jsonObject = new JSONObject();
        if (jsonArray != null && jsonArray.length() != 0) {  // 查询结果的JSONArray不为空
            jsonObject.put("returncode", "200");   // 状态码200
            jsonObject.put("data", jsonArray);    // 存入
            jsonObject.put("zongtiaoshu", keFuDengLu.getZongTiaoShu());  // 得到总条数
            jsonObject.put("xingMing", (String) request.getSession().getAttribute(SessionKey.KEFU));  // 登录人姓名存入
            action = jsonObject.toString();
        }
//        }
        return action;
    }

    /**
     * 修改状态，解除冻结
     *
     * @param request
     * @return 成功返回状态码200，失败返回-1
     */
    public String xiuGaiZhuangTai(HttpServletRequest request) {
        String action = "{\"returncode\":-1}";
        String id = request.getParameter("id");  // 得到id
        String zhuangTai = request.getParameter("zhuangTai");  // 得到状态
        if (id != null && !id.equals("") && zhuangTai != null && !zhuangTai.equals("")) {  // 如果状态和id不为空
            KeFuDengLu keFuDengLu = new KeFuDengLu(); // 客服登录pojo
            keFuDengLu.setId(Integer.parseInt(id)); // 传入id
            keFuDengLu.setZhuangTai(Integer.parseInt(zhuangTai)); // 传入状态
            KeFuDao keFuDao = new KeFuDao();
            keFuDao.xiuGaiZhuangTai(keFuDengLu);
            action = "{\"returncode\":200}";
        }
        return action;
    }

    /**
     * 新增客服
     * @param request
     * @return
     */
    public String xinZengKeFu(HttpServletRequest request) {
        String action = "{\"returncode\":-1}";    // 失败状态码为-1
        String xingMing = request.getParameter("xingMing");  // 得到姓名
        String shouJi = request.getParameter("shouJi");  // 得到手机号码
        String shenFen = request.getParameter("shenFen");  // 得到身份证号码
        if(xingMing != null && shouJi != null && shenFen != null &&    // 如果姓名，手机号码，身份证不为空
                !xingMing.trim().equals("") && !shouJi.trim().equals("") && !shenFen.trim().equals("")){
            KeFuDengLu keFuDengLu = new KeFuDengLu(); // 客服登录pojo对象
            keFuDengLu.setXingMing(xingMing.trim()); // 传入姓名,去掉头尾空格
            keFuDengLu.setShouJiHaoMa(shouJi.trim());  // 传入手机号码,去掉头尾空格
            keFuDengLu.setShenFenZheng(shenFen.trim()); // 传入身份证号码，去掉头尾空格
            KeFuDao keFuDao = new KeFuDao();  // 客服dao对象
            keFuDao.xinZengKeFu(keFuDengLu); // 调用新增客服方法
            action = "{\"returncode\":200}";  // 因为是阻塞方法，所以以上方法执行完毕才会执行
        }
        return action;
    }
}
