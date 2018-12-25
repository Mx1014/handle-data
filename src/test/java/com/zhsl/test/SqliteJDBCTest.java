package com.zhsl.test;

import com.alibaba.fastjson.JSON;
import com.zhsl.data.entity.VideoVo;
import com.zhsl.data.jdbc.sqlite.SqliteJDBC;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqliteJDBCTest {

    @Test
    public void test(){
        try {
            Connection connection = SqliteJDBC.createConnection("jdbc:sqlite:E:\\code\\vs\\FloodApplication\\FloodApplication\\bin\\x86\\Debug\\Data\\DB\\localDB");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from video");
            List<VideoVo> list = new ArrayList();
            while (rs.next()) {
                VideoVo video = new VideoVo();
                video.setVideoCode(rs.getString("videoCode"));
                video.setVideoName(rs.getString("videoName"));
                video.setParentId(rs.getString("ParentId"));
                list.add(video);
            }
            System.out.println(JSON.toJSONString(list));
        }catch (Exception e){

        }

    }

    @Test
    public void AreaToJSON(){
        try {
            Connection connection = SqliteJDBC.createConnection("jdbc:sqlite:E:\\code\\vs\\SmartWaterCloud\\GZFCApplication\\Debug\\Data\\DB\\localDB");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from 行政区划");
            List<Map<String,Object>> list = new ArrayList();
            while (rs.next()) {
                Map<String,Object> map = new HashMap<>();
                map.put("id",rs.getInt("id"));
                map.put("name",rs.getString("name"));
                map.put("level",rs.getString("level"));
                map.put("centerx",rs.getString("centerx"));
                map.put("centery",rs.getString("centery"));
                map.put("minx",rs.getString("minx"));
                map.put("miny",rs.getString("miny"));
                map.put("maxx",rs.getString("maxx"));
                map.put("maxy",rs.getString("maxy"));
                map.put("parent",rs.getInt("parent"));
                map.put("altitude",rs.getDouble("altitude"));
                map.put("adcd",rs.getString("adcd"));
                list.add(map);
            }
            System.out.println(JSON.toJSONString(list));
        }catch (Exception e){

        }

    }
}
