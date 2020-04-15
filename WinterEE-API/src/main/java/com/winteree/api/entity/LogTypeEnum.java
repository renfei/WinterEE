package com.winteree.api.entity;

/**
 * <p>Title: LogTypeEnum</p>
 * <p>Description: 日志类型枚举</p>
 *
 * @author RenFei
 * @date : 2020-04-12 22:13
 */
public enum LogTypeEnum {
    /**
     * 访问类型日志
     */
    ACCESS("ACCESS"),
    /**
     * 操作类型日志
     */
    OPERATION("OPERATION"),
    /**
     * 登陆类型日志
     */
    SIGNING("SIGNING"),
    /**
     * 系统类型日志
     */
    SYSTEM("SYSTEM");
    private String type;

    LogTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
