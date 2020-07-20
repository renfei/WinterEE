package com.winteree.api.entity;

/**
 * <p>Title: EsDataType</p>
 * <p>Description: ES搜索引擎的数据类型</p>
 *
 * @author RenFei
 * @date : 2020-07-19 16:35
 */
public enum EsDataType {
    /**
     * 文章
     */
    POST(1),
    /**
     * 微博
     */
    WEIBO(2);
    private final int type;

    EsDataType(int type) {
        this.type = type;
    }

    public int value() {
        return type;
    }
}
