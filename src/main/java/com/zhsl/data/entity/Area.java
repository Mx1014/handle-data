package com.zhsl.data.entity;

/**
 * @author xiangjg
 * @date 2019/3/18 11:24
 */
public class Area {

    private String adcd;
    private String adnm;
    private String padcd;
    private Integer level;
    public Area() {
    }

    public Area(String adcd, String adnm, String padcd) {
        this.adcd = adcd;
        this.adnm = adnm;
        this.padcd = padcd;
    }

    public String getAdcd() {
        return adcd;
    }

    public void setAdcd(String adcd) {
        this.adcd = adcd;
    }

    public String getAdnm() {
        return adnm;
    }

    public void setAdnm(String adnm) {
        this.adnm = adnm;
    }

    public String getPadcd() {
        return padcd;
    }

    public void setPadcd(String padcd) {
        this.padcd = padcd;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Area{" +
                "adcd='" + adcd + '\'' +
                ", adnm='" + adnm + '\'' +
                ", padcd='" + padcd + '\'' +
                ", level=" + level +
                '}';
    }
}
