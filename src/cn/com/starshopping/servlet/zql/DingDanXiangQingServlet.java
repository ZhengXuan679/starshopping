package cn.com.starshopping.servlet.zql;

import cn.com.starshopping.service.zql.DingDanXiangQingService;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: ZouQinLun
 * Date: 2019/01/09
 * Description:
 * Version: V1.0
 */
@WebServlet(urlPatterns = "/dingDanXiangQingServlet")
public class DingDanXiangQingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DingDanXiangQingService dingDanXiangQingService = new DingDanXiangQingService();
        JSONObject jsonObject = dingDanXiangQingService.dingDanXiangQingService(req);
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().print(jsonObject);
    }
}
