package org.fms.report.common.webapp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MeterBean {

    private Integer meterOrder;
    private String msModeName;
    private String meterTypeName;
    private String priceTypeName;
    private String tsTypeName;
    private String cosTypeName;
    private String baseMoneyFlagName;
    private BigDecimal needIndex;
    private String transLostTypeName; // 变损分摊方式

    public Integer getMeterOrder() {
        return meterOrder;
    }

    public void setMeterOrder(Integer meterOrder) {
        this.meterOrder = meterOrder;
    }

    public String getMsModeName() {
        return msModeName;
    }

    public void setMsModeName(String msModeName) {
        this.msModeName = msModeName;
    }

    public String getMeterTypeName() {
        return meterTypeName;
    }

    public void setMeterTypeName(String meterTypeName) {
        this.meterTypeName = meterTypeName;
    }

    public String getPriceTypeName() {
        return priceTypeName;
    }

    public void setPriceTypeName(String priceTypeName) {
        this.priceTypeName = priceTypeName;
    }

    public String getTsTypeName() {
        return tsTypeName;
    }

    public void setTsTypeName(String tsTypeName) {
        this.tsTypeName = tsTypeName;
    }

    public String getCosTypeName() {
        return cosTypeName;
    }

    public void setCosTypeName(String cosTypeName) {
        this.cosTypeName = cosTypeName;
    }

    public String getBaseMoneyFlagName() {
        return baseMoneyFlagName;
    }

    public void setBaseMoneyFlagName(String baseMoneyFlagName) {
        this.baseMoneyFlagName = baseMoneyFlagName;
    }

    public BigDecimal getNeedIndex() {
        return needIndex;
    }

    public void setNeedIndex(BigDecimal needIndex) {
        this.needIndex = needIndex;
    }

    public String getTransLostTypeName() {
        return transLostTypeName;
    }

    public void setTransLostTypeName(String transLostTypeName) {
        this.transLostTypeName = transLostTypeName;
    }
}
