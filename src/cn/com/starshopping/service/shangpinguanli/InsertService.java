package cn.com.starshopping.service.shangpinguanli;

import cn.com.starshopping.dao.CaoZuoJiLuDao;
import cn.com.starshopping.dao.shangpinguanli.InsertDao;
import cn.com.starshopping.util.SessionKey;
import org.apache.commons.fileupload.FileUploadException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Wong
 * Date: 2019/01/05
 * Description:
 * Version: V1.0
 */
public class InsertService {
    /**
     * 通过传入的执行码进入各自的执行命令
     * @param request
     * @return
     */
    public JSONObject insert(HttpServletRequest request){
        String xingMing = (String) request.getSession().getAttribute(SessionKey.KEFU);
        // 判断是否有用户名在session
        if(xingMing == null){
            return new JSONObject("{\"returncode\":-100}");
        }
        // 如果存在则获取执行码，有值则执行，无值则说明只是提取用户名并不操作
        String runCode = request.getParameter("runCode");
        JSONObject jsonObject = new JSONObject();
        InsertDao insertDao = new InsertDao();
        // 如果状态码为0，则进入类型规格信息表获取规格名
        if (runCode.equals("0")){
            String leiXing = request.getParameter("LeiXing");
            String zuBianHao;
            for (;;){
                zuBianHao = ("" + ((int)(Math.random() * 100000000) + 100000000)).substring(1);
                if (!insertDao.zuBianHao(zuBianHao)){
                    break;
                }
            }
            jsonObject = insertDao.guiGe(leiXing);
            jsonObject.put("zuBianHao",zuBianHao);
        }
        // 如果执行码为1则进入添加数据库并创建对应文件夹
        else if (runCode.equals("1")){
            String daLeiXin = insertDao.leixin(request);
            insertDao.insert(request,daLeiXin);
            insertDao.creatFile(request);
        }
        // 图片文件的上传
        else if (runCode.equals("2")){
            try {
                insertDao.fileUpLoad(request);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
        }
        jsonObject.put("username",xingMing);
        CaoZuoJiLuDao caoZuoJiLuDao = new CaoZuoJiLuDao();
        caoZuoJiLuDao.chaRuCaoZuoJiLu((String)request.getSession().getAttribute(SessionKey.KEFU),"新增");
        return jsonObject;
    }
}
