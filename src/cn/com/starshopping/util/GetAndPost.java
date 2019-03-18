package cn.com.starshopping.util;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: thinknovo
 * Date: 2018/12/21
 * Description:
 * Version: V1.0
 */
public class GetAndPost {
    /**
     *  url get请求远程服务
     * @param urlAddress   get请求路径+请求参数   例如：http://www.weather.com.cn/data/sk/101270101.html?city=成都&year=2018
     * @return      返回请求回来的结果，通常为json，如果为null代表请求失败或者错误
     */
    public String requestGet(String urlAddress) {
        String resultStr = null;
        try {
            URL url = new URL(urlAddress);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream(); //字节流,读取应该使用字节数组
            resultStr = IOUtils.toString(inputStream, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    /**
     * url post请求远程服务
     * @param urlAddress   post请求路径，不加请求参数，例如：http://www.weather.com.cn/data/sk/101270101.html
     * @param params       post中的请求参数， 例如：username='a'&pass='123'&age=15
     * @return             返回请求回来的结果，通常为json，如果为null代表请求失败或者错误
     */
    public String requestPost(String urlAddress, String params) {
        String resultStr = null;
        try {
            // 创建URL对象
            URL url = new URL(urlAddress);
            // 打开连接 获取连接对象
            URLConnection connection = url.openConnection();
            // 设置请求编码
            connection.addRequestProperty("encoding", "UTF-8");
            // 设置允许输入
            connection.setDoInput(true);
            // 设置允许输出
            connection.setDoOutput(true);
            // 从连接对象中获取输出字节流对象
            OutputStream outputStream = connection.getOutputStream();
            // 将输出的字节流对象包装成字符流写出对象
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            // 创建一个输出缓冲区对象,将要输出的字符流写出对象传入
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            // 向输出缓冲区中写入请求参数
            bufferedWriter.write(params);   // params 格式为  username='a'&pass='123'&age=15    注意：最前缀没有问号
            // 刷新输出缓冲区
            bufferedWriter.flush();
            // 从连接对象中获取输入字节流对象
            InputStream inputStream = connection.getInputStream();
            resultStr = IOUtils.toString(inputStream, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultStr;
    }

}
