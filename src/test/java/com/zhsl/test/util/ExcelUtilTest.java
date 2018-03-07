package com.zhsl.test.util;

import com.zhsl.data.jdbc.mysql.JDBCTools;
import com.zhsl.data.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelUtilTest {

    @Test
    public void importWeather(){
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
                    if("中国".equals(ExcelUtil.getCellValueForCell(list.get(i).getCell(3)))){
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

            for (List<String> mlist:myList
                 ) {
                String sql = "insert into c_weather_code(cid,cname,province,city) values('"+mlist.get(0)+"','"+mlist.get(1)+"','"+mlist.get(2)+"','"+mlist.get(3)+"')";
                JDBCTools.execute(sql);
            }
        }catch (Exception e){
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
}
