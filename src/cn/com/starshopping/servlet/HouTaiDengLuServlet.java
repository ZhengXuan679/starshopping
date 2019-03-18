package cn.com.starshopping.servlet;

import cn.com.starshopping.service.HouTaiDengLuService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: yanmin
 * Date: 2019/01/14
 * Description:
 * Version: V1.0
 */
@WebServlet(name = "HouTaiDengLuServlet", urlPatterns = "/houTaiDengLuServlet")
public class HouTaiDengLuServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HouTaiDengLuService houTaiDengLuService = new HouTaiDengLuService(); // 后台登录service方法
        String json = houTaiDengLuService.kongZhiTai(request, response); // 得到json字符串
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
