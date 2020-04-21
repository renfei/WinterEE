package com.winteree.api.entity;

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
public class MenuVO {
    private Long id;
    private String uuid;
    private String parentUuid;
    private String icon;
    private String icondown;
    private String text;
    private Long sort;
    private String href;
    private String target;
    private Boolean isShow;
    private Boolean isMenu;
    private String permission;
    private String remarks;
    private Boolean isDelete;
    private String i18n;
    private Boolean model = false;
    private List<MenuVO> children;
}
