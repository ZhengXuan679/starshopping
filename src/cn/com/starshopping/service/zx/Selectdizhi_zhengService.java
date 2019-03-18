package cn.com.starshopping.service.zx;

import cn.com.starshopping.dao.zx.SouHuoDiZhiDao_zheng;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

public class Selectdizhi_zhengService {

    /**
     * 查询收货地址
     * @param request
     * @return
     */
    public JSONObject ShouHuoDiZhi(HttpServletRequest request){
        SouHuoDiZhiDao_zheng souHuoDiZhiDao_zheng =new SouHuoDiZhiDao_zheng();
        String yonghuming =(String)request.getSession().getAttribute("yonghuming");

        JSONObject jsonObject = souHuoDiZhiDao_zheng.selectAllDiZhi(yonghuming);
       if(jsonObject!=null){//查询不为空
           return jsonObject;

       }
        return null;
    }
}
