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
        String cookie = "hz-learn-guide=true; AMCV_945D02BE532957400A490D4C%40AdobeOrg=-1248264605%7CMCIDTS%7C17725%7CMCMID%7C44688316177008566744436351426659067588%7CMCAAMLH-1531980894%7C11%7CMCAAMB-1531980894%7CRKhpRz8krg2tLO6pguXWp5olkAcUniQYPHaMWWgdJ3xzPWQmdj0y%7CMCOPTOUT-1531383294s%7CNONE%7CMCAID%7CNONE; mbox=session#7b319bc36c0b489e8bc3efb7d2787f61#1531377963|PC#7b319bc36c0b489e8bc3efb7d2787f61.24_13#1594620896; s_fid=6E4AF48B842D598E-1B4BA505E0C7A5B5; Hm_lvt_17e2bf163b651814f71c0bf28d1591c1=1535328568,1535448446,1535933431,1536105368; JSESSIONID=CD223884FB6526C5FF17E767FEA2EF72; rememberMe=1cDQwYF/vXRpu0SKJibf9i67FjRGp+B/oEyyC/xjAD3BIqXIRJNDgRteBDi51nuhVva9c5pjmOIQPaQPuE5mI22G7PVB8pri99KuH+wYKZgH22oeaYc6U5fze2FkwLX2K0VOOg3DvEhyiHezWzZD8e1iBcVuo1tF3wW8KkvxeJTa03zVaXHJIAe5Y2koyFFR6Tx2bavxB5urSVY1eFhCAkWIUstmVVD8UBtNbiFjvuFUhFO+QBIQFppjLyXposhCHoWp6GG0LV/iFCt2jZXkximzAhwVG8wvVMAnpHUIZUnXP4BgDf9fi3THhStEc6qxyUkqoP+aMU/OsVevpBaBX+IdKUOATlBGmde8B5yNepMmfEEjhf72q0Y/aACEZI8wRPE3oCu/ChK1Y9ZC6APd/zeX8CZcLKsErVhxeUWmvo1SvmLPcAjRUr4ZRLsLe38TCueGDZ1Tuiq8RlU6KX7T3vD6FnZKMXZ6dcIXL0gh8vD2c0hytL2hX6EyqPoeJGddbb9LaBAS0Zgfie1hkhMmGEhUAb50oTJRgC4KCyAzs6c=; rong_appId=uwd1c0sxdue31";
        String url = "http://www.training-china.com/course/index25/19.html";
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
