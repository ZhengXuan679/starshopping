package cn.com.starshopping.servlet.kehuguanli;


import cn.com.starshopping.service.kehuguanli.ZhuCeService;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: huxiaoyi
 * Date: 2019/01/11
 * Description:
 * Version: V1.0
 */
@WebServlet(name = "ZhuCeServlet",urlPatterns = "/servlet/zhuceServlet")
public class ZhuCeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ZhuCeService zhuCeService = new ZhuCeService();
        JSONObject json = zhuCeService.zhuce(request);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
