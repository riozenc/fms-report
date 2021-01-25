package org.fms.report.common.webapp.bean;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Date;

public class TableDataBean {

    public TableDataBean() {
    }

    private JRBeanCollectionDataSource tableData;

    private JRBeanCollectionDataSource tableData1;

    private JRBeanCollectionDataSource tableData2;

    private JRBeanCollectionDataSource tableData3;

    private JRBeanCollectionDataSource tableData4;

    private JRBeanCollectionDataSource tableData5;

    private JRBeanCollectionDataSource tableData6;

    private JRBeanCollectionDataSource tableData7;

    private String vName;

    private String vValue;

    private BigDecimal receivableSum;//本月电费

    private BigDecimal qTotalPowerSum;//总无功电量

    private BigDecimal cos;// 功率因数 COS decimal(5,2) 5 2 FALSE FALSE FALSE

    private BigDecimal totalPowerSum;//总有功电量

    private BigDecimal oweMoneySum;//本月欠费

    private BigDecimal lastBalance;//上次结余

    private BigDecimal thisBalance;//本次结余

    private BigDecimal factTotal;//实收总额


    private BigDecimal factPunish;

    // 月份
    private Integer mon;

    private Date date;

    // 电费年月-开始年月
    private Integer startMon;

    // 电费年月-结束年月
    private Integer endMon;

    private String printDate;
    private String Dept;
    private String startDate;
    private String endDate;
    private String lastWriteDate;//上次抄表日期
    private String writeDate;//本次抄表日期
    private String settlementNo;//结算编号
    private String tg;//台区
    private String settlementName;//结算户名称
    private String setAddress;//用电地址
    private BigDecimal calCapacity;//计费容量
    private BigDecimal surcharges;// 附加费/价调基金 SURCHARGES decimal(14,4) 14 4 FALSE FALSE FALSE
    private String writeSectName; //抄表段名称
    private BigDecimal deductionPowerSum;//扣表电量
    private BigDecimal chgPower;//换表电量
    private BigDecimal basicMoneySum;// 基本电费 BASIC_MONEY decimal(14,4) 14 4 FALSE FALSE FALSE
    private String moneyInWord;//金额大写
    private String writerName;//抄表员
    private String operatorName;//操作员
    private String userName;//当前操作员
    private BigDecimal factorNum;
    private String cosStdCode;
    private String cosFlag;
    private Integer arrearsNum;//欠费期数
    private BigDecimal powerRateMoney;// 力调电费 POWER_RATE_MONEY decimal(14,4) 14 4 FALSE FALSE FALSE
    private BigDecimal cosRate; //功率因数
    private String cosStdCodeName;//力率标准
    private String cosFlagName;//计算力调标志
    private BigDecimal refundMoney;

    private Long businessPlaceCode;
    private BigDecimal ladder1Limit;
    private BigDecimal ladder1Power;
    private BigDecimal ladder2Limit;
    private BigDecimal ladder2Power;
    private BigDecimal ladder3Limit;
    private BigDecimal ladder3Power;
    private String ladder4Limit;
    private BigDecimal ladder4Power;

    private String bankNo;
    private String bankName;
    private int year;
    private int day;
    private int oweNum;
    private String opendingName;
    private String connectName;
    private String dwBankNo;
    private String historyOweInfo;
    private BigDecimal addMoney8;
    private String businessPlaceCodeName;
    private String statisticalMethod;
    private Integer count;
    private BigDecimal oweMoney;//本月欠费
    private Long writorId;
    private Long writeSectId;
    private String userNo;
    private String voltLevelTypeName;
    private String msTypeName;
    private String phoneNum;
    private String ladderSn;
    private String lineName;
    private String stationName;
    private String lineLostName;//线损计算方式
    private String transLostName;//变损计算方式
    private String functionCodeName;
    private String madeNo;
    private String phaseName;
    private String ratedCurntCodeName;//标定电流
    private String ratedCtCodeName;
    private String ratedPtCodeName;
    private String assetsNo;
    private String price;
    private String modelName;
    private Long sn;
    private String writeSectNo;
    private String deskName;
    private String chargeModeName;
    private String userTypeName;
    private String writorName;
    private String linkMan;
    private Integer ladderNum;
    private String endNum;

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

    public BigDecimal getOweMoney() {
        return oweMoney;
    }

    public void setOweMoney(BigDecimal oweMoney) {
        this.oweMoney = oweMoney;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getStatisticalMethod() {
        return statisticalMethod;
    }

    public void setStatisticalMethod(String statisticalMethod) {
        this.statisticalMethod = statisticalMethod;
    }

    public String getBusinessPlaceCodeName() {
        return businessPlaceCodeName;
    }

    public void setBusinessPlaceCodeName(String businessPlaceCodeName) {
        this.businessPlaceCodeName = businessPlaceCodeName;
    }

    public BigDecimal getAddMoney8() {
        return addMoney8;
    }

    public void setAddMoney8(BigDecimal addMoney8) {
        this.addMoney8 = addMoney8;
    }

    public String getHistoryOweInfo() {
        return historyOweInfo;
    }

    public void setHistoryOweInfo(String historyOweInfo) {
        this.historyOweInfo = historyOweInfo;
    }

    public String getDwBankNo() {
        return dwBankNo;
    }

    public void setDwBankNo(String dwBankNo) {
        this.dwBankNo = dwBankNo;
    }

    public String getOpendingName() {
        return opendingName;
    }

    public void setOpendingName(String opendingName) {
        this.opendingName = opendingName;
    }

    public String getConnectName() {
        return connectName;
    }

    public void setConnectName(String connectName) {
        this.connectName = connectName;
    }

    public Long getBusinessPlaceCode() {
        return businessPlaceCode;
    }

    public void setBusinessPlaceCode(Long businessPlaceCode) {
        this.businessPlaceCode = businessPlaceCode;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }


    public int getOweNum() {
        return oweNum;
    }

    public void setOweNum(int oweNum) {
        this.oweNum = oweNum;
    }


    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getLadder1Limit() {
        if (ladder1Limit == null) {
            return BigDecimal.ZERO;
        } else {
            return ladder1Limit.setScale(0, RoundingMode.HALF_UP);
        }
    }

    public void setLadder1Limit(BigDecimal ladder1Limit) {
        this.ladder1Limit = ladder1Limit;
    }

    public BigDecimal getLadder1Power() {
        if (ladder1Power == null) {
            return BigDecimal.ZERO;
        } else {
            return ladder1Power.setScale(0, RoundingMode.HALF_UP);
        }
    }

    public void setLadder1Power(BigDecimal ladder1Power) {
        this.ladder1Power = ladder1Power;
    }

    public BigDecimal getLadder2Limit() {
        if (ladder2Limit == null) {
            return BigDecimal.ZERO;
        } else {
            return ladder2Limit.setScale(0, RoundingMode.HALF_UP);
        }
    }

    public void setLadder2Limit(BigDecimal ladder2Limit) {
        this.ladder2Limit = ladder2Limit;
    }

    public BigDecimal getLadder2Power() {
        if (ladder2Power == null) {
            return BigDecimal.ZERO;
        } else {
            return ladder2Power.setScale(0, RoundingMode.HALF_UP);
        }
    }

    public void setLadder2Power(BigDecimal ladder2Power) {
        this.ladder2Power = ladder2Power;
    }

    public BigDecimal getLadder3Limit() {
        if (ladder3Limit == null) {
            return BigDecimal.ZERO;
        } else {
            return ladder3Limit.setScale(0, RoundingMode.HALF_UP);
        }
    }

    public void setLadder3Limit(BigDecimal ladder3Limit) {
        this.ladder3Limit = ladder3Limit;
    }

    public BigDecimal getLadder3Power() {
        if (ladder3Power == null) {
            return BigDecimal.ZERO;
        } else {
            return ladder3Power.setScale(0, RoundingMode.HALF_UP);
        }
    }

    public void setLadder3Power(BigDecimal ladder3Power) {
        this.ladder3Power = ladder3Power;
    }

    public String getLadder4Limit() {
        return ladder4Limit;
    }

    public void setLadder4Limit(String ladder4Limit) {
        this.ladder4Limit = ladder4Limit;
    }

    public BigDecimal getLadder4Power() {
        if (ladder4Power == null) {
            return BigDecimal.ZERO;
        } else {
            return ladder4Power.setScale(0, RoundingMode.HALF_UP);
        }
    }

    public void setLadder4Power(BigDecimal ladder4Power) {
        this.ladder4Power = ladder4Power;
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
        if (cosRate == null) {
            return BigDecimal.ZERO;
        } else {
            return cosRate.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setCosRate(BigDecimal cosRate) {
        this.cosRate = cosRate;
    }

    public BigDecimal getChgPower() {
        return chgPower;
    }

    public void setChgPower(BigDecimal chgPower) {
        this.chgPower = chgPower;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getFactTotal() {
        if (factTotal == null) {
            return BigDecimal.ZERO;
        } else {
            return factTotal.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setFactTotal(BigDecimal factTotal) {
        this.factTotal = factTotal;
    }

    public BigDecimal getThisBalance() {
        if (thisBalance == null) {
            return BigDecimal.ZERO;
        } else {
            return thisBalance.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setThisBalance(BigDecimal thisBalance) {
        this.thisBalance = thisBalance;
    }

    public BigDecimal getFactPunish() {
        if (factPunish == null) {
            return BigDecimal.ZERO;
        } else {
            return factPunish.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setFactPunish(BigDecimal factPunish) {
        this.factPunish = factPunish;
    }

    public String getCosStdCodeName() {
        if (cosStdCodeName == null) {
            return "";
        } else {

            return cosStdCodeName;
        }
    }

    public void setCosStdCodeName(String cosStdCodeName) {
        this.cosStdCodeName = cosStdCodeName;
    }

    public String getCosFlagName() {
        if (cosFlagName == null) {
            return "";
        } else {
            return cosFlagName;
        }
    }

    public void setCosFlagName(String cosFlagName) {
        this.cosFlagName = cosFlagName;
    }

    public String getCosFlag() {
        return cosFlag;
    }

    public void setCosFlag(String cosFlag) {
        this.cosFlag = cosFlag;
    }

    public BigDecimal getPowerRateMoney() {
        if (powerRateMoney == null) {
            return BigDecimal.ZERO;
        } else {
            return powerRateMoney.setScale(2, RoundingMode.HALF_UP);
        }
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

    public BigDecimal getFactorNum() {
        return factorNum;
    }

    public void setFactorNum(BigDecimal factorNum) {
        this.factorNum = factorNum;
    }

    public String getCosStdCode() {
        return cosStdCode;
    }

    public void setCosStdCode(String cosStdCode) {
        this.cosStdCode = cosStdCode;
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

    public String getMoneyInWord() {
        return moneyInWord;
    }

    public void setMoneyInWord(String moneyInWord) {
        this.moneyInWord = moneyInWord;
    }

    public BigDecimal getBasicMoneySum() {
        if (basicMoneySum == null) {
            return BigDecimal.ZERO;
        } else {
            return basicMoneySum.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setBasicMoneySum(BigDecimal basicMoneySum) {
        this.basicMoneySum = basicMoneySum;
    }

    public BigDecimal getDeductionPowerSum() {
        return deductionPowerSum;
    }

    public void setDeductionPowerSum(BigDecimal deductionPowerSum) {
        this.deductionPowerSum = deductionPowerSum;
    }

    public BigDecimal getqTotalPowerSum() {
        return qTotalPowerSum;
    }

    public void setqTotalPowerSum(BigDecimal qTotalPowerSum) {
        this.qTotalPowerSum = qTotalPowerSum;
    }

    public String getLastWriteDate() {
        if (lastWriteDate == null || "".equals(lastWriteDate)) {
            return "";
        } else {
            return lastWriteDate;
        }
    }

    public void setLastWriteDate(String lastWriteDate) {
        this.lastWriteDate = lastWriteDate;
    }

    public String getWriteSectName() {
        return writeSectName;
    }

    public void setWriteSectName(String writeSectName) {
        this.writeSectName = writeSectName;
    }

    public String getWriteDate() {
        if (writeDate == null || "".equals(writeDate)) {
            return "";
        } else {
            return writeDate;
        }
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }

    public JRBeanCollectionDataSource getTableData() {
        return tableData;
    }

    public void setTableData(JRBeanCollectionDataSource tableData) {
        this.tableData = tableData;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public String getvValue() {
        return vValue;
    }

    public void setvValue(String vValue) {
        this.vValue = vValue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getMon() {
        return mon;
    }

    public void setMon(Integer mon) {
        this.mon = mon;
    }

    public BigDecimal getReceivableSum() {
        if (receivableSum == null) {
            return BigDecimal.ZERO;
        } else {
            return receivableSum.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setReceivableSum(BigDecimal receivableSum) {
        this.receivableSum = receivableSum;
    }

    public BigDecimal getTotalPowerSum() {
        if (totalPowerSum == null) {
            return BigDecimal.ZERO;
        } else {
            return totalPowerSum.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setTotalPowerSum(BigDecimal totalPowerSum) {
        this.totalPowerSum = totalPowerSum;
    }

    public BigDecimal getOweMoneySum() {
        if (oweMoneySum == null) {
            return BigDecimal.ZERO;
        } else {
            return oweMoneySum.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setOweMoneySum(BigDecimal oweMoneySum) {
        this.oweMoneySum = oweMoneySum;
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

    public String getPrintDate() {
        return printDate;
    }

    public void setPrintDate(String printDate) {
        this.printDate = printDate;
    }

    public String getDept() {
        return Dept;
    }

    public void setDept(String dept) {
        Dept = dept;
    }


    public BigDecimal getQTotalPowerSum() {
        return qTotalPowerSum;
    }

    public void setQTotalPowerSum(BigDecimal qTotalPowerSum) {
        this.qTotalPowerSum = qTotalPowerSum;
    }

    public BigDecimal getCos() {
        if (cos == null) {
            return BigDecimal.ZERO;
        } else {
            return cos.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setCos(BigDecimal cos) {
        this.cos = cos;
    }

    public BigDecimal getLastBalance() {
        if (lastBalance == null) {
            return BigDecimal.ZERO;
        } else {
            return lastBalance.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setLastBalance(BigDecimal lastBalance) {
        this.lastBalance = lastBalance;
    }

    public String getSettlementNo() {
        return settlementNo;
    }

    public void setSettlementNo(String settlementNo) {
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

    public String getSetAddress() {
        return setAddress;
    }

    public void setSetAddress(String setAddress) {
        this.setAddress = setAddress;
    }

    public BigDecimal getCalCapacity() {
        if (calCapacity == null) {
            return BigDecimal.ZERO;
        }
        return calCapacity;
    }

    public void setCalCapacity(BigDecimal calCapacity) {
        this.calCapacity = calCapacity;
    }

    public BigDecimal getSurcharges() {
        if (surcharges == null) {
            return BigDecimal.ZERO;
        } else {
            return surcharges.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setSurcharges(BigDecimal surcharges) {
        this.surcharges = surcharges;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getVoltLevelTypeName() {
        return voltLevelTypeName;
    }

    public void setVoltLevelTypeName(String voltLevelTypeName) {
        this.voltLevelTypeName = voltLevelTypeName;
    }

    public String getMsTypeName() {
        return msTypeName;
    }

    public void setMsTypeName(String msTypeName) {
        this.msTypeName = msTypeName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getLadderSn() {
        return ladderSn;
    }

    public void setLadderSn(String ladderSn) {
        this.ladderSn = ladderSn;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getLineLostName() {
        return lineLostName;
    }

    public void setLineLostName(String lineLostName) {
        this.lineLostName = lineLostName;
    }

    public String getTransLostName() {
        return transLostName;
    }

    public void setTransLostName(String transLostName) {
        this.transLostName = transLostName;
    }

    public String getFunctionCodeName() {
        return functionCodeName;
    }

    public void setFunctionCodeName(String functionCodeName) {
        this.functionCodeName = functionCodeName;
    }

    public String getMadeNo() {
        return madeNo;
    }

    public void setMadeNo(String madeNo) {
        this.madeNo = madeNo;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public String getRatedCurntCodeName() {
        return ratedCurntCodeName;
    }

    public void setRatedCurntCodeName(String ratedCurntCodeName) {
        this.ratedCurntCodeName = ratedCurntCodeName;
    }

    public String getRatedCtCodeName() {
        return ratedCtCodeName;
    }

    public void setRatedCtCodeName(String ratedCtCodeName) {
        this.ratedCtCodeName = ratedCtCodeName;
    }

    public String getRatedPtCodeName() {
        return ratedPtCodeName;
    }

    public void setRatedPtCodeName(String ratedPtCodeName) {
        this.ratedPtCodeName = ratedPtCodeName;
    }

    public String getAssetsNo() {
        return assetsNo;
    }

    public void setAssetsNo(String assetsNo) {
        this.assetsNo = assetsNo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Long getSn() {
        return sn;
    }

    public void setSn(Long sn) {
        this.sn = sn;
    }

    public String getWriteSectNo() {
        return writeSectNo;
    }

    public void setWriteSectNo(String writeSectNo) {
        this.writeSectNo = writeSectNo;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public String getChargeModeName() {
        return chargeModeName;
    }

    public void setChargeModeName(String chargeModeName) {
        this.chargeModeName = chargeModeName;
    }

    public String getUserTypeName() {
        return userTypeName;
    }

    public void setUserTypeName(String userTypeName) {
        this.userTypeName = userTypeName;
    }

    public String getWritorName() {
        return writorName;
    }

    public void setWritorName(String writorName) {
        this.writorName = writorName;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public Integer getLadderNum() {
        return ladderNum;
    }

    public void setLadderNum(Integer ladderNum) {
        this.ladderNum = ladderNum;
    }

    public JRBeanCollectionDataSource getTableData1() {
        return tableData1;
    }

    public void setTableData1(JRBeanCollectionDataSource tableData1) {
        this.tableData1 = tableData1;
    }

    public JRBeanCollectionDataSource getTableData2() {
        return tableData2;
    }

    public void setTableData2(JRBeanCollectionDataSource tableData2) {
        this.tableData2 = tableData2;
    }

    public JRBeanCollectionDataSource getTableData3() {
        return tableData3;
    }

    public void setTableData3(JRBeanCollectionDataSource tableData3) {
        this.tableData3 = tableData3;
    }

    public JRBeanCollectionDataSource getTableData4() {
        return tableData4;
    }

    public void setTableData4(JRBeanCollectionDataSource tableData4) {
        this.tableData4 = tableData4;
    }

    public JRBeanCollectionDataSource getTableData5() {
        return tableData5;
    }

    public void setTableData5(JRBeanCollectionDataSource tableData5) {
        this.tableData5 = tableData5;
    }

    public JRBeanCollectionDataSource getTableData6() {
        return tableData6;
    }

    public void setTableData6(JRBeanCollectionDataSource tableData6) {
        this.tableData6 = tableData6;
    }

    public JRBeanCollectionDataSource getTableData7() {
        return tableData7;
    }

    public void setTableData7(JRBeanCollectionDataSource tableData7) {
        this.tableData7 = tableData7;
    }

    public String getEndNum() {
        return endNum;
    }

    public void setEndNum(String endNum) {
        this.endNum = endNum;
    }
}
