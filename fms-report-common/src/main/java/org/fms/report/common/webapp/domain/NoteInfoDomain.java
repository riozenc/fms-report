package org.fms.report.common.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.annotation.TablePrimaryKey;
import com.riozenc.titanTool.mybatis.MybatisEntity;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class NoteInfoDomain extends ParamDomain implements MybatisEntity {
    @TablePrimaryKey
    private Long id;
    private Long chargeInfoId;
    private String notePrintNo;
    private Integer printMan;
    private Date printDate;
    private String address;
    private Integer connectBank;
    private String bankNo;
    private BigDecimal factMoney;
    private BigDecimal thisBalance;
    private BigDecimal lastBalance;
    private BigDecimal ladder1Limit;
    private BigDecimal ladder1Power;
    private BigDecimal ladder2Limit;
    private BigDecimal ladder2Power;
    private BigDecimal ladder3Limit;
    private BigDecimal ladder3Power;
    private BigDecimal ladder4Limit;
    private BigDecimal ladder4Power;
    private BigDecimal punishMoney;
    private BigDecimal powerRateMoney;
    private BigDecimal basicMoney;
    private BigDecimal arrears;
    private String settlementNo;
    private String settlementName;
    private Long settlementId;
    private Long priceTypeId;
    private String priceName;
    private String meterItem;
    private String noteFlowNo;
    private BigDecimal factPre;
    private Long meterId;
    private Long meterMoneyId;
    private Long ysTypeCode;
    private Long fChargeMode;
    private List<Long> ids;
    private Integer isPrint;
    private String printManName;
    private List<Long> chargeInfoIds;

    public Long getfChargeMode() {
        return fChargeMode;
    }

    public void setfChargeMode(Long fChargeMode) {
        this.fChargeMode = fChargeMode;
    }

    public List<Long> getChargeInfoIds() {
        return chargeInfoIds;
    }

    public void setChargeInfoIds(List<Long> chargeInfoIds) {
        this.chargeInfoIds = chargeInfoIds;
    }

    public String getPrintManName() {
        return printManName;
    }

    public void setPrintManName(String printManName) {
        this.printManName = printManName;
    }

    public Date getPrintDate() {
        return printDate;
    }

    public void setPrintDate(Date printDate) {
        this.printDate = printDate;
    }

    public Integer getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(Integer isPrint) {
        this.isPrint = isPrint;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public Long getYsTypeCode() {
        return ysTypeCode;
    }

    public void setYsTypeCode(Long ysTypeCode) {
        this.ysTypeCode = ysTypeCode;
    }

    public Long getMeterMoneyId() {
        return meterMoneyId;
    }

    public void setMeterMoneyId(Long meterMoneyId) {
        this.meterMoneyId = meterMoneyId;
    }

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public BigDecimal getFactPre() {
        return factPre;
    }

    public void setFactPre(BigDecimal factPre) {
        this.factPre = factPre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChargeInfoId() {
        return chargeInfoId;
    }

    public void setChargeInfoId(Long chargeInfoId) {
        this.chargeInfoId = chargeInfoId;
    }

    public String getNotePrintNo() {
        return notePrintNo;
    }

    public void setNotePrintNo(String notePrintNo) {
        this.notePrintNo = notePrintNo;
    }

    public Integer getPrintMan() {
        return printMan;
    }

    public void setPrintMan(Integer printMan) {
        this.printMan = printMan;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getConnectBank() {
        return connectBank;
    }

    public void setConnectBank(Integer connectBank) {
        this.connectBank = connectBank;
    }

    public String getBankNo() {
        if(bankNo==null){
            return "";
        }else{
            return bankNo;
        }
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public BigDecimal getFactMoney() {
        return factMoney;
    }

    public void setFactMoney(BigDecimal factMoney) {
        this.factMoney = factMoney;
    }

    public BigDecimal getThisBalance() {
        return thisBalance;
    }

    public void setThisBalance(BigDecimal thisBalance) {
        this.thisBalance = thisBalance;
    }

    public BigDecimal getLastBalance() {
        if(lastBalance==null){
            return BigDecimal.ZERO;
        }else{
            return lastBalance.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setLastBalance(BigDecimal lastBalance) {
        this.lastBalance = lastBalance;
    }

    public BigDecimal getLadder1Limit() {
        return ladder1Limit;
    }

    public void setLadder1Limit(BigDecimal ladder1Limit) {
        this.ladder1Limit = ladder1Limit;
    }

    public BigDecimal getLadder1Power() {
        return ladder1Power;
    }

    public void setLadder1Power(BigDecimal ladder1Power) {
        this.ladder1Power = ladder1Power;
    }

    public BigDecimal getLadder2Limit() {
        return ladder2Limit;
    }

    public void setLadder2Limit(BigDecimal ladder2Limit) {
        this.ladder2Limit = ladder2Limit;
    }

    public BigDecimal getLadder2Power() {
        return ladder2Power;
    }

    public void setLadder2Power(BigDecimal ladder2Power) {
        this.ladder2Power = ladder2Power;
    }

    public BigDecimal getLadder3Limit() {
        return ladder3Limit;
    }

    public void setLadder3Limit(BigDecimal ladder3Limit) {
        this.ladder3Limit = ladder3Limit;
    }

    public BigDecimal getLadder3Power() {
        return ladder3Power;
    }

    public void setLadder3Power(BigDecimal ladder3Power) {
        this.ladder3Power = ladder3Power;
    }

    public BigDecimal getLadder4Limit() {
        return ladder4Limit;
    }

    public void setLadder4Limit(BigDecimal ladder4Limit) {
        this.ladder4Limit = ladder4Limit;
    }

    public BigDecimal getLadder4Power() {
        return ladder4Power;
    }

    public void setLadder4Power(BigDecimal ladder4Power) {
        this.ladder4Power = ladder4Power;
    }

    public BigDecimal getPunishMoney() {
        return punishMoney;
    }

    public void setPunishMoney(BigDecimal punishMoney) {
        this.punishMoney = punishMoney;
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

    public BigDecimal getArrears() {
        return arrears;
    }

    public void setArrears(BigDecimal arrears) {
        this.arrears = arrears;
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

    public Long getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(Long settlementId) {
        this.settlementId = settlementId;
    }

    public Long getPriceTypeId() {
        return priceTypeId;
    }

    public void setPriceTypeId(Long priceTypeId) {
        this.priceTypeId = priceTypeId;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }

    public String getMeterItem() {
        return meterItem;
    }

    public void setMeterItem(String meterItem) {
        this.meterItem = meterItem;
    }

    public String getNoteFlowNo() {
        return noteFlowNo;
    }

    public void setNoteFlowNo(String noteFlowNo) {
        this.noteFlowNo = noteFlowNo;
    }
}
