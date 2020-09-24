package com.winteree.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>Title: MessageVO</p>
 * <p>Description: 消息传递对象</p>
 *
 * @author RenFei
 * @date : 2020-09-23 21:37
 */
@Data
@ApiModel("消息传递对象")
public class MessageVO {
    @ApiModelProperty("消息UUID")
    private String uuid;
    @ApiModelProperty("发送者")
    private String sender;
    @ApiModelProperty("消息标题")
    private String title;
    @ApiModelProperty("是否阅读")
    private Boolean isRead;
    @ApiModelProperty("发送时间")
    private Date sentDate;
}
