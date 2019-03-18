package cn.com.starshopping.servlet.zx;

import cn.com.starshopping.service.zx.Selectdizhi_zhengService;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Selectdizhi_zhengServlet",urlPatterns = "/selectdizhi_zheng")
public class Selectdizhi_zhengServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Selectdizhi_zhengService selectdizhi_zhengService = new Selectdizhi_zhengService();
        JSONObject jsonObject = selectdizhi_zhengService.ShouHuoDiZhi(request);
        response.setContentType("Application/json;charset=utf-8");
        response.getWriter().print(jsonObject);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
