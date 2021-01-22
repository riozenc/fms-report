package org.fms.report.common.webapp.bean;

import java.math.BigDecimal;

//电量电费排行 报表实体
public class ElectricityTariffRankBean {
    private String settlementNo;
    private String settlementName;
    private Integer startMon;
    private Integer endMon;
    private BigDecimal totalPower;
    private BigDecimal receivable;
    private Integer order;

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

    public Integer getStartMon() {
        return startMon;
    }

    public void setStartMon(Integer startMon) {
        this.startMon = startMon;
    }

    public Integer getEndMon() {
        return endMon;
    }

    public void setEndMon(Integer endMon) {
        this.endMon = endMon;
    }

    public BigDecimal getTotalPower() {
        return totalPower;
    }

    public void setTotalPower(BigDecimal totalPower) {
        this.totalPower = totalPower;
    }

    public BigDecimal getReceivable() {
        return receivable;
    }

    public void setReceivable(BigDecimal receivable) {
        this.receivable = receivable;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
