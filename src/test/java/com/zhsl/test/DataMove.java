package com.zhsl.test;

import com.zhsl.data.entity.Rain;
import com.zhsl.data.entity.River;
import com.zhsl.data.jdbc.pg.PgJdbc;
import com.zhsl.data.jdbc.sqlserver.SqlserverJdbc;
import com.zhsl.data.thread.RainThread;
import com.zhsl.data.thread.RiverThread;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiangjg
 * @date 2018/7/3 17:13
 */
public class DataMove {

    @Test
    public void move() throws SQLException {
        //迁移测站
//        List<Map<String, Object>> list = getSqlserverData();
//        for (Map<String, Object> map : list
//                ) {
//            insert(map);
//        }
        //迁移水位流量过程线
//        List<Map<String, Object>> list = getSqlserverWaterLineData();
//        for (Map<String, Object> map : list
//                ) {
//            insertWaterLine(map);
//        }


//        List<River> rivers = getSqlserverRiverData();
//        for (River river : rivers
//                ) {
//            insertRiver(river);
//        }
    }

    public static void main(String[] args) throws Exception {
        //迁移雨量水位数据
//        List<Rain> rains = DataMove.getSqlserverRainData();
//        List<Rain> rainsTmp = new ArrayList<>();
//        for (Rain rain : rains
//                ) {
//            rainsTmp.add(rain);
//            if (rainsTmp.size() >= 10000) {
//                RainThread thread = new RainThread(rainsTmp);
//                thread.start();
//                rainsTmp = new ArrayList<>();
//            }
//        }

        List<River> rivers = DataMove.getSqlserverRiverData();
        List<River> riversTmp = new ArrayList<>();
        for (River river : rivers
                ) {
            riversTmp.add(river);
            if (riversTmp.size() >= 10000) {
                RiverThread thread = new RiverThread(riversTmp);
                thread.start();
                riversTmp = new ArrayList<>();
            }
        }
    }



    private void insert(Map<String, Object> map) throws SQLException {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        try {
            con = PgJdbc.getConnection();
            sta = con.createStatement();
            String stcd = map.get("stcd").toString();
            String stnm = map.get("stnm").toString();
            String type = map.get("type").toString();
            String lgtd = "null";
            if (map.get("lgtd") != null)
                map.get("lgtd").toString();
            String lttd = "null";
            if (map.get("lttd") != null)
                map.get("lttd").toString();
            String proid = map.get("proid").toString();
            String client = map.get("client").toString();
//            String ratio = "null";
//            if(map.get("ratio")!=null)
//                map.get("ratio").toString();
            String insertSql = "INSERT INTO b_reservoir.b_station_info(stcd,stnm,type,lgtd,lttd,pro_id,client_id) " +
                    "values(" + stcd + ",'" + stnm + "'," + type + "," + lgtd + "," + lttd + ",'" + proid + "','" + client + "')";
            System.out.println(insertSql);
            sta.executeUpdate(insertSql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sta.close();
            con.close();
        }
    }

    public static List<Map<String, Object>> getSqlserverData() throws SQLException {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        java.sql.ResultSet rs = null;
        List<Map<String, Object>> list = new ArrayList<>();
        String strQuery;
        try {
            con = SqlserverJdbc.getConnection();
            sta = con.createStatement();
            strQuery = "select * from dbo.st_name_p "; // 查询语句
            rs = sta.executeQuery(strQuery); // 执行查询语句，返回记录集
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("stcd", rs.getString("stcd"));
                map.put("client", rs.getString("name"));
                map.put("type", rs.getInt("type"));
                map.put("stnm", rs.getString("value"));
                map.put("lgtd", rs.getString("lgtd"));
                map.put("lttd", rs.getString("lttd"));
                //map.put("ratio", rs.getDouble("ratio"));
                map.put("proid", rs.getString("projcd"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sta.close();
            con.close();
        }
        return list;
    }

    private List<Map<String, Object>> getSqlserverWaterLineData() throws SQLException {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        java.sql.ResultSet rs = null;
        List<Map<String, Object>> list = new ArrayList<>();
        String strQuery;
        try {
            con = SqlserverJdbc.getConnection();
            sta = con.createStatement();
            strQuery = "select * from qzdb.rain_line_relation "; // 查询语句
            rs = sta.executeQuery(strQuery); // 执行查询语句，返回记录集
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("stcd", rs.getString("stcd"));
                map.put("z", rs.getString("z"));
                map.put("q", rs.getString("q"));
                map.put("number", rs.getString("id"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sta.close();
            con.close();
        }
        return list;
    }

    private void insertWaterLine(Map<String, Object> map) throws SQLException {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        try {
            con = PgJdbc.getConnection();
            sta = con.createStatement();
            String stcd = map.get("stcd").toString();
            String z = map.get("z").toString();
            String q = map.get("q").toString();
            String number = map.get("number").toString();
            String insertSql = "INSERT INTO b_reservoir.b_water_flow(stcd,z,q,number) " +
                    "values(" + stcd + "," + z + "," + q + "," + number + ")";
            System.out.println(insertSql);
            sta.executeUpdate(insertSql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sta.close();
            con.close();
        }
    }

    public static List<Rain> getSqlserverRainData() throws SQLException {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        java.sql.ResultSet rs = null;
        List<Rain> list = new ArrayList<>();
        String strQuery;
        try {
            con = SqlserverJdbc.getConnection();
            sta = con.createStatement();
            strQuery = "select stcd,CONVERT(varchar(25),tm,120) as tm, drp, pdr from dbo.st_pptn_r "; // 查询语句
            rs = sta.executeQuery(strQuery); // 执行查询语句，返回记录集
            while (rs.next()) {
                Rain rain = new Rain();
                rain.setStcd(rs.getString("stcd"));
                rain.setTm(sdf.parse(rs.getString("tm")));
                rain.setDrp(new BigDecimal(rs.getString("drp")));
                if (rs.getString("pdr") != null)
                    rain.setPdr(new BigDecimal(rs.getString("pdr")));
                list.add(rain);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sta.close();
            con.close();
        }
        return list;
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static List<River> getSqlserverRiverData() throws SQLException {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        java.sql.ResultSet rs = null;
        List<River> list = new ArrayList<>();
        String strQuery;
        try {
            con = SqlserverJdbc.getConnection();
            sta = con.createStatement();
            strQuery = "select stcd,CONVERT(varchar(25),tm,120) as tm, q, z from dbo.st_river_r"; // 查询语句
            rs = sta.executeQuery(strQuery); // 执行查询语句，返回记录集
            while (rs.next()) {
                River river = new River();
                river.setStcd(rs.getString("stcd"));
                river.setTm(sdf.parse(rs.getString("tm")));
                river.setQ(rs.getString("q"));
                river.setZ(rs.getString("z"));
                list.add(river);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sta.close();
            con.close();
        }
        return list;
    }
}