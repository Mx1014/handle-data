package com.zhsl.data.niit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiangjg
 * @date 2018/9/6 8:04
 */
public class YunGZ {

    /**
     * http://www.yunguizhou.com 账号：15608505805  密码：15608505805
     * @param args
     */
    public static void main(String[] args) {
        String cookie = "PHPSESSID=f64pdnfe0v6bls9osfsknh2ls1; Hm_lvt_0f719439efe026d389f7303d1179ab62=1536193384; Hm_lpvt_0f719439efe026d389f7303d1179ab62=1536217503";
        String id = "5";
        go(cookie, id);
    }

    private static void go(String cookie,String id){
        try {
            HttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpPost request = new HttpPost("http://www.yunguizhou.com/api/Course/detail");
            request.addHeader("Referer","http://www.yunguizhou.com/");
            request.addHeader("Cookie", cookie);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("id", id));
            request.setEntity(new UrlEncodedFormEntity(nvps,HTTP.UTF_8));
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = JSON.parseObject(StringEscapeUtils.unescapeJava(strResult));
                JSONObject videos = jsonObject.getJSONObject("data").getJSONObject("videos_url");
                for(int i=1;i<10;i++){
                    JSONObject jsonObj = videos.getJSONObject(""+i);
                    if(jsonObj!=null){
                        JSONObject videos2 = jsonObj.getJSONObject("video");
                        for(int j=1;j<20;j++){
                            JSONObject video = videos2.getJSONObject(""+j);
                            if(video!=null){
                                System.out.println(JSON.toJSONString(video));
                                String num = j+"";
                                String time = video.getString("video_hour");
                                ready(cookie,id,i+"",num,time);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void ready(String cookie,String id,String chap,String num,String time){
        System.out.println("chap===="+chap+"  num===="+num+"   time===="+time);
        try {
            HttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpPost request = new HttpPost();
            request.setURI(new URI("http://www.yunguizhou.com/api/course/watchtime_edit"));
            request.addHeader("Referer","http://www.yunguizhou.com/");
            request.addHeader("Content-Type","application/x-www-form-urlencoded");
            request.addHeader("Cookie", cookie);

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("course_id", id));
            nvps.add(new BasicNameValuePair("chap", chap));
            nvps.add(new BasicNameValuePair("curs", num));
            nvps.add(new BasicNameValuePair("watch_time", time));
            request.setEntity(new UrlEncodedFormEntity(nvps,HTTP.UTF_8));

            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(response.getEntity());
                System.out.println(num+"===="+StringEscapeUtils.unescapeJava(strResult));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
