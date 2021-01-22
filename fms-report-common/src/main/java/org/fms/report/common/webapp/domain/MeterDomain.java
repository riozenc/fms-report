package org.fms.report.common.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.mybatis.MybatisEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MeterDomain extends ManagerParamEntity implements MybatisEntity {
    private Long id;// ID ID bigint TRUE FALSE TRUE
    private String meterNo; // 计量点号
    private Integer mon;
    private Long businessPlaceCode;// 营业区域ID
    private Long writeSectionId; // 所属抄表段 WRITE_SECTION_ID bigint
    private Long userId;
    private String userNo;
    private String userName;
    private String writeSectName;
    private Long writorId;// 抄表员ID
    private String setAddress;// 用电地址
    private String meterAssetsNo;//电表资产NO

    private Long priceType; // 电价分组
    private Integer elecTypeCode; // 用电类别
    private Byte baseMoneyFlag; // 基本电费计算方法
    private Byte tsType; // 分时计费标准
    private Byte sn;
    private List writorIds;
    private List writeSectionIds;
    private List businessPlaceCodes;
    private List ids;
    private List userIds;
    private List<Integer> mons;
    private String meterName; // 计量点名称
    private List<Integer> statuss;
    private Integer status;
    private Integer tradeType;
    private String writeSectNo;//
    private Byte voltLevelType; // 计量点电压
    private Byte msType; // 计量方式
    private Byte transLostType;
    private Byte lineLostType;
    private Integer ladderNum;
    private Byte cosType;
    private Integer meterOrder;
    private Long meterType;
    private BigDecimal needIndex;
    private Long basicPrice;
    private List<Long> priceTypeIds; // 电价分组
    private List<Long> meterIds;

    public Byte getCosType() {
        return cosType;
    }

    public void setCosType(Byte cosType) {
        this.cosType = cosType;
    }

    public Integer getLadderNum() {
        return ladderNum;
    }

    public void setLadderNum(Integer ladderNum) {
        this.ladderNum = ladderNum;
    }

    public String getWriteSectNo() {
        return writeSectNo;
    }

    public void setWriteSectNo(String writeSectNo) {
        this.writeSectNo = writeSectNo;
    }

    public Byte getBaseMoneyFlag() {
        return baseMoneyFlag;
    }

    public void setBaseMoneyFlag(Byte baseMoneyFlag) {
        this.baseMoneyFlag = baseMoneyFlag;
    }

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMeterAssetsNo() {
        return meterAssetsNo;
    }

    public void setMeterAssetsNo(String meterAssetsNo) {
        this.meterAssetsNo = meterAssetsNo;
    }

    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public Integer getMon() {
        return mon;
    }

    public void setMon(Integer mon) {
        this.mon = mon;
    }

    public Long getBusinessPlaceCode() {
        return businessPlaceCode;
    }

    public void setBusinessPlaceCode(Long businessPlaceCode) {
        this.businessPlaceCode = businessPlaceCode;
    }

    public Long getWriteSectionId() {
        return writeSectionId;
    }

    public void setWriteSectionId(Long writeSectionId) {
        this.writeSectionId = writeSectionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getWriteSectName() {
        return writeSectName;
    }

    public void setWriteSectName(String writeSectName) {
        this.writeSectName = writeSectName;
    }

    public Long getWritorId() {
        return writorId;
    }

    public void setWritorId(Long writorId) {
        this.writorId = writorId;
    }

    public String getSetAddress() {
        return setAddress;
    }

    public void setSetAddress(String setAddress) {
        this.setAddress = setAddress;
    }

    public Byte getTsType() {
        return tsType;
    }

    public void setTsType(Byte tsType) {
        this.tsType = tsType;
    }

    public Byte getSn() {
        return sn;
    }

    public void setSn(Byte sn) {
        this.sn = sn;
    }

    public List getWriteSectionIds() {
        return writeSectionIds;
    }

    public void setWriteSectionIds(List writeSectionIds) {
        this.writeSectionIds = writeSectionIds;
    }

    public List getBusinessPlaceCodes() {
        return businessPlaceCodes;
    }

    public void setBusinessPlaceCodes(List businessPlaceCodes) {
        this.businessPlaceCodes = businessPlaceCodes;
    }

    public List getWritorIds() {
        return writorIds;
    }

    public void setWritorIds(List writorIds) {
        this.writorIds = writorIds;
    }

    public List getIds() {
        return ids;
    }

    public void setIds(List ids) {
        this.ids = ids;
    }

    public Long getPriceType() {
        return priceType;
    }

    public void setPriceType(Long priceType) {
        this.priceType = priceType;
    }

    public Integer getElecTypeCode() {
        return elecTypeCode;
    }

    public void setElecTypeCode(Integer elecTypeCode) {
        this.elecTypeCode = elecTypeCode;
    }

    public List getUserIds() {
        return userIds;
    }

    public void setUserIds(List userIds) {
        this.userIds = userIds;
    }

    public List<Integer> getMons() {
        return mons;
    }

    public void setMons(List<Integer> mons) {
        this.mons = mons;
    }

    public List<Integer> getStatuss() {
        return statuss;
    }

    public void setStatuss(List<Integer> statuss) {
        this.statuss = statuss;
    }

    public Byte getVoltLevelType() {
        return voltLevelType;
    }

    public void setVoltLevelType(Byte voltLevelType) {
        this.voltLevelType = voltLevelType;
    }

    public Byte getMsType() {
        return msType;
    }

    public void setMsType(Byte msType) {
        this.msType = msType;
    }

    public Byte getTransLostType() {
        return transLostType;
    }

    public void setTransLostType(Byte transLostType) {
        this.transLostType = transLostType;
    }

    public Byte getLineLostType() {
        return lineLostType;
    }

    public void setLineLostType(Byte lineLostType) {
        this.lineLostType = lineLostType;
    }

    public Integer getMeterOrder() {
        return meterOrder;
    }

    public void setMeterOrder(Integer meterOrder) {
        this.meterOrder = meterOrder;
    }

    public Long getMeterType() {
        return meterType;
    }

    public void setMeterType(Long meterType) {
        this.meterType = meterType;
    }

    public BigDecimal getNeedIndex() {
        return needIndex;
    }

    public void setNeedIndex(BigDecimal needIndex) {
        this.needIndex = needIndex;
    }

    public Long getBasicPrice() {
        return basicPrice;
    }

    public void setBasicPrice(Long basicPrice) {
        this.basicPrice = basicPrice;
    }

    public List<Long> getPriceTypeIds() {
        return priceTypeIds;
    }

    public void setPriceTypeIds(List<Long> priceTypeIds) {
        this.priceTypeIds = priceTypeIds;
    }

    public List<Long> getMeterIds() {
        return meterIds;
    }

    public void setMeterIds(List<Long> meterIds) {
        this.meterIds = meterIds;
    }
}
