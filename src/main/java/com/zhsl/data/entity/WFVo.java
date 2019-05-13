package com.zhsl.data.entity;

/**
 * @author xiangjg
 * @date 2019/5/13 15:32
 */
public class WFVo {

    private int num;
    private String code;
    private String name;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "WFVo{" +
                "num=" + num +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
