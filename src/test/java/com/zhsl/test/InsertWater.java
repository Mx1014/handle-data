package com.zhsl.test;

import com.alibaba.fastjson.JSONObject;
import com.zhsl.data.jdbc.pg.PgJdbc;
import org.junit.Test;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * @author xiangjg
 * @date 2018/11/9 8:11
 */
public class InsertWater {

    @Test
    public void test(){
        java.sql.Connection con = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            con = PgJdbc.getNewConnection();
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.set(Calendar.HOUR_OF_DAY,16);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            System.out.println(sdf.format(c.getTime()));
            while (c.getTime().before(new Date())){
                String num = "1575.0"+getRandom(2,5);
                insertWater(num, sdf.format(c.getTime()), con);
                c.add(Calendar.HOUR_OF_DAY,1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void insertWater(String z,String tm, java.sql.Connection con) throws SQLException {
        java.sql.Statement sta = null;
        try {
            sta = con.createStatement();
            String insertSql = "INSERT INTO b_reservoir.d_river(stcd,tm,z) " +
                    "values(10000099,to_timestamp('" + tm + "','YYYY-MM-DD HH24:MI:SS')," + z + ")";
            System.out.println(insertSql);
            sta.executeUpdate(insertSql);

            insertSql = "INSERT INTO b_reservoir.d_rain(stcd,tm,drp) " +
                    "values(10000100,to_timestamp('" + tm + "','YYYY-MM-DD HH24:MI:SS'),0)";
            System.out.println(insertSql);
            sta.executeUpdate(insertSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getRandom(int min, int max){
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;

    }
}
