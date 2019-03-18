package cn.com.starshopping.filter;




import cn.com.starshopping.util.Mydb;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Jxhun
 * Date: 2018/12/13
 * Description:
 * Version: V1.0
 */
@WebFilter(filterName = "ZongFilter",urlPatterns = "/*",
        initParams = {
                @WebInitParam(name="driver_url",value="com.mysql.jdbc.Driver"),
                @WebInitParam(name="db_url",value="jdbc:mysql://localhost:3306/starshopping"
                        + "?useSSL=true&rewriteBatchedStatements=true"
                        + "&useUnicode=true&characterEncoding=utf-8&rewriteBatchedStatements=true"),
                @WebInitParam(name="user",value="root"),
                @WebInitParam(name="password",value="123"),
        })
// 120.79.192.122
public class ZongFilter implements Filter {
    public void destroy() {
        try {
            Mydb.myConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
        String driver_url = config.getInitParameter("driver_url");
        String db_url = config.getInitParameter("db_url");
        String user = config.getInitParameter("user");
        String password = config.getInitParameter("password");
        try {
            Mydb.jdbc(driver_url, db_url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
