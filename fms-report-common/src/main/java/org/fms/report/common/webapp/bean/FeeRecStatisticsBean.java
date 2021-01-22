package org.fms.report.common.webapp.bean;

import com.riozenc.titanTool.mybatis.MybatisEntity;

import java.math.BigDecimal;
import java.util.Date;

public class FeeRecStatisticsBean {
    //预收在CHARGE_IFNO中ARREARAGE_NO字段内填充YS标识
    Integer id;
    String name;
    Integer chargeNum;
    Integer consNum;
    BigDecimal chgPower;
    BigDecimal factTotal;
    BigDecimal TotalPower;
    BigDecimal factMoney;
    BigDecimal amount;//总电费
    BigDecimal surcharges;//附加费
    BigDecimal volumeCharge;//电度电费
    BigDecimal powerRateMoney;//力调
    BigDecimal basicMoney;//基本电费
    BigDecimal addMoney1;//国家水利工程建设基金
    BigDecimal addMoney2;//城市公用事业附加费
    BigDecimal addMoney3;//大中型水库移民后期扶持资金
    BigDecimal addMoney4;//地方水库移民后期扶持资金
    BigDecimal addMoney5;//可再生能源电价附加
    BigDecimal addMoney6;//农网还贷资金
    BigDecimal addMoney7;//农村电网维护费
    BigDecimal addMoney8;//价格调节基金
    BigDecimal addMoney9;
    BigDecimal shhx;
    BigDecimal factPunish;//违约金
    BigDecimal factPre;//预收
    BigDecimal reFactPre;//冲预收 factMoney---ChargeMoney
    int mon;
    String meterNo;
    String meterName;
    int meterNum;//计量点数量
    Integer fChargeMode;//收费方式
    String fChargeModeName;//收费方式
    String notePrintNo;//票号
    Long priceTypeId;//价格类型
    BigDecimal price; //单价
    String settlementName;//结算户名
    Long settlementId;//结算户ID
    Long settlementNo;//结算户号
    Integer ysTypeCode;//应收类型
    String ysTypeName;//应收类型
    String payDate;//支付时间
    Long operator;//操作员id
    String operatorName;//操作员名称
    BigDecimal startNum;//起码
    BigDecimal endNum;//止码
    BigDecimal factorNum;//倍率
    BigDecimal activeWritePower;// 有功抄见电量 ACTIVE_WRITE_POWER decimal(12,2) 12 2 FALSE FALSE FALSE
    BigDecimal addPower;
    private BigDecimal activeLineLossPower;// 有功线损电量 ACTIVE_LINE_LOSS_POWER decimal(12,2) 12 2 FALSE FALSE FALSE
    private BigDecimal reactiveLineLossPower;// 无功线损电量 REACTIVE_LINE_LOSS_POWER decimal(12,2) 12 2 FALSE FALSE FALSE
    private BigDecimal activeTransformerLossPower;// 有功变损电量 ACTIVE_TRANSFORMER_LOSS_POWER decimal(12,2) 12 2 FALSE FALSE
    private BigDecimal lineAndTransformerLossPower;//变线损
    private BigDecimal receivable;//应收电费
    private BigDecimal arrears;//CHARGE_INFO应收
    private BigDecimal thisBalance;
    private BigDecimal lastBalance;
    private String priceName;
    private BigDecimal powerSupply;//供电量
    private BigDecimal electricitySales;//售电量
    private BigDecimal LineLossRate;//线损率
    private BigDecimal recoveryRate;//电费回收率

    //meter
    private String baseMoneyFlagName;//基本电费计算方式名称


    //meterMoney
    private BigDecimal calCapacity;// 计费容量(最大需量) CAL_CAPACITY decimal(12,2) 12 2 FALSE FALSE FALSE

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

    //Transformer变压器
    private String transformerName;
    private Long transFormerId;
    private BigDecimal capacity; // 变压器容量
    private Byte isPubType;
    private Long lineId;

    private String businessPlaceCodeName;
    private BigDecimal sumFlow;

    private BigDecimal sumSettlement;

    private BigDecimal factAdvance;
    private String row1name;
    private String row2name;
    private Long sortColumn;
    private BigDecimal activeWritePower1;// 峰有功抄见电量 ACTIVE_WRITE_POWER_1 decimal(12,2) 12 2 FALSE FALSE FALSE
    private BigDecimal activeWritePower2;// 平有功抄见电量 ACTIVE_WRITE_POWER_2 decimal(12,2) 12 2 FALSE FALSE FALSE
    private BigDecimal activeWritePower3;//谷
    private BigDecimal plusTotal; //附加和
    private BigDecimal refundMoney;
    private Integer meterOrder;

    public BigDecimal getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(BigDecimal refundMoney) {
        this.refundMoney = refundMoney;
    }

    public String getBusinessPlaceCodeName() {
        return businessPlaceCodeName;
    }

    public void setBusinessPlaceCodeName(String businessPlaceCodeName) {
        this.businessPlaceCodeName = businessPlaceCodeName;
    }

    public BigDecimal getSumFlow() {
        return sumFlow;
    }

    public void setSumFlow(BigDecimal sumFlow) {
        this.sumFlow = sumFlow;
    }

    public BigDecimal getSumSettlement() {
        return sumSettlement;
    }

    public void setSumSettlement(BigDecimal sumSettlement) {
        this.sumSettlement = sumSettlement;
    }

    public BigDecimal getFactAdvance() {
        return factAdvance;
    }

    public void setFactAdvance(BigDecimal factAdvance) {
        this.factAdvance = factAdvance;
    }

    public String getRow1name() {
        return row1name;
    }

    public void setRow1name(String row1name) {
        this.row1name = row1name;
    }

    public String getRow2name() {
        return row2name;
    }

    public void setRow2name(String row2name) {
        this.row2name = row2name;
    }

    public Long getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(Long sortColumn) {
        this.sortColumn = sortColumn;
    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public void initFeeRecStatisticsBean(FeeRecStatisticsBean feeRecStatisticsBean){
        feeRecStatisticsBean.setCapacity(BigDecimal.ZERO);
        feeRecStatisticsBean.setPowerSupply(BigDecimal.ZERO);
        feeRecStatisticsBean.setElectricitySales(BigDecimal.ZERO);
        feeRecStatisticsBean.setReceivable(BigDecimal.ZERO);
        feeRecStatisticsBean.setFactMoney(BigDecimal.ZERO);
        feeRecStatisticsBean.setAddMoney1(BigDecimal.ZERO);
        feeRecStatisticsBean.setAddMoney2(BigDecimal.ZERO);
        feeRecStatisticsBean.setAddMoney3(BigDecimal.ZERO);
        feeRecStatisticsBean.setAddMoney4(BigDecimal.ZERO);
        feeRecStatisticsBean.setAddMoney5(BigDecimal.ZERO);
        feeRecStatisticsBean.setAddMoney6(BigDecimal.ZERO);
        feeRecStatisticsBean.setAddMoney7(BigDecimal.ZERO);
        feeRecStatisticsBean.setAddMoney8(BigDecimal.ZERO);
        feeRecStatisticsBean.setVolumeCharge(BigDecimal.ZERO);
        feeRecStatisticsBean.setFactPunish(BigDecimal.ZERO);
        feeRecStatisticsBean.setLineLossRate(BigDecimal.ZERO);
        feeRecStatisticsBean.setRecoveryRate(BigDecimal.ZERO);

    }

    public BigDecimal getCalCapacity() {
        return calCapacity;
    }

    public void setCalCapacity(BigDecimal calCapacity) {
        this.calCapacity = calCapacity;
    }

    public String getBaseMoneyFlagName() {
        return baseMoneyFlagName;
    }

    public void setBaseMoneyFlagName(String baseMoneyFlagName) {
        this.baseMoneyFlagName = baseMoneyFlagName;
    }

    public BigDecimal getSurcharges() {
        return surcharges;
    }

    public void setSurcharges(BigDecimal surcharges) {
        this.surcharges = surcharges;
    }

    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    public BigDecimal getAddMoney1() {
        return addMoney1;
    }

    public void setAddMoney1(BigDecimal addMoney1) {
        this.addMoney1 = addMoney1;
    }

    public Byte getIsPubType() {
        return isPubType;
    }

    public void setIsPubType(Byte isPubType) {
        this.isPubType = isPubType;
    }

    public BigDecimal getLineLossRate() {
        return LineLossRate;
    }

    public void setLineLossRate(BigDecimal lineLossRate) {
        LineLossRate = lineLossRate;
    }

    public BigDecimal getRecoveryRate() {
        return recoveryRate;
    }

    public void setRecoveryRate(BigDecimal recoveryRate) {
        this.recoveryRate = recoveryRate;
    }

    public BigDecimal getPowerSupply() {
        return powerSupply;
    }

    public void setPowerSupply(BigDecimal powerSupply) {
        this.powerSupply = powerSupply;
    }

    public BigDecimal getElectricitySales() {
        return electricitySales;
    }

    public void setElectricitySales(BigDecimal electricitySales) {
        this.electricitySales = electricitySales;
    }

    public String getTransformerName() {
        return transformerName;
    }

    public void setTransformerName(String transformerName) {
        this.transformerName = transformerName;
    }

    public Long getTransFormerId() {
        return transFormerId;
    }

    public void setTransFormerId(Long transFormerId) {
        this.transFormerId = transFormerId;
    }

    public BigDecimal getCapacity() {
        return capacity;
    }

    public void setCapacity(BigDecimal capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getChgPower() {
        return chgPower;
    }

    public void setChgPower(BigDecimal chgPower) {
        this.chgPower = chgPower;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getArrears() {
        return arrears;
    }

    public void setArrears(BigDecimal arrears) {
        this.arrears = arrears;
    }

    public Long getSettlementNo() {
        return settlementNo;
    }

    public void setSettlementNo(Long settlementNo) {
        this.settlementNo = settlementNo;
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

    public BigDecimal getFactorNum() {
        return factorNum;
    }

    public void setFactorNum(BigDecimal factorNum) {
        this.factorNum = factorNum;
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

    public BigDecimal getLineAndTransformerLossPower() {
        return lineAndTransformerLossPower;
    }

    public void setLineAndTransformerLossPower(BigDecimal lineAndTransformerLossPower) {
        this.lineAndTransformerLossPower = lineAndTransformerLossPower;
    }

    public BigDecimal getReceivable() {
        return receivable;
    }

    public void setReceivable(BigDecimal receivable) {
        this.receivable = receivable;
    }

    public void setPriceTypeId(Long priceTypeId) {
        this.priceTypeId = priceTypeId;
    }

    public Long getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(Long settlementId) {
        this.settlementId = settlementId;
    }

    public String getSettlementName() {
        return settlementName;
    }

    public void setSettlementName(String settlementName) {
        this.settlementName = settlementName;
    }

    public Integer getYsTypeCode() {
        return ysTypeCode;
    }

    public void setYsTypeCode(Integer ysTypeCode) {
        this.ysTypeCode = ysTypeCode;
    }

    public String getYsTypeName() {
        return ysTypeName;
    }

    public void setYsTypeName(String ysTypeName) {
        this.ysTypeName = ysTypeName;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getNotePrintNo() {
        return notePrintNo;
    }

    public void setNotePrintNo(String notePrintNo) {
        this.notePrintNo = notePrintNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getChargeNum() {
        return chargeNum;
    }

    public void setChargeNum(Integer chargeNum) {
        this.chargeNum = chargeNum;
    }

    public Integer getConsNum() {
        return consNum;
    }

    public void setConsNum(Integer consNum) {
        this.consNum = consNum;
    }

    public BigDecimal getFactTotal() {
        return factTotal;
    }

    public void setFactTotal(BigDecimal factTotal) {
        this.factTotal = factTotal;
    }

    public BigDecimal getTotalPower() {
        return TotalPower;
    }

    public void setTotalPower(BigDecimal totalPower) {
        TotalPower = totalPower;
    }

    public BigDecimal getFactMoney() {
        return factMoney;
    }

    public void setFactMoney(BigDecimal factMoney) {
        this.factMoney = factMoney;
    }

    public BigDecimal getVolumeCharge() {
        return volumeCharge;
    }

    public void setVolumeCharge(BigDecimal volumeCharge) {
        this.volumeCharge = volumeCharge;
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

    public BigDecimal getAddMoney2() {
        return addMoney2;
    }

    public void setAddMoney2(BigDecimal addMoney2) {
        this.addMoney2 = addMoney2;
    }

    public BigDecimal getAddMoney3() {
        return addMoney3;
    }

    public void setAddMoney3(BigDecimal addMoney3) {
        this.addMoney3 = addMoney3;
    }

    public BigDecimal getAddMoney4() {
        return addMoney4;
    }

    public void setAddMoney4(BigDecimal addMoney4) {
        this.addMoney4 = addMoney4;
    }

    public BigDecimal getAddMoney5() {
        return addMoney5;
    }

    public void setAddMoney5(BigDecimal addMoney5) {
        this.addMoney5 = addMoney5;
    }

    public BigDecimal getAddMoney6() {
        return addMoney6;
    }

    public void setAddMoney6(BigDecimal addMoney6) {
        this.addMoney6 = addMoney6;
    }

    public BigDecimal getAddMoney7() {
        return addMoney7;
    }

    public void setAddMoney7(BigDecimal addMoney7) {
        this.addMoney7 = addMoney7;
    }

    public BigDecimal getAddMoney8() {
        return addMoney8;
    }

    public void setAddMoney8(BigDecimal addMoney8) {
        this.addMoney8 = addMoney8;
    }

    public BigDecimal getAddMoney9() {
        return addMoney9;
    }

    public void setAddMoney9(BigDecimal addMoney9) {
        this.addMoney9 = addMoney9;
    }

    public BigDecimal getShhx() {
        return shhx;
    }

    public void setShhx(BigDecimal shhx) {
        this.shhx = shhx;
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

    public BigDecimal getReFactPre() {
        return reFactPre;
    }

    public void setReFactPre(BigDecimal reFactPre) {
        this.reFactPre = reFactPre;
    }

    public int getMon() {
        return mon;
    }

    public void setMon(int mon) {
        this.mon = mon;
    }

    public int getMeterNum() {
        return meterNum;
    }

    public void setMeterNum(int meterNum) {
        this.meterNum = meterNum;
    }

    public Integer getfChargeMode() {
        return fChargeMode;
    }

    public void setfChargeMode(Integer fChargeMode) {
        this.fChargeMode = fChargeMode;
    }

    public String getfChargeModeName() {
        return fChargeModeName;
    }

    public void setfChargeModeName(String fChargeModeName) {
        this.fChargeModeName = fChargeModeName;
    }

    public Long getPriceTypeId() {
        return priceTypeId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getActiveWritePower1() {
        return activeWritePower1;
    }

    public void setActiveWritePower1(BigDecimal activeWritePower1) {
        this.activeWritePower1 = activeWritePower1;
    }

    public BigDecimal getActiveWritePower2() {
        return activeWritePower2;
    }

    public void setActiveWritePower2(BigDecimal activeWritePower2) {
        this.activeWritePower2 = activeWritePower2;
    }

    public BigDecimal getActiveWritePower3() {
        return activeWritePower3;
    }

    public void setActiveWritePower3(BigDecimal activeWritePower3) {
        this.activeWritePower3 = activeWritePower3;
    }

    public BigDecimal getPlusTotal() {
        return plusTotal;
    }

    public void setPlusTotal(BigDecimal plusTotal) {
        this.plusTotal = plusTotal;
    }

    public Integer getMeterOrder() {
        return meterOrder;
    }

    public void setMeterOrder(Integer meterOrder) {
        this.meterOrder = meterOrder;
    }
}
