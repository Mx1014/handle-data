package com.zhsl.test.util;

import com.alibaba.fastjson.JSON;
import com.zhsl.data.jdbc.mysql.JDBCTools;
import com.zhsl.data.jdbc.pg.PgJdbc;
import com.zhsl.data.util.ExcelUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtilTest {

    @Test
    public void imporDuty() {
        try {
            ExcelUtil eu = new ExcelUtil();
            eu.setExcelPath("F:\\work\\水投集团度汛看板\\水库值班\\黔中值班.xlsx");
            eu.setSelectedSheetIdx(0);
            List<Row> list = eu.readExcel();
            Connection connection = null;
            Statement statement = null;
            String url = "jdbc:postgresql://42.123.116.132:5432/WaterCloudDB";
            String user = "postgres";
            String password = "watercloud-!@#123QWE";
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Statement sta = connection.createStatement();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            boolean isFirst = true;
            for (Row row : list
                    ) {
                String projnm = ExcelUtil.getCellValueForCell(row.getCell(0));
                if (!isFirst && !StringUtils.isEmpty(projnm)) {
                    String date = sdf.format(row.getCell(1).getDateCellValue());
                    String leader = ExcelUtil.getCellValueForCell(row.getCell(2));
                    String leaderPhone = ExcelUtil.getCellValueForCell(row.getCell(3));
                    String monitor = ExcelUtil.getCellValueForCell(row.getCell(4));
                    String monitorPhone = ExcelUtil.getCellValueForCell(row.getCell(5));
                    String dutyPhone = ExcelUtil.getCellValueForCell(row.getCell(6));
                    monitor = monitor.replaceAll("（", "(").replaceAll("）", ")").replaceAll("，", ",");
                    String insertSql = "INSERT INTO wcprojdb.p_duty(projnm,date,leader,leader_phone,monitor,monitor_phone,duty_phone) " +
                            "values('" + projnm + "',to_date('" + date + "','yyyy-mm-dd'),'" + leader + "','" + leaderPhone + "','" + monitor + "','" + monitorPhone + "','" + dutyPhone + "')";
                    System.out.println(insertSql);
                    sta.executeUpdate(insertSql);
                }
                isFirst = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void importWeather() {
        try {
            ExcelUtil eu = new ExcelUtil();
            eu.setExcelPath("F:\\thinkpage_cities\\thinkpage_cities.xls");
            eu.setSelectedSheetIdx(1);
            List<Row> list = eu.readExcel();
            System.out.println("------------");
            List<List<String>> myList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                    List<String> tmp = new ArrayList<>();
                    if ("中国".equals(ExcelUtil.getCellValueForCell(list.get(i).getCell(3)))) {
                        String value = ExcelUtil.getCellValueForCell(list.get(i).getCell(0));
                        tmp.add(value);
                        value = ExcelUtil.getCellValueForCell(list.get(i).getCell(1));
                        tmp.add(value);
                        value = ExcelUtil.getCellValueForCell(list.get(i).getCell(5));
                        tmp.add(value);
                        value = ExcelUtil.getCellValueForCell(list.get(i).getCell(7));
                        tmp.add(value);
                        myList.add(tmp);
                    }
                }
            }
            System.out.println(myList);
            JDBCTools.HOST = "192.168.1.2";
            JDBCTools.PASSWORD = "rootlys";
            JDBCTools.DATABASE_NAME = "zhsldb";

            for (List<String> mlist : myList
                    ) {
                String sql = "insert into c_weather_code(cid,cname,province,city) values('" + mlist.get(0) + "','" + mlist.get(1) + "','" + mlist.get(2) + "','" + mlist.get(3) + "')";
                JDBCTools.execute(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test() {
        try {
            ExcelUtil eu = new ExcelUtil();
            eu.setExcelPath("F:\\work\\专家库\\副本2017专家表（三级分类） (2).xlsx");
            eu.setSelectedSheetIdx(4);
            List<Row> list = eu.readExcel();
            System.out.println("------------");
            List<List<String>> myList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (i > 2) {
                    List<String> tmp = new ArrayList<>();
                    for (int j = 0; j < list.get(i).getLastCellNum(); j++) {
                        String value = ExcelUtil.getCellValueForCell(list.get(i).getCell(j));
                        tmp.add(value);
                    }
                    myList.add(tmp);
                }
            }
            System.out.println(myList);

            String sql = "delete from t_zhsl_cat";
            JDBCTools.execute(sql);

            addLevel(myList, 1);
            addLevel(myList, 2);
            addLevel(myList, 3);
            for (List<String> r : myList
                    ) {
                List<Map<String, String>> cats = JDBCTools.query("select cat_id from t_zhsl_cat where cat_name='" + r.get(8) + "' and level=3 and cat_pid = (select cat_id from t_zhsl_cat where cat_name='" + r.get(7) + "' and level = 2)");
                if (cats != null && cats.size() > 0) {
                    sql = "update t_zhsl_experts set three_cat = " + cats.get(0).get("cat_id") + "  where tel_num = '" + r.get(11) + "'";
                    JDBCTools.execute(sql);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addLevel(List<List<String>> myList, int level) {
        int pNameIndex = 0, nameIndex = 0, tmpLevel = 0;
        String sql;
        if (level == 1) {
            tmpLevel = level;
            pNameIndex = 6;
            nameIndex = 6;
        } else if (level == 2) {
            tmpLevel = level - 1;
            pNameIndex = 6;
            nameIndex = 7;
        } else if (level == 3) {
            tmpLevel = level - 1;
            pNameIndex = 7;
            nameIndex = 8;
        }
        try {
            if (pNameIndex != 0 && nameIndex != 0)
                for (List<String> r : myList
                        ) {

                    List<Map<String, String>> cats = JDBCTools.query("select cat_id from t_zhsl_cat where cat_name='" + r.get(pNameIndex).trim() + "' and level=" + tmpLevel);
                    if (level == 1) {
                        if (cats == null || cats.size() == 0) {
                            sql = "insert into t_zhsl_cat(cat_name,level,cat_pid) value('" + r.get(nameIndex).trim() + "'," + level + ",0)";
                            JDBCTools.execute(sql);
                        }
                    } else {
                        if (cats != null && cats.size() > 0) {
                            String pid = cats.get(0).get("cat_id");
                            cats = JDBCTools.query("select cat_name from t_zhsl_cat where cat_pid=" + pid + "");
                            boolean have = false;
                            for (int i = 0; i < cats.size(); i++) {
                                if (r.get(nameIndex).trim().equals(cats.get(i).get("cat_name").toString())) {
                                    have = true;
                                    break;
                                }
                            }
                            if (!have) {
                                sql = "insert into t_zhsl_cat(cat_name,level,cat_pid) value('" + r.get(nameIndex).trim() + "'," + level + ",'" + pid + "')";
                                JDBCTools.execute(sql);
                            }
                        }
                    }

                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void readWaterSafe() throws Exception {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        try {
            ExcelUtil eu = new ExcelUtil();
            eu.setExcelPath("F:\\work\\饮水安全\\（省汇总）全面解决农村饮水安全问题项目建议计划表3.5.xls");
            eu.setSelectedSheetIdx(0);
            List<Row> list = eu.readExcel();
            List<List<Object>> myList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (i > 6) {
                    List<Object> tmp = new ArrayList<>();
                    for (int j = 1; j < 13; j++) {
                        if (ExcelUtil.getCellValueForCell(list.get(i).getCell(2)).equals("合计") || j == 3 || j == 4 || j == 5)
                            continue;
                        String value = "";
                        if (j == 12)
                            value = ExcelUtil.getCellValueForCellNum(eu.wb, list.get(i).getCell(j));
                        else
                            value = ExcelUtil.getCellValueForCell(eu.wb, list.get(i).getCell(j));
                        if ("".equals(value))
                            value = "0";
                        tmp.add(value);
                    }
                    if (tmp.size() > 1 && "金海湖新区".equals(tmp.get(1))) {
                        tmp.add(105.431113243103);
                        tmp.add(27.232156434805);
                    }
                    if (tmp.size() > 1 && "百里杜鹃".equals(tmp.get(1))) {
                        tmp.add(105.93554);
                        tmp.add(27.17888);
                    }
                    myList.add(tmp);
                }
            }
            System.out.println(JSON.toJSONString(myList));

//            List<Map<String, Object>> list2 = new ArrayList<>();
//            con = PgJdbc.getConnection();
//            sta = con.createStatement();
//            ResultSet rs = sta.executeQuery("select * from wcdb.b_adcdcode where level=3 and adcd not like '520111%'");
//            while (rs.next()) {
//                Map<String, Object> map = new HashMap<>();
//                map.put("adnm", rs.getString("adnm"));
//                map.put("adcd", rs.getString("adcd"));
//                list2.add(map);
//            }
//
//            for (List<String> arr:myList
//                 ) {
//                if(arr.size()>0)
//                for(int i=0;i<list2.size();i++){
//                    if(arr.get(1).equals(list2.get(i).get("adnm").toString())){
//                        list2.get(i).put("list",arr);
//                        break;
//                    }
//                }
//            }
//            System.out.println(JSON.toJSONString(list2));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sta != null)
                sta.close();
            if (con != null)
                con.close();
        }
    }
}
