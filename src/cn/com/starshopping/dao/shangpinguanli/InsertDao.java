package cn.com.starshopping.dao.shangpinguanli;

import cn.com.starshopping.util.Mydb;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Wong
 * Date: 2019/01/05
 * Description:
 * Version: V1.0
 */
public class InsertDao {
    /**
     * 根据类型返回对应规格
     *
     * @param leiXing
     * @return
     */
    public JSONObject guiGe(String leiXing) {
        JSONObject jsonObject = new JSONObject();
        String sql = "select GuiGe1,GuiGe2 from leixingguigexinxibiao where LeiXing = ?";
        try {
            PreparedStatement preparedStatement = Mydb.myConnection.prepareStatement(sql);
            preparedStatement.setString(1, leiXing);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                jsonObject.put("LeiXing", leiXing);
                jsonObject.put("GuiGe1", resultSet.getString("GuiGe1"));
                jsonObject.put("GuiGe2", resultSet.getString("GuiGe2"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 生成组编号
     *
     * @param zuBianHao
     * @return
     */
    public boolean zuBianHao(String zuBianHao) {
        boolean action = false;
        String sql = "select id from shangpinxinxi where ShangPinZuBianHao = ?";
        try {
            PreparedStatement preparedStatement = Mydb.myConnection.prepareStatement(sql);
            preparedStatement.setString(1, zuBianHao);
            ResultSet resultSet = preparedStatement.executeQuery();
            action = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return action;
    }

    /**
     * 提取request信息并批处理添加数据库
     *
     * @param request
     */
    public void insert(HttpServletRequest request, String daLeiXin) {
        String sql = "insert into shangpinxinxi(shangpinbianhao, shangpinzubianhao, shangpinmingchen, shangpinleixin, shangpinguige1, shangpinguige2, pinpai, shangpinjiage, shangpinchengben, ShangPinMiaoSu,CaoZuoShiJian,leixin) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        String[] gg1 = request.getParameter("ShangPinGuiGe1").split(",");
        String[] gg2 = request.getParameter("ShangPinGuiGe2").split(",");
        try {
            PreparedStatement preparedStatement = Mydb.myConnection.prepareStatement(sql);
            Mydb.myConnection.setAutoCommit(false);
            for (int i = 0; i < gg1.length; i++) {
                for (int j = 0; j < gg2.length; j++) {
                    preparedStatement.setString(1, request.getParameter("ShangPinZuBianHao") + "-A" + i + "B" + j);
                    preparedStatement.setString(2, request.getParameter("ShangPinZuBianHao"));
                    preparedStatement.setString(3, request.getParameter("ShangPinMingChen"));
                    preparedStatement.setString(4, request.getParameter("ShangPinLeiXin"));
                    preparedStatement.setString(5, gg1[i]);
                    preparedStatement.setString(6, gg2[j]);
                    preparedStatement.setString(7, request.getParameter("PinPai"));
                    preparedStatement.setString(8, request.getParameter("ShangPinJiaGe"));
                    preparedStatement.setString(9, request.getParameter("ShangPinChengBen"));
                    preparedStatement.setString(10, request.getParameter("ShangPinMiaoSu"));
                    preparedStatement.setString(11, "" + new Timestamp(System.currentTimeMillis()));
                    preparedStatement.setString(12, daLeiXin);
                    preparedStatement.addBatch();
                }
            }
            preparedStatement.executeBatch();
            Mydb.myConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据组编号创建文件夹
     *
     * @param request
     */
    public void creatFile(HttpServletRequest request) {
        ServletContext servletContext = request.getServletContext();
        File file = null;
        String[] urls = new String[4];
        urls[0] = servletContext.getRealPath(File.separator + request.getParameter("ShangPinZuBianHao") + File.separator);
        urls[1] = servletContext.getRealPath(File.separator + request.getParameter("ShangPinZuBianHao") + File.separator + "缩略图" + File.separator);
        urls[2] = servletContext.getRealPath(File.separator + request.getParameter("ShangPinZuBianHao") + File.separator + "浏览图" + File.separator);
        urls[3] = servletContext.getRealPath(File.separator + request.getParameter("ShangPinZuBianHao") + File.separator + "详情图" + File.separator);
        for (String i : urls) {
            file = new File(i);
            if (!file.exists()) {
                try {
                    file.mkdir();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ShangPinZuBianHao", request.getParameter("ShangPinZuBianHao"));
    }

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
            int count = 0;
            String shangPinZuBianHao = request.getParameter("ShangPinZuBianHao");
            String url = request.getParameter("url");
            if (url.equals("0")) {
                url = "缩略图";
            } else if (url.equals("1")) {
                url = "浏览图";
            } else {
                url = "详情图";
            }
            for (FileItem item : items) {
                // 获取服务器物理路径的根路径，必须至少使用斜杠参数，否则会报空指针
                // 需要注意的是，在idea中测试时，路径会在当前工程的war_exploded路径下，此路径idea加载工程时，可能会被重置，如果在测试时遇到重置的情况，手动添加一个物理路径
                int i = item.getName().lastIndexOf('.');
                String type = item.getName().substring(i);
                String new_url = servletContext.getRealPath(File.separator) + File.separator + shangPinZuBianHao + File.separator + url + File.separator + count + type;
                // 基于服务器部署的物理路径创建文件对象
                File file = new File(new_url);
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
                count++;
            }
            action = true;     // 上传成功
        }
        return action;
    }

    public String leixin(HttpServletRequest request) {
        String ShangPinLeiXin = request.getParameter("ShangPinLeiXin");
        String sql = "select DaLeiXin from leixingguigexinxibiao where LeiXing = ?";
        try {
            PreparedStatement preparedStatement = Mydb.myConnection.prepareStatement(sql);
            preparedStatement.setString(1, ShangPinLeiXin);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            ShangPinLeiXin = resultSet.getString("daleixin");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ShangPinLeiXin;
    }
}