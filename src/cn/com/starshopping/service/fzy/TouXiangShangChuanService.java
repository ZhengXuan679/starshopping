package cn.com.starshopping.service.fzy;


import cn.com.starshopping.util.fzy.fileupload.MyFileUpLoad;
import org.apache.commons.fileupload.FileUploadException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TouXiangShangChuanService {

    public JSONObject editorAll(HttpServletRequest request, HttpServletResponse response){
        MyFileUpLoad myFileUpLoad = new MyFileUpLoad();
        String img_url = "";
        try {
            img_url = myFileUpLoad.fileUpLoad(request);
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code","200");
        jsonObject.put("img_url",img_url);
        return jsonObject;
    }
}
