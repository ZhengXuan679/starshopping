package cn.com.starshopping.servlet;

import cn.com.starshopping.service.XiaoLiangTongJiService;
import cn.com.starshopping.util.SessionKey;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "XiaoLiangTongJiServlet",urlPatterns = "/xiaoLiangTongJiServlet")
public class XiaoLiangTongJiServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String yongHuMing ="lzy";
        String yongHuMing = (String) request.getSession().getAttribute(SessionKey.KEFU);
        if (yongHuMing == null) {
            response.sendRedirect("/denglu.html");
        } else {
            XiaoLiangTongJiService xiaoLiangTongJiService = new XiaoLiangTongJiService();
            JSONObject jso = xiaoLiangTongJiService.xiaoLiang(request, response);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(jso);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
