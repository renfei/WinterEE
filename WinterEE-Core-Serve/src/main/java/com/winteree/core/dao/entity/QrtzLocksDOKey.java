package com.winteree.core.dao.entity;

public class QrtzLocksDOKey {
    private String schedName;

    private String lockName;

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName == null ? null : schedName.trim();
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName == null ? null : lockName.trim();
    }
}