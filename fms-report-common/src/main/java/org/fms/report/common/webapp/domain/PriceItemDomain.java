package org.fms.report.common.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.annotation.TablePrimaryKey;
import com.riozenc.titanTool.mybatis.MybatisEntity;

import java.util.Date;

//电价项目
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceItemDomain extends ManagerParamEntity implements MybatisEntity{
	@TablePrimaryKey
	private Long id;
	//项目名称
	private String priceItemName;
	//项目分类
	private Integer priceItemType;
	private Date createDate;
	private String remark;
	private Integer status;
	private String operator;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPriceItemName() {
		return priceItemName;
	}
	public void setPriceItemName(String priceItemName) {
		this.priceItemName = priceItemName;
	}
	public Integer getPriceItemType() {
		return priceItemType;
	}
	public void setPriceItemType(Integer priceItemType) {
		this.priceItemType = priceItemType;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
}
