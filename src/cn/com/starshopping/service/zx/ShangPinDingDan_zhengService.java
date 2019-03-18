package cn.com.starshopping.service.zx;

import cn.com.starshopping.dao.zx.GouWuCheDao_Zheng;
import cn.com.starshopping.pojo.DinDan;
import org.json.JSONArray;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Random;

public class ShangPinDingDan_zhengService {

    public void ShangPinDingDan_zhengService(HttpServletRequest request) {
        //前端ajax传递过来的数据，组装成一个数组
        String arr = request.getParameter("arr");
        String[] arrs = arr.split(",");
        String zongjia = request.getParameter("zongjia");//订单总价
        double zj = 0;
        if (zongjia != null && !zongjia.equals("")) {
            zj = Double.parseDouble(zongjia);
        }


        String shouhuodizhi = request.getParameter("shouhuodizhi");
        String num = request.getParameter("num");
        if (arr.length() == 1) {
            num = "123";
        }


//        //物流单号  10位随机数字组成
//        int wl=0;
//        String wldh="";
//        Random random = new Random();
//        for (int i=0;i<10;i++){
//            wl =random.nextInt(10);
//            wldh=wldh+wl;
//        }

        //订单编号  10位随机数字组成
        String ddbh = request.getParameter("ddbh");

        GouWuCheDao_Zheng gouWuCheDao_zheng = new GouWuCheDao_Zheng();
        //根据用户名和结算商品的编号查询购物车得到该订单信息
        String yonghuming = (String) request.getSession().getAttribute("yonghuming");
        JSONArray jsonArray = new JSONArray();
        if (num == null) {
            jsonArray = gouWuCheDao_zheng.selectDinDanShangPingXinXi(arrs, yonghuming);
        } else {
            jsonArray = gouWuCheDao_zheng.selectShangPinXinXi(arrs, 10);
        }


        DinDan dinDan = new DinDan();
        dinDan.setDingDanBianHao(ddbh);
        dinDan.setYongHuMing(yonghuming);
        dinDan.setShouHuoDiZhi(shouhuodizhi);
        dinDan.setShangPinXinXi(jsonArray.toString());
        dinDan.setDingDanZongE(zj);
        dinDan.setDingDanZhuangTai(0);

        boolean action = gouWuCheDao_zheng.insertDingDanXinXi(dinDan);
        if (action) {//订单插入成功

        }


    }

}
