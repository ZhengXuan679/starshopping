package cn.com.starshopping.service.zx;

import cn.com.starshopping.dao.zx.LiuLanShangPinDao_Zheng;
import cn.com.starshopping.pojo.ShangPinXinXi;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class ShangPinXiangQing_zhengService {
    public JSONObject ShangPinXiangQing(HttpServletRequest request) {
        String shangPinZuBianHao = request.getParameter("ShangPinZuBianHao");


        ShangPinXinXi shangPinXinXi = new ShangPinXinXi();
        shangPinXinXi.setShangPinZuBianHao(shangPinZuBianHao);
        LiuLanShangPinDao_Zheng liuLanShangPinDao_zheng = new LiuLanShangPinDao_Zheng();
        //查询商品信息的所有内容，通过查询到的内容组装成一个JSONObject{"success",JsonArray}在通过JSONObject得到组编号
        JSONObject jsonObject = liuLanShangPinDao_zheng.selectShangPinXiangQingDao(shangPinXinXi);
        JSONArray success =jsonObject.getJSONArray("success");
        String shangPinLeiXin = success.getJSONObject(0).getString("ShangPinLeiXin");
        ShangPinXinXi shangPinXinXi2 = new ShangPinXinXi();
        shangPinXinXi2.setShangPinZuBianHao(shangPinZuBianHao);
        shangPinXinXi2.setShangPinLeiXin(shangPinLeiXin);

        //通过得到的组编号查询商品类型关系表，得到商品的属性（颜色，尺码，型号。。。。。）
        JSONObject jsonObject1 = liuLanShangPinDao_zheng.selectShangPinLeiXing(shangPinXinXi2);

        //通过组编号，查询规格1的所有属性并组装成一个jsonArray
        JSONObject jsonObject2 = liuLanShangPinDao_zheng.selectShangGuiGe1(shangPinXinXi2);



        //通过组编号，查询规格2的所有属性并组装成一个jsonArray
        JSONObject jsonObject3 = liuLanShangPinDao_zheng.selectShangGuiGe2(shangPinXinXi2);


        int llt = liuLanShangPinDao_zheng.fileNum(shangPinZuBianHao, "浏览图",request);
        int xqt = liuLanShangPinDao_zheng.fileNum(shangPinZuBianHao, "详情图",request);


        JSONObject jsonObject4 = new JSONObject();
        jsonObject4.put("neirong",jsonObject);
        jsonObject4.put("guige",jsonObject1);
        jsonObject4.put("filed1",jsonObject2);
        jsonObject4.put("filed2",jsonObject3);
        jsonObject4.put("llt",llt);
        jsonObject4.put("xqt",xqt);

        if (jsonObject4 != null) {//如果商品不等于null代表查询商品详细信息成功
            System.out.println(jsonObject4);
            return jsonObject4;
        }
        return null;
    }
}
