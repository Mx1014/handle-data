package com.zhsl.test;

import com.alibaba.fastjson.JSON;
import com.zhsl.data.jdbc.mysql.JDBCTools;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryDataToJson {

    @Test
    public void test(){
        JDBCTools.URL = "jdbc:postgresql://192.168.1.2:5433/WaterCloudDB";
        JDBCTools.DRIVER = "org.postgresql.Driver";
        JDBCTools.USER_NAME = "postgres";
        JDBCTools.PASSWORD = "slydba123";
        try {
            String sql = "select monm,lgtd,lttd from wcdb.b_monitorobjectinf where motype='01' AND adcd='520221000000000'";
            List<Map<String, String>> list =  JDBCTools.query(sql);

            List<Map<String, Object>> data = new ArrayList<>();
            for (Map<String, String> m:list
                 ) {
                Map<String, Object> map = new HashMap<>();
                BigDecimal[] strs = new BigDecimal[]{new BigDecimal(m.get("lgtd").toString()),new BigDecimal(m.get("lttd").toString()),new BigDecimal(10)};
                map.put("name",m.get("monm").toString());
                map.put("value",strs);
                data.add(map);
            }
            System.out.println(JSON.toJSONString(data,true));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void updateData(){
        JDBCTools.URL = "jdbc:postgresql://202.98.194.42:5432/PowerDB";
        JDBCTools.DRIVER = "org.postgresql.Driver";
        JDBCTools.USER_NAME = "postgres";
        JDBCTools.PASSWORD = "postgresZhsl";
        String sql = "select * from sysdb.power_station where region='盘县'";
        try {
            List<Map<String, String>> list = JDBCTools.query(sql);
            for (Map<String, String> map:list
                 ) {
                sql = "select * from public.b_adcdcode where adcd='"+map.get("adcd").substring(0,4)+"00000000000'";
                List<Map<String, String>> adcdList = JDBCTools.query(sql);
                if(adcdList!=null&&adcdList.size()>0){
                    sql = "update sysdb.power_station set region='"+adcdList.get(0).get("adnm")+"' where id="+map.get("id");
                    JDBCTools.execute(sql);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
