package org.fms.report.common.webapp.returnDomain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.riozenc.titanTool.mybatis.MybatisEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FeeRecStatisticsBean implements MybatisEntity {
    //预收在CHARGE_IFNO中ARREARAGE_NO字段内填充YS标识
    Integer id;
    String name;
    Integer chargeNum;
    Integer consNum;
    BigDecimal chgPower;
    BigDecimal factTotal;
    BigDecimal totalPower;
    BigDecimal factMoney;
    BigDecimal volumeCharge;//电度电费
    BigDecimal powerRateMoney;//力调
    BigDecimal basicMoney;//基本电费
    BigDecimal addMoney1;//水利
    BigDecimal addMoney2;//国家水利工程建设基金
    BigDecimal addMoney3;//城市公用事业附加费
    BigDecimal addMoney4;//大中型水库移民后期扶持资金
    BigDecimal addMoney5;//地方水库移民后期扶持资金
    BigDecimal addMoney6;//可再生能源电价附加
    BigDecimal addMoney7;//农网还贷资金
    BigDecimal addMoney8;//农村电网维护费
    BigDecimal addMoney9;//价格调节基金
    BigDecimal shhx;
    BigDecimal factPunish;//违约金
    BigDecimal factPre;//预收
    BigDecimal reFactPre;//冲预收 factMoney---ChargeMoney
    int mon;
    String meterNo;
    int meterNum;//计量点数量
    Long meterId;
    Integer fChargeMode;//收费方式
    String fChargeModeName;//收费方式
    String notePrintNo;//票号
    Long priceTypeId;//价格类型
    BigDecimal price; //单价
    String settlementName;//结算户名
    Long settlementId;//结算户名
    String settlementNo;//结算户号
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
    private BigDecimal surcharges;
    private BigDecimal refundMoney;
    private Long businessPlaceCode;
    private String businessPlaceCodeName;
    private BigDecimal sumFlow;

    private BigDecimal sumSettlement;

    private BigDecimal factAdvance;
    private String row1name;
    private String row2name;
    private Long sortColumn;
    private Long writeSectId;

    public Long getWriteSectId() {
        return writeSectId;
    }

    public void setWriteSectId(Long writeSectId) {
        this.writeSectId = writeSectId;
    }


    public Long getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(Long sortColumn) {
        this.sortColumn = sortColumn;
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

    public BigDecimal getAddMoney1() {
        if(addMoney1==null){
            return BigDecimal.ZERO;
        }else{
            return addMoney1.setScale(2,RoundingMode.HALF_UP);
        }
    }

    public void setAddMoney1(BigDecimal addMoney1) {
        this.addMoney1 = addMoney1;
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

    public Long getBusinessPlaceCode() {
        return businessPlaceCode;
    }

    public void setBusinessPlaceCode(Long businessPlaceCode) {
        this.businessPlaceCode = businessPlaceCode;
    }

    public String getBusinessPlaceCodeName() {
        return businessPlaceCodeName;
    }

    public void setBusinessPlaceCodeName(String businessPlaceCodeName) {
        this.businessPlaceCodeName = businessPlaceCodeName;
    }

    public BigDecimal getRefundMoney() {
        if(refundMoney==null){
            return BigDecimal.ZERO;
        }else{
            return refundMoney.setScale(2,RoundingMode.HALF_UP);
        }
    }

    public void setRefundMoney(BigDecimal refundMoney) {
        this.refundMoney = refundMoney;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public BigDecimal getSurcharges() {
        if(surcharges==null){
            return BigDecimal.ZERO;
        }else{
            return surcharges.setScale(2,RoundingMode.HALF_UP);
        }
    }

    public void setSurcharges(BigDecimal surcharges) {
        this.surcharges = surcharges;
    }

    public BigDecimal getChgPower() {
        if(chgPower==null){
            return BigDecimal.ZERO;
        }else{
            return chgPower.setScale(0,RoundingMode.HALF_UP);
        }
    }

    public void setChgPower(BigDecimal chgPower) {
        this.chgPower = chgPower;
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

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
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

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public String getSettlementNo() {
        return settlementNo;
    }

    public void setSettlementNo(String settlementNo) {
        this.settlementNo = settlementNo;
    }

    public BigDecimal getReceivable() {
        if(receivable==null){
            return BigDecimal.ZERO;
        }else{
            return receivable.setScale(2,BigDecimal.ROUND_HALF_UP);
        }
    }

    public void setReceivable(BigDecimal receivable) {
        this.receivable = receivable;
    }

    public BigDecimal getLineAndTransformerLossPower() {
        if(lineAndTransformerLossPower==null){
            return BigDecimal.ZERO;
        }else{
            return lineAndTransformerLossPower.setScale(0,RoundingMode.HALF_UP);
        }
    }

    public void setLineAndTransformerLossPower(BigDecimal lineAndTransformerLossPower) {
        this.lineAndTransformerLossPower = lineAndTransformerLossPower;
    }

    public BigDecimal getFactorNum() {
        if(factorNum==null){
            return BigDecimal.ZERO;
        }else{
            return factorNum.setScale(0,RoundingMode.HALF_UP);
        }
    }

    public void setFactorNum(BigDecimal factorNum) {
        this.factorNum = factorNum;
    }

    public BigDecimal getActiveWritePower() {
        if(activeWritePower==null){
            return BigDecimal.ZERO;
        }else{
            return activeWritePower.setScale(0,RoundingMode.HALF_UP);
        }
    }

    public void setActiveWritePower(BigDecimal activeWritePower) {
        this.activeWritePower = activeWritePower;
    }

    public BigDecimal getAddPower() {
        if(addPower==null){
            return BigDecimal.ZERO;
        }else{
            return addPower.setScale(0,RoundingMode.HALF_UP);
        }
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
        if(activeTransformerLossPower==null){
            return BigDecimal.ZERO;
        }else{
            return activeTransformerLossPower.setScale(0,RoundingMode.HALF_UP);
        }
    }

    public void setActiveTransformerLossPower(BigDecimal activeTransformerLossPower) {
        this.activeTransformerLossPower = activeTransformerLossPower;
    }

    public BigDecimal getStartNum() {
        if(startNum==null){
            return BigDecimal.ZERO;
        }else{
            return startNum;
        }
    }

    public void setStartNum(BigDecimal startNum) {
        this.startNum = startNum;
    }

    public BigDecimal getEndNum() {
        if(endNum==null){
            return BigDecimal.ZERO;
        }else{
            return endNum;
        }
    }

    public void setEndNum(BigDecimal endNum) {
        this.endNum = endNum;
    }

    public Long getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(Long settlementId) {
        this.settlementId = settlementId;
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


    public Long getPriceTypeId() {
        return priceTypeId;
    }

    public void setPriceTypeId(Long priceTypeId) {
        this.priceTypeId = priceTypeId;
    }

    public BigDecimal getPrice() {
        if(price==null){
            return BigDecimal.ZERO;
        }else{
            return price;
        }
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
        if(factTotal==null){
            return BigDecimal.ZERO;
        }else{
            return factTotal.setScale(2,RoundingMode.HALF_UP);
        }
    }

    public void setFactTotal(BigDecimal factTotal) {
        this.factTotal = factTotal;
    }

    public BigDecimal getTotalPower() {
        if(totalPower==null){
            return BigDecimal.ZERO;
        }else{
            return totalPower.setScale(0,RoundingMode.HALF_UP);
        }
    }

    public void setTotalPower(BigDecimal totalPower) {
        this.totalPower = totalPower;
    }

    public BigDecimal getFactMoney() {
        if(factMoney==null){
            return BigDecimal.ZERO;
        }else{
            return factMoney.setScale(2,RoundingMode.HALF_UP);
        }
    }

    public void setFactMoney(BigDecimal factMoney) {
        this.factMoney = factMoney;
    }

    public BigDecimal getVolumeCharge() {
        if(volumeCharge==null){
            return BigDecimal.ZERO;
        }else{
            return volumeCharge.setScale(2, RoundingMode.HALF_UP);
        }

    }

    public BigDecimal getPowerRateMoney() {
        if(powerRateMoney==null){
            return BigDecimal.ZERO;
        }else{
            return powerRateMoney.setScale(2,RoundingMode.HALF_UP);
        }
    }

    public void setVolumeCharge(BigDecimal volumeCharge) {
        this.volumeCharge = volumeCharge;
    }

    public void setPowerRateMoney(BigDecimal powerRateMoney) {
        this.powerRateMoney = powerRateMoney;
    }

    public BigDecimal getBasicMoney()
    {  if(basicMoney==null){
        return BigDecimal.ZERO;
    }else{
        return basicMoney.setScale(2,RoundingMode.HALF_UP);
    }
    }

    public void setBasicMoney(BigDecimal basicMoney) {
        this.basicMoney = basicMoney;
    }

    public BigDecimal getAddMoney2() {
        if(addMoney2==null){
            return BigDecimal.ZERO;
        }else{
            return addMoney2.setScale(2,RoundingMode.HALF_UP);
        }
    }

    public void setAddMoney2(BigDecimal addMoney2) {
        this.addMoney2 = addMoney2;
    }

    public BigDecimal getAddMoney3() {
        if(addMoney3==null){
            return BigDecimal.ZERO;
        }else{
            return addMoney3.setScale(2,RoundingMode.HALF_UP);
        }
    }

    public void setAddMoney3(BigDecimal addMoney3) {
        this.addMoney3 = addMoney3;
    }

    public BigDecimal getAddMoney4() {
        if(addMoney4==null){
            return BigDecimal.ZERO;
        }else{
            return addMoney4.setScale(2,RoundingMode.HALF_UP);
        }
    }

    public void setAddMoney4(BigDecimal addMoney4) {
        this.addMoney4 = addMoney4;
    }

    public BigDecimal getAddMoney5() {
        if(addMoney5==null){
            return BigDecimal.ZERO;
        }else{
            return addMoney5.setScale(2,RoundingMode.HALF_UP);
        }
    }

    public void setAddMoney5(BigDecimal addMoney5) {
        this.addMoney5 = addMoney5;
    }

    public BigDecimal getAddMoney6() {
        if(addMoney6==null){
            return BigDecimal.ZERO;
        }else{
            return addMoney6.setScale(2,RoundingMode.HALF_UP);
        }
    }

    public void setAddMoney6(BigDecimal addMoney6) {
        this.addMoney6 = addMoney6;
    }

    public BigDecimal getAddMoney7() {
        if(addMoney7==null){
            return BigDecimal.ZERO;
        }else{
            return addMoney7.setScale(2,RoundingMode.HALF_UP);
        }
    }

    public void setAddMoney7(BigDecimal addMoney7) {
        this.addMoney7 = addMoney7;
    }

    public BigDecimal getAddMoney8() {
        if(addMoney8==null){
            return BigDecimal.ZERO;
        }else{
            return addMoney8.setScale(2,RoundingMode.HALF_UP);
        }
    }

    public void setAddMoney8(BigDecimal addMoney8) {
        this.addMoney8 = addMoney8;
    }

    public BigDecimal getAddMoney9() {
        if(addMoney9==null){
            return BigDecimal.ZERO;
        }else{
            return addMoney9.setScale(2,RoundingMode.HALF_UP);
        }
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
        if(factPunish==null){
            return BigDecimal.ZERO;
        }else{
            return factPunish.setScale(2,RoundingMode.HALF_UP);
        }
    }

    public void setFactPunish(BigDecimal factPunish) {
        this.factPunish = factPunish;
    }

    public BigDecimal getFactPre() {
        if(factPre==null){
            return BigDecimal.ZERO;
        }else{
            return factPre.setScale(2,RoundingMode.HALF_UP);
        }
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

    public String getNotePrintNo() {
        if(notePrintNo==null){
            return "";
        }else{
            return notePrintNo;
        }
    }

    public void setNotePrintNo(String notePrintNo) {
        this.notePrintNo = notePrintNo;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }
}
