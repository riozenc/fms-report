package org.fms.report.common.webapp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ArrearageBean {

    // 应收凭证号
    private String arrearageNo;
    // 计量点ID
    private Long meterId;
    // 收费截止日期
    private Date endDate;
    // 应收电费
    private BigDecimal receivable;
    // 滞纳金
    private BigDecimal punishMoney;
    // 计费电量
    private BigDecimal totalPower;
    private Date createDate;
    private String operator;
    private Integer status;
    // 电费年月
    private Integer mon;
    // 电费年月
    private Integer sn;
    // 明细id
    private Long meterMoneyId;
    // 是否结清
    private Integer isSettle;
    // 欠费
    private BigDecimal oweMoney;

    private String meterNo;
    private String meterName;
    private Long writeSectId;
    private String writeSectNo;
    private String writeSectName;
    private String userNo;
    private String userName;
    private Long businessPlaceCode;
    private String deptName;
    private String setAddress;
    private Long writorId;

    private Long userId;
    private Byte userType;//
    private Integer elecTypeCode; // 用电类别
    private String bankNo;// 银行卡号
    private String settlementPhone;// 结算人电话
    private Byte connectBank;// 联网银行
    private Byte chargeModeType;// 收费方式
    private int count; // 欠费期数
    private Long settlementNo;
    private String settlementName;
    private String writerName;
    private String businessPlaceName;


    public String getArrearageNo() {
        return arrearageNo;
    }

    public void setArrearageNo(String arrearageNo) {
        this.arrearageNo = arrearageNo;
    }

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getReceivable() {
        return receivable;
    }

    public void setReceivable(BigDecimal receivable) {
        this.receivable = receivable;
    }

    public BigDecimal getPunishMoney() {
        return punishMoney;
    }

    public void setPunishMoney(BigDecimal punishMoney) {
        this.punishMoney = punishMoney;
    }

    public BigDecimal getTotalPower() {
        return totalPower;
    }

    public void setTotalPower(BigDecimal totalPower) {
        this.totalPower = totalPower;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Long getMeterMoneyId() {
        return meterMoneyId;
    }

    public void setMeterMoneyId(Long meterMoneyId) {
        this.meterMoneyId = meterMoneyId;
    }

    public Integer getIsSettle() {
        return isSettle;
    }

    public void setIsSettle(Integer isSettle) {
        this.isSettle = isSettle;
    }

    public BigDecimal getOweMoney() {
        return oweMoney;
    }

    public void setOweMoney(BigDecimal oweMoney) {
        this.oweMoney = oweMoney;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    public String getWriteSectNo() {
        return writeSectNo;
    }

    public void setWriteSectNo(String writeSectNo) {
        this.writeSectNo = writeSectNo;
    }

    public String getWriteSectName() {
        return writeSectName;
    }

    public void setWriteSectName(String writeSectName) {
        this.writeSectName = writeSectName;
    }

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

    public Long getBusinessPlaceCode() {
        return businessPlaceCode;
    }

    public void setBusinessPlaceCode(Long businessPlaceCode) {
        this.businessPlaceCode = businessPlaceCode;
    }

    public String getSetAddress() {
        return setAddress;
    }

    public void setSetAddress(String setAddress) {
        this.setAddress = setAddress;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Long getWritorId() {
        return writorId;
    }

    public void setWritorId(Long writorId) {
        this.writorId = writorId;
    }

    public Long getWriteSectId() {
        return writeSectId;
    }

    public void setWriteSectId(Long writeSectId) {
        this.writeSectId = writeSectId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getUserType() {
        return userType;
    }

    public void setUserType(Byte userType) {
        this.userType = userType;
    }

    public Integer getElecTypeCode() {
        return elecTypeCode;
    }

    public void setElecTypeCode(Integer elecTypeCode) {
        this.elecTypeCode = elecTypeCode;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getSettlementPhone() {
        return settlementPhone;
    }

    public void setSettlementPhone(String settlementPhone) {
        this.settlementPhone = settlementPhone;
    }

    public Byte getConnectBank() {
        return connectBank;
    }

    public void setConnectBank(Byte connectBank) {
        this.connectBank = connectBank;
    }

    public Byte getChargeModeType() {
        return chargeModeType;
    }

    public void setChargeModeType(Byte chargeModeType) {
        this.chargeModeType = chargeModeType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Long getSettlementNo() {
        return settlementNo;
    }

    public void setSettlementNo(Long settlementNo) {
        this.settlementNo = settlementNo;
    }

    public String getSettlementName() {
        return settlementName;
    }

    public void setSettlementName(String settlementName) {
        this.settlementName = settlementName;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getBusinessPlaceName() {
        return businessPlaceName;
    }

    public void setBusinessPlaceName(String businessPlaceName) {
        this.businessPlaceName = businessPlaceName;
    }
}
