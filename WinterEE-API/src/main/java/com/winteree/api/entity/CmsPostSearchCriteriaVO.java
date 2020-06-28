package com.winteree.api.entity;

import lombok.Data;

/**
 * <p>Title: CmsPostSearchCriteriaVO</p>
 * <p>Description: CMS系统文章搜索条件</p>
 *
 * @author RenFei
 * @date : 2020-06-27 21:33:13
 */
@Data
public class CmsPostSearchCriteriaVO {
    // 所属站点，必选
    private String siteUuid;
    // 所属分类，可选
    private String categoryUuid;
    // 标题关键字，可选
    private String title;
    private Integer pages;
    private Integer rows;
}
