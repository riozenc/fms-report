package org.fms.report.common.webapp.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.annotation.TablePrimaryKey;
import com.riozenc.titanTool.mybatis.MybatisEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MeterMpedRelDomain extends ManagerParamEntity implements MybatisEntity{
	
	
	@TablePrimaryKey
	private Long id; //ID	ID	bigint
	private Long meterId;//计费点ID	METER_ID	bigint
	private Long mpedId;//计量点ID	MPED_ID	bigint
	private String phaseSeq;//相序	PHASE_SEQ	smallint
	private String functionCode;//功能代码	FUNCTION_CODE	smallint
	private String powerDirection;//功率方向	POWER_DIRECTION	smallint
	private String tsFlag;//分时标识	TS_FLAG	smallint
	private Date createDate;//创建时间	CREATE_DATE	datetime
	private String status;//状态	STATUS	smallint
	private BigDecimal factorNum;//综合倍率	FACTOR_NUM	decimal
	private Long writeSn;//抄表序号	WRITE_SN	bigint
	private String meterName;//METER_NAME
	private String name;//NAME
	private List<Long> mpedIds;
	private List<Long> meterIds;
	
	
	
	public List<Long> getMeterIds() {
		return meterIds;
	}
	public void setMeterIds(List<Long> meterIds) {
		this.meterIds = meterIds;
	}
	public List<Long> getMpedIds() {
		return mpedIds;
	}
	public void setMpedIds(List<Long> mpedIds) {
		this.mpedIds = mpedIds;
	}
	public String getMeterName() {
		return meterName;
	}
	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Long getMpedId() {
		return mpedId;
	}
	public void setMpedId(Long mpedId) {
		this.mpedId = mpedId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getPhaseSeq() {
		return phaseSeq;
	}
	public void setPhaseSeq(String phaseSeq) {
		this.phaseSeq = phaseSeq;
	}
	public String getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
	public String getPowerDirection() {
		return powerDirection;
	}
	public void setPowerDirection(String powerDirection) {
		this.powerDirection = powerDirection;
	}
	public String getTsFlag() {
		return tsFlag;
	}
	public void setTsFlag(String tsFlag) {
		this.tsFlag = tsFlag;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BigDecimal getFactorNum() {
		return factorNum;
	}
	public void setFactorNum(BigDecimal factorNum) {
		this.factorNum = factorNum;
	}
	public Long getWriteSn() {
		return writeSn;
	}
	public void setWriteSn(Long writeSn) {
		this.writeSn = writeSn;
	}

	
	
	
	
	
	
}
