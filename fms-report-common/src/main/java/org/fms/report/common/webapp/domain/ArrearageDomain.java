package org.fms.report.common.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.mybatis.MybatisEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

//欠费单
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArrearageDomain extends ParamDomain implements MybatisEntity {
    private Long id;
    // 应收凭证号
    private String arrearageNo;
    // 计量点ID
    private Long meterId;
    private Timestamp startDate;
    // 收费截止日期
    private Date endDate;
    private BigDecimal totalPower;
    private BigDecimal qTotalPower;
    // 应收电费
    private BigDecimal receivable;

    private BigDecimal punishMoney;
    private Date createDate;
    private String operator;
    private Integer status;
    // 电费年月
//    private Integer mon;
    // 电费年月
    private Integer sn;
    // 明细id
    private Long meterMoneyId;
    // 是否结清
    private Integer isSettle;
    // 欠费
    private BigDecimal oweMoney;
//    private Long writeSectId;// 抄表区段ID
//    private Long businessPlaceCode;// 营业区域ID

    private String meterNo;
    private String meterName;
    private String writeSectNo; //
    private String writeSectName; //


    private Long settlementId;


    private Short isLock;


    private Long userId;
    private String userNo;
    private String userName;
    private Byte userType;//
    private String deptName;
    private String setAddress;
    private Long settlementNo;
    private String tg;
    private String settlementName;
    private BigDecimal cos;// 功率因数 COS decimal(5,2) 5 2 FALSE FALSE FALSE
    private BigDecimal lastBalance;
    private BigDecimal thisBalance;//本次结余
    private BigDecimal calCapacity;//计费容量
    private BigDecimal surcharges;// 附加费/价调基金 SURCHARGES decimal(14,4) 14 4 FALSE FALSE FALSE
    private BigDecimal activeDeductionPower;// 有功扣表电量 ACTIVE_DEDUCTION_POWER decimal(12,2) 12 2 FALSE FALSE FALSE
    private BigDecimal reactiveDeductionPower;// 无功扣表电量 REACTIVE_DEDUCTION_POWER decimal(12,2) 12 2 FALSE FALSE FALSE
    private BigDecimal basicMoney;// 基本电费 BASIC_MONEY decimal(14,4) 14 4 FALSE FALSE FALSE
    private String writerName;
    private String operatorName;


    private Integer elecTypeCode; // 用电类别
    private String bankNo;// 银行卡号
    private String settlementPhone;// 结算人电话
    private Byte connectBank;// 联网银行
    private Byte chargeModeType;// 收费方式
    private int count; // 欠费期数


    //报表
    private List<Integer> mons;
    private List<Long> businessPlaceCodes;
    private List<Long> writeSectIds;
    private List<Long> writorIds;
    private List<Long> meterIds;
    private List<Long> settlementIds;

//    private Boolean isAgainStat; // 是否重新统计
    //    private String groupBy; // 分组条件
    private Long priceTypeId;
    private BigDecimal price;
    private String lastWriteDate;
    private String writeDate;
    private BigDecimal factorNum;
    BigDecimal startNum;//起码
    BigDecimal endNum;//止码
    BigDecimal activeWritePower;// 有功抄见电量 ACTIVE_WRITE_POWER decimal(12,2) 12 2 FALSE FALSE FALSE
    BigDecimal addPower;
    private BigDecimal activeLineLossPower;// 有功线损电量 ACTIVE_LINE_LOSS_POWER decimal(12,2) 12 2 FALSE FALSE FALSE
    private BigDecimal reactiveLineLossPower;// 无功线损电量 REACTIVE_LINE_LOSS_POWER decimal(12,2) 12 2 FALSE FALSE FALSE
    private BigDecimal activeTransformerLossPower;// 有功变损电量 ACTIVE_TRANSFORMER_LOSS_POWER decimal(12,2) 12 2 FALSE FALSE
    private Integer arrearsNum;//欠费期数
    private BigDecimal powerRateMoney;// 力调电费 POWER_RATE_MONEY decimal(14,4) 14 4 FALSE FALSE FALSE

    private String cosStdCodeName;//利率标准名称
    private String cosFlagName;//利率标准数值名称
    private String priceName;//电价名称

    BigDecimal factPunish;//违约金
    BigDecimal factTotal;
    private String name;
    private Integer functionCode;
    private BigDecimal chgPower;
    private BigDecimal cosRate;


    private BigDecimal deductionBalance;


    //meterMoney
    private BigDecimal ladder1Limit;
    private BigDecimal ladder1Power;
    private BigDecimal ladder1Money;
    private BigDecimal ladder2Limit;
    private BigDecimal ladder2Power;
    private BigDecimal ladder2Money;
    private BigDecimal ladder3Limit;
    private BigDecimal ladder3Power;
    private BigDecimal ladder3Money;
    private BigDecimal ladder4Limit;
    private BigDecimal ladder4Power;
    private BigDecimal ladder4Money;
    private BigDecimal volumeCharge;
    private BigDecimal refundMoney;
    private BigDecimal addMoney8;

    private Integer year;

    private String chargeModeName;

    private Long groupKey;

    public BigDecimal getAddMoney8() {
        return addMoney8;
    }

    public void setAddMoney8(BigDecimal addMoney8) {
        this.addMoney8 = addMoney8;
    }

    public String getChargeModeName() {
        return chargeModeName;
    }

    public void setChargeModeName(String chargeModeName) {
        this.chargeModeName = chargeModeName;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(Long settlementId) {
        this.settlementId = settlementId;
    }

    public BigDecimal getDeductionBalance() {
        if (deductionBalance == null) {
            return BigDecimal.ZERO;
        } else {
            return deductionBalance.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setDeductionBalance(BigDecimal deductionBalance) {
        this.deductionBalance = deductionBalance;
    }

    public BigDecimal getRefundMoney() {
        if (refundMoney == null) {
            return BigDecimal.ZERO;
        } else {
            return refundMoney.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setRefundMoney(BigDecimal refundMoney) {
        this.refundMoney = refundMoney;
    }

    public BigDecimal getCosRate() {
        return cosRate;
    }

    public void setCosRate(BigDecimal cosRate) {
        this.cosRate = cosRate;
    }

    public BigDecimal getVolumeCharge() {
        return volumeCharge;
    }

    public void setVolumeCharge(BigDecimal volumeCharge) {
        this.volumeCharge = volumeCharge;
    }

    public BigDecimal getChgPower() {
        return chgPower;
    }

    public void setChgPower(BigDecimal chgPower) {
        this.chgPower = chgPower;
    }

    public Integer getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(Integer functionCode) {
        this.functionCode = functionCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getSettlementIds() {
        return settlementIds;
    }

    public void setSettlementIds(List<Long> settlementIds) {
        this.settlementIds = settlementIds;
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

    public BigDecimal getLadder1Money() {
        return ladder1Money;
    }

    public void setLadder1Money(BigDecimal ladder1Money) {
        this.ladder1Money = ladder1Money;
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

    public BigDecimal getLadder2Money() {
        return ladder2Money;
    }

    public void setLadder2Money(BigDecimal ladder2Money) {
        this.ladder2Money = ladder2Money;
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

    public BigDecimal getLadder3Money() {
        return ladder3Money;
    }

    public void setLadder3Money(BigDecimal ladder3Money) {
        this.ladder3Money = ladder3Money;
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

    public BigDecimal getLadder4Money() {
        return ladder4Money;
    }

    public void setLadder4Money(BigDecimal ladder4Money) {
        this.ladder4Money = ladder4Money;
    }

    public BigDecimal getFactTotal() {
        return factTotal;
    }

    public void setFactTotal(BigDecimal factTotal) {
        this.factTotal = factTotal;
    }

    public BigDecimal getFactPunish() {
        return factPunish;
    }

    public void setFactPunish(BigDecimal factPunish) {
        this.factPunish = factPunish;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getThisBalance() {
        return thisBalance;
    }

    public void setThisBalance(BigDecimal thisBalance) {
        this.thisBalance = thisBalance;
    }

    public String getCosStdCodeName() {
        return cosStdCodeName;
    }

    public void setCosStdCodeName(String cosStdCodeName) {
        this.cosStdCodeName = cosStdCodeName;
    }

    public String getCosFlagName() {
        return cosFlagName;
    }

    public void setCosFlagName(String cosFlagName) {
        this.cosFlagName = cosFlagName;
    }

    public BigDecimal getPowerRateMoney() {
        return powerRateMoney;
    }

    public void setPowerRateMoney(BigDecimal powerRateMoney) {
        this.powerRateMoney = powerRateMoney;
    }

    public Integer getArrearsNum() {
        return arrearsNum;
    }

    public void setArrearsNum(Integer arrearsNum) {
        this.arrearsNum = arrearsNum;
    }

    public BigDecimal getActiveWritePower() {
        return activeWritePower;
    }

    public void setActiveWritePower(BigDecimal activeWritePower) {
        this.activeWritePower = activeWritePower;
    }

    public BigDecimal getAddPower() {
        return addPower;
    }

    public void setAddPower(BigDecimal addPower) {
        this.addPower = addPower;
    }

    public BigDecimal getActiveLineLossPower() {
        return activeLineLossPower;
    }

    public void setActiveLineLossPower(BigDecimal activeLineLossPower) {
        this.activeLineLossPower = activeLineLossPower;
    }

    public BigDecimal getReactiveLineLossPower() {
        return reactiveLineLossPower;
    }

    public void setReactiveLineLossPower(BigDecimal reactiveLineLossPower) {
        this.reactiveLineLossPower = reactiveLineLossPower;
    }

    public BigDecimal getActiveTransformerLossPower() {
        return activeTransformerLossPower;
    }

    public void setActiveTransformerLossPower(BigDecimal activeTransformerLossPower) {
        this.activeTransformerLossPower = activeTransformerLossPower;
    }


    public BigDecimal getFactorNum() {
        return factorNum;
    }

    public void setFactorNum(BigDecimal factorNum) {
        this.factorNum = factorNum;
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

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public BigDecimal getBasicMoney() {
        return basicMoney;
    }

    public void setBasicMoney(BigDecimal basicMoney) {
        this.basicMoney = basicMoney;
    }

    public BigDecimal getqTotalPower() {
        return qTotalPower;
    }

    public void setqTotalPower(BigDecimal qTotalPower) {
        this.qTotalPower = qTotalPower;
    }

    public BigDecimal getActiveDeductionPower() {
        return activeDeductionPower;
    }

    public void setActiveDeductionPower(BigDecimal activeDeductionPower) {
        this.activeDeductionPower = activeDeductionPower;
    }

    public BigDecimal getReactiveDeductionPower() {
        return reactiveDeductionPower;
    }

    public void setReactiveDeductionPower(BigDecimal reactiveDeductionPower) {
        this.reactiveDeductionPower = reactiveDeductionPower;
    }

    public String getLastWriteDate() {
        return lastWriteDate;
    }

    public void setLastWriteDate(String lastWriteDate) {
        this.lastWriteDate = lastWriteDate;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }

    public Long getSettlementNo() {
        return settlementNo;
    }

    public void setSettlementNo(Long settlementNo) {
        this.settlementNo = settlementNo;
    }

    public String getTg() {
        return tg;
    }

    public void setTg(String tg) {
        this.tg = tg;
    }

    public String getSettlementName() {
        return settlementName;
    }

    public void setSettlementName(String settlementName) {
        this.settlementName = settlementName;
    }

    public BigDecimal getQTotalPower() {
        return qTotalPower;
    }

    public void setQTotalPower(BigDecimal qTotalPower) {
        this.qTotalPower = qTotalPower;
    }

    public BigDecimal getCos() {
        return cos;
    }

    public void setCos(BigDecimal cos) {
        this.cos = cos;
    }

    public BigDecimal getLastBalance() {
        return lastBalance;
    }

    public void setLastBalance(BigDecimal lastBalance) {
        this.lastBalance = lastBalance;
    }

    public BigDecimal getCalCapacity() {
        return calCapacity;
    }

    public void setCalCapacity(BigDecimal calCapacity) {
        this.calCapacity = calCapacity;
    }

    public BigDecimal getSurcharges() {
        return surcharges;
    }

    public void setSurcharges(BigDecimal surcharges) {
        this.surcharges = surcharges;
    }

    public Short getIsLock() {
        return isLock;
    }

    public void setIsLock(Short isLock) {
        this.isLock = isLock;
    }


    public List<Long> getWriteSectIds() {
        return writeSectIds;
    }

    public void setWriteSectIds(List<Long> writeSectIds) {
        this.writeSectIds = writeSectIds;
    }

    private List writeSectionIds;

    private Date cutDate;
    //统计方式
    private int moy;


    public Long getPriceTypeId() {
        return priceTypeId;
    }

    public void setPriceTypeId(Long priceTypeId) {
        this.priceTypeId = priceTypeId;
    }

    public BigDecimal getPrice() {
        if (price == null) {
            return BigDecimal.ZERO;
        } else {
            return price;
        }
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

//    public Integer getMon() {
//        return mon;
//    }
//
//    public void setMon(Integer mon) {
//        this.mon = mon;
//    }

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
        if (oweMoney == null) {
            return BigDecimal.ZERO;
        } else {
            return oweMoney.setScale(2, RoundingMode.HALF_UP);
        }
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


    public List<Long> getWritorIds() {
        return writorIds;
    }

    public void setWritorIds(List<Long> writorIds) {
        this.writorIds = writorIds;
    }

//    public Long getWriteSectId() {
//        return writeSectId;
//    }
//
//    public void setWriteSectId(Long writeSectId) {
//        this.writeSectId = writeSectId;
//    }
//
//    public Long getBusinessPlaceCode() {
//        return businessPlaceCode;
//    }
//
//    public void setBusinessPlaceCode(Long businessPlaceCode) {
//        this.businessPlaceCode = businessPlaceCode;
//    }


    public List<Integer> getMons() {
        return mons;
    }

    public void setMons(List<Integer> mons) {
        this.mons = mons;
    }

    public List getWriteSectionIds() {
        return writeSectionIds;
    }

    public void setWriteSectionIds(List writeSectionIds) {
        this.writeSectionIds = writeSectionIds;
    }

    public List<Long> getBusinessPlaceCodes() {
        return businessPlaceCodes;
    }

    public void setBusinessPlaceCodes(List<Long> businessPlaceCodes) {
        this.businessPlaceCodes = businessPlaceCodes;
    }

    public List<Long> getMeterIds() {
        return meterIds;
    }

    public void setMeterIds(List<Long> meterIds) {
        this.meterIds = meterIds;
    }

//    public Boolean getAgainStat() {
//        return isAgainStat;
//    }
//
//    public void setAgainStat(Boolean againStat) {
//        isAgainStat = againStat;
//    }

    public BigDecimal getTotalPower() {
        return totalPower;
    }

    public void setTotalPower(BigDecimal totalPower) {
        this.totalPower = totalPower;
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getSetAddress() {
        return setAddress;
    }

    public void setSetAddress(String setAddress) {
        this.setAddress = setAddress;
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

//    public String getGroupBy() {
//        return groupBy;
//    }
//
//    public void setGroupBy(String groupBy) {
//        this.groupBy = groupBy;
//    }

    public int getMoy() {
        return moy;
    }

    public void setMoy(int moy) {
        this.moy = moy;
    }

    public Date getCutDate() {
        return cutDate;
    }

    public void setCutDate(Date cutDate) {
        this.cutDate = cutDate;
    }

    public Long getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(Long groupKey) {
        this.groupKey = groupKey;
    }
}
