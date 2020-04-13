package com.winteree.api.entity;

/**
 * <p>Title: LogSubTypeEnum</p>
 * <p>Description: 日志二级类型枚举</p>
 *
 * @author RenFei
 * @date : 2020-04-12 22:19
 */
public enum LogSubTypeEnum {
    /**
     * 增加
     */
    INSERT("INSERT"),
    /**
     * 删除
     */
    DELETE("DELETE"),
    /**
     * 修改
     */
    UPDATE("UPDATE"),
    /**
     * 查询
     */
    SELECT("SELECT"),
    /**
     * 成功
     */
    SUCCESS("SUCCESS"),
    /**
     * 失败
     */
    FAIL("FAIL"),
    /**
     * 调试
     */
    DEBUG("DEBUG"),
    /**
     * 信息
     */
    INFO("INFO"),
    /**
     * 警告
     */
    WARN("WARN"),
    /**
     * 错误
     */
    ERROR("ERROR"),
    /**
     * 致命错误
     */
    FATAL("FATAL");
    private String type;

    LogSubTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
