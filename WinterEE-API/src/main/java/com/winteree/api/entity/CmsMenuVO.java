package com.winteree.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class CmsMenuVO {
    private String uuid;
    private String puuid;
    private String siteUuid;
    private String menuText;
    private String menuLink;
    private String menuIcon;
    private Boolean isNewWin;
    private Integer menuType;
    private Integer orderNumber;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CmsMenuVO> children;
}
