package cn.com.starshopping.servlet.zx;


import cn.com.starshopping.service.zx.LiJiGouMaiService_zheng;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LiJiGouMaiServlet",urlPatterns = "/lijigoumai")
public class LiJiGouMaiServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LiJiGouMaiService_zheng liJiGouMaiService = new LiJiGouMaiService_zheng();
        String shangpinbianhao = liJiGouMaiService.ShangPingJieShuan_zhengService(request);
        response.getWriter().print(shangpinbianhao);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
