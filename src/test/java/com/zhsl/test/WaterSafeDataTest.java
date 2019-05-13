package com.zhsl.test;

import com.alibaba.fastjson.JSON;
import com.zhsl.data.entity.FamilyVo;
import com.zhsl.data.jdbc.pg.PgJdbc;
import com.zhsl.data.util.ExcelUtil;
import com.zhsl.data.util.Md5PasswordEncoder;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiangjg
 * @date 2019/3/2 20:09
 */
public class WaterSafeDataTest {

    @Test
    public void test() {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        try {
            con = PgJdbc.getConnection();
            ExcelUtil eu = new ExcelUtil();
            eu.setExcelPath("F:\\work\\饮水安全\\新浦区.xlsx");
            eu.setSelectedSheetIdx(0);
            List<Row> list = eu.readExcel();
            List<List<String>> myList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                    List<String> tmp = new ArrayList<>();
                    for (int j = 0; j < 3; j++) {
                        String value = ExcelUtil.getCellValueForCell(eu.wb, list.get(i).getCell(j));
                        tmp.add(value.trim());
                    }
                    myList.add(tmp);
                }
            }
            System.out.println(myList.toString());

            String adcd = "520309000000000";
            sta = con.createStatement();
            for (List<String> l : myList
                    ) {
                String county = l.get(0);
                String country = l.get(1);
                String village = l.get(2);
                String insert = "insert into drinkingwaterdb.t_check_village(county_adcd,county_adnm,country_adnm,village) values('" + adcd + "','" + county + "','" + country + "','" + village + "')";
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
    public void DataTest() {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            con = PgJdbc.getConnection();
            sta = con.createStatement();
            ResultSet rs = sta.executeQuery("select * from drinkingwaterdb.t_check_inspect where adcd='520200000000000'");
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("county", rs.getString("county").trim());
                map.put("country", rs.getString("country").trim());
                map.put("id", rs.getInt("id"));
                list.add(map);
            }
            System.out.println("需要匹配记录：" + list.size());
            int count = 0;
            for (Map<String, Object> map : list
                    ) {
                String county = map.get("county").toString();
                String country = map.get("country").toString();
                int id = (int) map.get("id");
                sta.execute("update drinkingwaterdb.t_check_inspect set county='" + country + "',country='" + county + "' where id=" + id);
                count++;
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
    @Test
    public void excelTest(){
        try {
            String path = "F:\\work\\饮水安全\\table2.xlsx";
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(path));
            Sheet sheet = wb.getSheetAt(0);
            int lastRowNum = sheet.getRow(0).getPhysicalNumberOfCells();
            System.out.println(lastRowNum);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void importUser() {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        try {
            con = PgJdbc.getConnection();
            sta = con.createStatement();
            ExcelUtil eu = new ExcelUtil();
            eu.setExcelPath("F:\\work\\饮水安全\\附表：大普查成果核查表.xlsx");
            eu.setSelectedSheetIdx(0);
            List<Row> list = eu.readExcel();

            System.out.println(list.toString());
            List<List<String>> myList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                    List<String> tmp = new ArrayList<>();
                    for (int j = 0; j < 6; j++) {
                        String value = ExcelUtil.getCellValueForCell(eu.wb, list.get(i).getCell(j));
                        tmp.add(value.trim());
                    }
                    myList.add(tmp);
                }
            }
            System.out.println(myList.toString());
//            for (List<String> l : myList) {
//                String mobile = l.get(5);
//                String userId = l.get(0);
//                String sql = "update sysdb.sys_user set mobile='" + mobile + "' where user_id='" + userId + "'";
//                sta.execute(sql);
//            }
//            String adcd = "522700000000000";
//            for (List<String> l:myList
//                 ) {
//                String userId = l.get(4);
//                String name = l.get(3);
//                String area = l.get(1);
//                String job = l.get(2);
//                String pass = Md5PasswordEncoder.encrypt("000000", userId);
//                sta.execute("insert into sysdb.sys_user(user_id,user_name,password,area_adcd,role_id,area,job) values('" + userId + "','" + name + "','" + pass + "','" + adcd + "',5,'"+area+"','"+job+"')");
//            }
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
    public void createUser() {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        String adcd = "522700000000000";
        String user = "qiannan";
        int nums = 44;
        try {
            con = PgJdbc.getConnection();
            sta = con.createStatement();
            for (int j = 1; j <= nums; j++) {
                String userId = user + "_" + j;
                String name = "黔南" + j + "组";
                String pass = Md5PasswordEncoder.encrypt("000000", userId);
                sta.execute("insert into sysdb.sys_user(user_id,user_name,password,area_adcd,role_id) values('" + userId + "','" + name + "','" + pass + "','" + adcd + "',5)");
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
    public void createOneUser() {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        String adcd = "522300000000000";
        String area = "";
        String job = "黔西南统计";
        String userId = "qianxntj";
        String name = "黔西南统计";
        try {
            con = PgJdbc.getConnection();
            sta = con.createStatement();
            String pass = Md5PasswordEncoder.encrypt("000000", userId);
            sta.execute("insert into sysdb.sys_user(user_id,user_name,password,area_adcd,role_id,area,job,mobile) values('" + userId + "','" + name + "','" + pass + "','" + adcd + "',6,'" + area + "','" + job + "','" + userId + "')");
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
    public void updatePWD() {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        try {
            con = PgJdbc.getConnection();
            sta = con.createStatement();
            List<Map<String, Object>> list = new ArrayList<>();
            ResultSet rs = sta.executeQuery("select * from sysdb.sys_user where role_id=7 and user_name like '%查询'");
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("user_id", rs.getString("user_id"));
                map.put("id", rs.getInt("id"));
                list.add(map);
            }
            for (Map<String, Object> o : list
                    ) {
                String userId = o.get("user_id").toString();

                String pass = Md5PasswordEncoder.encrypt("000000", userId);
                sta.execute("update sysdb.sys_user set password='" + pass + "' where user_id='" + userId+"'");
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
    public void updateUser() {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        try {
            con = PgJdbc.getConnection();
            sta = con.createStatement();
            List<Map<String, Object>> list = new ArrayList<>();
            ResultSet rs = sta.executeQuery("select * from sysdb.sys_user where role_id=5 and area_adcd='522700000000000'");
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("user_id", rs.getString("user_id"));
                map.put("id", rs.getInt("id"));
                list.add(map);
            }

            for (Map<String, Object> o : list
                    ) {
                String adcd = o.get("user_id").toString().split("_")[0];
                String num = o.get("user_id").toString().split("_")[1];
                int id = (int) o.get("id");
                if (num.length() == 1)
                    num = "00" + num;
                if (num.length() == 2)
                    num = "0" + num;
                String userId = adcd + num;
                String pass = Md5PasswordEncoder.encrypt("000000", userId);
                sta.execute("update sysdb.sys_user set user_id='" + userId + "',password='" + pass + "' where id=" + id);
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
    public void delFamily() {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        try {
            con = PgJdbc.getConnection();
            sta = con.createStatement();
            List<FamilyVo> list = new ArrayList<>();
            ResultSet rs = sta.executeQuery("select * from drinkingwaterdb.t_check_family where user_id='liups015'");
            while (rs.next()) {
                FamilyVo o = new FamilyVo();
                o.setName(rs.getString("name"));
                o.setGroup(rs.getString("_group"));
                o.setVillage(rs.getString("village"));
                o.setCountry(rs.getString("country"));
                o.setCounty(rs.getString("county"));
                o.setId(rs.getInt("id"));
                list.add(o);
            }
            List<FamilyVo> listTmp = new ArrayList<>();
            for (FamilyVo o : list
                    ) {
                if(containsFamily(o,listTmp)){
                    sta.execute("delete from drinkingwaterdb.t_check_family where id="+o.getId());
                }else{
                    listTmp.add(o);
                }
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

    private boolean containsFamily(FamilyVo o,List<FamilyVo> list){
        for(int i=0;i<list.size();i++){
            FamilyVo familyVo = list.get(i);
            if(familyVo.getCounty().equals(o.getCounty())&&familyVo.getCountry().equals(o.getCountry())&&familyVo.getVillage().equals(o.getVillage())&&familyVo.getGroup().equals(o.getGroup())&&familyVo.getName().equals(o.getName())){
                return true;
            }
        }
        return false;
    }


    @Test
    public void testTmp() {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        try {
            con = PgJdbc.getConnection();
            sta = con.createStatement();
            List<FamilyVo> list = new ArrayList<>();
            ResultSet rs = sta.executeQuery("select * from drinkingwaterdb.t_check_inspect where country like '%社区'");
            while (rs.next()) {
                FamilyVo o = new FamilyVo();
                o.setVillage(rs.getString("village"));
                o.setCountry(rs.getString("country"));
                o.setId(rs.getInt("id"));
                list.add(o);
            }
            for (FamilyVo o : list
                    ) {
                if(o.getCountry().contains("乡")||o.getCountry().contains("镇")||o.getCountry().contains("街道")||o.getCountry().contains("办事处")){
                    String country = "";
                    String village = "";
                    if(o.getCountry().contains("乡")){
                        country = o.getCountry().split("乡")[0]+"乡";
                        village = o.getCountry().split("乡")[1]+o.getVillage();
                    }else if(o.getCountry().contains("镇")){
                        country = o.getCountry().split("镇")[0]+"镇";
                        village = o.getCountry().split("镇")[1]+o.getVillage();
                    }else if(o.getCountry().contains("办事处")){
                        country = o.getCountry().split("办事处")[0]+"办事处";
                        village = o.getCountry().split("办事处")[1]+o.getVillage();
                    }
                    else{
                        country = o.getCountry().split("街道")[0]+"街道";
                        village = o.getCountry().split("街道")[1]+o.getVillage();
                    }
                    String sql = String.format("update drinkingwaterdb.t_check_inspect set country='%s',village='%s' where id=%d",country,village,o.getId());
                    System.out.println(sql);
                    sta.execute(sql);
                }
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
}
