package cn.com.starshopping.servlet.shangpinguanli;

import cn.com.starshopping.service.shangpinguanli.DeleteService;
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
 * Date: 2019/01/07
 * Description:
 * Version: V1.0
 */
@WebServlet(name = "DeleteServlet",urlPatterns = "/deleteServlet")
public class DeleteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DeleteService deleteService = new DeleteService();
        JSONObject jsonObject = deleteService.delete(request);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(jsonObject);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
