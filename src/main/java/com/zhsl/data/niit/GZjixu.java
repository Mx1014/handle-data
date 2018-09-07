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
     * http://guizhou.zxjxjy.com 15608505805 15608505805
     *
     * @param args
     */
    public static void main(String[] args) {
        String cookie = "UM_distinctid=165ac98f0cb0-047c3c1a76bd8-1571466f-1fa400-165ac98f0cc54e; edu-s=4d651f4e4b764390beaa1a55c4c5c3de; CNZZDATA1254056072=2068168552-1536198433-%7C1536216167; Hm_lvt_b21659b538990a950b60a32e93668ae4=1536199029,1536220619; Hm_lpvt_b21659b538990a950b60a32e93668ae4=1536220926; openWindow=true";
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
                        String data = getParam(cookie, time, url);
                        System.out.println(data);
                        ready(cookie, data, url);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getParam(String cookie, String time, String url) {
        //处理时间
        String[] times = time.split(":");
        BigDecimal hour = new BigDecimal(times[0]);
        BigDecimal minte = new BigDecimal(times[1]);
        BigDecimal ss = new BigDecimal(times[2]);
        hour = hour.multiply(BigDecimal.valueOf(60 * 60));
        minte = minte.multiply(BigDecimal.valueOf(60));
        ss = ss.add(BigDecimal.valueOf(1));
        time = hour.add(minte).add(ss).multiply(BigDecimal.valueOf(1000)).toString();

        //获取 usid scoid
        Map<String, String> us = getUserIdAndScoreId(cookie, url);
        String usid = us.get("usid");
        String scoid = us.get("scoid");
        //组装参数
        Map<String, String> map = URLRequest(url);
        String leid = map.get("courseid");
        String tlid = map.get("coursewareid");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("direct", "cmiputcat");
        jsonObject.put("usid", usid);
        jsonObject.put("tlid", tlid);
        jsonObject.put("scoid", scoid);
        jsonObject.put("leid", leid);

        JSONObject score = new JSONObject();
        score.put("raw", "");

        JSONObject core = new JSONObject();
        core.put("score", score);
        core.put("credit", "");
        core.put("entry", "");
        core.put("exit", "");
        core.put("lesson_mode", "");
        core.put("session_time", "");
        core.put("student_name", "");
        core.put("total_time", time);
        core.put("lesson_location", time);
        core.put("lesson_status", "completed");
        core.put("student_id", usid);

        JSONObject cmi = new JSONObject();
        cmi.put("comments", "");
        cmi.put("comments_from_lms", "");
        cmi.put("launch_data", "");
        cmi.put("comments", "");
        cmi.put("suspend_data", "");
        cmi.put("core", core);
        jsonObject.put("cmi", cmi);
        String data = JSON.toJSONString(jsonObject);
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

    private static void ready(String cookie, String data, String url) {
        checkSession(cookie);
        try {
            data = setEncryption(data);

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


    private static void checkSession(String cookie) {
        try {
            HttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpGet request = new HttpGet("http://guizhou.zxjxjy.com/userplatform/checkSession?_=" + System.currentTimeMillis());
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
