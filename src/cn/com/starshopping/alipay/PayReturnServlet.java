package cn.com.starshopping.alipay;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: thinknovo
 * Date: 2019/01/03
 * Description:
 * Version: V1.0
 */
@WebServlet(name = "PayReturnServlet", urlPatterns = "/payReturnServlet")
public class PayReturnServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("PayReturnServlet doPost 支付成功");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("PayReturnServlet doPost 支付成功");
    }
}
