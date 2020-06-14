package com.winteree.api.entity;

/**
 * <p>Title: OrgEnum</p>
 * <p>Description: 机构类型</p>
 *
 * @author RenFei
 * @date : 2020-06-12 12:57
 */
public enum OrgEnum {
    /**
     * 公司
     */
    COMPANY(1),
    /**
     * 部门
     */
    DEPARTMENT(2),;

    private final int value;

    OrgEnum(int value) {
        this.value = value;
    }

    public static OrgEnum valueOf(int value) {
        switch (value) {
            case 1:
                return COMPANY;
            case 2:
                return DEPARTMENT;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
