package com.winteree.api.entity;

/**
 * <p>Title: MessageType</p>
 * <p>Description: 消息类型</p>
 *
 * @author RenFei
 */
public enum MessageType {
    /**
     * 全部渠道
     */
    ALL(-1),
    /**
     * 站内信
     */
    STATION(0),
    /**
     * 指定公司广播
     */
    EMAIL(1),
    /**
     * 指定部门广播
     */
    APP(2);

    private final int value;

    MessageType(int value) {
        this.value = value;
    }

    public static MessageType valueOf(int value) {
        switch (value) {
            case -1:
                return ALL;
            case 0:
                return STATION;
            case 1:
                return EMAIL;
            case 2:
                return APP;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
