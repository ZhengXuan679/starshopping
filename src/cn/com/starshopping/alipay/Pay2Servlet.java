package cn.com.starshopping.alipay;

import cn.com.starshopping.util.UnicodeAndString;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: thinknovo
 * Date: 2019/01/02
 * Description:   电脑页面通过手机客户端扫码支付
 * Version: V1.0
 */
@WebServlet(name = "Pay2Servlet", urlPatterns = "/pay2Servlet")
public class Pay2Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//        System.out.println(shouhuodizhi2);
////        System.out.println(UnicodeAndString.decode(shouhuodizhi2));
//        shouhuodizhi2=URLDecoder.decode(shouhuodizhi2);
//        System.out.println(shouhuodizhi2);
//        System.out.println(request.getCharacterEncoding());


        //订单编号  10位随机数字组成
        Random random = new Random();
        int dd = 0;
        String ddbh = "";
        for (int i = 0; i < 10; i++) {
            dd = random.nextInt(10);
            ddbh = ddbh + dd;
        }

        String zongjia = request.getParameter("zongjia");//订单总价

        //前端ajax传递过来的数据，组装成一个数组
        String arr = request.getParameter("arr");

        String shouhuodizhi = request.getParameter("shouhuodizhi");
        String num = request.getParameter("num");


//        String shouhuodizhi = URLEncoder.encode(shouhuodizhi2, "UTF-8");
//        System.out.println(shouhuodizhi);
//
//        shouhuodizhi = URLDecoder.decode(shouhuodizhi, "utf-8");
//        System.out.println("转="+shouhuodizhi);
//


        String out_trade_no = ddbh;
        String product_code = "FAST_INSTANT_TRADE_PAY";      // 支付方式：FAST_INSTANT_TRADE_PAY扫码支付
        String total_amount = zongjia;                     // 这笔订单的价格
        String subject = "IphoneX 256G";                      // 订单商品名称
        String body = "这是一个用肾换来的电话";                // 订单商品描述

        String serverURL = "https://openapi.alipaydev.com/gateway.do";     // alipay测试支付地址   生产环境支付地址为https://openapi.alipay.com/gateway.do
        String APP_ID = "2016092400583449";   // 创建的应用ID
        // 自己APP的私钥
        String mKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCmiFTdk6pzSxOvZNOIYGiXXVOLVkeHZ3CCYcKqqMfjorOWT8f5pIKWz9PntEq6iZTMw0hx8eLhFG4w8G9wSEISMflVnLuv0yxh1XYU31MWDGGGmCVyo9bnPRV3hq841oSsF53S2w8ZrOpqkNAQGRvW4eXYpsrkptcGYKtKLieAoGb7ZMHMm3Kr3jmMFYJfOGcWWHq1oPDzHCOZrsY4agwJKcjb/VFV0/mL1S+B73KaQYZFcgX5vq0JynRUep+MeR4mBb6BFfJVTRhVKPyeKTWP5rIQ09IvcNFc+nvq85goa0t8Xm1Z39EqLMV1yh7Rr5TIELcSedWOuBhRuDYKUyuZAgMBAAECggEAPhnCw+oH+vx5N8O6mjWRaziTKfefrt+k9gnspOVRvRJ0M85GfVKanBCeLd5uq4cYpQ8BmfQgrdyHhtpyRAW+FbjrCMDswJFYEJegdAIfoq41RBLCjDCKuCpwMeVYvDJKwT4BTZsA04tG0zxzSj2Cwyv0IUl5ibCpqKgt1/jEJFOQfwy4k/wZ5TyC7UVvTRBJrnb+Bau/z3QyVvxSAifw/bcsb5hrSnt2mTcFHTdv63RtPS5Y3UpLX/zsH2A8ucDo4oMus7Cnny06Cj5dbWBjoAVfJQQegMO1LTtDaF5KrETWKIeJ8QH92l5+KU4krrLPahsZMpOX3ZuY1M+tWUK/vQKBgQDyKqBbp1r0EExas5WLG/bqj9crJBnR//hiIWmDZ5lM7j4NMZyGSJgYE8jTy2ErPzgMNh/RGWxf6qgpL9X8M+G6ZQBYSi1UmJ1d+KiH81xbwUJD1v+BkUsNYljLo2NH/Nvquaj/AfGu3d+QuUxqbAvT0NCfQI1ZEvPtHCvnTgfhmwKBgQCwC6fbvdsg76jJyFqgc18/raoQbaczcoQuSxWu7ywz5SvsMuwKEhjPNCyDY+F9vkYsKTLoFl8C0a2hsggeYDCJ8Q8bf0mfV2z1KdRlVZvDwL3vg/gU8UvdLBRqTK2/ZiGa93bbyqoMK8MuWT9DUgZy1/zG8grhWICcSyx643hE2wKBgQCHLaI750yPJrXof4x/jYmkX8zFSS6cmd0lse77Kg5Z8jdzF/l+v/Eep5SYHVRNVsnLjz7tctqbJBrgetJhKPjdGlo57pYlPdfCR/aThRZOJa/+vHn98oPJodddPQRDf/OmsCKUzpoBedq9J6JvWYa0o0CBVQBR85F0DyC2ykj99wKBgFlyoqhpD/TtMJLOJRYPaN7/C0Drph/i2Lx41BydU5VOpQGPYhU/2rE1LkTYNBNi4V7A1CfObhoNla5puexNLXx5KhbrlunKTwdGaK/QTqOoxBj1sdBaRhSrt/fG3CU67GpDwHWuxLYIalSbbiRzgelKSxp+kv6qrDY9j+0ktNAvAoGBAIIIrkbCI6WDrDj6X+bcpWwo2A83wEY/AAagT0cJU4HO/cJVTqqICcinUFeHiFI4A2W+jp+3nVa0GF+lyetKid8VhqDTqLZIpnpWiN/kN7QUpCOiCdx/S0244ahnOntxGfZE/Q/yBDkE4jVY3kiiAduZXxrmX9GhiMz2RJJdnLD9";
        // 支付宝的公钥(使用自己APP的公钥兑换)
        String aKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuYxoQWAmYwNhOS/BoaCsQp67jgNvFJRVdkCRfVht1w+WzNRcFXzjfoOo7gqmCPp987AoROPFfivfXU0vvfMbm7mBPEdhCTMwc23W0l6eXtts2Lp51KM4A6yRJFLFHXFKCLj8yctCPUhG+IPxsI7CDUsBm9nr+TYqxu7HgIvHVFUxTEswHh2fNWfsaBsAHe/tTLYvAKKiz+JGwP5V33YceqTt4GQcdQh01KxguZGs+ot1OicZC6DfJIyX4dqdfrOUcewm+49I91x/DLhPwywJBf08kfV6CYtjkw0UZfHonRhM9ilA1xkgtThPwYWyZ6wIRNZAf168XfGaRfosXh/77wIDAQAB";
        AlipayClient alipayClient = new DefaultAlipayClient(serverURL, APP_ID, mKey, "json", "utf-8", aKey, "RSA2"); //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();    // 创建API对应的request
        response.setContentType("application/json; charset=utf-8");
        alipayRequest.setReturnUrl("http://localhost:8080/shangpindingdan?arr=" + arr + "&ddbh=" + ddbh + "&zongjia=" + zongjia + "&shouhuodizhi=" + shouhuodizhi + "&num=" + num);          // 当调用接口成功，返回的页面
        alipayRequest.setNotifyUrl("http://localhost:8080/html/index.html");   // 在公共参数中设置回跳和通知地址

        /*
             填充业务参数
                 out_trade_no      每笔订单号需要自身的唯一id，支付宝会根据此id判断是否交易
                 total_amount      这笔订单的价格
                 subject           订单商品名称(也可以为标题)
                 body              订单描述
                 passback_params   回传参数
                 extend_params
                 product_code   支付方式：QUICK_WAP_PAY 登录账号支付
         */


        String bizContent = "{" +
                "\"out_trade_no\":\"" + out_trade_no + "\"," +
                "\"product_code\":\"" + product_code + "\"," +
                "\"total_amount\":\"" + total_amount + "\"," +
                "\"subject\":\"" + subject + "\"," +
                "\"body\":\"" + body + "\"}";
        alipayRequest.setBizContent(bizContent);                             // 提交支付请求
        System.out.println("支付生成json数据 bizContent=" + bizContent);    // 打印json支付数据

        String form = "";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(form);//直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
