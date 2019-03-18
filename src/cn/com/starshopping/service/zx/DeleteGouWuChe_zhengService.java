package cn.com.starshopping.service.zx;


import cn.com.starshopping.dao.zx.GouWuCheDao_Zheng;

import javax.servlet.http.HttpServletRequest;

public class DeleteGouWuChe_zhengService {
    public String DeleteGouWuChe_zhengService(HttpServletRequest request){
        String[] arrs = request.getParameterValues("arr[]");//前面如果传递过来的json数据是一个数组 后台接收数据则要在用数组的方式获取
        GouWuCheDao_Zheng gouWuCheDao_zheng = new GouWuCheDao_Zheng();
        String yonghuming =(String)request.getSession().getAttribute("yonghuming");

        Boolean action=gouWuCheDao_zheng.deleteGouWuCheDao(arrs,yonghuming);//商品编号组成的数组  用户名 默认为 zx
        if (action){//删除购物车成功
            return "/html/shopcart.html";

        }
        return "";
    }
}
