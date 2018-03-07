package com.zhsl.test;

import com.zhsl.data.entity.Manual;
import com.zhsl.data.jdbc.mysql.JDBCTools;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManualData {

    @Test
    public void test(){
        JDBCTools.DATABASE_NAME = "safemanagedb";
        JDBCTools.PASSWORD = "root";
        try {
            List<Manual> list = getAllManual();
            for (Manual m:list
                    ) {
                System.out.println(m.toString());
                String path = "";
                if(!StringUtils.isEmpty(m.getFilePath())){
                    path = m.getFilePath().replace("\\","\\\\");
                }else{
                    path = m.getFilePath();
                }
                String sql = "insert into t_manual (file_name, file_path, name, pid, id) values ('"+m.getFileName()+"', '"+path+"', '"+m.getName()+"', "+m.getPid()+", "+m.getId()+")";
                JDBCTools.execute(sql);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void update(){
        JDBCTools.DATABASE_NAME = "safemanagedb";
        JDBCTools.HOST = "192.168.1.2";
        JDBCTools.PASSWORD = "rootlys";
        String sql = "select * from t_manual";
        try {
            List<Map<String, String>> list = JDBCTools.query(sql);
            for (Map<String, String> map:list
                 ) {
               if(!StringUtils.isEmpty(map.get("file_path"))&&!"null".equals(map.get("file_path"))&&map.get("file_path").indexOf("_")>0){
                   String newPath = map.get("file_path").substring(0,map.get("file_path").indexOf("/"))+"/"+map.get("file_path").split("_")[1];
                   System.out.println(newPath);
                   sql = "update t_manual set file_path='"+newPath+"' where id="+map.get("id");
                    JDBCTools.execute(sql);
               }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Manual> getAllManual() {
        List<Manual> list = new ArrayList<>();
        list.add(new Manual(1,"施工安全管理常用表格",0));
        File file = new File("F:\\file");
        File[] array = file.listFiles();
        int index = 2;
        for (File f:array
                ) {
            int id = index;
            list.add(new Manual(id,f.getName(),1));
            File[] tempArray = f.listFiles();
            for (File ff:tempArray
                    ) {
                index++;
                list.add(new Manual(index,ff.getName().split("_")[0]+"("+ff.getName().split("_")[1].replaceAll(".doc","")+")",ff.getName(),ff.getPath(),id));
            }
            index++;
        }
        return list;
    }
}
