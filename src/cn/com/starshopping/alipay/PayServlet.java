package cn.com.starshopping.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: thinknovo
 * Date: 2019/01/02
 * Description:        支付步骤开发: 1.注册蚂蚁金服开放平台账户，并申请成为开发者 https://docs.open.alipay.com/291/105971
 *                                   2.创建web应用程序 https://openhome.alipay.com/platform/manageHome.htm
 *                                   3.生成APP的密钥和公钥 https://docs.open.alipay.com/291/105971
 *                                   4.通过APP公钥，兑换支付宝公钥 https://openhome.alipay.com/platform/appDaily.htm?tab=info
 *                                   5.在第4步中的页面下载沙箱测试环境支付宝APP(只支持android测试)
 *                                   6.在第4步中的页面左侧可以查看沙箱环境中的商家账户和个人支付测试账户，虚拟钱可以随意充值，但上限为999.99万
 *                                   7.在第4步中的页面最下面有各种接入计费规则可以用作测试
 *                                   8.下载alipay-sdk-java-3.4.49.ALL.jar
 *                                   9.编写实例代码完成测试
 *
 *                       沙箱环境详细说明   https://docs.open.alipay.com/200/105311/
 *                       支付请求参数查询   https://docs.open.alipay.com/270/alipay.trade.page.pay
 * Version: V1.0
 */
@WebServlet(name = "PayServlet", urlPatterns = "/payServlet")
public class PayServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String serverURL = "https://openapi.alipaydev.com/gateway.do";     // alipay测试支付地址   生产环境支付地址为https://openapi.alipay.com/gateway.do
        String APP_ID = "2016092500590097";   // 创建的应用ID
        String mKey="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQChPSiiDEwOcaB9t55eVIZ6UTvdnoPIo7cgRORDKPbhQMvdhxRkdQUaBo+hbSIXYWtt4taNENFKTDiS3VXs7i4fMlLu8fkxSYOjjRVMJFu1VWQZ0eMJLOvXhhaoUChd1pjj31qEaj5Q9Rz+XUsRJ5DKogq+ztoTQCF7UmnsiVifVyuAjRlDukETryYyaC6QWnrzb0vvfC4xiMilqICpuWe4kdiJB2DhjODpM5v1HvQcyqCJlfOL8mXvKbWoR0zGO0l5ZqIaOwFrSkW2p9tKSjosbtuvi5ETpIS4U94n8CBvrgip++okaG+DborO5xO+xuQunUGby1uI27ZMTwtwiiKzAgMBAAECggEBAKDLyhLY6pxY6rGVBWWTK80d5/LamxfjVMPkkDL9zj7TDLvucwK/xIew1Vop6J6ic61RU5+4js9mdeDYxFEVMn/AAv7x5w4k/xeCoiOd+qs67inlq2HCH6x+t2fBEJCMa0qMustk3KKF3xU4Wp1P9NBviwHAbbBW+0zt4l6Wlf/IJ0hevMNYxYCVetKPb3VNloDhdYAAHPYo7qszKGpEQzjZcFvwMXkZ9g9EFXhkV2F5QZsQzKdonytwoGt5bVb+L/5DA2x8WBBXXV/v3gu9ZPMnCL9DvrWxdvmFAbEqDUlpc/bJvdZ+qPzmhqvgV1moRk+QmCKRYCxc7zLoUwxWlmECgYEA7qZWmyfPYwGROVBlPrkPpFI7KDMhH9UTpNT4pbf0QUQEcxamZDvVSlS3psmkdDrrDfOY+9L5jfCJ/bYACYpe7IicSAcpGP6z7Bm6cnpFyRYU1RoZV7GYztByISc8XE//0/YvFLOJh9ZKzghgFaIMEHrlykfeAXzK+aSm+hLMTvECgYEArPYT9NPX7/xJrhINRQwhvRXfwVoavm5LuAYvb4k+YryXGwb3VFvAb5sBX7iBS9yvTsI9gSDXMeNq5id8nrpqUzeK0PazTbm73SCVOSsYr417VvpcSxL6QAd52SY2P+jFElCDtK66qNmmWIcHlMwX0cGENcnwW2hHGxvby5RIU+MCgYBNfXVhq+uLGlctZQ168FxD3u+WfgOjcmyOeRjDtpd5GbzYIeKlaUg4eomnfpIHq5nnTdG+8yJVFGW6G/6nGmb4JuOX/7s4gnupjjEcOteuE51wFKll8CTOAqEMYtEWqGNJUAa5khJ2KP7hJ5cLbzK7pyw2Il/T29GMfgo56SCuAQKBgHOUFQ3bqkbSHiJWKcvkKX+DaHx6S7jJV4vnhjdQTQIJ6CfFZe1XSqbJvAdM8gnRtMw6fIMXgPXzdHHMHTisdXSafwbYdEUSQnmn1k/Xfq2BK9SZhKceTWAQHtp+8G2QjHCKoRhSUqWWm9Q4l1ZVc+1VFZYkFZvfePSijyPhONvlAoGBAK1c/btYMaVTSWQ55afmrha9VOTz/y5k9bWApICpYHwtgThpsYTtupW6yJhMsZCFTHxMoAGMyJyLC1aZfewydJL1Z7uSheyUzC54Mq4+1sEMrcK9k/BD5NCGLUBlvOMzBCtRB2zy4ESjNx1ftZU8455Wj6C/HIeOoKYc4uPmPvf/";
        String aKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxA8MX82nCGHAwSdomye00BF4EFCCgTxXCFuzIZ9ZFge3x/1ABY9hFFyHW1aBAIt7J9GZgVm03V4gm10k0lEd21jmouha++0v72h6vqhfNHjkhVFAfYZXCsd24j0zToCbBaGaZV2abtdN4mtkayeEoP7C6N5e0DcT4awg/lkNNexYEguRyjLB4bOeFkHHr8W1v/V4Fk3VSRlJ90uLbCpuYUmjAERsYhExtQaVwiLCf9mnIVKPSCbP0Hu1szqcPki3AkK/r8SlHJKuZYgNjzofwWz/xVDpKvGgUcvatyABKukqPFnuGbgY+ZYKXqgBmk1t6QQNqR31Cjkcb/0C+iddxwIDAQAB";
        AlipayClient alipayClient = new DefaultAlipayClient(serverURL, APP_ID,
                mKey, "json", "UTF-8", aKey, "RSA2");   //  获得初始化的AlipayClient
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();   //  创建API对应的request
        alipayRequest.setReturnUrl("http://localhost:8080/success.html");        // 用户确认支付后，支付宝get请求returnUrl
        alipayRequest.setNotifyUrl("http://localhost:8080/payReturnServlet");    // 交易成功后，支付宝post请求notifyUrl（商户入参传入）
        /*
            由于同步返回的不可靠性，支付结果必须以异步通知或查询接口返回为准，不能依赖同步跳转。
            商户系统接收到异步通知以后，必须通过验签（验证通知中的sign参数）来确保支付通知是由支付宝发送的。
         */

        /*
             填充业务参数
                 out_trade_no   每笔订单号需要自身的唯一id，支付宝会根据此id判断是否交易
                 total_amount   这笔订单的价格
                 subject        订单商品名称
                 product_code   支付方式：QUICK_WAP_PAY 登录账号支付
         */
        alipayRequest.setBizContent("{" +
                " \"out_trade_no\":\"20150324010101103\"," +
                " \"total_amount\":\"88.88\"," +
                " \"subject\":\"Iphone6 16G\"," +
                " \"product_code\":\"QUICK_WAP_PAY\"" +
                " }");
        String form="";
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
