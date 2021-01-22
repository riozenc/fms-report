package org.fms.report.common.webapp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.math.RoundingMode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MeterMoneyBean {
    private String name;
    private String value;
    private Long writeSectId;
    private String writeSectNo;
    private String writeSectName;
    private Long writorId; // 抄表员
    private String writorName; // 抄表员姓名
    private int meters; // 计量点数
    private BigDecimal totalPower; // 计费电量
    private BigDecimal volumeCharge; // 电度电费
    private Long priceType; // 电价类别
    private Byte userType; // 用户类别
    private Integer elecTypeCode; // 用电分类
    private Long businessPlaceCode;// 营业区域ID
    private String deptName; // 营业区域名称
    private BigDecimal one; // 国家水利工程建设基金
    private BigDecimal two; // 国家水利工程建设基金
    private BigDecimal three; // 城市公用事业附加费
    private BigDecimal four; // 大中型水库移民后期扶持资金
    private BigDecimal five; // 地方水库移民后期扶持资金
    private BigDecimal six; // 可再生能源电价附加
    private BigDecimal seven; // 农网还贷资金
    private BigDecimal eight; // 农村电网维护费
    private BigDecimal nine; // 价格调节基金
    private BigDecimal totalMoney; // 总电费
    private BigDecimal basicMoney; // 基本电费
    private BigDecimal powerRateMoney; // 力率电费
    private BigDecimal refundMoney;
    private BigDecimal shouldMoney;
    private BigDecimal amount;
    private BigDecimal activeWritePower;// 有功抄见电量 ACTIVE_WRITE_POWER decimal(12,2) 12 2 FALSE FALSE FALSE
    private BigDecimal reactiveWritePower;
    private Integer timeSeg;
    private BigDecimal chgPower;
    private BigDecimal qchgPower;
    private BigDecimal addPower;
    private BigDecimal qaddPower;
    private BigDecimal qtotalPower;

    private BigDecimal activeDeductionPower;

    private BigDecimal sharePower;
    private BigDecimal activeTransformerLossPower;
    private BigDecimal reactiveTransformerLossPower;
    private BigDecimal price;
    private Integer meterOrder;

    /**
     * 还贷
     */
    private BigDecimal addMoney1;

    /**
     * 国家水利工程建设基金
     */
    private BigDecimal addMoney2;

    /**
     * 城市公用事业附加费
     */
    private BigDecimal addMoney3;

    /**
     * 大中型水库移民后期扶持资金
     */
    private BigDecimal addMoney4;

    /**
     * 地方水库移民后期扶持资金
     */
    private BigDecimal addMoney5;

    /**
     * 可再生能源电价附加
     */
    private BigDecimal addMoney6;

    /**
     * 农网还贷资金
     */
    private BigDecimal addMoney7;

    private BigDecimal addMoney8;

    /**
     * 还贷
     */
    private BigDecimal addMoney1Price;

    /**
     * 国家水利工程建设基金
     */
    private BigDecimal addMoney2Price;

    /**
     * 城市公用事业附加费
     */
    private BigDecimal addMoney3Price;

    /**
     * 大中型水库移民后期扶持资金
     */
    private BigDecimal addMoney4Price;

    /**
     * 地方水库移民后期扶持资金
     */
    private BigDecimal addMoney5Price;

    /**
     * 可再生能源电价附加
     */
    private BigDecimal addMoney6Price;

    /**
     * 农网还贷资金
     */
    private BigDecimal addMoney7Price;

    private BigDecimal addMoney8Price;

    private BigDecimal khPower;

    private BigDecimal surcharges;// 附加费

    private BigDecimal cosRate;//功率因数

    private BigDecimal cos;

    private BigDecimal calCapacity;

    private BigDecimal basicPrice;

    private String timeSegName;

    private Byte functionCode;

    public BigDecimal getAmount() {
        if(amount==null){
            return BigDecimal.ZERO;
        }else{
            return amount.setScale(2,RoundingMode.HALF_UP);
        }
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getShouldMoney() {
        if(shouldMoney==null){
            return BigDecimal.ZERO;
        }else{
            return shouldMoney.setScale(2,RoundingMode.HALF_UP);
        }
    }

    public void setShouldMoney(BigDecimal shouldMoney) {
        this.shouldMoney = shouldMoney;
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

    public BigDecimal getOne() {
        if(one==null){
            return BigDecimal.ZERO;
        }else{
            return one.setScale(2,RoundingMode.HALF_UP);
        }
    }

    public void setOne(BigDecimal one) {
        this.one = one;
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

    public String getWriteSectName() {
        return writeSectName;
    }

    public void setWriteSectName(String writeSectName) {
        this.writeSectName = writeSectName;
    }

    public BigDecimal getVolumeCharge() {
        if (volumeCharge == null) {
            return BigDecimal.ZERO;
        } else {
            return volumeCharge.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setVolumeCharge(BigDecimal volumeCharge) {
        this.volumeCharge = volumeCharge;
    }

    public BigDecimal getTotalMoney() {

        if (totalMoney == null) {
            return BigDecimal.ZERO;
        } else {
            return totalMoney.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public BigDecimal getBasicMoney() {
        if (basicMoney == null) {
            return BigDecimal.ZERO;
        } else {
            return basicMoney.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setBasicMoney(BigDecimal basicMoney) {
        this.basicMoney = basicMoney;
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

    public BigDecimal getTwo() {
        if (two == null) {
            return BigDecimal.ZERO;
        } else {
            return two.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setTwo(BigDecimal two) {
        this.two = two;
    }

    public BigDecimal getThree() {

        if (three == null) {
            return BigDecimal.ZERO;
        } else {
            return three.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setThree(BigDecimal three) {
        this.three = three;
    }

    public BigDecimal getFour() {
        if (four == null) {
            return BigDecimal.ZERO;
        } else {
            return four.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setFour(BigDecimal four) {
        this.four = four;
    }

    public BigDecimal getFive() {
        if (five == null) {
            return BigDecimal.ZERO;
        } else {
            return five.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setFive(BigDecimal five) {
        this.five = five;
    }

    public BigDecimal getSix() {
        if (six == null) {
            return BigDecimal.ZERO;
        } else {
            return six.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setSix(BigDecimal six) {
        this.six = six;
    }

    public BigDecimal getSeven() {
        if (seven == null) {
            return BigDecimal.ZERO;
        } else {
            return seven.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setSeven(BigDecimal seven) {
        this.seven = seven;
    }

    public BigDecimal getEight() {
        if (eight == null) {
            return BigDecimal.ZERO;
        } else {
            return eight.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setEight(BigDecimal eight) {
        this.eight = eight;
    }

    public BigDecimal getNine() {
        if (nine == null) {
            return BigDecimal.ZERO;
        } else {
            return nine.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public void setNine(BigDecimal nine) {
        this.nine = nine;
    }

    public Long getWritorId() {
        return writorId;
    }

    public void setWritorId(Long writorId) {
        this.writorId = writorId;
    }

    public String getWritorName() {
        return writorName;
    }

    public void setWritorName(String writorName) {
        this.writorName = writorName;
    }

    public Long getPriceType() {
        return priceType;
    }

    public void setPriceType(Long priceType) {
        this.priceType = priceType;
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

    public Long getBusinessPlaceCode() {
        return businessPlaceCode;
    }

    public void setBusinessPlaceCode(Long businessPlaceCode) {
        this.businessPlaceCode = businessPlaceCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getMeters() {
        return meters;
    }

    public void setMeters(int meters) {
        this.meters = meters;
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

    public BigDecimal getActiveWritePower() {
        return activeWritePower;
    }

    public void setActiveWritePower(BigDecimal activeWritePower) {
        this.activeWritePower = activeWritePower;
    }

    public BigDecimal getReactiveWritePower() {
        return reactiveWritePower;
    }

    public void setReactiveWritePower(BigDecimal reactiveWritePower) {
        this.reactiveWritePower = reactiveWritePower;
    }

    public Integer getTimeSeg() {
        return timeSeg;
    }

    public void setTimeSeg(Integer timeSeg) {
        this.timeSeg = timeSeg;
    }

    public BigDecimal getChgPower() {
        return chgPower;
    }

    public void setChgPower(BigDecimal chgPower) {
        this.chgPower = chgPower;
    }

    public BigDecimal getAddPower() {
        return addPower;
    }

    public void setAddPower(BigDecimal addPower) {
        this.addPower = addPower;
    }

    public BigDecimal getActiveDeductionPower() {
        return activeDeductionPower;
    }

    public void setActiveDeductionPower(BigDecimal activeDeductionPower) {
        this.activeDeductionPower = activeDeductionPower;
    }

    public BigDecimal getSharePower() {
        return sharePower;
    }

    public void setSharePower(BigDecimal sharePower) {
        this.sharePower = sharePower;
    }

    public BigDecimal getActiveTransformerLossPower() {
        return activeTransformerLossPower;
    }

    public void setActiveTransformerLossPower(BigDecimal activeTransformerLossPower) {
        this.activeTransformerLossPower = activeTransformerLossPower;
    }

    public BigDecimal getReactiveTransformerLossPower() {
        return reactiveTransformerLossPower;
    }

    public void setReactiveTransformerLossPower(BigDecimal reactiveTransformerLossPower) {
        this.reactiveTransformerLossPower = reactiveTransformerLossPower;
    }

    public BigDecimal getQchgPower() {
        return qchgPower;
    }

    public void setQchgPower(BigDecimal qchgPower) {
        this.qchgPower = qchgPower;
    }

    public BigDecimal getQaddPower() {
        return qaddPower;
    }

    public void setQaddPower(BigDecimal qaddPower) {
        this.qaddPower = qaddPower;
    }

    public BigDecimal getQtotalPower() {
        return qtotalPower;
    }

    public void setQtotalPower(BigDecimal qtotalPower) {
        this.qtotalPower = qtotalPower;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getMeterOrder() {
        return meterOrder;
    }

    public void setMeterOrder(Integer meterOrder) {
        this.meterOrder = meterOrder;
    }

    public BigDecimal getAddMoney1() {
        return addMoney1;
    }

    public void setAddMoney1(BigDecimal addMoney1) {
        this.addMoney1 = addMoney1;
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

    public BigDecimal getAddMoney1Price() {
        return addMoney1Price;
    }

    public void setAddMoney1Price(BigDecimal addMoney1Price) {
        this.addMoney1Price = addMoney1Price;
    }

    public BigDecimal getAddMoney2Price() {
        return addMoney2Price;
    }

    public void setAddMoney2Price(BigDecimal addMoney2Price) {
        this.addMoney2Price = addMoney2Price;
    }

    public BigDecimal getAddMoney3Price() {
        return addMoney3Price;
    }

    public void setAddMoney3Price(BigDecimal addMoney3Price) {
        this.addMoney3Price = addMoney3Price;
    }

    public BigDecimal getAddMoney4Price() {
        return addMoney4Price;
    }

    public void setAddMoney4Price(BigDecimal addMoney4Price) {
        this.addMoney4Price = addMoney4Price;
    }

    public BigDecimal getAddMoney5Price() {
        return addMoney5Price;
    }

    public void setAddMoney5Price(BigDecimal addMoney5Price) {
        this.addMoney5Price = addMoney5Price;
    }

    public BigDecimal getAddMoney6Price() {
        return addMoney6Price;
    }

    public void setAddMoney6Price(BigDecimal addMoney6Price) {
        this.addMoney6Price = addMoney6Price;
    }

    public BigDecimal getAddMoney7Price() {
        return addMoney7Price;
    }

    public void setAddMoney7Price(BigDecimal addMoney7Price) {
        this.addMoney7Price = addMoney7Price;
    }

    public BigDecimal getKhPower() {
        return khPower;
    }

    public void setKhPower(BigDecimal khPower) {
        this.khPower = khPower;
    }

    public BigDecimal getSurcharges() {
        return surcharges;
    }

    public void setSurcharges(BigDecimal surcharges) {
        this.surcharges = surcharges;
    }

    public BigDecimal getCosRate() {
        return cosRate;
    }

    public void setCosRate(BigDecimal cosRate) {
        this.cosRate = cosRate;
    }

    public BigDecimal getCos() {
        return cos;
    }

    public void setCos(BigDecimal cos) {
        this.cos = cos;
    }

    public BigDecimal getCalCapacity() {
        return calCapacity;
    }

    public void setCalCapacity(BigDecimal calCapacity) {
        this.calCapacity = calCapacity;
    }

    public BigDecimal getBasicPrice() {
        return basicPrice;
    }

    public void setBasicPrice(BigDecimal basicPrice) {
        this.basicPrice = basicPrice;
    }

    public BigDecimal getAddMoney8() {
        return addMoney8;
    }

    public void setAddMoney8(BigDecimal addMoney8) {
        this.addMoney8 = addMoney8;
    }

    public BigDecimal getAddMoney8Price() {
        return addMoney8Price;
    }

    public void setAddMoney8Price(BigDecimal addMoney8Price) {
        this.addMoney8Price = addMoney8Price;
    }

    public String getTimeSegName() {
        return timeSegName;
    }

    public void setTimeSegName(String timeSegName) {
        this.timeSegName = timeSegName;
    }

    public Byte getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(Byte functionCode) {
        this.functionCode = functionCode;
    }
}
