package org.fms.report.common.webapp.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.mybatis.MybatisEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

//抄表单
@JsonIgnoreProperties(ignoreUnknown = true)
public class WriteFilesDomain extends ManagerParamEntity implements MybatisEntity {
    private String id;

    private Long functionCode;//功能代码

    private Long meterId;// 计量点ID

    private Byte phaseSeq;// 相序

    private Byte powerDirection;//功率方向

    private Byte sn;// 本月次数 SN smallint FALSE FALSE FALSE

    private Byte timeSeg;// 时段 TIME_SEG smallint FALSE FALSE FALSE

    private Long businessPlaceCode;// 营业区域

//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//	private Date initDate;// 初始化时间 INIT_DATE datetime FALSE FALSE FALSE

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp writeDate;//  INIT_DATE datetime FALSE FALSE FALSE

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp lastWriteDate;

    private Long meterAssetsId; // 电表资产ID

    private String meterNo; // 计量点号

    private Integer mon;// 月份

    private BigDecimal startNum;// 起码 START_NUM decimal(12,2) 12 2 FALSE FALSE FALSE

    private Long userId; // 所属用户 USER_ID bigint

    private String userName; // 客户名称

    private String userNo;// 客户编号

    private Long writeSectionId;// 所属抄表区段

    private String writeSectNo;

    private String writeSectName;

    private Long writeSn;//抄表序号

    private BigDecimal factorNum; // 综合倍率

    private Byte writeFlag;// 抄表标志 WRITE_FLAG smallint FALSE FALSE FALSE

    private BigDecimal diffNum;// 度差 DIFF_NUM decimal(12,2) 12 2 FALSE FALSE FALSE

    private BigDecimal endNum;// 止码 END_NUM decimal(12,2) 12 2 FALSE FALSE FALSE

    private Long writorId;//抄表员ID

    private String writorName; // 抄表员姓名

    private BigDecimal lastEndNum;// 上次止码 END_NUM decimal(12,2) 12 2 FALSE FALSE FALSE

    private BigDecimal lastDiffNum;// 上次度差

    private BigDecimal volatility; // 波动率

    private String address;

    private Long deptId;

    private String deptName;

    private BigDecimal chgPower;

//	private List lastWriteFiles;
//
//	private List writeSect;
//
//	private List electricMeter;

    private String searchType;

    private Byte tsType; // 分时计费标准

    private Long priceType;//电价

    private Integer elecTypeCode; // 用电类别

    private Long meterOrder;// 表序号

    private BigDecimal powerRate; // 电量波动率（本月度差-上月止码）/上月止码

    private String timeSegName;//时段

    private String functionCodeName;//功能代码

    private String meterAssetsNo; // 电表资产NO

    private List businessPlaceCodes;

    private List writeSectionIds;

    private Boolean isAgainStat; // 是否重新统计

    private Boolean backToLine;//电表回行

    private Boolean isWrite;//已抄

    private Boolean endNumZero;// 止码为零

    private Boolean endNumNull;// 止码为null

    private BigDecimal addPower;

    private BigDecimal writePower;

    private List<Long> meterIds;

    private Long settlementId;

    private Long meterSn;

    private List<Long> meterAssetsIds;
    public Long getMeterSn() {
        return meterSn;
    }

    public void setMeterSn(Long meterSn) {
        this.meterSn = meterSn;
    }

    public Long getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(Long settlementId) {
        this.settlementId = settlementId;
    }

    public BigDecimal getChgPower() {
        return chgPower;
    }

    public void setChgPower(BigDecimal chgPower) {
        this.chgPower = chgPower;
    }

    public List<Long> getMeterIds() {
        return meterIds;
    }

    public void setMeterIds(List<Long> meterIds) {
        this.meterIds = meterIds;
    }

    public BigDecimal getAddPower() {
        return addPower;
    }

    public void setAddPower(BigDecimal addPower) {
        this.addPower = addPower;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public Byte getPhaseSeq() {
        return phaseSeq;
    }

    public void setPhaseSeq(Byte phaseSeq) {
        this.phaseSeq = phaseSeq;
    }

    public Byte getPowerDirection() {
        return powerDirection;
    }

    public void setPowerDirection(Byte powerDirection) {
        this.powerDirection = powerDirection;
    }

    public Byte getSn() {
        return sn;
    }

    public void setSn(Byte sn) {
        this.sn = sn;
    }

    public Byte getTimeSeg() {
        return timeSeg;
    }

    public void setTimeSeg(Byte timeSeg) {
        this.timeSeg = timeSeg;
    }

//	public Date getInitDate() {
//		return initDate;
//	}
//
//	public void setInitDate(Date initDate) {
//		this.initDate = initDate;
//	}

    public Long getMeterAssetsId() {
        return meterAssetsId;
    }

    public void setMeterAssetsId(Long meterAssetsId) {
        this.meterAssetsId = meterAssetsId;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public BigDecimal getStartNum() {
        return startNum;
    }

    public void setStartNum(BigDecimal startNum) {
        this.startNum = startNum;
    }

    public String getMeterAssetsNo() {
        return meterAssetsNo;
    }

    public void setMeterAssetsNo(String meterAssetsNo) {
        this.meterAssetsNo = meterAssetsNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public Long getWriteSectionId() {
        return writeSectionId;
    }

    public void setWriteSectionId(Long writeSectionId) {
        this.writeSectionId = writeSectionId;
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

    public Long getWriteSn() {
        return writeSn;
    }

    public void setWriteSn(Long writeSn) {
        this.writeSn = writeSn;
    }

//	public Date getWriteDate() {
//		return writeDate;
//	}
//
//	public void setWriteDate(Date writeDate) {
//		this.writeDate = writeDate;
//	}

    public Byte getWriteFlag() {
        return writeFlag;
    }

    public void setWriteFlag(Byte writeFlag) {
        this.writeFlag = writeFlag;
    }

    public BigDecimal getDiffNum() {
        return diffNum;
    }

    public void setDiffNum(BigDecimal diffNum) {
        this.diffNum = diffNum;
    }

    public BigDecimal getEndNum() {
        return endNum;
    }

    public void setEndNum(BigDecimal endNum) {
        this.endNum = endNum;
    }

    public String getWritorName() {
        return writorName;
    }

    public void setWritorName(String writorName) {
        this.writorName = writorName;
    }

    public BigDecimal getLastDiffNum() {
        return lastDiffNum;
    }

    public void setLastDiffNum(BigDecimal lastDiffNum) {
        this.lastDiffNum = lastDiffNum;
    }

    public BigDecimal getLastEndNum() {
        return lastEndNum;
    }

    public void setLastEndNum(BigDecimal lastEndNum) {
        this.lastEndNum = lastEndNum;
    }

    public BigDecimal getVolatility() {
        return volatility;
    }

    public void setVolatility(BigDecimal volatility) {
        this.volatility = volatility;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

//	public List getLastWriteFiles() {
//		return lastWriteFiles;
//	}
//
//	public void setLastWriteFiles(List lastWriteFiles) {
//		this.lastWriteFiles = lastWriteFiles;
//	}
//
//	public List getWriteSect() {
//		return writeSect;
//	}
//
//	public void setWriteSect(List writeSect) {
//		this.writeSect = writeSect;
//	}
//
//	public List getElectricMeter() {
//		return electricMeter;
//	}
//
//	public void setElectricMeter(List electricMeter) {
//		this.electricMeter = electricMeter;
//	}

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public Long getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(Long functionCode) {
        this.functionCode = functionCode;
    }

    public Long getBusinessPlaceCode() {
        return businessPlaceCode;
    }

    public void setBusinessPlaceCode(Long businessPlaceCode) {
        this.businessPlaceCode = businessPlaceCode;
    }

    public Integer getMon() {
        return mon;
    }

    public void setMon(Integer mon) {
        this.mon = mon;
    }

    public Long getWritorId() {
        return writorId;
    }

    public void setWritorId(Long writorId) {
        this.writorId = writorId;
    }

    public Byte getTsType() {
        return tsType;
    }

    public void setTsType(Byte tsType) {
        this.tsType = tsType;
    }

    public Long getPriceType() {
        return priceType;
    }

    public void setPriceType(Long priceType) {
        this.priceType = priceType;
    }

    public Long getMeterOrder() {
        return meterOrder;
    }

    public void setMeterOrder(Long meterOrder) {
        this.meterOrder = meterOrder;
    }

    public Timestamp getWriteDate() {
        return writeDate;
    }

    public Integer getElecTypeCode() {
        return elecTypeCode;
    }

    public void setElecTypeCode(Integer elecTypeCode) {
        this.elecTypeCode = elecTypeCode;
    }

    public String getTimeSegName() {
        return timeSegName;
    }

    public void setTimeSegName(String timeSegName) {
        this.timeSegName = timeSegName;
    }

    public String getFunctionCodeName() {
        return functionCodeName;
    }

    public void setFunctionCodeName(String functionCodeName) {
        this.functionCodeName = functionCodeName;
    }

    public Boolean getBackToLine() {
        return backToLine;
    }

    public void setBackToLine(Boolean backToLine) {
        this.backToLine = backToLine;
    }



    public BigDecimal getPowerRate() {
        return powerRate;
    }

    public void setPowerRate(BigDecimal powerRate) {
        this.powerRate = powerRate;
    }

    public List getBusinessPlaceCodes() {
        return businessPlaceCodes;
    }

    public void setBusinessPlaceCodes(List businessPlaceCodes) {
        this.businessPlaceCodes = businessPlaceCodes;
    }

    public List getWriteSectionIds() {
        return writeSectionIds;
    }

    public void setWriteSectionIds(List writeSectionIds) {
        this.writeSectionIds = writeSectionIds;
    }

    public Boolean getAgainStat() {
        return isAgainStat;
    }

    public void setAgainStat(Boolean againStat) {
        isAgainStat = againStat;
    }

    public BigDecimal getFactorNum() {
        return factorNum;
    }

    public void setFactorNum(BigDecimal factorNum) {
        this.factorNum = factorNum;
    }

    public void setWriteDate(Timestamp writeDate) {
        this.writeDate = writeDate;
    }

    public Timestamp getLastWriteDate() {
        return lastWriteDate;
    }

    public void setLastWriteDate(Timestamp lastWriteDate) {
        this.lastWriteDate = lastWriteDate;
    }

    public Boolean getWrite() {
        return isWrite;
    }

    public void setWrite(Boolean write) {
        isWrite = write;
    }

    public Boolean getIsWrite() {
        return isWrite;
    }

    public void setIsWrite(Boolean isWrite) {
        this.isWrite = isWrite;
    }

    public Boolean getEndNumZero() {
        return endNumZero;
    }

    public void setEndNumZero(Boolean endNumZero) {
        this.endNumZero = endNumZero;
    }

    public Boolean getEndNumNull() {
        return endNumNull;
    }

    public void setEndNumNull(Boolean endNumNull) {
        this.endNumNull = endNumNull;
    }

    public BigDecimal getWritePower() {
        return writePower;
    }

    public void setWritePower(BigDecimal writePower) {
        this.writePower = writePower;
    }

    public List<Long> getMeterAssetsIds() {
        return meterAssetsIds;
    }

    public void setMeterAssetsIds(List<Long> meterAssetsIds) {
        this.meterAssetsIds = meterAssetsIds;
    }
}
