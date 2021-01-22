/**
 *    Auth:riozenc
 *    Date:2019年3月8日 下午3:22:58
 *    Title:com.riozenc.cim.webapp.domain.CustomerDomain.java
 **/
package org.fms.report.common.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.annotation.TablePrimaryKey;
import com.riozenc.titanTool.mybatis.MybatisEntity;

import java.util.Date;

/**
 * 供电局开户银行维护
 *SF_POWER_BANK
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SfPowerBankDomain extends ManagerParamEntity implements MybatisEntity {

	private Long id;// ID ID bigint TRUE FALSE TRUE
	private Long businessPlaceCode;		//	收款单位	BUSINESS_PLACE_CODE	bigint	
	private String bankName;		//	开户银行	BANK_NAME	varchar(128)
	private String account;			//	帐号	ACCOUNT	varchar(128)	128	
	private String dwName;			//	收款单位全称	DW_NAME	varchar(128)
	private Byte bankCode;			//	总行	BANK_CODE	smallint
	private Date createDate;		//	修改时间	CREATE_DATE	datetime
	private String address;			//	地址	ADDRESS	varchar(128)	
	private String tel;				//	电话	TEL	varchar(128)	
	private String remark; 			//备注
	private Byte status; 			//状态
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getBusinessPlaceCode() {
		return businessPlaceCode;
	}
	public void setBusinessPlaceCode(Long businessPlaceCode) {
		this.businessPlaceCode = businessPlaceCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getDwName() {
		return dwName;
	}
	public void setDwName(String dwName) {
		this.dwName = dwName;
	}
	public Byte getBankCode() {
		return bankCode;
	}
	public void setBankCode(Byte bankCode) {
		this.bankCode = bankCode;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
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
	


}
