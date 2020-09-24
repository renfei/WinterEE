package com.winteree.core.dao.entity;

public class MessageContextDOWithBLOBs extends MessageContextDO {
    private String content;

    private String extendedLinks;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getExtendedLinks() {
        return extendedLinks;
    }

    public void setExtendedLinks(String extendedLinks) {
        this.extendedLinks = extendedLinks == null ? null : extendedLinks.trim();
    }
}