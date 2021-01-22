package org.fms.report.common.webapp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.mybatis.MybatisEntity;

import java.math.BigDecimal;

//银行托收清单参数实体
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankCollectionDetailBean {
    private String settlementNo;
    private String settlementName;
    private String businessPlaceCodeName;
    private String address;
    private Integer mon;
    private Integer sn;
    private Integer meterSn;
    private BigDecimal startNum;
    private BigDecimal endNum;
    private String priceName;
    private Integer factorNum;
    private BigDecimal addPower;
    private BigDecimal activeTransformerLossPower;
    private BigDecimal totalPower;
    private BigDecimal powerRateMoney;
    private BigDecimal basicMoney;
    private BigDecimal volumeCharge;
    private BigDecimal refundMoney;
    private BigDecimal totalMoney;
    private Long meterId;
    private Long functionCode;
    private BigDecimal shouldMoney;

    public BigDecimal getShouldMoney() {
        return shouldMoney;
    }

    public void setShouldMoney(BigDecimal shouldMoney) {
        this.shouldMoney = shouldMoney;
    }

    public Long getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(Long functionCode) {
        this.functionCode = functionCode;
    }

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public String getSettlementNo() {
        return settlementNo;
    }

    public void setSettlementNo(String settlementNo) {
        this.settlementNo = settlementNo;
    }

    public String getSettlementName() {
        return settlementName;
    }

    public void setSettlementName(String settlementName) {
        this.settlementName = settlementName;
    }

    public String getBusinessPlaceCodeName() {
        return businessPlaceCodeName;
    }

    public void setBusinessPlaceCodeName(String businessPlaceCodeName) {
        this.businessPlaceCodeName = businessPlaceCodeName;
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

    public Integer getSn() {
        return sn;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
    }

    public Integer getMeterSn() {
        return meterSn;
    }

    public void setMeterSn(Integer meterSn) {
        this.meterSn = meterSn;
    }

    public BigDecimal getStartNum() {
        return startNum;
    }

    public void setStartNum(BigDecimal startNum) {
        this.startNum = startNum;
    }

    public BigDecimal getEndNum() {
        return endNum;
    }

    public void setEndNum(BigDecimal endNum) {
        this.endNum = endNum;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }

    public Integer getFactorNum() {
        return factorNum;
    }

    public void setFactorNum(Integer factorNum) {
        this.factorNum = factorNum;
    }

    public BigDecimal getAddPower() {
        return addPower;
    }

    public void setAddPower(BigDecimal addPower) {
        this.addPower = addPower;
    }

    public BigDecimal getActiveTransformerLossPower() {
        return activeTransformerLossPower;
    }

    public void setActiveTransformerLossPower(BigDecimal activeTransformerLossPower) {
        this.activeTransformerLossPower = activeTransformerLossPower;
    }

    public BigDecimal getTotalPower() {
        return totalPower;
    }

    public void setTotalPower(BigDecimal totalPower) {
        this.totalPower = totalPower;
    }

    public BigDecimal getPowerRateMoney() {
        return powerRateMoney;
    }

    public void setPowerRateMoney(BigDecimal powerRateMoney) {
        this.powerRateMoney = powerRateMoney;
    }

    public BigDecimal getBasicMoney() {
        return basicMoney;
    }

    public void setBasicMoney(BigDecimal basicMoney) {
        this.basicMoney = basicMoney;
    }

    public BigDecimal getVolumeCharge() {
        return volumeCharge;
    }

    public void setVolumeCharge(BigDecimal volumeCharge) {
        this.volumeCharge = volumeCharge;
    }

    public BigDecimal getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(BigDecimal refundMoney) {
        this.refundMoney = refundMoney;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }
}
