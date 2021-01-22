package org.fms.report.common.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

//银行托收参数实体
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankCollectionEntity {
    private Long settlementId;
    private String settlementNo;
    private String settlementName;
    private Integer mon;
    private Integer sn;
    private String bankNo;
    private Long opendingBank;
    private String opendingBankName;

    private BigDecimal oweMoney;
    private BigDecimal punishMoney;
    private BigDecimal collectTotalMoney;
    private Long writeSectId;
    private String writeSectNo;
    private Long businessPlaceCode;
    private Long connectBank;
    private String connectBankName;
    //单位总行名称
    private String dwBankNo;
    private Long businssPlaceCode;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDwBankNo() {
        return dwBankNo;
    }

    public void setDwBankNo(String dwBankNo) {
        this.dwBankNo = dwBankNo;
    }

    public Long getBusinssPlaceCode() {
        return businssPlaceCode;
    }

    public void setBusinssPlaceCode(Long businssPlaceCode) {
        this.businssPlaceCode = businssPlaceCode;
    }

    public Long getOpendingBank() {
        return opendingBank;
    }

    public void setOpendingBank(Long opendingBank) {
        this.opendingBank = opendingBank;
    }

    public String getOpendingBankName() {
        return opendingBankName;
    }

    public void setOpendingBankName(String opendingBankName) {
        this.opendingBankName = opendingBankName;
    }

    public String getConnectBankName() {
        return connectBankName;
    }

    public void setConnectBankName(String connectBankName) {
        this.connectBankName = connectBankName;
    }

    public Long getConnectBank() {
        return connectBank;
    }

    public void setConnectBank(Long connectBank) {
        this.connectBank = connectBank;
    }


    public Long getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(Long settlementId) {
        this.settlementId = settlementId;
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

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }


    public BigDecimal getOweMoney() {
        return oweMoney;
    }

    public void setOweMoney(BigDecimal oweMoney) {
        this.oweMoney = oweMoney;
    }

    public BigDecimal getPunishMoney() {
        return punishMoney;
    }

    public void setPunishMoney(BigDecimal punishMoney) {
        this.punishMoney = punishMoney;
    }

    public BigDecimal getCollectTotalMoney() {
        return collectTotalMoney;
    }

    public void setCollectTotalMoney(BigDecimal collectTotalMoney) {
        this.collectTotalMoney = collectTotalMoney;
    }

    public Long getWriteSectId() {
        return writeSectId;
    }

    public void setWriteSectId(Long writeSectId) {
        this.writeSectId = writeSectId;
    }

    public String getWriteSectNo() {
        return writeSectNo;
    }

    public void setWriteSectNo(String writeSectNo) {
        this.writeSectNo = writeSectNo;
    }

    public Long getBusinessPlaceCode() {
        return businessPlaceCode;
    }

    public void setBusinessPlaceCode(Long businessPlaceCode) {
        this.businessPlaceCode = businessPlaceCode;
    }
}
