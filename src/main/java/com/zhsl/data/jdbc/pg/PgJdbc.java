package com.zhsl.data.jdbc.pg;

public class PgJdbc {

    private static java.sql.Connection con = null;
    public static java.sql.Connection getConnection(){
        if(con!=null)
            return con;
        try {
            con = getNewConnection();
        }catch (Exception e){
            System.out.println("加载数据库驱动失败："+e.getMessage());
        }
        return con;
    }

    public static java.sql.Connection getNewConnection(){
        java.sql.Connection con = null;
        try {
            Class.forName("org.postgresql.Driver");
            String strCon = "jdbc:postgresql://ip:port/db";
            String strUserName = "postgres"; // 数据库的用户名称
            String strPWD = "pass"; // 数据库的密码
            con = java.sql.DriverManager.getConnection(strCon, strUserName, strPWD);
        }catch (Exception e){
            System.out.println("加载数据库驱动失败："+e.getMessage());
        }
        return con;
    }

    public static void close(){
        try {
            if(con!=null)
                con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
