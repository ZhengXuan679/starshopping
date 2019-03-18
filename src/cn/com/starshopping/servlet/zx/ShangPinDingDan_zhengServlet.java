package cn.com.starshopping.servlet.zx;


import cn.com.starshopping.service.zx.ShangPinDingDan_zhengService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ShangPinDingDan_zhengServlet",urlPatterns = "/shangpindingdan")
public class ShangPinDingDan_zhengServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ShangPinDingDan_zhengService shangPinDingDan_zhengService = new ShangPinDingDan_zhengService();
        shangPinDingDan_zhengService.ShangPinDingDan_zhengService(request);
        response.sendRedirect("/html/index.html");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ShangPinDingDan_zhengService shangPinDingDan_zhengService = new ShangPinDingDan_zhengService();
        shangPinDingDan_zhengService.ShangPinDingDan_zhengService(request);
        response.sendRedirect("/html/index.html");
    }
}
