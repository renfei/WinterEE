package com.winteree.api.entity;

/**
 * <p>Title: LicenseType</p>
 * <p>Description: 授权类型</p>
 *
 * @author RenFei
 * @date : 2020-08-05 23:05
 */
public enum LicenseType {
    /**
     * 社区开源
     */
    COMMUNITY("COMMUNITY"),
    /**
     * 演示版
     */
    DEMO("DEMO"),
    /**
     * 商用版
     */
    COMMERCIAL("COMMERCIAL");
    private final String type;

    LicenseType(String type) {
        this.type = type;
    }

    public String value() {
        return this.type;
    }
}
