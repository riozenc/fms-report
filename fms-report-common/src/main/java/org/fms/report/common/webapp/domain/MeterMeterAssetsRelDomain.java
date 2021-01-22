package org.fms.report.common.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeterMeterAssetsRelDomain  extends ManagerParamEntity{
    private Long id;// ID ID bigint TRUE FALSE TRUE
    // 计量点ID
    private Long meterId;
    private Long meterAssetsId;// 电能表ID
    private Integer mon;// 电费年月
    private Long meterOrder;// 表序号
    private Byte functionCode;// 功能代码
    private Byte powerDirection;// 功率方向
    private Byte phaseSeq;// 相序--（ABCD）
    private Long writeSn;
    private BigDecimal factorNum;
    private List meterIds;
    private List<Long> meterAssetsIds;
    private Integer meterSn;

    public Integer getMeterSn() {
        return meterSn;
    }

    public void setMeterSn(Integer meterSn) {
        this.meterSn = meterSn;
    }

    public List<Long> getMeterAssetsIds() {
        return meterAssetsIds;
    }

    public void setMeterAssetsIds(List<Long> meterAssetsIds) {
        this.meterAssetsIds = meterAssetsIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public Long getMeterAssetsId() {
        return meterAssetsId;
    }

    public void setMeterAssetsId(Long meterAssetsId) {
        this.meterAssetsId = meterAssetsId;
    }

    public Integer getMon() {
        return mon;
    }

    public void setMon(Integer mon) {
        this.mon = mon;
    }

    public Long getMeterOrder() {
        return meterOrder;
    }

    public void setMeterOrder(Long meterOrder) {
        this.meterOrder = meterOrder;
    }

    public Byte getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(Byte functionCode) {
        this.functionCode = functionCode;
    }

    public Byte getPowerDirection() {
        return powerDirection;
    }

    public void setPowerDirection(Byte powerDirection) {
        this.powerDirection = powerDirection;
    }

    public Byte getPhaseSeq() {
        return phaseSeq;
    }

    public void setPhaseSeq(Byte phaseSeq) {
        this.phaseSeq = phaseSeq;
    }

    public BigDecimal getFactorNum() {
        return factorNum;
    }

    public void setFactorNum(BigDecimal factorNum) {
        this.factorNum = factorNum;
    }

    public List getMeterIds() {
        return meterIds;
    }

    public void setMeterIds(List meterIds) {
        this.meterIds = meterIds;
    }

    public Long getWriteSn() {
        return writeSn;
    }

    public void setWriteSn(Long writeSn) {
        this.writeSn = writeSn;
    }
}
