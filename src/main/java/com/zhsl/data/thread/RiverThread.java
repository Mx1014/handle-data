package com.zhsl.data.thread;

import com.zhsl.data.entity.Rain;
import com.zhsl.data.entity.River;
import com.zhsl.data.jdbc.pg.PgJdbc;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author xiangjg
 * @date 2018/7/4 13:21
 */
public class RiverThread extends Thread {
    private List<River> rivers;

    public RiverThread(List<River> rivers) {
        this.rivers = rivers;
        System.out.println("初始化线程完成");
        System.out.println("数据:"+this.rivers.size());
    }

    public void run() {
        System.out.println("开始运行线程");
        System.out.println("数据:"+rivers.size());
        java.sql.Connection con = null;
        try {
            con = PgJdbc.getNewConnection();
            for (River river : rivers
                    ) {
                insertRiver(river, con);
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
    public void insertRiver(River river, java.sql.Connection con) throws SQLException {
        java.sql.Statement sta = null;
        try {
            sta = con.createStatement();
            String stcd = river.getStcd();
            String tm = sdf.format(river.getTm());
            String q = river.getQ();
            String z = river.getZ();
            String insertSql = "INSERT INTO b_reservoir.d_river(stcd,tm,z,q) " +
                    "values(" + stcd + ",to_timestamp('" + tm + "','YYYY-MM-DD HH24:MI:SS')," + z + "," + q + ")";
            System.out.println(insertSql);
            sta.executeUpdate(insertSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
