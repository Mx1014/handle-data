package com.zhsl.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

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
}
