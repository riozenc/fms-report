package org.fms.report.common.webapp.bean;

import java.math.BigDecimal;

public class ArrearageSumBean {
    //排序字段
    private String orderBy;

    private String name;

    private String value;
    // 欠费笔数
    private int arrearageNumber;
    // 欠费金额
    private BigDecimal oweMoneySum;

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getArrearageNumber() {
        return arrearageNumber;
    }

    public void setArrearageNumber(int arrearageNumber) {
        this.arrearageNumber = arrearageNumber;
    }

    public BigDecimal getOweMoneySum() {
        return oweMoneySum;
    }

    public void setOweMoneySum(BigDecimal oweMoneySum) {
        this.oweMoneySum = oweMoneySum;
    }
}
