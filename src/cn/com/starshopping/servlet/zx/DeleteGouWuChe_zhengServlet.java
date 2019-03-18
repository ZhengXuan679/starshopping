package cn.com.starshopping.servlet.zx;


import cn.com.starshopping.service.zx.DeleteGouWuChe_zhengService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteGouWuChe_zhengServlet",urlPatterns = "/deletegouwuche_zheng")
public class DeleteGouWuChe_zhengServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DeleteGouWuChe_zhengService deleteGouWuChe_zhengService = new DeleteGouWuChe_zhengService();
        String url=deleteGouWuChe_zhengService.DeleteGouWuChe_zhengService(request);
        response.setContentType("Application/json;charset=utf-8");
        System.out.println(url);
        response.getWriter().print(url);


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
