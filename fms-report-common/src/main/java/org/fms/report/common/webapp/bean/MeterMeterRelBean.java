package org.fms.report.common.webapp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MeterMeterRelBean {

    private String pUserNo;
    private String pUserName;
    private Integer pMeterOrder;
    private String userNo;
    private Integer meterOrder;
    private String userName;
    private String meterRelationType;

    public String getpUserNo() {
        return pUserNo;
    }

    public void setpUserNo(String pUserNo) {
        this.pUserNo = pUserNo;
    }

    public String getpUserName() {
        return pUserName;
    }

    public void setpUserName(String pUserName) {
        this.pUserName = pUserName;
    }


    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public Integer getpMeterOrder() {
        return pMeterOrder;
    }

    public void setpMeterOrder(Integer pMeterOrder) {
        this.pMeterOrder = pMeterOrder;
    }

    public Integer getMeterOrder() {
        return meterOrder;
    }

    public void setMeterOrder(Integer meterOrder) {
        this.meterOrder = meterOrder;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMeterRelationType() {
        return meterRelationType;
    }

    public void setMeterRelationType(String meterRelationType) {
        this.meterRelationType = meterRelationType;
    }
}
