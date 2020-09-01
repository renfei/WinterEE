package com.winteree.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Title: CmsTagDTO</p>
 * <p>Description: CMS系统文章标签传输对象</p>
 *
 * @author RenFei
 * @date : 2020-06-28 20:04
 */
@Data
@ApiModel(value = "CMS系统文章标签", description = "CMS系统文章标签")
public class CmsTagDTO {
    @ApiModelProperty(value = "所属站点唯一编号")
    private String siteUuid;
    @ApiModelProperty(value = "英文名（URL会用到）")
    private String enName;
    @ApiModelProperty(value = "中文名称")
    private String zhName;
    @ApiModelProperty(value = "唯一编号")
    private String uuid;
    @ApiModelProperty(value = "描述")
    private String describe;
    @ApiModelProperty(value = "标签下的文章数量")
    private Long count;
}
