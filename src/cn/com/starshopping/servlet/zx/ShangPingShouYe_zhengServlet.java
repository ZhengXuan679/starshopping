package cn.com.starshopping.servlet.zx;

import cn.com.starshopping.service.zx.ShangPingShouYe_zhengService;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ShangPingShouYe_zhengServlet",urlPatterns = "/shangpingshouye_zheng")
public class ShangPingShouYe_zhengServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ShangPingShouYe_zhengService shangPingShouYe_zhengService = new ShangPingShouYe_zhengService();
        JSONObject jsonObject = shangPingShouYe_zhengService.shangPinShouYe(request);
        response.setContentType("Application/json;charset=utf-8");
        response.getWriter().print(jsonObject);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
