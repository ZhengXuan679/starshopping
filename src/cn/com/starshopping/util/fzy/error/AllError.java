package cn.com.starshopping.util.fzy.error;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AllError {
   private static AllError allError = new AllError();//SingleTon: 饿汉模式

   private AllError(){};

   public static synchronized AllError myInstance(){
       return allError;
   }

    public void doGetError(HttpServletRequest request, HttpServletResponse response) throws IOException {
          response.setContentType("text/html;charset=utf-8");
          response.sendError(666,"抱歉，找不到此服务");
    }


}
