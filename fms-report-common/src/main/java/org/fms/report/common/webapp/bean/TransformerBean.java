package org.fms.report.common.webapp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransformerBean {

    private String transformerNo;
    private BigDecimal transformerGroupNo;
    private String isPubTypeName;
    private BigDecimal day;
    private String transformerLossTypeName;
    private BigDecimal capacity;
    private String transformerModelTypeName;

    public String getTransformerNo() {
        return transformerNo;
    }

    public void setTransformerNo(String transformerNo) {
        this.transformerNo = transformerNo;
    }

    public String getIsPubTypeName() {
        return isPubTypeName;
    }

    public void setIsPubTypeName(String isPubTypeName) {
        this.isPubTypeName = isPubTypeName;
    }


    public String getTransformerLossTypeName() {
        return transformerLossTypeName;
    }

    public void setTransformerLossTypeName(String transformerLossTypeName) {
        this.transformerLossTypeName = transformerLossTypeName;
    }

    public BigDecimal getCapacity() {
        return capacity;
    }

    public void setCapacity(BigDecimal capacity) {
        this.capacity = capacity;
    }

    public String getTransformerModelTypeName() {
        return transformerModelTypeName;
    }

    public void setTransformerModelTypeName(String transformerModelTypeName) {
        this.transformerModelTypeName = transformerModelTypeName;
    }

    public BigDecimal getTransformerGroupNo() {
        return transformerGroupNo;
    }

    public void setTransformerGroupNo(BigDecimal transformerGroupNo) {
        this.transformerGroupNo = transformerGroupNo;
    }

    public BigDecimal getDay() {
        return day;
    }

    public void setDay(BigDecimal day) {
        this.day = day;
    }
}
