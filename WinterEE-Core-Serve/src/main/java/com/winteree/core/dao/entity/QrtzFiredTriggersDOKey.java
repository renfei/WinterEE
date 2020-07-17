package com.winteree.core.dao.entity;

public class QrtzFiredTriggersDOKey {
    private String schedName;

    private String entryId;

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName == null ? null : schedName.trim();
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId == null ? null : entryId.trim();
    }
}