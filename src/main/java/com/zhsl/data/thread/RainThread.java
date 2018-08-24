package com.zhsl.data.thread;

import com.zhsl.data.entity.Rain;
import com.zhsl.data.jdbc.pg.PgJdbc;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiangjg
 * @date 2018/7/4 12:41
 */
public class RainThread extends Thread {
    private List<Rain> rains;

    public RainThread(List<Rain> rains) {
        this.rains = rains;
        System.out.println("初始化线程完成");
        System.out.println("数据:"+this.rains.size());
    }

    public void run() {
        System.out.println("开始运行线程");
        System.out.println("数据:"+rains.size());
        java.sql.Connection con = null;
        try {
            con = PgJdbc.getNewConnection();
            for (Rain rain : rains
                    ) {
                insertRain(rain, con);
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
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public void insertRain(Rain rain, java.sql.Connection con) throws SQLException {
        java.sql.Statement sta = null;
        try {
            sta = con.createStatement();
            String stcd = rain.getStcd();
            String tm = sdf.format(rain.getTm());
            String drp = rain.getDrp().toString();
            String pdr = "null";
            if (rain.getPdr() != null)
                rain.getPdr().toString();
            String insertSql = "INSERT INTO b_reservoir.d_rain(stcd,tm,drp,pdr) " +
                    "values(" + stcd + ",to_timestamp('" + tm + "','YYYY-MM-DD HH24:MI:SS')," + drp + "," + pdr + ")";
            System.out.println(insertSql);
            sta.executeUpdate(insertSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
