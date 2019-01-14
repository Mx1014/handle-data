package com.zhsl.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

public class JsonTest
{

    @Test
    public void test(){
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","包昭");
        jsonObject.put("age",18);
        array.add(jsonObject);
        System.out.println(array);
        array.getJSONObject(0).put("name","向敬光");
        System.out.println(array);
    }

    @Test
    public void changeData(){
        int toR = 50;
        try {
            String jsonStr = FileUtils.readFileToString(new File(""));
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for(int i=0;i<jsonArray.size();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if(jsonObject1.getString("type") == "line"){
                    JSONArray pointArr = jsonObject1.getJSONArray("data");
                    
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
