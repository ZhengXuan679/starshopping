package cn.com.starshopping.servlet.zql;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: thinknovo
 * Date: 2019/01/04
 * Description:
 * Version: V1.0
 */
@WebServlet(name = "ClientServlet", urlPatterns = "/clientServlet")
public class ClientServlet extends HttpServlet {
    String json = "";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        //  假设1为server.html获取消息，2为client.html发送消息
        if(action.equals("1")) {
            response.setContentType("application/json;charset=utf-8");   // 服务端页面获取消息，每一次获取完毕，则清空变量
                response.getWriter().print(json);
            json = "";
        }
        else if(action.equals("2")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msgKey", request.getParameter("msgKey")); // 客户端发送过来消息，需要储存
            jsonObject.put("yongHuMing", request.getParameter("yongHuMing")); // 存入用户名
            String shiJian = (new Timestamp(System.currentTimeMillis())).toString();   // 得到当前时间
            jsonObject.put("shiJian", shiJian.substring(0,shiJian.lastIndexOf("."))); // 存入时间
            json = jsonObject.toString();   // jsonObject字符串存入属性
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
