package com.winteree.api.entity;

/**
 * <p>Title: GeospatialType</p>
 * <p>Description: 地理空间数据类型</p>
 *
 * @author RenFei
 * @date : 2020-06-11 14:34
 */
public enum GeospatialEnum {
    /**
     * 租户
     */
    TENANT(1),
    /**
     * 公司
     */
    COMPANY(2);
    private int type;

    GeospatialEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static GeospatialEnum valueOf(int value) {
        switch (value) {
            case 1:
                return TENANT;
            case 2:
                return COMPANY;
            default:
                return null;
        }
    }

    public int value() {
        return this.type;
    }
}
