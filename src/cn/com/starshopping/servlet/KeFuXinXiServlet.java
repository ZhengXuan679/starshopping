package cn.com.starshopping.servlet;


import cn.com.starshopping.service.kehuguanli.KeFuXinXiService;
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
 * Description:
 * Version: V1.0
 */
@WebServlet(name = "KeFuXinXiServlet",urlPatterns = "/servlet/kefuxinxiServlet")
public class KeFuXinXiServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        KeFuXinXiService keFuService = new KeFuXinXiService();
        JSONObject json = keFuService.kefuxinxiService(request);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(json);
    }
}
