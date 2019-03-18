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

/**
 * Created with IntelliJ IDEA.
 * User: huxiaoyi
 * Date: 2019/01/05
 * Description: 客户管理service层
 * Version: V1.0
 */
public class KeHuService {
    public JSONObject kehuguanli(HttpServletRequest request) throws IOException {
        //判断客服是否登录，若没登录则返回错误代码-100
        JSONObject jsonObject1 = new JSONObject();
        String kefu = (String) request.getSession().getAttribute(SessionKey.KEFU);
        if (kefu == null || kefu.trim().equals("")) {
            jsonObject1 = new JSONObject();
            jsonObject1.put("code", -100);
            return jsonObject1;
        } else {
            //记录操作
            CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();
            caoZuoJiLuDao.chaRuCaoZuoJiLu(kefu, "搜索用户");
        }
        //得到前台传入的搜索条件
        BufferedReader bufferedReader = request.getReader();
        String json = bufferedReader.readLine();
        KeHuDao keHuDao = new KeHuDao();
        KeHuDengLu keHuDengLu = new KeHuDengLu();
        JSONObject jsonObject = new JSONObject(json);
        String action = jsonObject.get("action").toString();
        //搜索操作
        if (action.equals("1")) {
            JSONObject sousuojsonObject = jsonObject.getJSONObject("data");
            keHuDengLu.setYongHuMing(sousuojsonObject.getString("yonghuming"));
            keHuDengLu.setShouJiHaoMa(sousuojsonObject.getString("shoujihaoma"));
            if (sousuojsonObject.getString("dengludatemin") != null &&    // 如果传入的时间不为空
                    !sousuojsonObject.getString("dengludatemin").equals("")) {
                keHuDengLu.setZuiHouYiCiDengLuShiJian(Timestamp.valueOf(sousuojsonObject.getString("dengludatemin")+" 00:00:00.000"));
            }
            if (sousuojsonObject.getString("zhucedatemin") != null &&    // 如果传入的时间不为空
                    !sousuojsonObject.getString("zhucedatemin").equals("")) {
                keHuDengLu.setZhuCeShiJian(Timestamp.valueOf(sousuojsonObject.getString("zhucedatemin")+" 00:00:00.000"));
            }
            String dengludatemax = "";
            String zhucedatemax = "";
            if (sousuojsonObject.getString("dengludatemax") != null &&    // 如果传入的时间不为空
                    !sousuojsonObject.getString("dengludatemax").equals("")) {
                dengludatemax = sousuojsonObject.getString("dengludatemax")+" 23:59:59.999";
            }
            if (sousuojsonObject.getString("zhucedatemax") != null &&    // 如果传入的时间不为空
                    !sousuojsonObject.getString("zhucedatemax").equals("")) {
                zhucedatemax = sousuojsonObject.getString("zhucedatemax")+" 23:59:59.999";
            }
            int page = sousuojsonObject.getInt("page");
            int limit = sousuojsonObject.getInt("limit");
            if (limit == 0) {
                limit = 10;   //每页显示条数，默认为10
            }

            jsonObject1 = keHuDao.souSuo(keHuDengLu, dengludatemax, zhucedatemax, page, limit);
            jsonObject1.put("username", kefu);
            return jsonObject1;
        }
        //启用和停用用户状态操作
        else if (action.equals("2")) {
            int zhuangtai = jsonObject.getInt("zhuangtai");
            int id = jsonObject.getInt("id");
            int code = keHuDao.bianJi(zhuangtai, id);
            jsonObject1 = new JSONObject();
            jsonObject1.put("code", code);
            jsonObject1.put("username", kefu);
            return jsonObject1;
        }
        //查看用户信息
        else if (action.equals("3")) {
            String yonghuming = jsonObject.getString("yonghuming");
            jsonObject1 = keHuDao.chaKan(yonghuming);
            jsonObject1.put("username", kefu);
            return jsonObject1;
        }
        jsonObject1 = new JSONObject();
        return jsonObject1;
    }
}
