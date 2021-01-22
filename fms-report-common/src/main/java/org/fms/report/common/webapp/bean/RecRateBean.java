package org.fms.report.common.webapp.bean;

import java.math.BigDecimal;

public class RecRateBean {
    String name;
    long yshs ;
    BigDecimal ysje ;
    long sshs ;
    BigDecimal ssje ;
    long qfhs ;
    BigDecimal qfje ;
    BigDecimal recovery;
    String vName;

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getYshs() {
        return yshs;
    }


    public void setYshs(long yshs) {
        this.yshs = yshs;
    }

    public BigDecimal getYsje() {
        return ysje;
    }

    public void setYsje(BigDecimal ysje) {
        this.ysje = ysje;
    }

    public long getQfhs() {
        return qfhs;
    }

    public void setQfhs(long qfhs) {
        this.qfhs = qfhs;
    }

    public BigDecimal getQfje() {
        return qfje;
    }

    public void setQfje(BigDecimal qfje) {
        this.qfje = qfje;
    }

    public long getSshs() {
        return sshs;
    }

    public void setSshs(long sshs) {
        this.sshs = sshs;
    }

    public BigDecimal getSsje() {
        return ssje;
    }

    public void setSsje(BigDecimal ssje) {
        this.ssje = ssje;
    }

    public BigDecimal getRecovery() {
        return recovery;
    }

    public void setRecovery(BigDecimal recovery) {
        this.recovery = recovery;
    }
}
