package cn.com.starshopping.servlet.zx;


import cn.com.starshopping.service.zx.Zengjiadizhi_zhengService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ZengJiadizhi_zhengServlet",urlPatterns = "/zengjiadizhi_zheng")
public class ZengJiadizhi_zhengServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Zengjiadizhi_zhengService zengjiadizhi_zhengService = new Zengjiadizhi_zhengService();
        int i = zengjiadizhi_zhengService.Zengjiadizhi_zhengService(request);
        System.out.println(i);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
