package org.fms.report.common.webapp.bean;

import java.math.BigDecimal;

//托收凭证
public class BankCollectBean {
    private String settlementName;
    private String bankNo;
    private String bankName;
    private int year;
    private int mon;
    private int day;
    private BigDecimal oweMoney;
    private String moneyInWord;
    private int oweNum;
    private String companyBank;
    private String companyBankNo;

    public int getOweNum() {
        return oweNum;
    }

    public void setOweNum(int oweNum) {
        this.oweNum = oweNum;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMon() {
        return mon;
    }

    public void setMon(int mon) {
        this.mon = mon;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }


    public BigDecimal getOweMoney() {
        return oweMoney;
    }

    public void setOweMoney(BigDecimal oweMoney) {
        this.oweMoney = oweMoney;
    }

    public String getMoneyInWord() {
        return moneyInWord;
    }

    public void setMoneyInWord(String moneyInWord) {
        this.moneyInWord = moneyInWord;
    }


    public String getCompanyBank() {
        return companyBank;
    }

    public void setCompanyBank(String companyBank) {
        this.companyBank = companyBank;
    }

    public String getCompanyBankNo() {
        return companyBankNo;
    }

    public void setCompanyBankNo(String companyBankNo) {
        this.companyBankNo = companyBankNo;
    }
}
