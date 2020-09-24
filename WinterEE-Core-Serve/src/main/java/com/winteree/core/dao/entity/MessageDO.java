package com.winteree.core.dao.entity;

import java.util.Date;

public class MessageDO {
    private Long id;

    private String uuid;

    private String contextUuid;

    private Integer msgType;

    private String sendUuid;

    private String receiveUuid;

    private String title;

    private Boolean isRead;

    private Date sentDate;

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

    public String getContextUuid() {
        return contextUuid;
    }

    public void setContextUuid(String contextUuid) {
        this.contextUuid = contextUuid == null ? null : contextUuid.trim();
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getSendUuid() {
        return sendUuid;
    }

    public void setSendUuid(String sendUuid) {
        this.sendUuid = sendUuid == null ? null : sendUuid.trim();
    }

    public String getReceiveUuid() {
        return receiveUuid;
    }

    public void setReceiveUuid(String receiveUuid) {
        this.receiveUuid = receiveUuid == null ? null : receiveUuid.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }
}