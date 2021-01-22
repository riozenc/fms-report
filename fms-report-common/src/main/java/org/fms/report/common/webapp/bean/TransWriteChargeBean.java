package org.fms.report.common.webapp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransWriteChargeBean {
    private Long id; // 变压器ID
    private String transformerNo; // 变压器编号
    private String deskName; // 变压器名称
    private BigDecimal capacity; // 变压器容量
    private BigDecimal supplyPower; // 供电量
    private BigDecimal salePower; // 售电量
    private BigDecimal receivable; // 应收电费
    private BigDecimal factMoney; //实收电费
    private BigDecimal volumeCharge;// 电度电费
    private BigDecimal punishMoney; // 违约金
    private BigDecimal activeLineLossPower;// 有功线损电量
    private BigDecimal lineLossRate; // 线损率
    private BigDecimal moneyReceiveRate; // 电费回收率

    // 附加费
    private BigDecimal two; // 国家水利工程建设基金
    private BigDecimal three; // 城市公用事业附加费
    private BigDecimal four; // 大中型水库移民后期扶持资金
    private BigDecimal five; // 地方水库移民后期扶持资金
    private BigDecimal six; // 可再生能源电价附加
    private BigDecimal seven; // 农网还贷资金
    private BigDecimal eight; // 农村电网维护费
    private BigDecimal nine; // 价格调节基金

    private Byte isPubType; // 公用变标志

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransformerNo() {
        return transformerNo;
    }

    public void setTransformerNo(String transformerNo) {
        this.transformerNo = transformerNo;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public BigDecimal getCapacity() {
        return capacity;
    }

    public void setCapacity(BigDecimal capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getSupplyPower() {
        return supplyPower;
    }

    public void setSupplyPower(BigDecimal supplyPower) {
        this.supplyPower = supplyPower;
    }

    public BigDecimal getSalePower() {
        return salePower;
    }

    public void setSalePower(BigDecimal salePower) {
        this.salePower = salePower;
    }

    public BigDecimal getReceivable() {
        return receivable;
    }

    public void setReceivable(BigDecimal receivable) {
        this.receivable = receivable;
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

    public BigDecimal getPunishMoney() {
        return punishMoney;
    }

    public void setPunishMoney(BigDecimal punishMoney) {
        this.punishMoney = punishMoney;
    }

    public BigDecimal getActiveLineLossPower() {
        return activeLineLossPower;
    }

    public void setActiveLineLossPower(BigDecimal activeLineLossPower) {
        this.activeLineLossPower = activeLineLossPower;
    }

    public BigDecimal getLineLossRate() {
        return lineLossRate;
    }

    public void setLineLossRate(BigDecimal lineLossRate) {
        this.lineLossRate = lineLossRate;
    }

    public BigDecimal getMoneyReceiveRate() {
        return moneyReceiveRate;
    }

    public void setMoneyReceiveRate(BigDecimal moneyReceiveRate) {
        this.moneyReceiveRate = moneyReceiveRate;
    }

    public BigDecimal getTwo() {
        return two;
    }

    public void setTwo(BigDecimal two) {
        this.two = two;
    }

    public BigDecimal getThree() {
        return three;
    }

    public void setThree(BigDecimal three) {
        this.three = three;
    }

    public BigDecimal getFour() {
        return four;
    }

    public void setFour(BigDecimal four) {
        this.four = four;
    }

    public BigDecimal getFive() {
        return five;
    }

    public void setFive(BigDecimal five) {
        this.five = five;
    }

    public BigDecimal getSix() {
        return six;
    }

    public void setSix(BigDecimal six) {
        this.six = six;
    }

    public BigDecimal getSeven() {
        return seven;
    }

    public void setSeven(BigDecimal seven) {
        this.seven = seven;
    }

    public BigDecimal getEight() {
        return eight;
    }

    public void setEight(BigDecimal eight) {
        this.eight = eight;
    }

    public BigDecimal getNine() {
        return nine;
    }

    public void setNine(BigDecimal nine) {
        this.nine = nine;
    }

    public Byte getIsPubType() {
        return isPubType;
    }

    public void setIsPubType(Byte isPubType) {
        this.isPubType = isPubType;
    }
}
