package cn.com.starshopping.service.shangpinguanli;

import cn.com.starshopping.dao.CaoZuoJiLuDao;
import cn.com.starshopping.dao.shangpinguanli.UpdateDao;
import cn.com.starshopping.pojo.ShangPinXinXi;
import cn.com.starshopping.util.SessionKey;
import org.json.JSONObject;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Wong
 * Date: 2019/01/09
 * Description:
 * Version: V1.0
 */
public class UpdateService {
    public JSONObject update(HttpServletRequest request){
        // 获取用户名
        String xingMing = (String) request.getSession().getAttribute(SessionKey.KEFU);
        // 若为空则直接返回错误的状态码
        if(xingMing == null){
            return new JSONObject("{\"returncode\":-100}");
        }
        // 没进入判断则继续往下走
        JSONObject jsonObject = new JSONObject();
        // 获取前端传入的状态码
        String runCode = request.getParameter("runCode");
        // 如果状态码存在则进入，以此排除第一次进入判断成功却不带值的情况
        if (runCode != null) {
            UpdateDao updateDao = new UpdateDao();
            // 获取为0代表进入查询方法展现给操作员获取的组编号对应的全部信息
            if (runCode.equals("0")) {
                String shangPinZuBianHao = request.getParameter("ShangPinZuBianHao");
                jsonObject.put("GuiGe", updateDao.selectGG(shangPinZuBianHao));
                jsonObject.put("All", updateDao.selectAll(shangPinZuBianHao));
            } else {
                // 先删去除的规格，再加新增的规格，再根据修改其他公共内容
                String[] oldGuiGe1 = request.getParameter("OldGuiGe1").split(",");
                String[] oldGuiGe2 = request.getParameter("OldGuiGe2").split(",");
                String[] newGuiGe1 = request.getParameter("NewGuiGe1").split(",");
                String[] newGuiGe2 = request.getParameter("NewGuiGe2").split(",");
                ArrayList<String[]> arrayList = new ArrayList<>();
                arrayList.add(oldGuiGe1);
                arrayList.add(oldGuiGe2);
                arrayList.add(newGuiGe1);
                arrayList.add(newGuiGe2);
                // 对取回的规格集合进行处理，获取一个处理后的集合
                ArrayList<ArrayList<String>> arrayLists = updateDao.createArray(arrayList);
                // 先根据旧规格1以外的进行删除，再根据旧规格2以外的进行删除
                updateDao.delete(request, "ShangPinGuiGe1", arrayLists.get(0));
                updateDao.delete(request, "ShangPinGuiGe2", arrayLists.get(2));
                // 删除完后根据处理后的集合与前端数据组成POJO集合
                ArrayList<ShangPinXinXi> arrayListPOJO = updateDao.createPOJO(arrayLists, request);
                // 根据POJO集合进行批处理添加
                if (arrayListPOJO.size() == 0) {
                    updateDao.insert(arrayListPOJO);
                }
                // 随便提出一个POJO传进去取公共字段值
                updateDao.update(request);
            }
            CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();
            // 用户名对应操作传入操作记录表
            caoZuoJiLuDao.chaRuCaoZuoJiLu(xingMing, "编辑");
        }
        jsonObject.put("username",xingMing);
        return jsonObject;
    }
}
