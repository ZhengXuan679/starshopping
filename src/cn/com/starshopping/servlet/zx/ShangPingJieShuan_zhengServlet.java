package cn.com.starshopping.servlet.zx;

import cn.com.starshopping.service.zx.ShangPingJieShuan_zhengService;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ShangPingJieShuan_zhengServlet",urlPatterns = "/shangPingJieShuan_zheng")
public class ShangPingJieShuan_zhengServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ShangPingJieShuan_zhengService jieShuan_zhengService = new ShangPingJieShuan_zhengService();
        JSONObject jsonObject = jieShuan_zhengService.ShangPingJieShuan_zhengService(request);
        response.setContentType("Application/json;charset=utf-8");
        response.getWriter().print(jsonObject);


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
