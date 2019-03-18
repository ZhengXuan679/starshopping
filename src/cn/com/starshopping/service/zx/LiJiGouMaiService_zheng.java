package cn.com.starshopping.service.zx;

import cn.com.starshopping.dao.zx.GouWuCheDao_Zheng;
import cn.com.starshopping.pojo.ShangPinXinXi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LiJiGouMaiService_zheng {

    public String ShangPingJieShuan_zhengService(HttpServletRequest request){

//        HttpSession session = request.getSession();
//        session.setAttribute("yonghuming","zx");
        String yonghuming =(String)request.getSession().getAttribute("yonghuming");


        //获得商品的组编号 规格 价格 数量 照片地址 商品名称
        String ShangPinZuBianHao = request.getParameter("ShangPinZuBianHao");
        String guige1 = request.getParameter("guige1");
        String guige2 = request.getParameter("guige2");

        String ms = request.getParameter("ms");

        ShangPinXinXi shangPinXinXi = new ShangPinXinXi();
        shangPinXinXi.setShangPinGuiGe1(guige1);
        shangPinXinXi.setShangPinGuiGe2(guige2);
        shangPinXinXi.setShangPinZuBianHao(ShangPinZuBianHao);
        GouWuCheDao_Zheng gouWuCheDao_zheng = new GouWuCheDao_Zheng();

        if(yonghuming!=null ){//用户名不为null
            if( guige1!=null &&guige2!=null && !guige1.equals("") && !guige2.equals("")){//规格不为空才可以加入购物车
                //根据组编号 规格查询得到商品的编号
                shangPinXinXi = gouWuCheDao_zheng.select_shangpinbianhao(shangPinXinXi);
                String shangpinbianhao = shangPinXinXi.getShangPinBianHao();
               return shangpinbianhao;
                }
            }
        return "";

    }


}
