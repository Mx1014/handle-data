package com.zhsl.data.entity;

import java.io.Serializable;


public class Manual implements Serializable {

    private Integer id;
    private String name;
    private String fileName;
    private String filePath;
    private Integer pid;

    public Manual() {
    }

    public Manual(Integer id, String name, Integer pid) {
        this.id = id;
        this.name = name;
        this.pid = pid;
    }

    public Manual(Integer id, String name, String fileName, String filePath, Integer pid) {
        this.id = id;
        this.name = name;
        this.fileName = fileName;
        this.filePath = filePath;
        this.pid = pid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "Manual{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", pid=" + pid +
                '}';
    }
}
