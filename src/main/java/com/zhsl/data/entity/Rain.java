package com.zhsl.data.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Rain implements Serializable {

    private String stcd;
    private Date tm;
    private BigDecimal drp;
    private BigDecimal pdr;

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

    public BigDecimal getDrp() {
        return drp;
    }

    public void setDrp(BigDecimal drp) {
        this.drp = drp;
    }

    public BigDecimal getPdr() {
        return pdr;
    }

    public void setPdr(BigDecimal pdr) {
        this.pdr = pdr;
    }

    @Override
    public String toString() {
        return "Rain{" +
                "stcd='" + stcd + '\'' +
                ", tm=" + tm +
                ", drp=" + drp +
                '}';
    }
}
