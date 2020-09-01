package com.winteree.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "CMS系统文章传输对象", description = "CMS系统文章传输对象")
public class CmsPostsDTO {
    @ApiModelProperty(value = "自增ID")
    private Long id;
    @ApiModelProperty(value = "唯一编号")
    private String uuid;
    @ApiModelProperty(value = "所属站点唯一编号")
    private String siteUuid;
    @ApiModelProperty(value = "所属文章分类唯一编号")
    private String categoryUuid;
    @ApiModelProperty(value = "是否原创")
    private Boolean isOriginal;
    @ApiModelProperty(value = "浏览量")
    private Long views;
    @ApiModelProperty(value = "点赞数量")
    private Long thumbsUp;
    @ApiModelProperty(value = "点踩数量")
    private Long thumbsDown;
    @ApiModelProperty(value = "发布时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date releaseTime;
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;
    @ApiModelProperty(value = "创建人唯一编号")
    private String createBy;
    @ApiModelProperty(value = "更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updateTime;
    @ApiModelProperty(value = "更新人唯一编号")
    private String updateBy;
    @ApiModelProperty(value = "软删除")
    private Boolean isDelete;
    @ApiModelProperty(value = "是否允许评论")
    private Boolean isComment;
    @ApiModelProperty(value = "平均浏览指数")
    private Double avgViews;
    @ApiModelProperty(value = "平均评论指数")
    private Double avgComment;
    @ApiModelProperty(value = "页面评级")
    private Double pageRank;
    @ApiModelProperty(value = "页面评级更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date pageRankUpdateTime;
    @ApiModelProperty(value = "特色图像")
    private String featuredImage;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "正文内容")
    private String content;
    @ApiModelProperty(value = "原文地址")
    private String sourceUrl;
    @ApiModelProperty(value = "来源名称")
    private String sourceName;
    @ApiModelProperty(value = "简介")
    private String describes;
    @ApiModelProperty(value = "关键字")
    private String keyword;
    @ApiModelProperty(value = "标签列表")
    private List<String> tagIds;
}
