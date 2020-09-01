package com.winteree.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Title: RegionDTO</p>
 * <p>Description: 行政区划数据传输对象</p>
 *
 * @author RenFei
 * @date : 2020-07-21 22:15
 */
@Data
@ApiModel(value = "行政区划数据传输对象", description = "行政区划数据传输对象")
public class RegionDTO {
    @ApiModelProperty(value = "行政区划代码")
    private String regionCode;
    @ApiModelProperty(value = "行政区划名称")
    private String regionName;
}
