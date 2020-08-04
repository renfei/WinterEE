package com.winteree.api.entity;

import lombok.Data;

/**
 * <p>Title: TableInfoDTO</p>
 * <p>Description: </p>
 *
 * @author RenFei
 * @date : 2020-08-04 21:11
 */
@Data
public class TableInfoDTO {
    private String columnName;
    private String isNullable;
    private String dataType;
    private String length;
    private String columnComment;
}
