package cn.com.starshopping.service.zx;

import cn.com.starshopping.dao.zx.ShangPinShouYeDao;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

public class ShangPingShouYe_zhengService {
    public JSONObject shangPinShouYe(HttpServletRequest request){
        ShangPinShouYeDao shangPinShouYeDao = new ShangPinShouYeDao();
        JSONObject jsonObject = shangPinShouYeDao.selectAllShangping();
        if(jsonObject!=null){
            return jsonObject;
        }
        return null;
    }
}
