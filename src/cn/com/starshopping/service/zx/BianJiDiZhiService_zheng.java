package cn.com.starshopping.service.zx;

import cn.com.starshopping.dao.zx.SouHuoDiZhiDao_zheng;
import cn.com.starshopping.pojo.ShouHuoDiZhi;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class BianJiDiZhiService_zheng {

    /**
     * 删除地址
     * @param request
     * @return
     */
    public int ShanChuDiZhiZhiService(HttpServletRequest request) {
        String[] id = request.getParameterValues("id[]");
        SouHuoDiZhiDao_zheng souHuoDiZhiDao_zheng = new SouHuoDiZhiDao_zheng();
        String yonghuming =(String)request.getSession().getAttribute("yonghuming");

        boolean action = souHuoDiZhiDao_zheng.deleteShouHuoDiZhiDao(id, yonghuming);
        if(action){
            return 200;//删除成功 200代表成功
        }
        return -1;//删除失败 -1代表失败
    }

    public JSONObject BianJiDiZhiService(HttpServletRequest request){
        SouHuoDiZhiDao_zheng souHuoDiZhiDao_zheng = new SouHuoDiZhiDao_zheng();
        String yonghuming =(String)request.getSession().getAttribute("yonghuming");

        JSONObject jsonObject = souHuoDiZhiDao_zheng.selectAllDiZhi(yonghuming);
        System.out.println(jsonObject);
        if (jsonObject!=null){//删除购物车成功
            return jsonObject;
        }
        return null;
    }

    /**
     * 修改地址
     * @param request
     * @return
     */
    public int  XiuGaiDiZhiService(HttpServletRequest request) {
        String id = request.getParameter("id");
        int id2=0;
        if(id!=null&& !id.equals("")){
             id2 = Integer.parseInt(id);
        }
        String ShouHuoRen = request.getParameter("ShouHuoRen");
        String SuoZaiDiQu = request.getParameter("SuoZaiDiQu");
        String XiangXiDiZhi = request.getParameter("XiangXiDiZhi");
        String LianXiDianHua = request.getParameter("LianXiDianHua");
        ShouHuoDiZhi shouHuoDiZhi = new ShouHuoDiZhi();
        shouHuoDiZhi.setId(id2);
        shouHuoDiZhi.setShouHuoRen(ShouHuoRen);
        shouHuoDiZhi.setSuoZaiDiQu(SuoZaiDiQu);
        shouHuoDiZhi.setXiangXiDiZhi(XiangXiDiZhi);
        shouHuoDiZhi.setLianXiDianHua(LianXiDianHua);

        SouHuoDiZhiDao_zheng souHuoDiZhiDao_zheng = new SouHuoDiZhiDao_zheng();
        boolean action = souHuoDiZhiDao_zheng.updateShouHuoDiZhi(shouHuoDiZhi);
        if(action){//true代表修改地址成功  返回200状态码
            return 200;
        }
        return -1;
    }
}
