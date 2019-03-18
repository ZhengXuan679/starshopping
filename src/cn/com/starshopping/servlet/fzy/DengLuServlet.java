package cn.com.starshopping.servlet.fzy;


import cn.com.starshopping.service.fzy.DengLuService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cn.com.starshopping.util.fzy.error.AllError.myInstance;

@WebServlet(name = "DengLuServlet",urlPatterns = "/DengLuServlet")
public class DengLuServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DengLuService dengLuService = new DengLuService();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(dengLuService.editorAll(request,response));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        myInstance().doGetError(request,response);
    }
}
