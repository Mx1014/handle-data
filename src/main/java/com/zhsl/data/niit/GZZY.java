package com.zhsl.data.niit;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * @author xiangjg
 * @date 2018/12/29 9:51
 */
public class GZZY {
    public static void main(String[] args) {

        final Thread t1 = new Thread() {
            public void run() {
                while (true) {
                    try {
                        String cookie = "JSESSIONID=A1B08B8FEB9AF235BBA79DA477B6CEF0-o1";
                        go(cookie);
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t1.start();
    }

    public static void go(String cookie) {
        try {
            HttpClient client = new DefaultHttpClient();

            //发送get请求
            HttpGet request = new HttpGet("http://user.gzzj.ghlearning.com/train/cms/my-video/count-timing.gson?myClassId=c92cf5aa-a3b1-45b7-b87e-36f16849d50a&myClassCourseId=0e5de88d-c61e-4b4d-9a11-2a563a299487&myClassCourseVideoId=d200724f-ed15-4c25-9adb-dd291b3488f1&watchInfo=%7B%22playduration%22%3A30%2C%22vid%22%3A%22a18dce4e43185ef9d1e23f056d74c605_a%22%2C%22sign%22%3A%2266017e2cf151222cb543b29985bce2168a5c377b%22%2C%22pid%22%3A%221546048133470X1021443%22%2C%22timestamp%22%3A1546048256532%7D");
            request.addHeader("Cookie", cookie);
            HttpResponse response = client.execute(request);
            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());

                System.out.println(strResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
