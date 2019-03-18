package cn.com.starshopping.servlet.kehuguanli;

import cn.com.starshopping.service.kehuguanli.KeHuService;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: huxiaoyi
 * Date: 2019/01/05
 * Description: kehuguanli页面搜索用户servlet层
 * Version: V1.0
 */
@WebServlet(name = "KeHuServlet",urlPatterns = "/servlet/kehuServlet")
public class KeHuServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        KeHuService kehuService = new KeHuService();
        JSONObject json = kehuService.kehuguanli(request);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
