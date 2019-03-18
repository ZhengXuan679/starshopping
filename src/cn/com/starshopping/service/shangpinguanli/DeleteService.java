package cn.com.starshopping.service.shangpinguanli;

import cn.com.starshopping.dao.CaoZuoJiLuDao;
import cn.com.starshopping.dao.shangpinguanli.DeleteDao;
import cn.com.starshopping.util.SessionKey;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Wong
 * Date: 2019/01/07
 * Description:
 * Version: V1.0
 */
public class DeleteService {
    public JSONObject delete(HttpServletRequest request) {
        String xingMing = (String) request.getSession().getAttribute(SessionKey.KEFU);
        if(xingMing == null){
            return new JSONObject("{\"returncode\":-100}");
        }
        JSONObject jsonObject = new JSONObject();
        DeleteDao deleteDao = new DeleteDao();
        // 获取前端组成的id字符串
        String idArray = request.getParameter("ids");
        // 如果该字符串长度为0说明前端没有进行勾选，不进行操作直接返回
        if (idArray.length() == 0) {
            jsonObject.put("returnCode", 2);
        } else {
            // 证明有值后将字符串分为字符串数组
            String[] ids = idArray.split(",");
            ArrayList<String> arrayList = new ArrayList<>();
            // 轮询数组放入集合中，排除空值
            for (int i = 0; i < ids.length; i++) {
                if (!ids[i].equals("")) {
                    arrayList.add(ids[i]);
                }
            }
            // 将集合遍历组成新的sql片段
            String newsql = deleteDao.newsql(arrayList);
            // 查询传入的id中有没有对应正在上架中的货物，有则不操作返回，无则进行删除操作
            boolean action = deleteDao.select(newsql);
            if (action) {
                // 将id传入删除对应的信息
                deleteDao.delete(newsql);
                jsonObject.put("returnCode", 0);
                CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();
                caoZuoJiLuDao.chaRuCaoZuoJiLu((String) request.getSession().getAttribute(SessionKey.KEFU), "删除");
            } else {
                jsonObject.put("returnCode", 1);
            }
        }
        jsonObject.put("username",xingMing);
        return jsonObject;
    }
}
