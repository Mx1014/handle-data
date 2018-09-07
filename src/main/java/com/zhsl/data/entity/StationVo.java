package com.zhsl.data.entity;

import java.io.Serializable;

/**
 * @author xiangjg
 * @date 2018/9/3 15:24
 */
public class StationVo implements Serializable {
    private String stcd;
    private String stnm;

    public StationVo(String stcd, String stnm) {
        this.stcd = stcd;
        this.stnm = stnm;
    }

    public String getStcd() {
        return stcd;
    }

    public void setStcd(String stcd) {
        this.stcd = stcd;
    }

    public String getStnm() {
        return stnm;
    }

    public void setStnm(String stnm) {
        this.stnm = stnm;
    }
}
