package com.zhsl.test;

import com.zhsl.data.entity.FastDFSFile;
import com.zhsl.data.jdbc.pg.PgJdbc;
import com.zhsl.data.util.FastDFSClient;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiangjg
 * @date 2019/3/13 17:01
 */
public class FastDFSTest {

    @Test
    public void test() {
        try {
            FastDFSFile fastDFSFile = new FastDFSFile();
            fastDFSFile.setContent(FileUtils.readFileToByteArray(new File("D:\\wKgB5VyBFIeAZ60WAO1-e1lkXq4522.jpg")));
            fastDFSFile.setExt("jpg");
            fastDFSFile.setName("wKgB5VyBFIeAZ60WAO1-e1lkXq4522.jpg");
            String[] ret = FastDFSClient.upload(fastDFSFile);
            System.out.println(ret);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test2() {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            con = PgJdbc.getConnection();
            sta = con.createStatement();
            ResultSet rs = sta.executeQuery("select * from drinkingwaterdb.t_project_file where size<5000 and size>=1000");
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("group_name", rs.getString("group_name").trim());
                map.put("file_url", rs.getString("file_url").trim());
                map.put("img_id", rs.getInt("img_id"));
                list.add(map);
            }
            System.out.println(list.toString());
            int count = 0;
            for (Map<String, Object> map : list
                    ) {
                String group_name = map.get("group_name").toString();
                String file_url = map.get("file_url").toString();
                int img_id = (int) map.get("img_id");
                String urlStr = "http://139.9.85.57:8088/" + group_name + "/" + file_url;
                URL url = new URL(urlStr);
                HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
                int length = urlcon.getContentLength()/1024;
                System.out.println(urlStr);
                System.out.println(length);
                sta.execute("update drinkingwaterdb.t_project_file set size=" + length + " where img_id=" + img_id);
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
}
