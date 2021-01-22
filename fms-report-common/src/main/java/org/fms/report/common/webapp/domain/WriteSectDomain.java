package org.fms.report.common.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.annotation.TablePrimaryKey;
import com.riozenc.titanTool.mybatis.MybatisEntity;

import java.util.Date;
import java.util.List;

//抄表区段
@JsonIgnoreProperties(ignoreUnknown = true)
public class WriteSectDomain extends ManagerParamEntity implements MybatisEntity {
    @TablePrimaryKey
    private Long id;
    // 抄表区段
    private String writeSectNo;
    // 抄表区段名称
    private String writeSectName;
    // 抄表员ID
    private Long writorId;
    // 计算人ID
    private Long calculatorId;
    // 营业区域
    private Long businessPlaceCode;
    // 抄表班组
    private String deptCode;
    // 抄表方式
    private Integer writeType;
    // 应抄日期
    private Integer shouldWriteDays;
    // 标准抄表天数
    private Integer standardWriteDays;
    // 应计算日期
    private Integer shouldCalDays;
    // 区段用户类型
    private Long sectUserType;
    // 应算违约金天数
    private Integer punishDays;
    // 创建时间
    private String createDate;
    private String remark;
    private Integer status;

    private Integer mon;// 电费月份

    // mongo
    private Integer startMon;
    private Integer endMon;

    private Integer complete;// 已经完成抄表的数量
    private Integer uncomplete;// 未完成抄表的数量

    private List writeSectionIds;

    private List businessPlaceCodes;

    public Integer getStartMon() {
        return startMon;
    }

    public void setStartMon(Integer startMon) {
        this.startMon = startMon;
    }

    public Integer getEndMon() {
        return endMon;
    }

    public void setEndMon(Integer endMon) {
        this.endMon = endMon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getWritorId() {
        return writorId;
    }

    public void setWritorId(Long writorId) {
        this.writorId = writorId;
    }

    public Long getCalculatorId() {
        return calculatorId;
    }

    public void setCalculatorId(Long calculatorId) {
        this.calculatorId = calculatorId;
    }

    public Long getBusinessPlaceCode() {
        return businessPlaceCode;
    }

    public void setBusinessPlaceCode(Long businessPlaceCode) {
        this.businessPlaceCode = businessPlaceCode;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public Integer getWriteType() {
        return writeType;
    }

    public void setWriteType(Integer writeType) {
        this.writeType = writeType;
    }

    public Integer getShouldWriteDays() {
        return shouldWriteDays;
    }

    public void setShouldWriteDays(Integer shouldWriteDays) {
        this.shouldWriteDays = shouldWriteDays;
    }

    public Integer getStandardWriteDays() {
        return standardWriteDays;
    }

    public void setStandardWriteDays(Integer standardWriteDays) {
        this.standardWriteDays = standardWriteDays;
    }

    public Integer getShouldCalDays() {
        return shouldCalDays;
    }

    public void setShouldCalDays(Integer shouldCalDays) {
        this.shouldCalDays = shouldCalDays;
    }

    public Long getSectUserType() {
        return sectUserType;
    }

    public void setSectUserType(Long sectUserType) {
        this.sectUserType = sectUserType;
    }

    public Integer getPunishDays() {
        return punishDays;
    }

    public void setPunishDays(Integer punishDays) {
        this.punishDays = punishDays;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
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

    public Integer getMon() {
        return mon;
    }

    public void setMon(Integer mon) {
        this.mon = mon;
    }

    public Integer getComplete() {
        return complete;
    }

    public void setComplete(Integer complete) {
        this.complete = complete;
    }

    public Integer getUncomplete() {
        return uncomplete;
    }

    public void setUncomplete(Integer uncomplete) {
        this.uncomplete = uncomplete;
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
}
