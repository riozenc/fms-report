/**
 * Author : czy
 * Date : 2019年12月3日 下午6:56:32
 * Title : com.riozenc.billing.webapp.domain.mongo.MeterMoneyDomain.java
 *
**/
package org.fms.report.common.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.annotation.TablePrimaryKey;
import com.riozenc.titanTool.mybatis.MybatisEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MeterMoneyDomain extends ParamDomain implements MybatisEntity {
	@TablePrimaryKey
	private Long id;// ID ID bigint TRUE FALSE TRUE
	private Long meterId;// 计量点ID METER_ID bigint FALSE FALSE FALSE
	//private Integer mon;// 电费年月 MON int FALSE FALSE FALSE
	private Byte sn;// 当月次数 SN smallint FALSE FALSE FALSE
	private BigDecimal activeWritePower;// 有功抄见电量 ACTIVE_WRITE_POWER decimal(12,2) 12 2 FALSE FALSE FALSE
	private BigDecimal reactiveWritePower;// 正向无功抄见电量 REACTIVE_WRITE_POWER decimal(12,2) 12 2 FALSE FALSE FALSE
	private BigDecimal reverseActiveWritePower;// 反向有功抄见电力 REVERSE_ACTIVE_WRITE_POWER decimal(12,2) 12 2 FALSE FALSE
	private BigDecimal totalPower;// 有功总电量 TOTAL_POWER decimal(12,2) 12 2 FALSE FALSE FALSE
	private BigDecimal qTotalPower;// 无功总电量 Q_TOTAL_POWER decimal(12,2) 12 2 FALSE FALSE FALSE
	private BigDecimal cos;// 功率因数 COS decimal(5,2) 5 2 FALSE FALSE FALSE
	private BigDecimal calCapacity;// 计费容量(最大需量) CAL_CAPACITY decimal(12,2) 12 2 FALSE FALSE FALSE
	private BigDecimal shareCapacity;// 分摊容量 SHARE_CAPACITY decimal(12,2) 12 2 FALSE FALSE FALSE
	private BigDecimal volumeCharge;// 电度电费 VOLUME_CHARGE decimal(14,4) 14 4 FALSE FALSE FALSE
	private BigDecimal basicMoney;// 基本电费 BASIC_MONEY decimal(14,4) 14 4 FALSE FALSE FALSE
	private BigDecimal powerRateMoney;// 力调电费 POWER_RATE_MONEY decimal(14,4) 14 4 FALSE FALSE FALSE
	private BigDecimal surcharges;// 附加费 SURCHARGES decimal(14,4) 14 4 FALSE FALSE FALSE
	private BigDecimal amount;
    private BigDecimal totalAmount;// 总电费 AMOUNT decimal(14,4) 14 4 FALSE FALSE
    // FALSE
	private Date createDate;// 创建时间 CREATE_DATE datetime FALSE FALSE FALSE
	private String remark;// 备注 REMARK varchar(256) 256 FALSE FALSE FALSE
	private Byte status;// 状态 STATUS smallint FALSE FALSE FALSE
	private BigDecimal activeLineLossPower;// 有功线损电量
	private Long priceTypeId;
	private Map<String,BigDecimal> surchargeDetail;// 附加费
	private BigDecimal two; // 国家水利工程建设基金
	private BigDecimal three; // 城市公用事业附加费
	private BigDecimal four; // 大中型水库移民后期扶持资金
	private BigDecimal five; // 地方水库移民后期扶持资金
	private BigDecimal six; // 可再生能源电价附加
	private BigDecimal seven; // 农网还贷资金
	private BigDecimal eight; // 农村电网维护费
	private BigDecimal nine; // 价格调节基金

	private String writeSectNo;

	private String writeSectName;

	private BigDecimal chgPower;// 有功换表电量 CHG_POWER decimal(12,2) 12 2 FALSE FALSE FALSE
	private BigDecimal addPower;// 有功增减电量 ADD_POWER decimal(12,2) 12 2 FALSE FALSE FALSE
	private BigDecimal activeTransformerLossPower;// 有功变损电量 ACTIVE_TRANSFORMER_LOSS_POWER decimal(12,2) 12 2 FALSE FALSE
	private BigDecimal reactiveTransformerLossPower;

	//private Boolean isAgainStat; // 是否重新统计

	// 营业区域
	//private Long businessPlaceCode;




	// 营业区域名称
	private String deptName;


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

	/**
	 * 农村电网维护费
	 */
	private BigDecimal addMoney8;

	/**
	 * 价格调节基金
	 */
	private BigDecimal addMoney9;

	private BigDecimal addMoney10;

	//private String groupBy; // 分组条件

	private Integer elecTypeCode; // 用电类别

	private Long userId;

	private Byte userType;// 分类标识

	private List<Long> meterIds;

	private List<Long> writeSectIds;

	private String priceName;

	private String userTypeName;

	private String elecTypeName;

	private Integer tradeType;

	private String tradeTypeName;

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
	private BigDecimal ladderTotalPower;
	private BigDecimal refundMoney;
	private BigDecimal activeDeductionPower;

	//ChargeInfo
	private BigDecimal factMoney;//
	private BigDecimal factPunish;

	//变压器
	private String transformerName;
	private Long transFormerId;
	private BigDecimal capacity; // 变压器容量
	private Byte isPubType;
	private Long lineId;
	private String lineName;
	private BigDecimal refundChargeMoney;
	private List<Long> priceTypeIds;
	private String settlementNo;
	private String settlementName;
	private String meterNo;
	private BigDecimal plusTotal;

	private BigDecimal activeWritePower1;// 峰有功抄见电量 ACTIVE_WRITE_POWER_1 decimal(12,2) 12 2 FALSE FALSE FALSE
	private BigDecimal activeWritePower2;// 平有功抄见电量 ACTIVE_WRITE_POWER_2 decimal(12,2) 12 2 FALSE FALSE FALSE
	private BigDecimal activeWritePower3;//谷
	private TransformerCalculateMongoDomain transformerCalculateInfo;
	private List<WriteFilesMongoDomain> meterDataInfo;
	private BigDecimal cosRate;
	private BigDecimal transformerRunDay;
    private BigDecimal transformerGroupNo;
	private List<LadderMongoDomain> ladderDataModels;
	public List<Long> getPriceTypeIds() {
		return priceTypeIds;
	}

	public void setPriceTypeIds(List<Long> priceTypeIds) {
		this.priceTypeIds = priceTypeIds;
	}

	public BigDecimal getRefundChargeMoney() {
		return refundChargeMoney;
	}

	public void setRefundChargeMoney(BigDecimal refundChargeMoney) {
		this.refundChargeMoney = refundChargeMoney;
	}

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public Byte getIsPubType() {
		return isPubType;
	}

	public void setIsPubType(Byte isPubType) {
		this.isPubType = isPubType;
	}

	public Long getTransFormerId() {
		return transFormerId;
	}

	public void setTransFormerId(Long transFormerId) {
		this.transFormerId = transFormerId;
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

	public BigDecimal getCapacity() {
		return capacity;
	}

	public void setCapacity(BigDecimal capacity) {
		this.capacity = capacity;
	}

	public String getTransformerName() {
		return transformerName;
	}

	public void setTransformerName(String transformerName) {
		this.transformerName = transformerName;
	}

	public BigDecimal getActiveDeductionPower() {
		return activeDeductionPower;
	}

	public void setActiveDeductionPower(BigDecimal activeDeductionPower) {
		this.activeDeductionPower = activeDeductionPower;
	}

	public BigDecimal getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(BigDecimal refundMoney) {
		this.refundMoney = refundMoney;
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

	public BigDecimal getActiveTransformerLossPower() {
		return activeTransformerLossPower;
	}

	public void setActiveTransformerLossPower(BigDecimal activeTransformerLossPower) {
		this.activeTransformerLossPower = activeTransformerLossPower;
	}

	public BigDecimal getLadderTotalPower() {
		return ladderTotalPower;
	}

	public void setLadderTotalPower(BigDecimal ladderTotalPower) {
		this.ladderTotalPower = ladderTotalPower;
	}

	public String getElecTypeName() {
		return elecTypeName;
	}

	public void setElecTypeName(String elecTypeName) {
		this.elecTypeName = elecTypeName;
	}

	public String getUserTypeName() {
		return userTypeName;
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}

	public String getPriceName() {
		return priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMeterId() {
		return meterId;
	}

	public void setMeterId(Long meterId) {
		this.meterId = meterId;
	}

	/*public Integer getMon() {
		return mon;
	}

	public void setMon(Integer mon) {
		this.mon = mon;
	}*/

	public Byte getSn() {
		return sn;
	}

	public void setSn(Byte sn) {
		this.sn = sn;
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

	public BigDecimal getAddMoney10() {
		return addMoney10;
	}

	public void setAddMoney10(BigDecimal addMoney10) {
		this.addMoney10 = addMoney10;
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

	public BigDecimal getReverseActiveWritePower() {
		return reverseActiveWritePower;
	}

	public void setReverseActiveWritePower(BigDecimal reverseActiveWritePower) {
		this.reverseActiveWritePower = reverseActiveWritePower;
	}

	public BigDecimal getTotalPower() {
		return totalPower;
	}

	public void setTotalPower(BigDecimal totalPower) {
		this.totalPower = totalPower;
	}

	public BigDecimal getqTotalPower() {
		return qTotalPower;
	}

	public void setqTotalPower(BigDecimal qTotalPower) {
		this.qTotalPower = qTotalPower;
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

	public BigDecimal getShareCapacity() {
		return shareCapacity;
	}

	public void setShareCapacity(BigDecimal shareCapacity) {
		this.shareCapacity = shareCapacity;
	}

	public BigDecimal getVolumeCharge() {
		return volumeCharge;
	}

	public void setVolumeCharge(BigDecimal volumeCharge) {
		this.volumeCharge = volumeCharge;
	}

	public BigDecimal getBasicMoney() {
		return basicMoney;
	}

	public void setBasicMoney(BigDecimal basicMoney) {
		this.basicMoney = basicMoney;
	}

	public BigDecimal getPowerRateMoney() {
		return powerRateMoney;
	}

	public void setPowerRateMoney(BigDecimal powerRateMoney) {
		this.powerRateMoney = powerRateMoney;
	}

	public BigDecimal getSurcharges() {
		return surcharges;
	}

	public void setSurcharges(BigDecimal surcharges) {
		this.surcharges = surcharges;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Long getPriceTypeId() {
		return priceTypeId;
	}

	public void setPriceTypeId(Long priceTypeId) {
		this.priceTypeId = priceTypeId;
	}

	public Map<String, BigDecimal> getSurchargeDetail() {
		return surchargeDetail;
	}

	public void setSurchargeDetail(Map<String, BigDecimal> surchargeDetail) {
		this.surchargeDetail = surchargeDetail;
	}

	/*public Long getBusinessPlaceCode() {
		return businessPlaceCode;
	}

	public void setBusinessPlaceCode(Long businessPlaceCode) {
		this.businessPlaceCode = businessPlaceCode;
	}
*/


	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}


/*public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}*/

	public Integer getElecTypeCode() {
		return elecTypeCode;
	}

	public void setElecTypeCode(Integer elecTypeCode) {
		this.elecTypeCode = elecTypeCode;
	}

	public Byte getUserType() {
		return userType;
	}

	public void setUserType(Byte userType) {
		this.userType = userType;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/*public Boolean getAgainStat() {
		return isAgainStat;
	}

	public void setAgainStat(Boolean againStat) {
		isAgainStat = againStat;
	}*/

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

	public List<Long> getMeterIds() {
		return meterIds;
	}

	public void setMeterIds(List<Long> meterIds) {
		this.meterIds = meterIds;
	}

	public List<Long> getWriteSectIds() {
		return writeSectIds;
	}

	public void setWriteSectIds(List<Long> writeSectIds) {
		this.writeSectIds = writeSectIds;
	}

	public BigDecimal getActiveLineLossPower() {
		return activeLineLossPower;
	}

	public void setActiveLineLossPower(BigDecimal activeLineLossPower) {
		this.activeLineLossPower = activeLineLossPower;
	}

	public String getTradeTypeName() {
		return tradeTypeName;
	}

	public void setTradeTypeName(String tradeTypeName) {
		this.tradeTypeName = tradeTypeName;
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
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

	public String getMeterNo() {
		return meterNo;
	}

	public void setMeterNo(String meterNo) {
		this.meterNo = meterNo;
	}

	public BigDecimal getPlusTotal() {
		return plusTotal;
	}

	public void setPlusTotal(BigDecimal plusTotal) {
		this.plusTotal = plusTotal;
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

	public TransformerCalculateMongoDomain getTransformerCalculateInfo() {
		return transformerCalculateInfo;
	}

	public void setTransformerCalculateInfo(TransformerCalculateMongoDomain transformerCalculateInfo) {
		this.transformerCalculateInfo = transformerCalculateInfo;
	}

	public BigDecimal getReactiveTransformerLossPower() {
		return reactiveTransformerLossPower;
	}

	public void setReactiveTransformerLossPower(BigDecimal reactiveTransformerLossPower) {
		this.reactiveTransformerLossPower = reactiveTransformerLossPower;
	}

	public List<WriteFilesMongoDomain> getMeterDataInfo() {
		return meterDataInfo;
	}

	public void setMeterDataInfo(List<WriteFilesMongoDomain> meterDataInfo) {
		this.meterDataInfo = meterDataInfo;
	}

    public BigDecimal getCosRate() {
        return cosRate;
    }

    public void setCosRate(BigDecimal cosRate) {
        this.cosRate = cosRate;
    }

    public BigDecimal getTransformerRunDay() {
        return transformerRunDay;
    }

    public void setTransformerRunDay(BigDecimal transformerRunDay) {
        this.transformerRunDay = transformerRunDay;
    }

    public BigDecimal getTransformerGroupNo() {
        return transformerGroupNo;
    }

    public void setTransformerGroupNo(BigDecimal transformerGroupNo) {
        this.transformerGroupNo = transformerGroupNo;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

	public List<LadderMongoDomain> getLadderDataModels() {
		return ladderDataModels;
	}

	public void setLadderDataModels(List<LadderMongoDomain> ladderDataModels) {
		this.ladderDataModels = ladderDataModels;
	}
}
