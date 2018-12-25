package com.zhsl.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhsl.data.jdbc.pg.PgJdbc;
import org.junit.Test;

import java.io.File;
import java.sql.SQLException;

/**
 * @author xiangjg
 * @date 2018/11/14 14:44
 */
public class InsertImages {

    @Test
    public void test() {
        java.sql.Connection con = null;
        try {
            con = PgJdbc.getNewConnection();
            String dir = "E:\\code\\vs\\WisdomWater\\WisdomWater\\bin\\Debug\\Data\\image\\images\\water";
            File file = new File(dir);
            String[] filelist = file.list();
            for (String str : filelist
                    ) {
                if (str.startsWith("021")) {
                    String siteCode = str;
                    File imgDir = new File(dir + File.separator + siteCode);
                    String[] images = imgDir.list();
                    for (String img : images
                            ) {
                        String url = "http://42.123.116.200:8085/image/water/" + siteCode + "/" + img;
                        insertImage(siteCode, url, con);
                    }
                }
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

    public void insertImage(String siteCode, String url, java.sql.Connection con) throws SQLException {
        java.sql.Statement sta = null;
        try {
            sta = con.createStatement();
            String insertSql = "INSERT INTO b_reservoir.d_image(id,url) " +
                    "values('" + siteCode + "','" + url + "')";
            System.out.println(insertSql);
            sta.executeUpdate(insertSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
