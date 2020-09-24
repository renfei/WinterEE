package com.winteree.api.entity;

/**
 * <p>Title: MessageScope</p>
 * <p>Description: 消息范围</p>
 *
 * @author RenFei
 */
public enum MessageScope {
    /**
     * 全系统广播
     */
    ALL(0),
    /**
     * 指定租户广播
     */
    TENANT(1),
    /**
     * 指定公司广播
     */
    COMPANY(2),
    /**
     * 指定部门广播
     */
    DEPARTMENT(3);

    private final int value;

    MessageScope(int value) {
        this.value = value;
    }

    public static MessageScope valueOf(int value) {
        switch (value) {
            case 0:
                return ALL;
            case 1:
                return TENANT;
            case 2:
                return COMPANY;
            case 3:
                return DEPARTMENT;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
