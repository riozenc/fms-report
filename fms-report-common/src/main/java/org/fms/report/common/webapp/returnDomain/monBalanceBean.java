package org.fms.report.common.webapp.returnDomain;

import com.riozenc.titanTool.mybatis.MybatisEntity;

import java.math.BigDecimal;

public class monBalanceBean implements MybatisEntity {

    private String settlementNo;
    private String settlementName;
    private String bankNo;
    private BigDecimal lastBalance;

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

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public BigDecimal getLastBalance() {
        return lastBalance;
    }

    public void setLastBalance(BigDecimal lastBalance) {
        this.lastBalance = lastBalance;
    }
}
