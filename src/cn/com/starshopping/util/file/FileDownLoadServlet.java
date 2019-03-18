package cn.com.starshopping.util.file;


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
@WebServlet(name = "FileDownLoadServlet")
public class FileDownLoadServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MyFileDownLoad myFileDownLoad = new MyFileDownLoad();
        myFileDownLoad.fileDownLoad(request, response);
    }
}
