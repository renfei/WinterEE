package com.winteree.api.entity;

/**
 * <p>Title: CmsMenuEnum</p>
 * <p>Description: 内容管理系统中菜单类别</p>
 *
 * @author RenFei
 * @date : 2020-07-15 20:41
 */
public enum CmsMenuEnum {
    /**
     * 主菜单
     */
    MAIN(1),
    /**
     * 头部菜单
     */
    HEAD(2),
    /**
     * 页脚菜单
     */
    FOOT(3),
    /**
     * 页尾菜单
     */
    END(4);

    /**
     * 菜单类型
     */
    private final int type;

    CmsMenuEnum(int type) {
        this.type = type;
    }

    public int value() {
        return this.type;
    }

    public static CmsMenuEnum valueOf(int type) {
        switch (type) {
            case 1:
                return MAIN;
            case 2:
                return HEAD;
            case 3:
                return FOOT;
            case 4:
                return END;
            default:
                return null;
        }
    }
}
