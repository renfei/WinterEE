package com.winteree.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Title: CmsPostSearchCriteriaVO</p>
 * <p>Description: CMS系统文章搜索条件</p>
 *
 * @author RenFei
 * @date : 2020-06-27 21:33:13
 */
@Data
@ApiModel(value = "CMS文章搜索参数对象", description = "封装了CMS文章搜索参数")
public class CmsPostSearchCriteriaVO {
    // 所属站点，必选
    @ApiModelProperty(value = "所属站点唯一编号")
    private String siteUuid;
    // 所属分类，可选
    @ApiModelProperty(value = "所属文章分类唯一编号")
    private String categoryUuid;
    // 标题关键字，可选
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "分页查询的页码")
    private Integer pages;
    @ApiModelProperty(value = "分页查询的每页容量")
    private Integer rows;
}
