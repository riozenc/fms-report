package org.fms.report.common.webapp.bean;

import java.math.BigDecimal;

public class PowerBean {
    private String userNo;// 客户编号
    private Integer mon;// 电费年月
    private Byte sn;// 本月次数
    private String userName; // 客户名称
    private String address;// 用电地址
    private String writeSectNo;// 抄表区段
    private Long meterOrder;// 表序号
    private String writeSn;//抄表序号
    private Integer elecTypeCode; // 用电类别
    private Long priceType;//电价
    private BigDecimal diffNum;// 度差
    private BigDecimal lastDiffNum;// 上次度差
    private BigDecimal volatility; // 波动率

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public Integer getMon() {
        return mon;
    }

    public void setMon(Integer mon) {
        this.mon = mon;
    }

    public Byte getSn() {
        return sn;
    }

    public void setSn(Byte sn) {
        this.sn = sn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWriteSectNo() {
        return writeSectNo;
    }

    public void setWriteSectNo(String writeSectNo) {
        this.writeSectNo = writeSectNo;
    }

    public Long getMeterOrder() {
        return meterOrder;
    }

    public void setMeterOrder(Long meterOrder) {
        this.meterOrder = meterOrder;
    }

    public String getWriteSn() {
        return writeSn;
    }

    public void setWriteSn(String writeSn) {
        this.writeSn = writeSn;
    }

    public Integer getElecTypeCode() {
        return elecTypeCode;
    }

    public void setElecTypeCode(Integer elecTypeCode) {
        this.elecTypeCode = elecTypeCode;
    }

    public Long getPriceType() {
        return priceType;
    }

    public void setPriceType(Long priceType) {
        this.priceType = priceType;
    }

    public BigDecimal getDiffNum() {
        return diffNum;
    }

    public void setDiffNum(BigDecimal diffNum) {
        this.diffNum = diffNum;
    }

    public BigDecimal getLastDiffNum() {
        return lastDiffNum;
    }

    public void setLastDiffNum(BigDecimal lastDiffNum) {
        this.lastDiffNum = lastDiffNum;
    }

    public BigDecimal getVolatility() {
        return volatility;
    }

    public void setVolatility(BigDecimal volatility) {
        this.volatility = volatility;
    }
}
