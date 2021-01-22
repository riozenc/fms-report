package org.fms.report.common.webapp.bean;

import java.math.BigDecimal;

public class UnWriteBean {
    private String userNo;// 客户编号
    private String userName; // 客户名称
    private String address;// 用电地址
    private Integer mon;// 电费年月
    private Byte sn;// 本月次数
    private Long meterOrder;// 表序号
    private String writeSn;//抄表序号
    private Long meterAssetsId; // 电表资产ID
    private Long functionCode;//功能代码
    private Byte powerDirection;//功率方向
    private BigDecimal factorNum; // 综合倍率
    private Byte tsType; // 分时计费标准
    private BigDecimal lastEndNum;// 上次止码
    private String writeSectNo;// 抄表区段

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
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

    public Long getMeterAssetsId() {
        return meterAssetsId;
    }

    public void setMeterAssetsId(Long meterAssetsId) {
        this.meterAssetsId = meterAssetsId;
    }

    public Long getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(Long functionCode) {
        this.functionCode = functionCode;
    }

    public Byte getPowerDirection() {
        return powerDirection;
    }

    public void setPowerDirection(Byte powerDirection) {
        this.powerDirection = powerDirection;
    }

    public BigDecimal getFactorNum() {
        return factorNum;
    }

    public void setFactorNum(BigDecimal factorNum) {
        this.factorNum = factorNum;
    }

    public Byte getTsType() {
        return tsType;
    }

    public void setTsType(Byte tsType) {
        this.tsType = tsType;
    }

    public BigDecimal getLastEndNum() {
        return lastEndNum;
    }

    public void setLastEndNum(BigDecimal lastEndNum) {
        this.lastEndNum = lastEndNum;
    }

    public String getWriteSectNo() {
        return writeSectNo;
    }

    public void setWriteSectNo(String writeSectNo) {
        this.writeSectNo = writeSectNo;
    }
}
