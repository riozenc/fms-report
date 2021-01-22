package org.fms.report.common.webapp.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.annotation.TablePrimaryKey;
import com.riozenc.titanTool.mybatis.MybatisEntity;

import java.math.BigDecimal;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargeInfoDomain  extends ParamDomain implements MybatisEntity {
    @TablePrimaryKey
    private Long id;
    private Short sn;
    private String arrearageNo;
    private Long meterId;
    private BigDecimal deductionBalance;
    private BigDecimal arrears;
    private BigDecimal factMoney;
    private BigDecimal factPunish;
    private BigDecimal factPre;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Timestamp payDate;
    private Date inDate;
    private Short balanceFlag;
    private Short fChargeMode;
    private Long settlementId;
    private Short paidFlag;
    private String relaUserNo;
    private Long operator;
    private String remark;
    private Short status;
    private String flowNo;
    private String appNo;
    private Short jzFlag;
    private BigDecimal factTotal;
    private Long meterMoneyId;
    private Integer chargeMode;
    private BigDecimal thisBalance;
    private BigDecimal lastBalance;
    private List<Long> meterIds;
    //private Long businessPlaceCode;
    //额外
    private List<Integer> mons;
    //private String groupBy; // 分组方式
    //private Boolean isAgainStat; // 是否重新统计
    private Timestamp startDate;
    private Timestamp endDate;
    private List<Long> settlementIds;
    private String operatorName;

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public List<Long> getSettlementIds() {
        return settlementIds;
    }

    public void setSettlementIds(List<Long> settlementIds) {
        this.settlementIds = settlementIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getSn() {
        return sn;
    }

    public void setSn(Short sn) {
        this.sn = sn;
    }

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

    public BigDecimal getDeductionBalance() {
        return deductionBalance;
    }

    public void setDeductionBalance(BigDecimal deductionBalance) {
        this.deductionBalance = deductionBalance;
    }

    public BigDecimal getArrears() {
        return arrears;
    }

    public void setArrears(BigDecimal arrears) {
        this.arrears = arrears;
    }

    public BigDecimal getFactMoney() {
        return factMoney;
    }

    public void setFactMoney(BigDecimal factMoney) {
        this.factMoney = factMoney;
    }

    public BigDecimal getFactPunish() {
        return factPunish;
    }

    public void setFactPunish(BigDecimal factPunish) {
        this.factPunish = factPunish;
    }

    public BigDecimal getFactPre() {
        return factPre;
    }

    public void setFactPre(BigDecimal factPre) {
        this.factPre = factPre;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Timestamp payDate) {
        this.payDate = payDate;
    }

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public Short getBalanceFlag() {
        return balanceFlag;
    }

    public void setBalanceFlag(Short balanceFlag) {
        this.balanceFlag = balanceFlag;
    }

    public Short getfChargeMode() {
        return fChargeMode;
    }

    public void setfChargeMode(Short fChargeMode) {
        this.fChargeMode = fChargeMode;
    }

    public Long getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(Long settlementId) {
        this.settlementId = settlementId;
    }

    public Short getPaidFlag() {
        return paidFlag;
    }

    public void setPaidFlag(Short paidFlag) {
        this.paidFlag = paidFlag;
    }

    public String getRelaUserNo() {
        return relaUserNo;
    }

    public void setRelaUserNo(String relaUserNo) {
        this.relaUserNo = relaUserNo;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }

    public Short getJzFlag() {
        return jzFlag;
    }

    public void setJzFlag(Short jzFlag) {
        this.jzFlag = jzFlag;
    }

    public BigDecimal getFactTotal() {
        return factTotal;
    }

    public void setFactTotal(BigDecimal factTotal) {
        this.factTotal = factTotal;
    }

    public Long getMeterMoneyId() {
        return meterMoneyId;
    }

    public void setMeterMoneyId(Long meterMoneyId) {
        this.meterMoneyId = meterMoneyId;
    }

    public Integer getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(Integer chargeMode) {
        this.chargeMode = chargeMode;
    }

    public BigDecimal getThisBalance() {
        return thisBalance;
    }

    public void setThisBalance(BigDecimal thisBalance) {
        this.thisBalance = thisBalance;
    }

    public BigDecimal getLastBalance() {
        return lastBalance;
    }

    public void setLastBalance(BigDecimal lastBalance) {
        this.lastBalance = lastBalance;
    }

    public List<Long> getMeterIds() {
        return meterIds;
    }

    public void setMeterIds(List<Long> meterIds) {
        this.meterIds = meterIds;
    }

    /*public Long getBusinessPlaceCode() {
        return businessPlaceCode;
    }

    public void setBusinessPlaceCode(Long businessPlaceCode) {
        this.businessPlaceCode = businessPlaceCode;
    }*/

    public List<Integer> getMons() {
        return mons;
    }

    public void setMons(List<Integer> mons) {
        this.mons = mons;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChargeInfoDomain that = (ChargeInfoDomain) o;
        return id == that.id &&
                Objects.equals(super.getMon(), that.getMon()) &&
                Objects.equals(sn, that.sn) &&
                Objects.equals(arrearageNo, that.arrearageNo) &&
                Objects.equals(meterId, that.meterId) &&
                Objects.equals(deductionBalance, that.deductionBalance) &&
                Objects.equals(arrears, that.arrears) &&
                Objects.equals(factMoney, that.factMoney) &&
                Objects.equals(factPunish, that.factPunish) &&
                Objects.equals(factPre, that.factPre) &&
                Objects.equals(payDate, that.payDate) &&
                Objects.equals(inDate, that.inDate) &&
                Objects.equals(balanceFlag, that.balanceFlag) &&
                Objects.equals(fChargeMode, that.fChargeMode) &&
                Objects.equals(settlementId, that.settlementId) &&
                Objects.equals(paidFlag, that.paidFlag) &&
                Objects.equals(relaUserNo, that.relaUserNo) &&
                Objects.equals(operator, that.operator) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(status, that.status) &&
                Objects.equals(flowNo, that.flowNo) &&
                Objects.equals(appNo, that.appNo) &&
                Objects.equals(jzFlag, that.jzFlag) &&
                Objects.equals(factTotal, that.factTotal) &&
                Objects.equals(meterMoneyId, that.meterMoneyId) &&
                Objects.equals(chargeMode, that.chargeMode) &&
                Objects.equals(thisBalance, that.thisBalance) &&
                Objects.equals(lastBalance, that.lastBalance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, super.getMon(), sn, arrearageNo, meterId,
                deductionBalance, arrears, factMoney, factPunish, factPre, payDate, inDate, balanceFlag, fChargeMode, settlementId, paidFlag, relaUserNo, operator, remark, status, flowNo, appNo, jzFlag, factTotal, meterMoneyId, chargeMode, thisBalance, lastBalance);
    }
}
