package com.zhsl.test;

import com.zhsl.data.entity.MyNumber;
import com.zhsl.data.jdbc.mysql.JDBCTools;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author xiangjg
 * @date 2019/1/14 10:36
 */
public class SSCTest {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Test
    public void test(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH,-1);
        while (c.getTime().before(new Date())){
            inDB(sdf.format(c.getTime()));
            c.add(Calendar.DAY_OF_MONTH,1);
        }
    }

    private void inDB(String date){
        try {
            String url = "https://m.500.com/info/kaijiang/ssc/"+date+".shtml";
            Document doc = Jsoup.parse(new URL(url), 5000);
            Elements list = doc.body().getElementsByClass("info-table-cont");
            List<MyNumber> data = new ArrayList<>();
            for (int i=0;i<list.size();i++
                    ) {
                Element e = list.get(i);
                String num = e.child(0).child(0).html();
                if("期号".equals(num))
                    continue;
                String rewardNum = e.child(0).child(2).html().replace(",","");
                MyNumber myNumber = new MyNumber(Integer.parseInt(num.substring(2)),rewardNum);
                String sql = "insert into n_num(num,rewardNum,_group) select "+myNumber.getNum()+",'"+myNumber.getRewardNum()+"',"+myNumber.getGroup()+" from dual where not exists(" +
                        "select num from n_num where num="+myNumber.getNum()+")";
                System.out.println(sql);
                try {
                    JDBCTools.execute(sql);
                }catch (Exception ex){
                    ex.printStackTrace();
                    continue;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void test2(){
        try {
            List<Map<String, String>> data = new ArrayList<>();
            Map<Integer, Integer> map = new HashMap<>();
            List<Map<String, String>> list = JDBCTools.query("select * from n_num");
            for (Map<String, String> m:list
                 ) {
                int num = Integer.parseInt(m.get("num"))/1000;
                if(map.get(num) == null){
                    map.put(num,0);
                }else{
                    if(m.get("_group")!=null){
                        int group = Integer.parseInt(m.get("_group"));
                        if(group == 60)
                            map.put(num,map.get(num) + 1);
                    }
                }
            }
            System.out.println(map.toString());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void simulation(List<MyNumber> data){
        double m = 10000.00;
        int count6 = 0;
        for (MyNumber num:data
                ) {
            if(num.getGroup() == 60){
                m += 157;
                count6++;
            }
            else{
                m -= 168;
            }

        }
        System.out.println(m);
        System.out.println(data.size());
        System.out.println(count6);
    }
}
