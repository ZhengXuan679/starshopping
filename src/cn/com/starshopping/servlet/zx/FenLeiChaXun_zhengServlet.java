package cn.com.starshopping.servlet.zx;

import cn.com.starshopping.service.zx.FenLeiChaXunService_zheng;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "FenLeiChaXun_zhengServlet",urlPatterns = "/fenlei_zheng")
public class FenLeiChaXun_zhengServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FenLeiChaXunService_zheng fenLeiChaXunService_zheng = new FenLeiChaXunService_zheng();
        JSONObject jsonObject = fenLeiChaXunService_zheng.FenLeiChaXunService_zheng(request);
        response.setContentType("Application/json;charset=utf-8");
        response.getWriter().print(jsonObject);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
