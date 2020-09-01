package com.winteree.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Title: CmsCategoryDTO</p>
 * <p>Description: CMS系统分类数据传输对象</p>
 *
 * @author RenFei
 * @date : 2020-06-27 20:51
 */
@Data
@ApiModel(value = "CMS系统分类数据传输对象", description = "CMS系统分类数据传输对象")
public class CmsCategoryDTO {
    @ApiModelProperty(value = "所属站点唯一编号")
    private String siteUuid;
    @ApiModelProperty(value = "英文名称（URL中要使用）")
    private String enName;
    @ApiModelProperty(value = "中文名称")
    private String zhName;
    @ApiModelProperty(value = "唯一编号")
    private String uuid;
}
