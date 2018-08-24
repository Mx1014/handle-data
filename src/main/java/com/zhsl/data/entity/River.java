package com.zhsl.data.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xiangjg
 * @date 2018/7/4 8:37
 */
public class River implements Serializable {
    private String stcd;
    private Date tm;
    private String z;
    private String q;

    public String getStcd() {
        return stcd;
    }

    public void setStcd(String stcd) {
        this.stcd = stcd;
    }

    public Date getTm() {
        return tm;
    }

    public void setTm(Date tm) {
        this.tm = tm;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}
