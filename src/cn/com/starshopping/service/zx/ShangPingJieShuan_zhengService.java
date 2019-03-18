package cn.com.starshopping.service.zx;

import cn.com.starshopping.dao.zx.GouWuCheDao_Zheng;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

public class ShangPingJieShuan_zhengService {

    public  JSONObject  ShangPingJieShuan_zhengService(HttpServletRequest request){
        //前端ajax传递过来的数据，组装成一个数组
        String arr = request.getParameter("arr");
        String[] arrs = arr.split(",");
        System.out.println(arrs[0]);

        GouWuCheDao_Zheng gouWuCheDao_zheng = new GouWuCheDao_Zheng();
        //根据用户名和结算商品的编号查询购
        String yonghuming =(String)request.getSession().getAttribute("yonghuming");

        JSONObject jsonObject = gouWuCheDao_zheng.selectAllDingDan(arrs, yonghuming);
        if(jsonObject!=null){//代表查询成功
            return jsonObject;
        }
        return null;

    }


}
