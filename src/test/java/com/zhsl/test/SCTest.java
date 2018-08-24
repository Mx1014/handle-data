package com.zhsl.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiangjg
 * @date 2018/6/29 10:44
 */
public class SCTest {

    @Test
    public void test() {
        String str = "F:\\work\\水城智慧水务\\shuicheng.json";

        JSONObject json = JSON.parseObject(readToString(str));
        JSONArray jsonArray = json.getJSONArray("features").getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates");
        String strArr = jsonArray.get(0).toString().replace("],["," ").replace("[","").replace("]","");
        List<List<Map<String,Object>>> ret = new ArrayList<>();
        List<Map<String,Object>> list = new ArrayList<>();

        for(int i=0;i<strArr.split(" ").length;i++){
            list = new ArrayList<>();
            String[] xyArr = strArr.split(" ")[i].split(",");
            Double[] doubles = new Double[]{Double.valueOf(xyArr[0]),Double.valueOf(xyArr[1])};
            Map<String,Object> map = new HashMap<>();
            map.put("value",10);
            map.put("coord",doubles);
            list.add(map);

            int index = 0;
            if(i<strArr.split(" ").length-1){
                index = i+1;
            }
            xyArr = strArr.split(" ")[index].split(",");
            doubles = new Double[]{Double.valueOf(xyArr[0]),Double.valueOf(xyArr[1])};
            map = new HashMap<>();
            map.put("value",10);
            map.put("coord",doubles);
            list.add(map);

            ret.add(list);
        }

        System.out.println(JSON.toJSONString(ret));


        List<Map<String,Object>> ret2 = new ArrayList<>();
        for(int i=0;i<strArr.split(" ").length;i++){
            String[] xyArr = strArr.split(" ")[i].split(",");
            Double[] doubles = new Double[]{Double.valueOf(xyArr[0]),Double.valueOf(xyArr[1])};
            Map<String,Object> map = new HashMap<>();
            map.put("lon",doubles[0]);
            map.put("lat",doubles[1]);
            ret2.add(map);
        }
        String[] xyArr = strArr.split(" ")[0].split(",");
        Double[] doubles = new Double[]{Double.valueOf(xyArr[0]),Double.valueOf(xyArr[1])};
        Map<String,Object> map = new HashMap<>();
        map.put("lon",doubles[0]);
        map.put("lat",doubles[1]);
        ret2.add(map);
        System.out.println(JSON.toJSONString(ret2));
    }

    public String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }
}
