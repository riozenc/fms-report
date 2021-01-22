package org.fms.report.common.webapp.returnDomain;

import java.io.Serializable;
import java.util.Date;

public class UserInfoEntity implements Serializable {
    private Integer id;

    private String userId;

    private String userName;

    private String password;

    private String phone;

    /**
     * male 男、female 女
     */
    private Integer sex;

    /**
     * 0 禁用、1 启用
     */
    private Integer status;

    private String mailAddress;

    private String imageUrl;

    private String remark;

    private Date createDate;

    private Date updateDate;

    private static final long serialVersionUID = 1L;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getSex() {
        return sex;
    }

    /**
     * 0：女    1：男
     * @param sex
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getStatus() {
        return status;
    }

    /**
     * 1 正常、2 禁用、3 删除、4 锁定
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}