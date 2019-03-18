package cn.com.starshopping.util.fzy.fileupload;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: thinknovo
 * Date: 2018/12/23
 * Description:  下载文件工具类
 * Version: V1.0
 */
public class MyFileDownLoad {
    public void fileDownLoad(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 注意，在实际开发中，尽量把服务器的下载目录通过filter屏蔽掉，因为在没有屏蔽之前，用户可以通过手动请求路径获取文件信息
        String fileName = request.getParameter("fileName");
        if(fileName != null && !fileName.trim().equals("")) {
            // 查找服务器物理路径所在位置
            String username = request.getSession().getAttribute("username").toString();
            String muLiLuJing = request.getServletContext().getRealPath(File.separator);
            File file = new File(muLiLuJing + File.separator +"fileupload"+ File.separator +username+ File.separator  + fileName);
            if(file.exists()){
                FileInputStream fis = new FileInputStream(file);
                String filename = URLEncoder.encode(file.getName(),"utf-8"); //解决中文文件名下载后乱码的问题
                byte[] b = new byte[fis.available()];
                fis.read(b);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Disposition","attachment;filename=" + filename + "");
                //获取响应报文输出流对象
                ServletOutputStream out =response.getOutputStream();
                //输出
                out.write(b);
                out.flush();
                out.close();
            }
        }
    }
}
