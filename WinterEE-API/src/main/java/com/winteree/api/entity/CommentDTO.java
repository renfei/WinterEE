package com.winteree.api.entity;

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
public class CommentDTO {
    private Long id;
    private String uuid;
    private String postUuid;
    private String accountUuid;
    private Date addtime;
    private Boolean isDelete;
    private Long parentId;
    private Boolean isOwner;
    private String author;
    private String authorEmail;
    private String authorUrl;
    private String authorIp;
    private String authorAddress;
    private String content;
    private List<CommentDTO> child;
}
