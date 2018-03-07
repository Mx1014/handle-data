package com.zhsl.test;

import com.zhsl.data.jdbc.mysql.JDBCTools;
import com.zhsl.data.util.ExcelUtil;
import com.zhsl.data.util.Md5PasswordEncoder;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PowerData
{
    @Test
    public void initUser()throws Exception{
        List<List<String>> myList = getExcelData(3);
        JDBCTools.URL = "jdbc:postgresql://202.98.194.42:5432/PowerDB";
        JDBCTools.DRIVER = "org.postgresql.Driver";
        JDBCTools.USER_NAME = "postgres";
        JDBCTools.PASSWORD = "postgresZhsl";
        String sql = "";
//        for (List<String> list: myList
//             ) {
//            sql = "insert into sysdb.sys_user(userid,username,password,adcd,roleid,sex) values('"+list.get(0)+"','"+list.get(1)+"','"+ Md5PasswordEncoder.encrypt(list.get(2),list.get(0))+"','"+list.get(0)+"000000000',3,1)";
//            JDBCTools.execute(sql);
//        }

        myList = getExcelData(2);
        for (List<String> list: myList
                ) {
            sql = "insert into sysdb.sys_user(userid,username,password,adcd,roleid,sex) values('"+list.get(0)+"','"+list.get(1)+"','"+ Md5PasswordEncoder.encrypt(list.get(2),list.get(0))+"','"+list.get(0)+"00000000000',2,1)";
            JDBCTools.execute(sql);
        }
    }

    private List<List<String>> getExcelData(Integer level)throws Exception{
        ExcelUtil eu = new ExcelUtil();
        if(level == 2){
            eu.setExcelPath("F:\\work\\农电项目\\市级用户.xlsx");
        }else if(level==3){
            eu.setExcelPath("F:\\work\\农电项目\\县级用户.xlsx");
        }
        eu.setSelectedSheetIdx(0);
        List<Row> list = eu.readExcel();
        System.out.println("------------");
        List<List<String>> myList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            List<String> tmp = new ArrayList<>();
            for (int j = 0; j < list.get(i).getLastCellNum(); j++) {
                String value = ExcelUtil.getCellValueForCell(list.get(i).getCell(j));
                tmp.add(value);
            }
            myList.add(tmp);
        }
        System.out.println(myList);
        return  myList;
    }

    @Test
    public void clearPowerData(){
        ExcelUtil eu = new ExcelUtil();
        eu.setExcelPath("F:\\123.xlsx");
        eu.setSelectedSheetIdx(0);
        try {
            List<Row> list = eu.readExcel();

            List<List<String>> myList = new ArrayList<>();
            for (int i = 2; i < list.size(); i++) {
                List<String> tmp = new ArrayList<>();
                for (int j = 0; j < list.get(i).getLastCellNum(); j++) {
                    String value = ExcelUtil.getCellValueForCell(list.get(i).getCell(j));
                    tmp.add(value);
                }
                if(tmp.get(2)==null||tmp.get(2)==""||tmp.get(2).replaceAll(" ","").length()==0)
                    continue;
                myList.add(tmp);
            }

            List<String> nameList = new ArrayList<>();
            for (List<String> power:myList
                 ) {
                nameList.add(power.get(1));
            }
            System.out.println(nameList.size());//1661

            JDBCTools.URL = "jdbc:postgresql://202.98.194.42:5432/PowerDB";
            JDBCTools.DRIVER = "org.postgresql.Driver";
            JDBCTools.USER_NAME = "postgres";
            JDBCTools.PASSWORD = "postgresZhsl";
            String sql = "select * from sysdb.power_station";
            List<Map<String, String>> dbPowers = JDBCTools.query(sql);
            for (Map<String, String> map:dbPowers
                 ) {
                System.out.println(map);
                if(nameList.indexOf(map.get("name").toString())<0){
                    sql = "delete from sysdb.power_station where id="+map.get("id").toString();
                    JDBCTools.execute(sql);
                }
            }
            //检查数据
//            for (String name:nameList
//                 ) {
//                boolean isHave = false;
//                for (int i=0;i<dbPowers.size();i++){
//                    if(name.trim().equals(dbPowers.get(i).get("name").toString().trim())){
//                        isHave = true;
//                        continue;
//                    }
//                }
//                if(!isHave)
//                    System.out.println(name);
//            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
