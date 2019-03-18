package cn.com.starshopping.service.fzy;


import cn.com.starshopping.dao.hzy.CaoZuoJiLuDao;
import cn.com.starshopping.dao.hzy.GeRenZiLiaoDao;
import cn.com.starshopping.pojo.ShouHuoDiZhi;
import cn.com.starshopping.pojo.YongHu;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;

public class GeRenZiLiaoService implements CaoZuoJiLuInterface {
    private final CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();

    public JSONObject editorAll(HttpServletRequest request, HttpServletResponse response){
        String option = "";
        String yonghuming = "";
        JSONObject jsonObject = null;
        GeRenZiLiaoDao geRenZiLiaoDao = new GeRenZiLiaoDao();
        try {
            BufferedReader br = request.getReader();
            String jsonstr = br.readLine();
            jsonObject = new JSONObject(jsonstr);
            option = jsonObject.getString("option");
            yonghuming = jsonObject.getString("yonghuming");
        } catch (Exception e) {
            option = request.getParameter("option");
        }
        zhiXingChaRu(option,yonghuming);
        ShouHuoDiZhi shouHuoDiZhiPojo = new ShouHuoDiZhi();
        YongHu yongHuPojo = new YongHu();
        switch (option){
            case "1"://根据yonghuming查询出yonghu表和shouhuodizhi表中所有字段的值
                 jsonObject = returnJSON(yonghuming,jsonObject,geRenZiLiaoDao);
                break;
            case "2"://将头像地址写入数据库对应yonghuming的yonghu表中
                geRenZiLiaoDao.chaRuTouXiangDiZhi(yonghuming,jsonObject.getString("touxiangdizhi"));
                break;
            case "3"://yonghu提交按钮
                yongHuPojo.setYongHuMing(yonghuming);
                yongHuPojo.setNiCheng(jsonObject.getString("nicheng"));
                yongHuPojo.setXingMing(jsonObject.getString("xingming"));
                yongHuPojo.setXingBie(jsonObject.getString("xingbie"));
                yongHuPojo.setChuShengRiQi(jsonObject.getString("chushengriqi"));
                yongHuPojo.setXingZuo(jsonObject.getString("xingzuo"));
                yongHuPojo.setJuZhuDi(jsonObject.getString("jvzhudi"));
                yongHuPojo.setJiaXiang(jsonObject.getString("jiaxiang"));
                geRenZiLiaoDao.yongHuTiJiao(yongHuPojo);
                break;
            case "4"://新增地址信息
                shouHuoDiZhiPojo.setYongHuMing(yonghuming);
                shouHuoDiZhiPojo.setShouHuoRen(jsonObject.getString("shouhuoren"));
                shouHuoDiZhiPojo.setSuoZaiDiQu(jsonObject.getString("suozaidiqu"));
                shouHuoDiZhiPojo.setXiangXiDiZhi(jsonObject.getString("xiangxidizhi"));
                shouHuoDiZhiPojo.setYouBian(jsonObject.getString("youbian"));
                shouHuoDiZhiPojo.setLianXiDianHua(jsonObject.getString("lianxidianhua"));
                shouHuoDiZhiPojo.setZhuangTai(jsonObject.getInt("zhuangtai"));
                 geRenZiLiaoDao.insertIntoShouHuoDiZhi(shouHuoDiZhiPojo);
                jsonObject = returnJSON(yonghuming,jsonObject,geRenZiLiaoDao);
                break;
            case "5"://修改地址信息
                shouHuoDiZhiPojo.setYongHuMing(yonghuming);
                shouHuoDiZhiPojo.setId(jsonObject.getInt("id"));
                shouHuoDiZhiPojo.setShouHuoRen(jsonObject.getString("shouhuoren"));
                shouHuoDiZhiPojo.setSuoZaiDiQu(jsonObject.getString("suozaidiqu"));
                shouHuoDiZhiPojo.setXiangXiDiZhi(jsonObject.getString("xiangxidizhi"));
                shouHuoDiZhiPojo.setYouBian(jsonObject.getString("youbian"));
                shouHuoDiZhiPojo.setLianXiDianHua(jsonObject.getString("lianxidianhua"));
                geRenZiLiaoDao.updateShouHuoDiZhi(shouHuoDiZhiPojo);
                break;
            case "6"://删除地址信息
                shouHuoDiZhiPojo.setYongHuMing(yonghuming);
                shouHuoDiZhiPojo.setId(jsonObject.getInt("id"));
                geRenZiLiaoDao.deleteShouHuoDiZhi(shouHuoDiZhiPojo);
                break;
            case "7"://设为默认地址
                shouHuoDiZhiPojo.setYongHuMing(yonghuming);
                shouHuoDiZhiPojo.setId(jsonObject.getInt("id"));
                geRenZiLiaoDao.updateMoRen(shouHuoDiZhiPojo);
                break;

        }
        return jsonObject;
    }

    public JSONObject returnJSON(String yonghuming,JSONObject jsonObject,GeRenZiLiaoDao geRenZiLiaoDao){
        jsonObject.put("yonghu",geRenZiLiaoDao.selectYongHu(yonghuming));
        jsonObject.put("shouhuodizhi",geRenZiLiaoDao.selectShouHuoDiZhi(yonghuming));
        jsonObject.put("kehudenglu",geRenZiLiaoDao.selectKeHuDengLu(yonghuming));
        return jsonObject;
    }

    /**
     *  对参数进行判断,然后调用并传参Dao层的chaRuCaoZuoJiLu（）方法
     * @param option  switch 对应的操作请求参数
     * @param yonghuming 执行此操作用户对应的用户名
     */
    public void zhiXingChaRu(String option,String yonghuming){
        String caozuojilu = "";
        if(option.equals("2")){
            caozuojilu = "上传头像";
        }else if(option.equals("3")){
            caozuojilu = "编辑了个人资料";
        }else if(option.equals("4")){
            caozuojilu = "新增了收货地址";
        }else if(option.equals("5")){
            caozuojilu = "修改了收货地址信息";
        }else if(option.equals("6")){
            caozuojilu = "删除了一条收货地址信息";
        }else if(option.equals("7")){
            caozuojilu = "设置了默认收货地址";
        }
        caoZuoJiLuDao.chaRuCaoZuoJiLu(yonghuming,caozuojilu);
    }

}
