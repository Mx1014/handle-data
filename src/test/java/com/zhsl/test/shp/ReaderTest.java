package com.zhsl.test.shp;

import com.alibaba.fastjson.JSON;
import com.zhsl.data.shp.ReaderShape;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReaderTest {

    @Test
    public void test(){
        List<Map<String,Object>> list =  ReaderShape.readSHP("F:\\work\\汇川水务\\huichuan_shp\\huichuanpoint.shp");


        List<Map<String,Object>> reservoir = new ArrayList<>();
        List<Map<String,Object>> waterworks = new ArrayList<>();
        int index = 1;
        for (Map<String,Object> map:list
             ) {
            Map<String,Object> tmap = new HashMap<>();
            if(index<28){
                tmap.put("name","水库"+index);
                BigDecimal[] xy = {new BigDecimal(map.get("x").toString()),new BigDecimal(map.get("y").toString())};
                tmap.put("value",xy);
                tmap.put("type","01");
                reservoir.add(tmap);
            }else{
                tmap.put("name","污水厂"+index);
                BigDecimal[] xy = {new BigDecimal(map.get("x").toString()),new BigDecimal(map.get("y").toString())};
                tmap.put("value",xy);
                tmap.put("type","02");
                waterworks.add(tmap);
            }
            index++;
        }

        System.out.println(JSON.toJSONString(reservoir,true));
        System.out.println(JSON.toJSONString(waterworks,true));
    }
}
