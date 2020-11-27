package com.winteree.api.entity;

/**
 * <p>Title: ValidationType</p>
 * <p>Description: 验证码类别</p>
 *
 * @author RenFei
 * @date : 2020-07-21 23:09
 */
public enum ValidationType {
    /**
     * 登陆验证码
     */
    SIGNIN("SIGNIN"),
    /**
     * 注册证码
     */
    SIGNUP("SIGNUP"),
    /**
     * 重置密码验证码
     */
    RESETPWD("RESETPWD");
    private final String type;

    ValidationType(String type) {
        this.type = type;
    }

    public String value() {
        return this.type;
    }
}
