package cn.com.starshopping.servlet.fzy;


import cn.com.starshopping.service.fzy.TouXiangShangChuanService;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "TouXiangShangChuanServlet",urlPatterns = "/TouXiangShangChuanServlet")
public class TouXiangShangChuanServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonObject = new TouXiangShangChuanService().editorAll(request,response);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(jsonObject);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
