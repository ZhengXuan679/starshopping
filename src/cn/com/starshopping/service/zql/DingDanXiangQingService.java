package cn.com.starshopping.service.zql;

import cn.com.starshopping.dao.zql.DingDanXiangQingDao;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: ZouQinLun
 * Date: 2019/01/09
 * Description:
 * Version: V1.0
 */
public class DingDanXiangQingService {
    public JSONObject dingDanXiangQingService(HttpServletRequest request){
        JSONObject jsonObject = null;
        String dingDanBianHao = request.getParameter("dingDanBianHao");//获取订单编号
        DingDanXiangQingDao dingDanXiangQingDao = new DingDanXiangQingDao();//创建DingDanXiangQingDao对象
        jsonObject = dingDanXiangQingDao.dingDanXiangQingDao(dingDanBianHao);//获取该订单编号的所有信息的JSONObject
        if (jsonObject != null &&!jsonObject.equals("")){            //如果返回的JSONObject不为null且不为空，则在JSONObject中放入returnCode:200
            jsonObject.put("returnCode",200);
        }
        else{
            jsonObject.put("returnCode",500);                   //否则，则在JSONObject中放入returnCode:200
        }
        return jsonObject;      //返回JSONObject
    }
}
