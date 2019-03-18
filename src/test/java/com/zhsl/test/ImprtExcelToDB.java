package com.zhsl.test;

import com.zhsl.data.jdbc.pg.PgJdbc;
import com.zhsl.data.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiangjg
 * @date 2019/3/15 8:47
 */
public class ImprtExcelToDB {

    @Test
    public void importPoorVillage(){
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        try {
            con = PgJdbc.getConnection();
            ExcelUtil eu = new ExcelUtil();
            eu.setExcelPath("F:\\work\\饮水安全\\2760个贫困发生率在20%以上的村1.xls");
            eu.setSelectedSheetIdx(1);
            List<Row> list = eu.readExcel();
            List<List<String>> myList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                    List<String> tmp = new ArrayList<>();
                    for (int j = 0; j < 5; j++) {
                        String value = ExcelUtil.getCellValueForCell(eu.wb, list.get(i).getCell(j));
                        tmp.add(value.trim());
                    }
                    myList.add(tmp);
                }
            }
            System.out.println(myList.toString());

            sta = con.createStatement();
            for (List<String> l : myList
                    ) {
                String adcd = l.get(0);
                String city = l.get(1);
                String county = l.get(2);
                String country = l.get(3);
                String village = l.get(4);
                String insert = "insert into drinkingwaterdb.t_check_poor_village values('" + adcd + "','" + city + "','" + county + "','" + country + "','" + village + "')";
                System.out.println(insert);
                sta.execute(insert);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (sta != null)
                    sta.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Test
    public void updateVillage() {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> listPoor = new ArrayList<>();
        try {
            con = PgJdbc.getConnection();
            sta = con.createStatement();
            ResultSet rs = sta.executeQuery("select * from drinkingwaterdb.t_check_village where is_poor=0");
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("village", rs.getString("village").trim());
                map.put("county", rs.getString("county_adnm").trim());
                map.put("id", rs.getInt("id"));
                list.add(map);
            }
            System.out.println("需要匹配记录：" + list.size());
            rs = sta.executeQuery("select * from drinkingwaterdb.t_check_poor_village");
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("village", rs.getString("village").trim());
                map.put("county", rs.getString("county").trim());
                listPoor.add(map);
            }
            int count = 0;
            for (Map<String, Object> map : list
                    ) {
                boolean poor = false;
                for(int i=0;i<listPoor.size();i++){
                    Map<String, Object> poorMap = listPoor.get(i);
                    if(poorMap.get("county").equals(map.get("county").toString())&&map.get("village").toString().contains(poorMap.get("village").toString())){
                        poor = true;
                        break;
                    }
                }

                if(poor){
                    int id = (int) map.get("id");
                    String sql = "update drinkingwaterdb.t_check_village set is_poor=1 where id=" + id;
                    sta.execute(sql);
                    System.out.println(sql);
                    count++;
                }
            }
            System.out.println("更新记录：" + count);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (sta != null)
                    sta.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
