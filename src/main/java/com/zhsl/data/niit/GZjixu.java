package com.zhsl.data.niit;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiangjg
 * @date 2018/9/6 9:55
 */
public class GZjixu {

    /**
     * http://guizhou.zxjxjy.com 18585230345 316145286
     *
     * @param args
     */
    public static void main(String[] args) {
        String cookie = "UM_distinctid=16c659830a0125-0bef4171ad9dce-5f1d3a17-1fa400-16c659830a151a; edu-s=02a65b78302a4021ad53ac869ff3adb1; CNZZDATA1254056072=33943665-1565071460-null%7C1565308458; Hm_lvt_b21659b538990a950b60a32e93668ae4=1565223252,1565233145,1565233252,1565312012; Hm_lpvt_b21659b538990a950b60a32e93668ae4=1565312026";
        go(cookie);
    }

    public static void go(String cookie) {
        try {
            HttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpPost request = new HttpPost("http://guizhou.zxjxjy.com/p/course/uncompleted");
            request.addHeader("Referer", "http://guizhou.zxjxjy.com/p/course/uncompleted");
            request.addHeader("Cookie", cookie);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("hasLogin", "true"));
            request.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(response.getEntity());
                Document doc = Jsoup.parse(strResult);
                Elements elements = doc.getElementsByClass("xszt_ztl");
                for (int i = 0; i < elements.size(); i++) {
                    Element element = elements.get(i);
                    String time = element.getElementsByClass("color_d").first().html();
                    Element a = element.getElementsByTag("a").first();
                    String isReady = a.html();
                    String url = a.attr("href");
                    String name = element.getElementsByClass("xszt_name").first().html();
                    if ("开始学习".equals(isReady) || "继续学习".equals(isReady)) {
                        System.out.println("time=" + time + "  isReady=" + isReady + "  url=" + url + "  name=" + name);

                        //处理时间
                        String[] times = time.split(":");
                        Integer hour = new Integer(times[0]);
                        Integer minte = new Integer(times[1]);
                        Integer ss = new Integer(times[2]);
                        Integer total = (hour * 60 * 60 + minte * 60 + ss) * 1000;
                        Integer tt = new Integer(120000);

                        String status = "02";
                        long currentTimeMillis = System.currentTimeMillis();
//                        while (tt <= total) {
//                            time = tt.toString();
//                            System.out.println(time);
//
//                            String data = getParam(cookie, time, url, status);
//                            System.out.println(data);
//                            ready(cookie, data, url, currentTimeMillis);
//                            if ((tt + new Integer(120000)) > total && tt != total) {
//                                status = "completed";
//                                tt = total;
//                            } else
//                                tt += new Integer(120000);
//
//                            currentTimeMillis += 120*1000;
//                        }
                        status = "completed";
                        String data = getParam(cookie, total.toString(), url, status);
                        System.out.println(data);
                        ready(cookie, data, url, currentTimeMillis);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getParam(String cookie, String time, String url, String status) {
        //获取 usid scoid
        Map<String, String> us = getUserIdAndScoreId(cookie, url);
        String usid = us.get("usid");
        String scoid = us.get("scoid");
        //组装参数
        Map<String, String> map = URLRequest(url);
        String leid = map.get("courseid");
        String tlid = map.get("coursewareid");

         String data = "{\"direct\":\"cmiputcat\",\"usid\":\"" + usid + "\",\"tlid\":\"" + tlid + "\",\"scoid\":\"" + scoid + "\",\"leid\":\"" + leid + "\",\"cmi\":{\"core\":{\"score\":{\"raw\":\"\"},\"credit\":\"\",\"entry\":\"\",\"exit\":\"\",\"lesson_location\":\"" + time + "\",\"lesson_mode\":\"\",\"lesson_status\":\"" + status + "\",\"session_time\":\"\",\"student_id\":\"" + usid + "\",\"student_name\":\"\",\"total_time\":\"\"},\"comments\":\"\",\"comments_from_lms\":\"\",\"launch_data\":\"\",\"suspend_data\":\"\"}}";
        return data;
    }

    private static Map<String, String> getUserIdAndScoreId(String cookie, String url) {
        Map<String, String> us = new HashMap<>();
        try {
            HttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpGet request = new HttpGet("http://guizhou.zxjxjy.com" + url);
            request.addHeader("Referer", "http://guizhou.zxjxjy.com/p/classroom/simple");
            request.addHeader("Cookie", cookie);
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(response.getEntity());//System.out.println(strResult);
                String temp = strResult.substring(strResult.indexOf("setUS_ID('"), strResult.indexOf("setUS_ID('") + 45);
                String usid = temp.substring(temp.indexOf("'") + 1, temp.lastIndexOf("'"));
                temp = strResult.substring(strResult.indexOf("setSCOID('"), strResult.indexOf("setSCOID('") + 145);
                String scoid = temp.substring(temp.indexOf("'") + 1, temp.lastIndexOf("'"));
                us.put("usid", usid);
                us.put("scoid", scoid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return us;
    }

    private static void ready(String cookie, String data, String url, long currentTimeMillis) {
        checkSession(cookie, currentTimeMillis);
        try {
            data = setEncryption(data);
            //System.out.println(data);
            HttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpPost request = new HttpPost();
            request.setURI(new URI("http://guizhou.zxjxjy.com/userplatform/inProcessOf"));
            request.addHeader("Referer", "http://guizhou.zxjxjy.com" + url);
            request.addHeader("Connection", "keep-alive");
            request.addHeader("X-Requested-With", "XMLHttpRequest");
            request.addHeader("Origin", "http://guizhou.zxjxjy.com");
            request.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            request.addHeader("Cookie", cookie);

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("data", data));
            request.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(response.getEntity());
                System.out.println(strResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void checkSession(String cookie, long currentTimeMillis) {
        try {
            HttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpGet request = new HttpGet("http://guizhou.zxjxjy.com/userplatform/checkSession?_=" + currentTimeMillis);
            request.addHeader("Referer", "http://guizhou.zxjxjy.com/p/classroom/simple");
            request.addHeader("Cookie", cookie);
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(response.getEntity());
                //System.out.println(strResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void refresh(String cookie, Long old, Long _new) {
        try {
            HttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpGet request = new HttpGet("http://guizhou.zxjxjy.com/userplatform/refresh/0d4a8b0f977a4ec7906b9fc8fc049def/0597a4fafa0d431eba4d926412bba52d/a1d16aa0ecb843f09766a9524df596b5?_data=" + old + "&_=" + _new);
            request.addHeader("Referer", "http://guizhou.zxjxjy.com/p/classroom/simple");
            request.addHeader("Cookie", cookie);
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(response.getEntity());
                System.out.println(strResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密
     * oldWord：需要加密的文字/比如密码
     */
    public static String setEncryption(String oldWord) {
        String encodeWord = "";
        try {
            encodeWord = (new BASE64Encoder()).encode(oldWord.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeWord;
    }

    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param URL url地址
     * @return url请求参数部分
     */
    public static Map<String, String> URLRequest(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();

        String[] arrSplit = null;

        String strUrlParam = TruncateUrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
        //每个键值为一组 www.2cto.com
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String TruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;

        strURL = strURL.trim();

        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }

        return strAllParam;
    }
}
