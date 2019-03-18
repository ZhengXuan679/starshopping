package cn.com.starshopping.util.fzy.wangjimimamail;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: thinknovo
 * Date: 2019/01/02
 * Description:  需要mail-1.4.7.jar的支持  https://mvnrepository.com/artifact/javax.mail/mail/1.4.7
 *               这里以QQ邮箱账户为发送方，首先需要进入QQ邮箱，找到左上方的设置，然后选择账户，找到POP3/IMAP/SMTP/Exchange/CardDAV/CalDAV服务，开启pop3，获得授权码
 * Version: V1.0
 */
public class MyJavaMail implements Runnable {

    private String username;          // 登录的用户名
    private String email;             // 收件人邮箱
    private String uuid;              // uuid唯一码
    private String dengluming;        //收件人页面登录名
    private String xinmima;           //收件人新修改的密码
    private final String option = "7";//Service的请求指令参数

    public MyJavaMail(String email,String dengluming,String xinmima) {  // username 平台用户名
        this.dengluming = dengluming;
        this.xinmima = xinmima;
        this.email = email;
        // java系统默认生成的uuid，但是默认的uuid是有横杠隔开的，项目中需要手动去掉横杠
        this.uuid = UUID.randomUUID().toString().replaceAll("-","");  // 生成唯一随机码;
    }

    @Override
    public void run() {

        int aitindex = email.lastIndexOf("@");
        String houzhui = email.substring(aitindex+1);
        String host = "smtp."+houzhui;                    // 指定发送邮件的主机smtp.qq.com(QQ)|smtp.163.com(网易)  这里需要继续扩展做判断分支处理
        Properties properties = System.getProperties();      // 获取系统属性

        properties.setProperty("mail.smtp.host", host);     // 设置邮件服务器
        properties.setProperty("mail.smtp.auth", "true");  // 打开认证

        try {
            //QQ邮箱需要下面这段代码，163邮箱不需要
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);

            // 1.获取默认session对象
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(MailFiled.mailName, MailFiled.mailCode); // 发件人邮箱账号、授权码
                }
            });

            // 2.创建邮件对象
            Message message = new MimeMessage(session);

            // 3.设置发件人
            message.setFrom(new InternetAddress(MailFiled.mailName));

            // 4.设置收件人
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            // 5.设置邮件主题
            message.setSubject("StartShopping账号激活");

            // 6.设置邮件内容
            String content = "<html><head></head><body><h1>这是一封激活邮件,激活请点击以下链接，如果不是本人操作，请勿点击!!(<font color='red'>若成功则自动跳转登录页面</font>)</h1><h3>" +
                    "<a href='http://localhost:8080/WangJiMiMaServlet?option="+option+"&dengluming="+dengluming+"&xinmima="+xinmima+"' style='color:#f64a4a'>" +
                    "http://www.StartShopping.com?uuid=" + uuid
                    + "</a></h3></body></html>";
            message.setContent(content, "text/html;charset=UTF-8");
            // 7.发送邮件
            Transport.send(message);   // 阻塞方法
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // pop3协议如果权限认证失败，会报535错误
        // 启动一个子线程发送邮件，第一个参数为用户账户，第二参数为用户邮箱
//        String shouJianRenMail = "2811228581@qq.com";
//        new Thread(new MyJavaMail(shouJianRenMail)).start();
//        System.out.println(UUID.randomUUID().toString().replace("-",""));
    }
}
