package cn.com.starshopping.servlet;

import cn.com.starshopping.service.CaoZuoRiZhiService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: yanmin
 * Date: 2019/01/15
 * Description: 操作日志
 * Version: V1.0
 */
@WebServlet(name = "CaoZuoRiZhiServlet", urlPatterns = "/caoZuoRiZhiServlet")
public class CaoZuoRiZhiServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CaoZuoRiZhiService caoZuoRiZhiService = new CaoZuoRiZhiService();
        String json = caoZuoRiZhiService.kongZhiTai(request, response);
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
