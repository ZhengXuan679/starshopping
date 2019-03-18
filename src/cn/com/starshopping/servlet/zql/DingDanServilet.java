package cn.com.starshopping.servlet.zql;

import cn.com.starshopping.service.zql.DingDanService;
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
 * Date: 2019/01/05
 * Description:
 * Version: V1.0
 */
@WebServlet(urlPatterns = "/dingDanServilet")
public class DingDanServilet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");         //从前台获取状态码
        DingDanService dingDanService = new DingDanService();   //创建DingDanService对象
        JSONObject jsonObject = null;
        resp.setContentType("application/json;charset=utf-8");  //设置输出格式
        if(action.equals("1")){                                 //如果状态码为1，则进行全部数据查询，返回封装有分页后，订单状态为0的所有数据的JSONObject
            jsonObject = dingDanService.dingDanService(req);
                resp.getWriter().print(jsonObject);
        }
        else if(action.equals("2")){                            //如果状态码为2，则是修改订单状态为1，返回封装有修改后，带分页，订单状态为0的数据的JSONObject
            boolean b=dingDanService.shanChu(req);
            if(b){
              jsonObject = dingDanService.dingDanService(req);
              resp.getWriter().print(jsonObject);
            }
            else resp.getWriter().print("删除失败，请重新尝试！");
        }
        else if(action.equals("3")){                            //如果状态码为3，则是返回按条件搜索的，订单状态为0的数据的JSONObject
            jsonObject=dingDanService.souSuo(req);
            resp.getWriter().print(jsonObject);
        }
        else if(action.equals("4")){                            //如果订单状态为4，则往数据库中的操作日志表插入删除操作的记录
            dingDanService.shanChuRiZhi(req);
//
        }
        else if(action.equals("5")){                            //如果订单状态为5，则往数据库中的操作日志表插入搜索操作的记录
            dingDanService.souSuoRiZhi(req);
//
        }

    }
}
