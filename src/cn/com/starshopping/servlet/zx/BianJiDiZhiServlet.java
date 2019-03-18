package cn.com.starshopping.servlet.zx;

import cn.com.starshopping.service.zx.BianJiDiZhiService_zheng;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "BianJiDiZhiServlet",urlPatterns = "/bianjidizhi")
public class BianJiDiZhiServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BianJiDiZhiService_zheng bianJiDiZhiService = new BianJiDiZhiService_zheng();
        JSONObject jsonObject = bianJiDiZhiService.BianJiDiZhiService(request);
        response.setContentType("Application/json;charset=utf-8");
        response.getWriter().print(jsonObject);
    }
}
