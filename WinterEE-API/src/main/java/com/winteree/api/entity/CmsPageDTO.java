package com.winteree.api.entity;

import lombok.Data;

import java.util.Date;

/**
 * <p>Title: CmsPageDTO</p>
 * <p>Description: CMS系统自定义页面传输对象</p>
 *
 * @author RenFei
 * @date : 2020-08-05 20:12
 */
@Data
public class CmsPageDTO {
    private Long id;
    private String uuid;
    private String siteUuid;
    private String title;
    private Long views;
    private Long thumbsUp;
    private Long thumbsDown;
    private Date releaseTime;
    private Date addTime;
    private Boolean isDelete;
    private String featuredImage;
    private String content;
    private String describes;
    private String keyword;
}
