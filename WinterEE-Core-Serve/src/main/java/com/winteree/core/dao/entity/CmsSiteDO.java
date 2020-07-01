package com.winteree.core.dao.entity;

import java.util.Date;

public class CmsSiteDO {
    private Long id;

    private String uuid;

    private String tenantUuid;

    private String officeUuid;

    private String departmentUuid;

    private String siteName;

    private String siteDomain;

    private String siteKeyword;

    private String siteDescription;

    private String icpNo;

    private Boolean isComment;

    private String gonganNo;

    private String analysisCode;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;

    private Boolean siteEnable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public String getTenantUuid() {
        return tenantUuid;
    }

    public void setTenantUuid(String tenantUuid) {
        this.tenantUuid = tenantUuid == null ? null : tenantUuid.trim();
    }

    public String getOfficeUuid() {
        return officeUuid;
    }

    public void setOfficeUuid(String officeUuid) {
        this.officeUuid = officeUuid == null ? null : officeUuid.trim();
    }

    public String getDepartmentUuid() {
        return departmentUuid;
    }

    public void setDepartmentUuid(String departmentUuid) {
        this.departmentUuid = departmentUuid == null ? null : departmentUuid.trim();
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName == null ? null : siteName.trim();
    }

    public String getSiteDomain() {
        return siteDomain;
    }

    public void setSiteDomain(String siteDomain) {
        this.siteDomain = siteDomain == null ? null : siteDomain.trim();
    }

    public String getSiteKeyword() {
        return siteKeyword;
    }

    public void setSiteKeyword(String siteKeyword) {
        this.siteKeyword = siteKeyword == null ? null : siteKeyword.trim();
    }

    public String getSiteDescription() {
        return siteDescription;
    }

    public void setSiteDescription(String siteDescription) {
        this.siteDescription = siteDescription == null ? null : siteDescription.trim();
    }

    public String getIcpNo() {
        return icpNo;
    }

    public void setIcpNo(String icpNo) {
        this.icpNo = icpNo == null ? null : icpNo.trim();
    }

    public Boolean getIsComment() {
        return isComment;
    }

    public void setIsComment(Boolean isComment) {
        this.isComment = isComment;
    }

    public String getGonganNo() {
        return gonganNo;
    }

    public void setGonganNo(String gonganNo) {
        this.gonganNo = gonganNo == null ? null : gonganNo.trim();
    }

    public String getAnalysisCode() {
        return analysisCode;
    }

    public void setAnalysisCode(String analysisCode) {
        this.analysisCode = analysisCode == null ? null : analysisCode.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public Boolean getSiteEnable() {
        return siteEnable;
    }

    public void setSiteEnable(Boolean siteEnable) {
        this.siteEnable = siteEnable;
    }
}