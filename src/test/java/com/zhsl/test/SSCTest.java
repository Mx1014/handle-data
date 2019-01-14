package com.zhsl.test;

import com.zhsl.data.entity.MyNumber;
import com.zhsl.data.util.HttpUtil;
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
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiangjg
 * @date 2019/1/14 10:36
 */
public class SSCTest {

    @Test
    public void test(){
        try {
            String url = "https://m.500.com/info/kaijiang/ssc/2019-01-13.shtml";
            Document doc = Jsoup.parse(new URL(url), 5000);
            Elements list = doc.body().getElementsByClass("info-table-cont");
            List<MyNumber> data = new ArrayList<>();
            for (int i=0;i<list.size();i++
                 ) {
                Element e = list.get(i);
                String num = e.child(0).child(0).html();
                if("期号".equals(num))
                    continue;
                String rewardNum = e.child(0).child(2).html().replace(",","");
                MyNumber myNumber = new MyNumber(num,rewardNum);
                data.add(myNumber);
            }
            System.out.println(data.toString());


            double m = 10000.00;
            int count6 = 0;
            for (MyNumber num:data
                 ) {
                if(num.getGroup() == 60){
                    m += 157;
                    count6++;
                }
                else{
                    m -= 168;
                }

            }
            System.out.println(m);
            System.out.println(data.size());
            System.out.println(count6);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
