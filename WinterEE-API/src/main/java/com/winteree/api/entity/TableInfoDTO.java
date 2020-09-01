package com.winteree.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Title: TableInfoDTO</p>
 * <p>Description: </p>
 *
 * @author RenFei
 * @date : 2020-08-04 21:11
 */
@Data
@ApiModel(value = "数据库表信息数据传递对象", description = "数据库表信息数据传递对象")
public class TableInfoDTO {
    @ApiModelProperty(value = "列名")
    private String columnName;
    @ApiModelProperty(value = "是否允许为NULL")
    private String isNullable;
    @ApiModelProperty(value = "字段类型")
    private String dataType;
    @ApiModelProperty(value = "字段长度")
    private String length;
    @ApiModelProperty(value = "列备注")
    private String columnComment;
}
