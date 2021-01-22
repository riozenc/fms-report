package org.fms.report.common.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.annotation.TablePrimaryKey;
import com.riozenc.titanTool.mybatis.MybatisEntity;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemCommonConfigDomain extends ManagerParamEntity implements MybatisEntity {
    @TablePrimaryKey
    private long id;
    private String type;
    private String paramName;
    private Integer paramKey;
    private String paramValue;
    private Short status;
    private Short paramOrder;
    private String remark1;
    private String remark2;
    private String remark3;
    private String remark4;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }


    public Integer getParamKey() {
        return paramKey;
    }

    public void setParamKey(Integer paramKey) {
        this.paramKey = paramKey;
    }


    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }


    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }


    public Short getParamOrder() {
        return paramOrder;
    }

    public void setParamOrder(Short paramOrder) {
        this.paramOrder = paramOrder;
    }


    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }


    public String getRemark3() {
        return remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }


    public String getRemark4() {
        return remark4;
    }

    public void setRemark4(String remark4) {
        this.remark4 = remark4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemCommonConfigDomain that = (SystemCommonConfigDomain) o;
        return id == that.id &&
                Objects.equals(type, that.type) &&
                Objects.equals(paramName, that.paramName) &&
                Objects.equals(paramKey, that.paramKey) &&
                Objects.equals(paramValue, that.paramValue) &&
                Objects.equals(status, that.status) &&
                Objects.equals(paramOrder, that.paramOrder) &&
                Objects.equals(remark1, that.remark1) &&
                Objects.equals(remark2, that.remark2) &&
                Objects.equals(remark3, that.remark3) &&
                Objects.equals(remark4, that.remark4);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, paramName, paramKey, paramValue, status, paramOrder, remark1, remark2, remark3, remark4);
    }
}
