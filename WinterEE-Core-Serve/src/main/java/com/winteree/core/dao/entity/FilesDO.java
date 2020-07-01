package com.winteree.core.dao.entity;

import java.util.Date;

public class FilesDO {
    private Long id;

    private String uuid;

    private String tenantUuid;

    private String officeUuid;

    private String departmentUuid;

    private String fileName;

    private String originalFileName;

    private String storageType;

    private String storagePath;

    private Date createTime;

    private String createBy;

    private String buckeName;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName == null ? null : originalFileName.trim();
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType == null ? null : storageType.trim();
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath == null ? null : storagePath.trim();
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

    public String getBuckeName() {
        return buckeName;
    }

    public void setBuckeName(String buckeName) {
        this.buckeName = buckeName == null ? null : buckeName.trim();
    }
}