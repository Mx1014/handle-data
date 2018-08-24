package com.zhsl.data.niit;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Collections;

/**
 * @author xiangjg
 * @date 2018/7/25 8:46
 */
public class StartApplication {

    private static final int ORDER_ASC = 0;
    private static final int ORDER_DESC = 1;

    public static void main(String[] args) {
        String cookie = "m_lvt_17e2bf163b651814f71c0bf28d1591c1=1534920629; rong_appId=uwd1c0sxdue31; JSESSIONID=047F9FF256B71150E62FF87417F81ECC; rememberMe=kW2SXS1LjQpZzsK4HRVuwpgJOIhuPFMbLiKxYxr9ivEivqKZtDhG/zW4Wa+2btbkE/N8QI6hqj4JjGAnyLc7cmAwgmh5FBKGO0HVOQMeGD3v0PM5cy+/Y45ClEwbm4+mffJiIAaaP6+a/WbWXnCAcaXn1fb493PTJedG6rVpW1mkFycI0So4nmksj0e7CKCj5Mn3zdTB5WMCd98YBKAk/LvL8DdW8JP2vKmu2qde2bLI7bKt6RHDm8zTJH4zJr2r77bKqVjES2MrujeOJ5PmlUs59vC1FPhGPQAV4u5ZuscNfdUKr2/kLoGm6vEqHhYuAvJEyI01l5NgAnCX7TewUElYAas9w0GKqD2GdMvRJTKRdIc8S7JaQUhEuEpYY2HmW8f+VNkT2knziNVfutSFfipdcmvkt+TxN2L7e1gxlG4CL5j6buR03z0cg027u1CWNgCJvvjd5Z5Vfv4k8hnJU/YgNyrmGOEBsWmenjLMOAM5SyObe7irkdaS50VWItN4eP1XyoYfBRusTizSj6SPaxuNY+0kAmf0XgFpkDolKM8=; Hm_lpvt_17e2bf163b651814f71c0bf28d1591c1=1534920869";
        String url = "http://www.training-china.com/course/index52/30.html";
        int order = ORDER_ASC;
        go(cookie, url, order);
    }

    private static void go(String cookie,String url,int order){
        try {
            HttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpGet request = new HttpGet(url);
            request.addHeader("Cookie", cookie);
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(response.getEntity());
                Document doc = Jsoup.parse(strResult);
                Elements elements = doc.getElementsByTag("a");
                String index = url.split("/")[4];
                if(order == ORDER_DESC&&elements!=null&&elements.size()>0)
                    Collections.reverse(elements);
                reader(elements,index,cookie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void reader(Elements elements,String index,String cookie){
        for (int i = 0; i < elements.size(); i++){
            Element e = elements.get(i);
            String href = e.attr("href");
            if (href.indexOf("html") < 0 && href.indexOf(index) < 0)
                continue;
            Elements lis = e.getElementsByTag("i");
            if (lis.size() == 0)
                continue;
            Element li = lis.get(0);
            if ("es-icon es-icon-undone status-icon".equals(li.attr("class"))){
                String uri = href.replace(".html", "/finish.html");
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet("http://www.training-china.com/" + uri);
                request.addHeader("Cookie", cookie);
                try {
                    client.execute(request);
                    System.out.println("完成：" + href);
                }catch (Exception ex){
                    System.out.println("失败：" + href+"  "+ex.getMessage());
                }
            }
        }
    }
}
