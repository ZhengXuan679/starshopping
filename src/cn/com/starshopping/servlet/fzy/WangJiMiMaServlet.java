package cn.com.starshopping.servlet.fzy;


import cn.com.starshopping.service.fzy.WangJiMiMaService;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "WangJiMiMaServlet",urlPatterns = "/WangJiMiMaServlet")
public class WangJiMiMaServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WangJiMiMaService wangJiMiMaService = new WangJiMiMaService();
        JSONObject jsonObject = wangJiMiMaService.editorAll(request,response);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(jsonObject);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String uri = request.getRequestURI();
        doPost(request,response);
    }
}
