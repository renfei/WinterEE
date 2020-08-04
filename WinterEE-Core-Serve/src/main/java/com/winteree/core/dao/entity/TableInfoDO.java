package com.winteree.core.dao.entity;

import lombok.Data;

/**
 * <p>Title: TableInfoDO</p>
 * <p>Description: 数据库表信息</p>
 *
 * @author RenFei
 * @date : 2020-08-04 21:03
 */
@Data
public class TableInfoDO {
    private String columnName;
    private String isNullable;
    private String dataType;
    private String length;
    private String columnComment;
}
