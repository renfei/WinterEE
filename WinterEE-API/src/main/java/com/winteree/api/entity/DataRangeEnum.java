package com.winteree.api.entity;

/**
 * <p>Title: DataRangeEnum</p>
 * <p>Description: 数据范围</p>
 *
 * @author RenFei
 * @date : 2020-06-12 12:57
 */
public enum DataRangeEnum {
    /**
     * 全部数据（危险）
     */
    ALL(0),
    /**
     * 本公司及以下
     */
    COMPANY(1),
    /**
     * 本公司和本部门
     */
    COMPANY_AND_DEPARTMENT(2),
    /**
     * 本部门
     */
    DEPARTMENT(3);

    private final int value;

    DataRangeEnum(int value) {
        this.value = value;
    }

    public static DataRangeEnum valueOf(int value) {
        switch (value) {
            case 0:
                return ALL;
            case 1:
                return COMPANY;
            case 2:
                return COMPANY_AND_DEPARTMENT;
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
