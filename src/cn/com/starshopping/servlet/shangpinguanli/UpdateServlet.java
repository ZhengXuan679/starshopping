package cn.com.starshopping.servlet.shangpinguanli;

import cn.com.starshopping.service.shangpinguanli.UpdateService;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Wong
 * Date: 2019/01/08
 * Description:
 * Version: V1.0
 */
@WebServlet(name = "UpdateServlet",urlPatterns = "/updateServlet")
public class UpdateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UpdateService updateService = new UpdateService();
        JSONObject jsonObject = updateService.update(request);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(jsonObject);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
