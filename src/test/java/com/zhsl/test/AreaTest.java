package com.zhsl.test;

import com.alibaba.fastjson.JSON;
import com.zhsl.data.entity.Area;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiangjg
 * @date 2019/3/18 11:18
 */
public class AreaTest {

    //统计局行政区划代码入库
    @Test
    public void test() {
        try {
            String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018/52.html";
            List<Area> list = getArea(url, "520000000000000", 2);
            System.out.println(list.toString());
            // 1：利用File类找到要操作的对象
            File file = new File("D:" + File.separator + "area" + File.separator + "area.json");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            //2：准备输出流
            Writer out = new FileWriter(file);
            out.write(JSON.toJSONString(list));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Area> getArea(String url, String padcd, int level) throws Exception {

        String name = "";
        if (level == 2)
            name = "citytr";
        if (level == 3)
            name = "countytr";
        if (level == 4)
            name = "towntr";
        if (level == 5)
            name = "villagetr";
        List<Area> list = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(url), 5000);
        } catch (Exception e) {
            doc = Jsoup.parse(new URL(url), 5000);
        }

        Elements trList = doc.getElementsByClass(name);
        for (Element e : trList
                ) {
            Area area = new Area();
            Elements aList = e.getElementsByTag("a");
            if (aList.size() > 0) {
                area.setAdcd(aList.get(0).html());
                area.setAdnm(aList.get(1).html());
                area.setPadcd(padcd);
                area.setLevel(level);

                list.add(area);
                String cUrl = url.substring(0, url.lastIndexOf("/") + 1);
                list.addAll(getArea(cUrl + aList.get(0).attr("href"), aList.get(0).html(), level + 1));
            } else {
                Elements tdList = e.getElementsByTag("td");
                if (tdList.size() > 0) {
                    area.setAdcd(tdList.get(0).html());
                    area.setAdnm(tdList.get(tdList.size() - 1).html());
                    area.setPadcd(padcd);
                    area.setLevel(level);
                    System.out.println(area.toString());
                    list.add(area);
                }
            }
        }

        return list;
    }
}
