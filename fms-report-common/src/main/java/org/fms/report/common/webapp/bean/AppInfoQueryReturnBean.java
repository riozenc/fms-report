package org.fms.report.common.webapp.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;
import java.util.Date;

//业扩流程查询返回实体参数
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppInfoQueryReturnBean {
    private String appNo;
    private String userNo;
    private String userName;
    private String address;
    private Long elecType;
    private String elecTypeName;
    private Long templateId;
    private String templateName;
    private String allCapacity;
    private String appStatus;
    private String phoneNumber;
    private Long    finishMan;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Timestamp finishDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Timestamp applyDate;

    private String finishDateString;

    private String applyDateString;

    private String finishManName;

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getElecType() {
        return elecType;
    }

    public void setElecType(Long elecType) {
        this.elecType = elecType;
    }

    public String getAllCapacity() {
        return allCapacity;
    }

    public void setAllCapacity(String allCapacity) {
        this.allCapacity = allCapacity;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getFinishMan() {
        return finishMan;
    }

    public void setFinishMan(Long finishMan) {
        this.finishMan = finishMan;
    }

    public Timestamp getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Timestamp finishDate) {
        this.finishDate = finishDate;
    }

    public Timestamp getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Timestamp applyDate) {
        this.applyDate = applyDate;
    }

    public String getElecTypeName() {
        return elecTypeName;
    }

    public void setElecTypeName(String elecTypeName) {
        this.elecTypeName = elecTypeName;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getFinishManName() {
        return finishManName;
    }

    public void setFinishManName(String finishManName) {
        this.finishManName = finishManName;
    }

    public String getFinishDateString() {
        return finishDateString;
    }

    public void setFinishDateString(String finishDateString) {
        this.finishDateString = finishDateString;
    }

    public String getApplyDateString() {
        return applyDateString;
    }

    public void setApplyDateString(String applyDateString) {
        this.applyDateString = applyDateString;
    }
}
