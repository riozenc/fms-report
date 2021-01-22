/**
 *    Auth:riozenc
 *    Date:2019年3月14日 下午6:07:30
 *    Title:com.riozenc.cim.webapp.domain.MeterRelationDomain.java
 **/
package org.fms.report.common.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.annotation.TablePrimaryKey;
import com.riozenc.titanTool.mybatis.MybatisEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MeterRelationDomain extends ManagerParamEntity implements MybatisEntity {
	
	private Long id;
	private Long meterId;				//从表
	private Long pMeterId;				//主表
	private Byte pMeterRateFlag;		//	主计量点分时标志	
	private Byte meterRateFlag;			//	主计量点分时标志
	private String shareRate;			//	分时分摊方式	SHARE_RATE	 1:1:1:1
	private Byte meterRelationType;		//
	private BigDecimal meterRelationValue;//关系值
	private Date createDate;
	private String remark;
	private Byte status;
	
	private String meterName;
	private String meterNo;
	private String meterOrder;

	private List<Long> pMeterIds;

	
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
	public Long getpMeterId() {
		return pMeterId;
	}
	public void setpMeterId(Long pMeterId) {
		this.pMeterId = pMeterId;
	}
	public Byte getMeterRateFlag() {
		return meterRateFlag;
	}
	public void setMeterRateFlag(Byte meterRateFlag) {
		this.meterRateFlag = meterRateFlag;
	}
	public String getShareRate() {
		return shareRate;
	}
	public void setShareRate(String shareRate) {
		this.shareRate = shareRate;
	}
	public Byte getMeterRelationType() {
		return meterRelationType;
	}
	public void setMeterRelationType(Byte meterRelationType) {
		this.meterRelationType = meterRelationType;
	}
	public BigDecimal getMeterRelationValue() {
		return meterRelationValue;
	}
	public void setMeterRelationValue(BigDecimal meterRelationValue) {
		this.meterRelationValue = meterRelationValue;
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
	public Byte getpMeterRateFlag() {
		return pMeterRateFlag;
	}
	public void setpMeterRateFlag(Byte pMeterRateFlag) {
		this.pMeterRateFlag = pMeterRateFlag;
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

	public String getMeterOrder() {
		return meterOrder;
	}

	public void setMeterOrder(String meterOrder) {
		this.meterOrder = meterOrder;
	}

	public List<Long> getpMeterIds() {
		return pMeterIds;
	}

	public void setpMeterIds(List<Long> pMeterIds) {
		this.pMeterIds = pMeterIds;
	}
}
