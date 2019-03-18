package cn.com.starshopping.util.fzy.sms;

public class TestSendSMS {

    public static String testSendSMS(){
        String returnstr = null;
        int mobile_code = (int)((Math.random() * 9 + 1) * 100000);
        returnstr = mobile_code+"";
        System.out.println(returnstr);
        return  returnstr;
    }
}
