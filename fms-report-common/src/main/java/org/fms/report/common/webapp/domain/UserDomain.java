package org.fms.report.common.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.annotation.TablePrimaryKey;
import com.riozenc.titanTool.mybatis.MybatisEntity;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDomain extends ManagerParamEntity implements MybatisEntity {
    private Long id;            // ID ID bigint
    private String userNo;        // 客户编号 USER_NO varchar(16)
    private String userName;    // 客户名称 USER_NAME varchar(64)
    private Integer mon;
    private Byte userType; // 用户类别
    private List ids;
    private String address;
    private List<Long> writeSectIds;
    private BigDecimal allCapacity;
    private Long customerId;

    public BigDecimal getAllCapacity() {
        return allCapacity;
    }

    public void setAllCapacity(BigDecimal allCapacity) {
        this.allCapacity = allCapacity;
    }

    public List<Long> getWriteSectIds() {
        return writeSectIds;
    }

    public void setWriteSectIds(List<Long> writeSectIds) {
        this.writeSectIds = writeSectIds;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List getIds() {
        return ids;
    }

    public void setIds(List ids) {
        this.ids = ids;
    }

    public Byte getUserType() {
        return userType;
    }

    public void setUserType(Byte userType) {
        this.userType = userType;
    }

    public Integer getMon() {
        return mon;
    }

    public void setMon(Integer mon) {
        this.mon = mon;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
