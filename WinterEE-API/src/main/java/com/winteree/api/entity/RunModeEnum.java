package com.winteree.api.entity;

/**
 * <p>Title: RunModeEnum</p>
 * <p>Description: 运行模式枚举</p>
 *
 * @author RenFei
 * @date : 2020-04-24 16:58
 */
public enum RunModeEnum {
    /**
     * 演示模式
     */
    DEMO("demo"),
    /**
     * 开发模式
     */
    DEV("dev"),
    /**
     * 生产模式
     */
    PROD("prod");
    private String mode;

    RunModeEnum(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
