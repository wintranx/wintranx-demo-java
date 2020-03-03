package com.wintranx.client.demo.util;

//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.methods.GetMethod;
//import org.apache.commons.httpclient.methods.PostMethod;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;


/**
 * http请求客户端
 *
 * @author Administrator
 *
 */
public class HttpClientUtil {

    public static String doGet(String url, Map<String, String> param) { 
  
     // 创建Httpclient对象 
     CloseableHttpClient httpclient = HttpClients.createDefault(); 
    //设置请求超时时间（各项超时参数具体含义链接）
    RequestConfig requestConfig = RequestConfig.custom()
       .setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000)

        .setSocketTimeout(10000)
                .build();

     String resultString = ""; 
     CloseableHttpResponse response = null; 
     try { 
       // 创建uri 
       URIBuilder builder = new URIBuilder(url); 
       if (param != null) { 
         for (String key : param.keySet()) { 
           builder.addParameter(key, param.get(key)); 
         } 
       } 
       URI uri = builder.build(); 
       // 创建http GET请求 
       HttpGet httpGet = new HttpGet(uri); 

            //给这个请求设置请求配置
            httpGet.setConfig(requestConfig);
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
 
       // 执行请求 
       response = httpclient.execute(httpGet); 
       // 判断返回状态是否为200 
       if (response.getStatusLine().getStatusCode() == 200) { 
         resultString = EntityUtils.toString(response.getEntity(), "UTF-8"); 
       } 
     } catch (Exception e) { 
       e.printStackTrace(); 
     } finally { 
       try { 
         if (response != null) { 
           response.close(); 
         } 
         httpclient.close(); 
       } catch (IOException e) { 
         e.printStackTrace(); 
       } 
     } 
     return resultString; 
   } 
               
             
    public static String doPost(String url, Map<String, String> param) {
        // 创建Httpclient对象 
        CloseableHttpClient httpClient = HttpClients.createDefault(); 

      //设置请求超时时间
      RequestConfig requestConfig = RequestConfig.custom()         
             .setConnectTimeout(10000)
             .setConnectionRequestTimeout(10000)

             .setSocketTimeout(10000)
             .build();

        CloseableHttpResponse response = null; 
        String resultString = ""; 
        try { 
            // 创建Http Post请求 
            HttpPost httpPost = new HttpPost(url); 
            httpPost.setConfig(requestConfig);
            // 创建参数列表 
            if (param != null) { 
                List<NameValuePair> paramList = new ArrayList<>(); 
                for (String key : param.keySet()) { 
                    paramList.add(new BasicNameValuePair(key, param.get(key))); 
                } 
                // 模拟表单 
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList); 
                httpPost.setEntity(entity); 
            } 
            // 执行http请求 
            response = httpClient.execute(httpPost); 

            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                resultString = EntityUtils.toString(response.getEntity(), "utf-8"); 
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
            try { 
                response.close(); 
            } catch (IOException e) { 
                // TODO Auto-generated catch block 
                e.printStackTrace(); 
            } 
        } 
 
        return resultString; 
    } 
                 
        public static String doPostJson(String url, String json) {
        // 创建Httpclient对象 
        CloseableHttpClient httpClient = HttpClients.createDefault(); 

       //设置请求超时时间
       RequestConfig requestConfig = RequestConfig.custom()         
             .setConnectTimeout(50000)
             .setConnectionRequestTimeout(50000)
             .setSocketTimeout(100000)
             .build();

        CloseableHttpResponse response = null; 
        String resultString = ""; 
        try { 
            // 创建Http Post请求 
            HttpPost httpPost = new HttpPost(url); 

            httpPost.setConfig(requestConfig);
            // 创建请求内容 ，发送json数据需要设置contentType
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity); 
            // 执行http请求 
            response = httpClient.execute(httpPost); 
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                resultString = EntityUtils.toString(response.getEntity(), "utf-8"); 
            }
            System.out.println("postResult:" + resultString);
        } catch (SocketTimeoutException e) {
            System.out.println("请求超时 ");
            resultString = "{\"resultCode\":\"9999\",\"resultMessage\":\"time out!\"}";
            System.out.println("postResult:" + resultString);
            e.printStackTrace();
        } catch (Exception e) {
            resultString = "{\"resultCode\":\"9999\",\"resultMessage\":\"访问异常!\"}";
            System.out.println("postResult:" + resultString);
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) { 
                // TODO Auto-generated catch block 
                e.printStackTrace(); 
            } 
        } 
 
        return resultString; 
    }

}