package cn.com.starshopping.service.shangpinguanli;

import cn.com.starshopping.dao.CaoZuoJiLuDao;
import cn.com.starshopping.dao.shangpinguanli.SelectDao;
import cn.com.starshopping.util.SessionKey;
import org.json.JSONArray;
import org.json.JSONObject;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Wong
 * Date: 2019/01/05
 * Description:
 * Version: V1.0
 */
public class SelectService {
    public JSONObject select(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        String xingMing = (String) request.getSession().getAttribute(SessionKey.KEFU);
        String runCode = request.getParameter("runCode");
        if(xingMing == null){
            // 若用户名为空则未登录，直接return回去错误状态码
            return new JSONObject("{\"returncode\":-100}");
        }
//        // 如果有用户名则获取状态码，如果不为零说明是获取数据用，反之是获取用户名用
//        else if (runCode != null && runCode.equals("0")){
//            return new JSONObject("{\"username\":"+xingMing+"}");
//        }
            int page = 1;
            int limit = 10;
            String now_page = request.getParameter("page");
            String now_limit = request.getParameter("limit");
            // 设初始页码为1，若从前端能获取到页码则替换
            if (!now_page.equals("")){
                page = Integer.parseInt(now_page);
            }
            // 设分页信息为10，若从前端能获取到页码则替换
            if (!now_limit.equals("")){
                limit = Integer.parseInt(now_limit);
            }
            // 将页码传入构造函数封装
            SelectDao selectDao = new SelectDao(page,limit);
            // SelectDao selectDao = new SelectDao();
            // 将前端数据传入Dao取出各字段值组成集合，并将对应的占位符字符串放入集合最后
            ArrayList<String> newsql = selectDao.newSql(request);
            // 将组成的集合传入后台，将集合最后拼接进sql，再遍历值进入占位符得到返回的JSONArray
            JSONArray jsonArray = selectDao.select(newsql);
            int all_page = (jsonArray.length() - 1) / 10 + 1;
            // 传入前端插件需要的全部信息数
            jsonObject.put("total",all_page);
            // 传入前端插件需要的全部信息数
            jsonObject.put("count",selectDao.count(newsql));
            // 传入前端插件需要的返回状态码
            jsonObject.put("code",0);
            // 传入前端插件需要的回执信息
            jsonObject.put("msg","");
            // 传入前端插件需要的全部信息
            jsonObject.put("data",jsonArray);
            // 传入前端需要的用户名
            CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();
            caoZuoJiLuDao.chaRuCaoZuoJiLu((String)request.getSession().getAttribute(SessionKey.KEFU),"查询");
        return jsonObject;
    }
}
