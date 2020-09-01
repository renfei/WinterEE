package com.winteree.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "CMS系统自定义页面传输对象", description = "CMS系统自定义页面传输对象")
public class CmsPageDTO {
    @ApiModelProperty(value = "自增ID")
    private Long id;
    @ApiModelProperty(value = "唯一编号")
    private String uuid;
    @ApiModelProperty(value = "所属站点唯一编号")
    private String siteUuid;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "浏览量")
    private Long views;
    @ApiModelProperty(value = "点赞数量")
    private Long thumbsUp;
    @ApiModelProperty(value = "点踩数量")
    private Long thumbsDown;
    @ApiModelProperty(value = "发布时间")
    private Date releaseTime;
    @ApiModelProperty(value = "添加时间")
    private Date addTime;
    @ApiModelProperty(value = "软删除")
    private Boolean isDelete;
    @ApiModelProperty(value = "特色图像")
    private String featuredImage;
    @ApiModelProperty(value = "正文内容")
    private String content;
    @ApiModelProperty(value = "简介")
    private String describes;
    @ApiModelProperty(value = "关键字")
    private String keyword;
}
