package com.zhsl.test;

import com.zhsl.data.entity.Rain;
import com.zhsl.data.jdbc.sqlserver.SqlserverJdbc;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SqlserverTest {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void test() {
        String[] stcds = new String[]{"6"};//{"1", "2", "3", "4", "5", "6", "8"};
        try {
            for (String stcd : stcds
                    ) {
                completion(stcd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void completion(String stcd) throws Exception {
        java.sql.Connection con = null;
        java.sql.Statement sta = null;
        java.sql.ResultSet rs = null;
        List<Rain> list = new ArrayList<>();
        String strQuery;
        try {
            con = SqlserverJdbc.getConnection();
            sta = con.createStatement();
            strQuery = "SELECT top 1 CONVERT(varchar(100), tm, 20) as tm FROM dbo.ST_PPTN_R where stcd='"+stcd+"' and drp=0  order by tm desc "; // 查询语句
            rs = sta.executeQuery(strQuery); // 执行查询语句，返回记录集
            String newDate = "";
            if(rs.next())
                newDate = rs.getString("tm");
            System.out.println("newDate:" + newDate);
            if(newDate==null||newDate.length()==0)
                return;
            strQuery = "SELECT CONVERT(varchar(100), tm, 20) as tm,drp FROM dbo.ST_PPTN_R where stcd='"+stcd+"' and tm>=CONVERT(datetime,'"+newDate+"',20)  order by tm desc "; // 查询语句
            rs = sta.executeQuery(strQuery); // 执行查询语句，返回记录集
            int count = 0; // 计数器
            List<String> dates = new ArrayList<>();
            while (rs.next()) {
                Rain rain = new Rain();
                rain.setStcd(stcd);
                rain.setTm(sdf.parse(rs.getString("tm")));
                rain.setDrp(rs.getBigDecimal("drp"));
                list.add(rain);
                dates.add(rs.getString("tm"));
                count++; // 计数器计数
            }
            System.out.println("共查询到" + count + "行数据。"); // 统计查询出多少条数据
//            System.out.println(list.toString()); // 打印查询出来的数据

            Calendar c = Calendar.getInstance();
            String startD = newDate;
            String endD = sdf.format(new java.util.Date());
            c.setTime(sdf.parse(startD));
            c.add(Calendar.HOUR_OF_DAY,1);
            startD = sdf.format(c.getTime());
            while (sdf.parse(startD).before(sdf.parse(endD))){
                if(!dates.contains(startD)){
                    String insertSql = "INSERT INTO dbo.ST_PPTN_R(stcd,tm,drp) values('"+stcd+"',CONVERT(datetime,'"+startD+"',20),0)";
                    System.out.println(insertSql);
                    sta.executeUpdate(insertSql) ;
                }
                c.setTime(sdf.parse(startD));
                c.add(Calendar.HOUR_OF_DAY,1);
                startD = sdf.format(c.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rs.close(); // 关闭记录集
            sta.close(); // 关闭语句对象句柄
            con.close(); // 关闭连接
        }
    }
}
