package cn.com.starshopping.util.file;

import org.apache.commons.fileupload.FileUploadException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: thinknovo
 * Date: 2018/12/23
 * Description:      上传下载演示，需要commons-fileupload-1.3.3.jar
 * Version: V1.0
 */
@WebServlet(name = "FileUpLoadServlet")
public class FileUpLoadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MyFileUpLoad myFileUpLoad = new MyFileUpLoad();
        response.setContentType("text/html;charset=utf-8");
        try {
            boolean action = myFileUpLoad.fileUpLoad(request);
            if(action) {
                response.getWriter().print("{returncode:0,messsage:'上传成功!'}");  // {returncode:0,messsage:"上传成功!"}
            }
            else {
                response.getWriter().print("{returncode:-1,messsage:'上传失败!'}");
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
            response.getWriter().print("{returncode:-1,messsage:'服务器错误!'}");
        }
    }
}
