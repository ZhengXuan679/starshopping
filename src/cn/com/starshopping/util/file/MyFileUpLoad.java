package cn.com.starshopping.util.file;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: thinknovo
 * Date: 2018/12/23
 * Description:  上传文件工具类，支持同时多文件上传，在html页面中每一个file文件必须有唯一的name属性，需要commons-fileupload-1.3.3.jar支持
 * Version: V1.0
 */
public class MyFileUpLoad {
    public boolean fileUpLoad(HttpServletRequest request) throws IOException, FileUploadException {
        // 注意，在实际开发中，尽量把服务器的下载目录通过filter屏蔽掉，因为在没有屏蔽之前，用户可以通过手动请求路径获取文件信息
        // 状态机，false代表上传失败，true代表成功
        boolean action = false;
        // 设置请求对象语言编码为UTF-8
        request.setCharacterEncoding("UTF-8");
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            System.out.println("true");
            // 创建工厂（这里用的是工厂模式）
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 获取ServletContext
            ServletContext servletContext = request.getServletContext();
            // 获取从ServletContext中得到上传来的数据，fileupload固定的参数：javax.servlet.context.tempdir
            File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
            // fileupload封装上传来的文件的各种属性（上传来的文件的大小、文件名等）
            factory.setRepository(repository);
            // fileupload生成对应的数据参数对象
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 把request转成fileupload中FileItem的实例
            List<FileItem> items = upload.parseRequest(request);
            for (FileItem item : items) {
                // 获取服务器物理路径的根路径，必须至少使用斜杠参数，否则会报空指针
                // 需要注意的是，在idea中测试时，路径会在当前工程的war_exploded路径下，此路径idea加载工程时，可能会被重置，如果在测试时遇到重置的情况，手动添加一个物理路径
                System.out.println(servletContext.getRealPath(File.separator));
                // 也可以这样组装，会是这样的效果  war_exploded\out   相当于增加一个out目录
                System.out.println(servletContext.getRealPath("out" + File.separator));
                // 也可以这样组装，会是这样的效果  war_exploded\out    相当于增加一个out目录，但是没有上面的做法合适，因为系统环境不同斜杠不同
//                System.out.println(servletContext.getRealPath("/out"));
                File file = new File(servletContext.getRealPath("FileUpload" + File.separator)); // 通过将给定路径名字符串转换为抽象路径名来创建一个新 File 实例
                file.mkdir();                                                                       // 创建此抽象路径名指定的目录。
                // 基于服务器部署的物理路径创建文件对象
                file = new File(servletContext.getRealPath("FileUpload" + File.separator) + File.separator + item.getName());
                if (!file.exists()) {   // 判断文件是否存在
                    try {
                        // 这里的警告可以使用注解或者jdk新版的注释解决
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileOutputStream fos = new FileOutputStream(file);
                // 创建输入流 从输入流获取字节数组
                InputStream fis = item.getInputStream();
                // 创建接收字节数组
                byte b[] = new byte[1024];
                // 默认读取-1
                int read = -1;
                // 循环读取知道读取完毕
                while ((read = fis.read(b)) != -1) {
                    fos.write(b, 0, read);
                }
                fis.close();
                fos.flush();
                fos.close();
            }
            action = true;     // 上传成功
        }
        return action;
    }
}
