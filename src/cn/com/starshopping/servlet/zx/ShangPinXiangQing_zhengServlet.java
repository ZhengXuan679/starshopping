package cn.com.starshopping.servlet.zx;

import cn.com.starshopping.service.zx.ShangPinXiangQing_zhengService;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ShangPinXiangQing_zhengServlet",urlPatterns = "/shangpinxiangqing_zheng")
public class ShangPinXiangQing_zhengServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ShangPinXiangQing_zhengService shangPinXiangQing_zhengService = new ShangPinXiangQing_zhengService();
        JSONObject jsonObject = shangPinXiangQing_zhengService.ShangPinXiangQing(request);
        response.setContentType("Application/json;charset=utf-8");
        response.getWriter().print(jsonObject);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
