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
    private String icon;
    private String icondown;
    private String text;
    private String href;
    private Boolean model = false;
    private List<MenuVO> children;
}
