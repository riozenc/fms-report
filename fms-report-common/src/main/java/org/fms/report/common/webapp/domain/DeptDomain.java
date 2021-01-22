package org.fms.report.common.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.mybatis.MybatisEntity;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeptDomain extends ParamDomain implements MybatisEntity {
    private Long id;
    private Long parentId;
    private Long deptId;
    private String deptName;  // 营业区域 DEPT_NAME varchar(64)
    private Integer depeType;
    private String title;
    private List<DeptDomain> children;


    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getDepeType() {
        return depeType;
    }

    public void setDepeType(Integer depeType) {
        this.depeType = depeType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DeptDomain> getChildren() {
        return children;
    }

    public void setChildren(List<DeptDomain> children) {
        this.children = children;
    }
}
