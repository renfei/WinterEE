package com.winteree.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>Title: ListData</p>
 * <p>Description: 数据承载类</p>
 *
 * @author RenFei
 * @date : 2020-04-26 21:26
 */
@Data
@ApiModel(value = "数据承载对象", description = "数据承载对象")
public class ListData<T> {
    @ApiModelProperty(value = "数据负载")
    List<T> data;
    @ApiModelProperty(value = "对象总数")
    Long total;
}
