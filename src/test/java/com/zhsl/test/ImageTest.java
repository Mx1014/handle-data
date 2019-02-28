package com.zhsl.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiangjg
 * @date 2019/2/24 18:25
 */
public class ImageTest {

    @Test
    public void test(){
        File file = new File("E:\\code\\node\\WaterSafeWeb\\public\\images\\patrol");
        File[] fileList = file.listFiles();
        List<Map<String,Object>> mapList = new ArrayList<>();
        for (File f:fileList
             ) {
            String index = f.getPath().substring(f.getPath().lastIndexOf("\\")+1);

            Map map = new HashMap<>();
            map.put("index",index);
            List<String> list = new ArrayList<>();
            for (File ff:f.listFiles()
                    ) {
                System.out.println(ff.getPath());
                list.add(ff.getPath().substring(ff.getPath().lastIndexOf("\\")+1));
            }
            map.put("imgs",list);
            mapList.add(map);
        }
        System.out.println(mapList.toString());
        System.out.println(JSON.toJSONString(mapList));
    }
}
