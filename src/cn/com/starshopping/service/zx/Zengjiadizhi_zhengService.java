package cn.com.starshopping.service.zx;

import cn.com.starshopping.dao.zx.SouHuoDiZhiDao_zheng;
import cn.com.starshopping.pojo.ShouHuoDiZhi;

import javax.servlet.http.HttpServletRequest;

public class Zengjiadizhi_zhengService {

    /**
     * 增加收货地址
     * @param request
     */
    public int  Zengjiadizhi_zhengService(HttpServletRequest request){
        String myuser = request.getParameter("myuser");
        String sjhm = request.getParameter("sjhm");
        String szdq = request.getParameter("szdq");
        String xxdz = request.getParameter("xxdz");
        String yonghuming =(String)request.getSession().getAttribute("yonghuming");

        ShouHuoDiZhi shouHuoDiZhi = new ShouHuoDiZhi();
        shouHuoDiZhi.setYongHuMing(yonghuming);
        shouHuoDiZhi.setShouHuoRen(myuser);
        shouHuoDiZhi.setLianXiDianHua(sjhm);
        shouHuoDiZhi.setSuoZaiDiQu(szdq);
        shouHuoDiZhi.setXiangXiDiZhi(xxdz);

        //增加收货地址
        SouHuoDiZhiDao_zheng souHuoDiZhiDao_zheng = new SouHuoDiZhiDao_zheng();
        boolean action = souHuoDiZhiDao_zheng.insertShouHuoDiZhi(shouHuoDiZhi);
        if(action){
            //返回ajax的状态码 200 代表成功  -1代表失败
           return 200;
        }

        return  -1;
    }
}
