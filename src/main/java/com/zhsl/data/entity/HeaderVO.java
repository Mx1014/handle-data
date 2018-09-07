package com.zhsl.data.entity;

import java.io.Serializable;

/**
 * @author xiangjg
 * @date 2018/9/3 16:15
 */
public class HeaderVO implements Serializable {
    private String filed;
    private String text;

    public HeaderVO(String filed, String text) {
        this.filed = filed;
        this.text = text;
    }

    public String getFiled() {
        return filed;
    }

    public void setFiled(String filed) {
        this.filed = filed;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
