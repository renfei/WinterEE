package com.winteree.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>Title: MenuVO</p>
 * <p>Description: 菜单视图实体</p>
 *
 * @author RenFei
 * @date : 2020-04-18 15:30
 */
@Data
@ApiModel(value = "菜单视图实体", description = "菜单视图实体")
public class MenuVO {
    @ApiModelProperty(value = "自增ID")
    private Long id;
    @ApiModelProperty(value = "唯一编号")
    private String uuid;
    @ApiModelProperty(value = "父级唯一编号")
    private String parentUuid;
    @ApiModelProperty(value = "图标")
    private String icon;
    @ApiModelProperty(value = "下拉图标")
    private String icondown;
    @ApiModelProperty(value = "菜单文字")
    private String text;
    @ApiModelProperty(value = "排序")
    private Long sort;
    @ApiModelProperty(value = "链接")
    private String href;
    @ApiModelProperty(value = "打开方式")
    private String target;
    @ApiModelProperty(value = "是否显示")
    private Boolean isShow;
    @ApiModelProperty(value = "是否是菜单")
    private Boolean isMenu;
    @ApiModelProperty(value = "权限表达式")
    private String permission;
    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "是否删除")
    private Boolean isDelete;
    @ApiModelProperty(value = "国际化代码")
    private String i18n;
    private Boolean model = false;
    @ApiModelProperty(value = "子级菜单")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MenuVO> children;
}
