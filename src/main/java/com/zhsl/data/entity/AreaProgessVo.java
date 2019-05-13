package com.zhsl.data.entity;

import java.math.BigDecimal;

/**
 * @author xiangjg
 * @date 2019/4/10 11:00
 */
public class AreaProgessVo {
    private String adcd;
    private String adnm;
    private BigDecimal family;
    private BigDecimal poorFamily;
    private BigDecimal num;
    private BigDecimal poorNum;
    private Integer process;

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

    public BigDecimal getFamily() {
        return family;
    }

    public void setFamily(BigDecimal family) {
        this.family = family;
    }

    public BigDecimal getPoorFamily() {
        return poorFamily;
    }

    public void setPoorFamily(BigDecimal poorFamily) {
        this.poorFamily = poorFamily;
    }

    public BigDecimal getNum() {
        return num;
    }

    public void setNum(BigDecimal num) {
        this.num = num;
    }

    public BigDecimal getPoorNum() {
        return poorNum;
    }

    public void setPoorNum(BigDecimal poorNum) {
        this.poorNum = poorNum;
    }

    public Integer getProcess() {
        return process;
    }

    public void setProcess(Integer process) {
        this.process = process;
    }

    @Override
    public String toString() {
        return "AreaProgessVo{" +
                "adcd='" + adcd + '\'' +
                ", adnm='" + adnm + '\'' +
                ", family=" + family +
                ", poorFamily=" + poorFamily +
                ", num=" + num +
                ", poorNum=" + poorNum +
                ", process=" + process +
                '}';
    }
}
