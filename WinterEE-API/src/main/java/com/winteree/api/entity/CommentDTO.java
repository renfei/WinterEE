package com.winteree.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>Title: CommentDTO</p>
 * <p>Description: 评论数据传输对象</p>
 *
 * @author RenFei
 * @date : 2020-07-28 22:56
 */
@Data
@ApiModel(value = "评论数据传输对象", description = "评论数据传输对象")
public class CommentDTO {
    @ApiModelProperty(value = "自增ID")
    private Long id;
    @ApiModelProperty(value = "唯一编号")
    private String uuid;
    @ApiModelProperty(value = "文章唯一编号")
    private String postUuid;
    @ApiModelProperty(value = "账户唯一编号")
    private String accountUuid;
    @ApiModelProperty(value = "添加时间")
    private Date addtime;
    @ApiModelProperty(value = "是否删除")
    private Boolean isDelete;
    @ApiModelProperty(value = "父级ID")
    private Long parentId;
    @ApiModelProperty(value = "是否是官方作者")
    private Boolean isOwner;
    @ApiModelProperty(value = "评论人名称")
    private String author;
    @ApiModelProperty(value = "评论人邮箱")
    private String authorEmail;
    @ApiModelProperty(value = "评论人的连接")
    private String authorUrl;
    @ApiModelProperty(value = "评论人IP地址")
    private String authorIp;
    @ApiModelProperty(value = "评论人所在地")
    private String authorAddress;
    @ApiModelProperty(value = "评论内容")
    private String content;
    @ApiModelProperty(value = "子级评论")
    private List<CommentDTO> child;
}
