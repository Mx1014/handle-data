package com.zhsl.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhsl.data.entity.Gps;
import com.zhsl.data.entity.Rain;
import com.zhsl.data.jdbc.pg.PgJdbc;
import com.zhsl.data.util.HttpUtil;
import com.zhsl.data.util.PositionUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * @author xiangjg
 * @date 2018/11/7 8:55
 */
public class WisdomDataToDB {

    @Test
    public void test() {
        java.sql.Connection con = null;
        try {
            con = PgJdbc.getNewConnection();
            String response = HttpUtil.doPost("http://111.85.91.84:9103/rest/siteInfo/siteInfoToGIS", "{\"siteType\": 2}");
            System.out.println(response);
            JSONObject jsonObject = JSON.parseObject(response);
            JSONArray jsonArray = jsonObject.getJSONObject("resultMap").getJSONArray("results");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                insertData(jsonObject1, con);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void insertData(JSONObject jsonObject, java.sql.Connection con) throws SQLException {
        java.sql.Statement sta = null;
        try {
            sta = con.createStatement();
            String deviceCode = jsonObject.getString("deviceCode");
            String deviceName = jsonObject.getString("deviceName");
            String siteCode = jsonObject.getString("siteCode");
            String lgtd = jsonObject.getString("siteLongitude");
            String lttd = jsonObject.getString("siteLatitude");
            String paramType = jsonObject.getString("paramType");
            String paramName = jsonObject.getString("paramName");
            String unit = jsonObject.getString("unit");
            String siteAddr = jsonObject.getString("siteAddr");
            String siteName = jsonObject.getString("siteName");

            Gps gps = covertGps(lgtd, lttd);

            String insertSql = "INSERT INTO b_reservoir.b_water_equipment(device_code,device_name,site_code,lgtd,lttd,param_type,param_name,unit,site_addr,site_name) " +
                    "values('" + deviceCode + "','" + deviceName + "','" + siteCode + "'," + gps.getWgLon() + "," + gps.getWgLat() + "," + paramType + ",'" + paramName + "','" + unit + "','" + siteAddr + "','" + siteName + "')";
            System.out.println(insertSql);
            sta.executeUpdate(insertSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Gps covertGps(String lgtd, String lttd) {
        Gps gps = new Gps(Double.valueOf(lttd), Double.valueOf(lgtd));
        Gps gcj = PositionUtil.bd09_To_Gcj02(gps.getWgLat(), gps.getWgLon());
        Gps star = PositionUtil.gcj_To_Gps84(gcj.getWgLat(), gcj.getWgLon());
        return star;
    }
}
