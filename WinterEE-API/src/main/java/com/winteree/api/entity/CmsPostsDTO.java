package com.winteree.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * <p>Title: CmsPostsDTO</p>
 * <p>Description: CMS系统文章传输对象</p>
 *
 * @author RenFei
 * @date : 2020-06-27 21:52
 */
@Data
public class CmsPostsDTO {
    private String uuid;
    private String siteUuid;
    private String categoryUuid;
    private Boolean isOriginal;
    private Long views;
    private Long thumbsUp;
    private Long thumbsDown;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date releaseTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;
    private String createBy;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updateTime;
    private String updateBy;
    private Boolean isDelete;
    private Boolean isComment;
    private Double avgViews;
    private Double avgComment;
    private Double pageRank;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date pageRankUpdateTime;
    private String featuredImage;
    private String title;
    private String content;
    private String sourceUrl;
    private String sourceName;
    private String describes;
    private String keyword;
    private List<String> tagIds;
}
