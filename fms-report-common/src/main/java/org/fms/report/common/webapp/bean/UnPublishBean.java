package org.fms.report.common.webapp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//未发行
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnPublishBean {
    private String userNo;
    private String userName;
    private String businessPlaceCodeName;
    private String writeSectName;
    private String statusName;
    private String meterNo;
    private Long writeSectionId;

    public Long getWriteSectionId() {
        return writeSectionId;
    }

    public void setWriteSectionId(Long writeSectionId) {
        this.writeSectionId = writeSectionId;
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


    public String getBusinessPlaceCodeName() {
        return businessPlaceCodeName;
    }

    public void setBusinessPlaceCodeName(String businessPlaceCodeName) {
        this.businessPlaceCodeName = businessPlaceCodeName;
    }

    public String getWriteSectName() {
        return writeSectName;
    }

    public void setWriteSectName(String writeSectName) {
        this.writeSectName = writeSectName;
    }


    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }
}
