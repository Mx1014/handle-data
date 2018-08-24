package com.zhsl.data.jdbc.sqlserver;

public class SqlserverJdbc {

    public static java.sql.Connection getConnection(){
        java.sql.Connection con = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String strCon = "jdbc:sqlserver://42.123.116.135:1433;databaseName=RWDB_B";
            String strUserName = "sa"; // 数据库的用户名称
            String strPWD = "ZhslAbc2008"; // 数据库的密码
            con = java.sql.DriverManager.getConnection(strCon, strUserName, strPWD);
        }catch (Exception e){
            System.out.println("加载数据库驱动失败："+e.getMessage());
        }
        return con;
    }
}
