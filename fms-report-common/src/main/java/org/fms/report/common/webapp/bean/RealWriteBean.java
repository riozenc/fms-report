package org.fms.report.common.webapp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RealWriteBean {

    // 应抄有功表数
    private Integer sYNumber;

    // 应抄无功表数
    private Integer sWNumber;

    // 应抄表总数
    private Integer sTNumber;

    // 实抄有功表数
    private Integer fYNumber;

    // 实抄无功表数
    private Integer fWNumber;

    // 实抄表总数
    private Integer fTNumber;

    // 实抄率
    private String fPercent;

    // 划零有功表数
    private Integer zYNumber;

    // 划零无功表数
    private Integer zWNumber;

    // 异常表数
    private Integer abnormalNumber;

    private String firstName;

    private String firstValue;

    private String writorName;

    private String writeSectName;

    public Integer getsYNumber() {
        return sYNumber;
    }

    public void setsYNumber(Integer sYNumber) {
        this.sYNumber = sYNumber;
    }

    public Integer getsWNumber() {
        return sWNumber;
    }

    public void setsWNumber(Integer sWNumber) {
        this.sWNumber = sWNumber;
    }

    public Integer getsTNumber() {
        return sTNumber;
    }

    public void setsTNumber(Integer sTNumber) {
        this.sTNumber = sTNumber;
    }

    public Integer getfYNumber() {
        return fYNumber;
    }

    public void setfYNumber(Integer fYNumber) {
        this.fYNumber = fYNumber;
    }

    public Integer getfWNumber() {
        return fWNumber;
    }

    public void setfWNumber(Integer fWNumber) {
        this.fWNumber = fWNumber;
    }

    public Integer getfTNumber() {
        return fTNumber;
    }

    public void setfTNumber(Integer fTNumber) {
        this.fTNumber = fTNumber;
    }

    public Integer getzYNumber() {
        return zYNumber;
    }

    public void setzYNumber(Integer zYNumber) {
        this.zYNumber = zYNumber;
    }

    public Integer getzWNumber() {
        return zWNumber;
    }

    public void setzWNumber(Integer zWNumber) {
        this.zWNumber = zWNumber;
    }

    public Integer getAbnormalNumber() {
        return abnormalNumber;
    }

    public void setAbnormalNumber(Integer abnormalNumber) {
        this.abnormalNumber = abnormalNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstValue() {
        return firstValue;
    }

    public void setFirstValue(String firstValue) {
        this.firstValue = firstValue;
    }

    public String getfPercent() {
        return fPercent;
    }

    public void setfPercent(String fPercent) {
        this.fPercent = fPercent;
    }

    public String getWritorName() {
        return writorName;
    }

    public void setWritorName(String writorName) {
        this.writorName = writorName;
    }

    public String getWriteSectName() {
        return writeSectName;
    }

    public void setWriteSectName(String writeSectName) {
        this.writeSectName = writeSectName;
    }
}
