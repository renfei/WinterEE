package com.winteree.api.entity;

/**
 * <p>Title: MsgType</p>
 * <p>Description: 消息类型</p>
 *
 * @author RenFei
 * @date : 2020-09-23 21:47
 */
public enum MsgType {
    /**
     * 租户下全部数据
     */
    STATION(0),
    /**
     * 本公司下的数据
     */
    EMAIL(1),
    /**
     * 本公司下的和本部门下的数据
     */
    APP(2);

    private int value;

    MsgType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static MsgType valueOf(int value) {
        switch (value) {
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
}
