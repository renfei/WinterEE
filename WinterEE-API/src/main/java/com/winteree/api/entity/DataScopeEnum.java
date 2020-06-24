package com.winteree.api.entity;

/**
 * <p>Title: DataScopeEnum</p>
 * <p>Description: 数据范围</p>
 *
 * @author RenFei
 * @date : 2020-06-15 10:36
 */
public enum DataScopeEnum {
    /**
     * 租户下全部数据
     */
    ALL(0),
    /**
     * 本公司下的数据
     */
    COMPANY(1),
    /**
     * 本公司下的和本部门下的数据
     */
    COMPANY_AND_DEPARTMENT(2),
    /**
     * 本部门下的数据
     */
    DEPARTMENT(3);

    private int value;

    DataScopeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static DataScopeEnum valueOf(int value) {
        switch (value) {
            case 0:
                return ALL;
            default:
                return null;
        }
    }
}
