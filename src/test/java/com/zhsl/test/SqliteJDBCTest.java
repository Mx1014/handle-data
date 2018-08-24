package com.zhsl.test;

import com.alibaba.fastjson.JSON;
import com.zhsl.data.entity.VideoVo;
import com.zhsl.data.jdbc.sqlite.SqliteJDBC;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
}
