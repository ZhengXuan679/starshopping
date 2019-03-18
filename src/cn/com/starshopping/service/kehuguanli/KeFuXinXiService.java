package cn.com.starshopping.service.kehuguanli;

import cn.com.starshopping.dao.KeFuXinXiDao;

import cn.com.starshopping.pojo.KeFuDengLu;
import cn.com.starshopping.util.SessionKey;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: huxiaoyi
 * Date: 2019/01/05
 * Description:
 * Version: V1.0
 */
public class KeFuXinXiService {
    public JSONObject kefuxinxiService(HttpServletRequest request)throws IOException{
        //判断客服是否登录，若没登录则返回错误代码-100
        JSONObject jsonObject1= new JSONObject();
        String kefu = (String) request.getSession().getAttribute(SessionKey.KEFU);
        if (kefu == null || kefu.trim().equals("")) {
            jsonObject1.put("code", -100);
            return jsonObject1;
        }
        BufferedReader bufferedReader = request.getReader();
        String json = bufferedReader.readLine();
        JSONObject jsonObject = new JSONObject(json);
        KeFuXinXiDao keFuXinXiDaoDao = new KeFuXinXiDao();
        String msg = "";
        if(jsonObject.getString("action").equals("chakan")){
            jsonObject1 = keFuXinXiDaoDao.chakan(kefu);
            return jsonObject1;
        }
        //执行签到操作
        else if(jsonObject.getString("action").equals("qiandao")){
            boolean action = keFuXinXiDaoDao.qiandao(kefu);
            if(action==true){
                msg = "签到成功";
            }
            else{
                msg = "签到失败，请稍后再试";
            }
        }
        jsonObject1.put("msg",msg);
      return   jsonObject1;
    }
}
