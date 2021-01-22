package org.fms.report.common.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.mybatis.MybatisEntity;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ParamDomain extends ManagerParamEntity implements MybatisEntity {
    private Integer mon;
    private String groupBy; // 分组方式
    private Long businessPlaceCode;
    private String businessPlaceName;
    private Long writeSectId;
    private String writeSectionName;
    private Long writorId;//抄表员ID
    private String writorName;
    private Boolean isAgainStat; // 是否重新统计
    private List<Long> deptParamList;
    private List<Long> settParamList;
    private List<Long> userParamList;
    private Long deptParam;
    private Long userParam;
    private Long settParam;
    private Long writeSectIdParam;
    private String queryCriteria;
    private Byte customerType;
    private String groupByDate;
    private String statisticalMethod;//统计方式
    private Integer startMon;
    private Integer endMon;

    public String getStatisticalMethod() {
        return statisticalMethod;
    }

    public void setStatisticalMethod(String statisticalMethod) {
        this.statisticalMethod = statisticalMethod;
    }

    public String getGroupByDate() {
        return groupByDate;
    }

    public void setGroupByDate(String groupByDate) {
        this.groupByDate = groupByDate;
    }

    public Byte getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }

    public String getQueryCriteria() {
        return queryCriteria;
    }

    public void setQueryCriteria(String queryCriteria) {
        this.queryCriteria = queryCriteria;
    }
    private String deptIdLike;


    public Long getWritorId() {
        return writorId;
    }

    public void setWritorId(Long writorId) {
        this.writorId = writorId;
    }

    public String getWritorName() {
        return writorName;
    }

    public void setWritorName(String writorName) {
        this.writorName = writorName;
    }

    public String getDeptIdLike() {
        return deptIdLike;
    }

    public void setDeptIdLike(String deptIdLike) {
        this.deptIdLike = deptIdLike;
    }

    public List<Long> getUserParamList() {
        return userParamList;
    }

    public void setUserParamList(List<Long> userParamList) {
        this.userParamList = userParamList;
    }

    public Integer getMon() {
        return mon;
    }

    public void setMon(Integer mon) {
        this.mon = mon;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public Long getBusinessPlaceCode() {
        return businessPlaceCode;
    }

    public void setBusinessPlaceCode(Long businessPlaceCode) {
        this.businessPlaceCode = businessPlaceCode;
    }



    public Boolean getAgainStat() {
        return isAgainStat;
    }

    public void setAgainStat(Boolean againStat) {
        isAgainStat = againStat;
    }

    public String getBusinessPlaceName() {
        return businessPlaceName;
    }

    public void setBusinessPlaceName(String businessPlaceName) {
        this.businessPlaceName = businessPlaceName;
    }

    public Long getWriteSectId() {
        return writeSectId;
    }

    public void setWriteSectId(Long writeSectId) {
        this.writeSectId = writeSectId;
    }

    public String getWriteSectionName() {
        return writeSectionName;
    }

    public void setWriteSectionName(String writeSectionName) {
        this.writeSectionName = writeSectionName;
    }

    public List<Long> getDeptParamList() {
        return deptParamList;
    }

    public void setDeptParamList(List<Long> deptParamList) {
        this.deptParamList = deptParamList;
    }

    public List<Long> getSettParamList() {
        return settParamList;
    }

    public void setSettParamList(List<Long> settParamList) {
        this.settParamList = settParamList;
    }

    public Long getDeptParam() {
        return deptParam;
    }

    public void setDeptParam(Long deptParam) {
        this.deptParam = deptParam;
    }

    public Long getUserParam() {
        return userParam;
    }

    public void setUserParam(Long userParam) {
        this.userParam = userParam;
    }

    public Long getSettParam() {
        return settParam;
    }

    public void setSettParam(Long settParam) {
        this.settParam = settParam;
    }

    public Long getWriteSectIdParam() {
        return writeSectIdParam;
    }

    public void setWriteSectIdParam(Long writeSectIdParam) {
        this.writeSectIdParam = writeSectIdParam;
    }

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
}
