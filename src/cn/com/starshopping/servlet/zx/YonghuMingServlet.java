package cn.com.starshopping.servlet.zx;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "YonghuMingServlet",urlPatterns = "/yonghuming")
public class YonghuMingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object yonghuming2 = session.getAttribute("yonghuming");
        String yonghuming="";
        if(yonghuming2!=null&& !yonghuming2.equals("")){
             yonghuming = (String)yonghuming2;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("yonghuming",yonghuming);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(jsonObject);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
