package cn.com.starshopping.servlet.zx;

import cn.com.starshopping.service.zx.JiaRuGouWuChe_zhengService;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "JiaRuGouWuChe_zhengServlet",urlPatterns = "/jiarugouwuche_zheng")
public class JiaRuGouWuChe_zhengServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JiaRuGouWuChe_zhengService jiaRuGouWuChe_zhengService = new JiaRuGouWuChe_zhengService();
        JSONObject jsonObject = jiaRuGouWuChe_zhengService.jiarugouwuche_zheng(request);
        response.setContentType("Application/json;charset=utf-8");
        response.getWriter().print(jsonObject);


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
