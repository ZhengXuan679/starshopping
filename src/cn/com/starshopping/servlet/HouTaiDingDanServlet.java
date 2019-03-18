package cn.com.starshopping.servlet;

import cn.com.starshopping.service.HouTaiDingDanService;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: yanmin
 * Date: 2019/01/05
 * Description:
 * Version: V1.0
 */
@WebServlet(name = "HouTaiDingDanServlet", urlPatterns = "/houTaiDingDanServlet")
public class HouTaiDingDanServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = request.getParameter("action");
        HouTaiDingDanService houTaiDingDanService = new HouTaiDingDanService();
        String json = houTaiDingDanService.kongZhiTai(request, response);
//        if (action != null && !action.trim().equals("")) {  // 如果状态不为空
//            if (action.equals("chaxun")) {              // 如果前端是查询订单列表
//                json = houTaiDingDanService.chaXun(request);
//            } else if (action.equals("xiangqing")) {  //  如果前端是查询订单详情
//                json = houTaiDingDanService.chaXunDingDan(request);  // 调用查询方法
//            } else if (action.equals("fahuo")) {
//                json = houTaiDingDanService.xiuGaiWuLiu(request);  // 调用查询方法
//            } else if (action.equals("tongji")) {
//                json = houTaiDingDanService.tongJi(request);  // 调用查询方法
//            } else if (action.equals("daochu")) {
//                houTaiDingDanService.daoChu(request, response);  // 调用查询方法
//            }
//        }
        response.setContentType("application/json; charset=utf-8");
//        JSONObject jsonObject = new JSONObject(json);
        response.getWriter().print(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
