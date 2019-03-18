package cn.com.starshopping.service.zx;

import cn.com.starshopping.dao.zx.GouWuCheDao_Zheng;
import cn.com.starshopping.pojo.GouWuChe;
import cn.com.starshopping.pojo.ShangPinXinXi;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class JiaRuGouWuChe_zhengService {
    public JSONObject jiarugouwuche_zheng(HttpServletRequest request){
//        HttpSession session = request.getSession();
//        session.setAttribute("yonghuming","zx");

        String yonghuming =(String)request.getSession().getAttribute("yonghuming");

        //获得商品的组编号 规格 价格 数量 照片地址 商品名称
        String ShangPinZuBianHao = request.getParameter("ShangPinZuBianHao");
        String guige1 = request.getParameter("end2");
        String guige2 = request.getParameter("end");
        String jiage1 = request.getParameter("jiage");
        String num=request.getParameter("num");
        String imgurl=request.getParameter("imgurl");
        String ShangPinMingChen=request.getParameter("mc");
        int num1=-1;
        double jiage=0.0;
        //数量和价格不为空时进行强转
        if(num!=null && !num.equals("")) {
             num1= Integer.parseInt(request.getParameter("num"));
        }
        if(jiage1!=null && !jiage1.equals("")) {
            jiage= Double.parseDouble(request.getParameter("jiage"));
        }
        String ms = request.getParameter("ms");

        ShangPinXinXi shangPinXinXi = new ShangPinXinXi();
        shangPinXinXi.setShangPinGuiGe1(guige1);
        shangPinXinXi.setShangPinGuiGe2(guige2);
        shangPinXinXi.setShangPinJiaGe(jiage);
        shangPinXinXi.setShangPinMiaoSu(ms);
        shangPinXinXi.setShangPinZuBianHao(ShangPinZuBianHao);
        shangPinXinXi.setShangPinMingChen(ShangPinMingChen);
        GouWuCheDao_Zheng gouWuCheDao_zheng = new GouWuCheDao_Zheng();


        if(yonghuming!=null ){//用户名不为null

            if( guige1!=null &&guige2!=null && !guige1.equals("") && !guige2.equals("")){//规格不为空才可以加入购物车
                //根据组编号 规格查询得到商品的编号
                shangPinXinXi = gouWuCheDao_zheng.select_shangpinbianhao(shangPinXinXi);
                String shangpinbianhao = shangPinXinXi.getShangPinBianHao();
                if(shangpinbianhao!=null){//查询商品编号成功，进行插入操作
                    GouWuChe gouWuChe = new GouWuChe();
                    gouWuChe.setShangPinBianHao(shangpinbianhao);
                    gouWuChe.setYongHuMing("zx");
                    int shuliang = gouWuCheDao_zheng.selectGouWuCheShuLiang(gouWuChe);
                    if(shuliang>0){//购物车中是否有该商品的编号，有的话在修改购物车中该商品的数量，没有的话则插入该商品
                        int total=num1+shuliang;//修改后商品的总数量
                        gouWuChe.setShangPinShuLiang(total);
                        gouWuCheDao_zheng.updateGouWuChe(gouWuChe);
                    }else{//购物车中没有此商品，执行插入操作
                        boolean action = gouWuCheDao_zheng.insertGouWuChe(shangPinXinXi, yonghuming, num1,imgurl);
                        if(action){//true代表数据插入购物车成功
                            JSONObject  jsonObject = gouWuCheDao_zheng.selectAllGouWuChe(yonghuming);
                            return  jsonObject;
                        }
                    }

                }
            }

            //用户名不为空点击购物车就进行查询购物车
            JSONObject jsonObject = gouWuCheDao_zheng.selectAllGouWuChe(yonghuming);
            return jsonObject;

        }else{
            System.out.println("请先登录");

        }
        return null;
    }
}
