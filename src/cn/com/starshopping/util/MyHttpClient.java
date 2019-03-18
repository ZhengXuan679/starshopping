package cn.com.starshopping.util;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: thinknovo
 * Date: 2018/12/21
 * Description:  使用HttpClient工具，需要加入三个jar包commons-logging-1.2.jar、httpclient-4.5.6.jar、httpcore-4.4.10.jar
 * Version: V1.0
 */
public class MyHttpClient {
    public static void main(String[] args) {
        // post请求
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("userid","47053876@qq.com");
//        hashMap.put("seckey","64af2a4705b5678247e510783ea37e61");
//        hashMap.put("startStation","成都");
//        hashMap.put("arriveStation","重庆");
//        System.out.println(httpClientDoPost("http://www.chepiao100.com/api/qichepiao", hashMap));

        // get请求
//        System.out.println(httpClientDoGet("http://www.weather.com.cn/data/sk/101010100.html"));
    }
    /**
     * httpClient post请求(用于key-value格式的参数)
     * @param url         网络请求路径
     * @param params      网络请求参数
     * @return            如果获取到结果，返回JSON，如果没有获取到结果，返回null
     */
    public String httpClientDoPost(String url, Map<String, String> params) {
        try {
            // 实例化HTTP方法
            HttpPost request = new HttpPost();
            // 封装请求地址
            request.setURI(new URI(url));
            // 设置参数
            List<NameValuePair> nameValuePairsList = new ArrayList<NameValuePair>();
            // 封装数据值
            for (String name : params.keySet()) {
                String value = String.valueOf(params.get(name));
                nameValuePairsList.add(new BasicNameValuePair(name, value));
            }
            request.setEntity(new UrlEncodedFormEntity(nameValuePairsList, HTTP.UTF_8));
            return checkReturn(request);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * httpClient get请求
     * @param url   网络请求路径 + 参数
     * @return      如果获取到结果，返回JSON，如果没有获取到结果，返回null
     */
    public String httpClientDoGet(String url) {
        // 定义HttpClient
        HttpGet request = new HttpGet(url);
        return checkReturn(request);
    }

    /**
     * get和post共有性处理返回数据方法
     * @param request  httpget或者httppost对象
     * @return         如果得到数据，返回对应的数据结果，如果没有得到数据，返回null
     */
    private String checkReturn(HttpRequestBase request) {
        HttpClient client = new DefaultHttpClient();
        try {
            HttpResponse response = client.execute(request);
            int code = response.getStatusLine().getStatusCode();
            if(code == 200){	//请求成功
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity()
                        .getContent(),"utf-8"));
                StringBuilder sb = new StringBuilder("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line).append(NL);
                }
                in.close();
                return sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}