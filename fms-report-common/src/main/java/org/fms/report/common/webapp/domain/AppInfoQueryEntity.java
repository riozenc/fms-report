package org.fms.report.common.webapp.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.mybatis.pagination.Page;

import java.sql.Timestamp;
import java.util.List;

//业扩流程查询参数
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppInfoQueryEntity extends Page {
    private Long businessPlaceCode;
    private Integer templateType;
    private Long elecTypeCode;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Timestamp startApplyDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Timestamp startFinishDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Timestamp endApplyDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Timestamp endFinishDate;
    private Long appStatus;
    private List<Long> businessPlaceCodes;

    public Long getBusinessPlaceCode() {
        return businessPlaceCode;
    }

    public void setBusinessPlaceCode(Long businessPlaceCode) {
        this.businessPlaceCode = businessPlaceCode;
    }

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public Long getElecTypeCode() {
        return elecTypeCode;
    }

    public void setElecTypeCode(Long elecTypeCode) {
        this.elecTypeCode = elecTypeCode;
    }

    public Timestamp getStartApplyDate() {
        return startApplyDate;
    }

    public void setStartApplyDate(Timestamp startApplyDate) {
        this.startApplyDate = startApplyDate;
    }

    public Timestamp getStartFinishDate() {
        return startFinishDate;
    }

    public void setStartFinishDate(Timestamp startFinishDate) {
        this.startFinishDate = startFinishDate;
    }

    public Timestamp getEndApplyDate() {
        return endApplyDate;
    }

    public void setEndApplyDate(Timestamp endApplyDate) {
        this.endApplyDate = endApplyDate;
    }

    public Timestamp getEndFinishDate() {
        return endFinishDate;
    }

    public void setEndFinishDate(Timestamp endFinishDate) {
        this.endFinishDate = endFinishDate;
    }

    public Long getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(Long appStatus) {
        this.appStatus = appStatus;
    }

    public List<Long> getBusinessPlaceCodes() {
        return businessPlaceCodes;
    }

    public void setBusinessPlaceCodes(List<Long> businessPlaceCodes) {
        this.businessPlaceCodes = businessPlaceCodes;
    }
}
