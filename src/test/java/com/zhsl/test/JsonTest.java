package com.zhsl.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhsl.data.jdbc.pg.PgJdbc;
import com.zhsl.data.util.HttpUtil;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonTest {

    @Test
    public void test() {
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "包昭");
        jsonObject.put("age", 18);
        array.add(jsonObject);
        System.out.println(array);
        array.getJSONObject(0).put("name", "向敬光");
        System.out.println(array);
    }

    @Test
    public void changeData() {
        int toR = 50;
        try {
            String jsonStr = FileUtils.readFileToString(new File(""));
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if (jsonObject1.getString("type") == "line") {
                    JSONArray pointArr = jsonObject1.getJSONArray("data");

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void dataToJson() throws Exception {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            con = PgJdbc.getConnection();
            sta = con.createStatement();
            ResultSet rs = sta.executeQuery("select adcd,adnm,st_astext(pgeom) as wkt from wcdb.b_adcdcode where level=2");
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                String adnm = rs.getString("adnm");
                map.put("adcd", rs.getString("adcd").substring(0, 6));
                map.put("adnm", adnm);
                map.put("wkt", rs.getString("wkt"));
                list.add(map);
            }
            System.out.println(JSON.toJSONString(list));


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sta.close();
            con.close();
        }
    }

    @Test
    public void process()throws Exception {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            con = PgJdbc.getConnection();
            sta = con.createStatement();
            ResultSet rs = sta.executeQuery("select p.id,p.name from drinkingwaterdb.t_projects p where county_adcd='522634000000000' and p.year=2019 and p.process=1 and p.project_img is not null and EXISTS (select 1 from drinkingwaterdb.t_relation where pro_id=p.id) limit 100");
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", rs.getBigDecimal("id"));
                map.put("name", rs.getString("name"));
                list.add(map);
            }
            System.out.println(JSON.toJSONString(list));


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sta.close();
            con.close();
        }
    }
}
