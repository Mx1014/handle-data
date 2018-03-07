package com.zhsl.test.task;

import com.zhsl.data.entity.MyNumber;
import com.zhsl.data.jdbc.mysql.JDBCTools;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.*;

public class ImportNum {

    public static void main(String[] args){
        ImportData id = new ImportData();
        id.start();
    }
}

class ImportData{
    public void start(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                try {
                    initData();
                }catch (Exception e){
                    System.out.println("执行任务失败:"+e.getMessage());
                    //e.printStackTrace();
                }
            }
        }, 1000 , 60000);
    }
    private void initData()throws Exception{
        Connection.Response response = Jsoup.connect("http://buy.cqcp.net/trend/ssc/scchart_11.aspx").userAgent("Mozilla/5.0").method(Connection.Method.POST).data("DropDownList1","100").timeout(10000).execute();
        Document document = response.parse();
        String str = document.body().toString();
        str = str.substring(str.indexOf("Con_BonusCode"));
        str = str.replace("Con_BonusCode = \"","").substring(0,str.indexOf("\";")).replace("\";","");
        String[] strs = str.split(";");
        //System.out.println(str);
        List<MyNumber> list = new ArrayList<>();
        for (String s:strs
                ) {
            MyNumber mn = new MyNumber(Integer.parseInt(s.split("=")[0]),s.split("=")[1].replace(",",""));
            list.add(mn);
        }
        System.out.println(list.get(list.size()-1));

        int maxNum = 0;
        String sql = "select case when num is null then 0 else max(num) end as maxNum from n_num";
        List<Map<String, String>> data = JDBCTools.query(sql);
        if(data!=null&&data.size()>0)
            maxNum = Integer.parseInt(data.get(0).get("maxNum"));
        for (MyNumber mn:list
                ) {
            if(mn.getNum()>maxNum){
                System.out.println("发现新期号："+mn.getNum());
                System.out.println("准备入库...");
                sql = "insert into n_num values("+mn.getNum()+",'"+mn.getRewardNum()+"',"+mn.getGroup()+")";
                JDBCTools.execute(sql);
                System.out.println("入库成功，期号："+mn.getNum());
            }
        }
    }
}
