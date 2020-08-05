package com.winteree.core.dao.entity;

import java.util.Date;

public class CoreSettingDO {
    private Long id;

    private String uuid;

    private String myKey;

    private String myValue;

    private Date createTime;

    private Date updateTime;

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

    public String getMyKey() {
        return myKey;
    }

    public void setMyKey(String myKey) {
        this.myKey = myKey == null ? null : myKey.trim();
    }

    public String getMyValue() {
        return myValue;
    }

    public void setMyValue(String myValue) {
        this.myValue = myValue == null ? null : myValue.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}