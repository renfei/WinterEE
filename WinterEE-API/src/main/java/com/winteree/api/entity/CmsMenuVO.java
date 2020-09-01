package com.winteree.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>Title: CmsMenuVO</p>
 * <p>Description: 菜单</p>
 *
 * @author RenFei
 * @date : 2020-07-16 10:41
 */
@Data
@ApiModel(value = "CMS系统菜单", description = "CMS系统菜单")
public class CmsMenuVO {
    @ApiModelProperty(value = "唯一编号")
    private String uuid;
    @ApiModelProperty(value = "父级唯一编号")
    private String puuid;
    @ApiModelProperty(value = "所属站点唯一编号")
    private String siteUuid;
    @ApiModelProperty(value = "菜单文字")
    private String menuText;
    @ApiModelProperty(value = "菜单连接")
    private String menuLink;
    @ApiModelProperty(value = "菜单图标")
    private String menuIcon;
    @ApiModelProperty(value = "是否新窗口打开")
    private Boolean isNewWin;
    @ApiModelProperty(value = "菜单类型")
    private Integer menuType;
    @ApiModelProperty(value = "菜单排序")
    private Integer orderNumber;
    @ApiModelProperty(value = "子级菜单")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CmsMenuVO> children;
}
