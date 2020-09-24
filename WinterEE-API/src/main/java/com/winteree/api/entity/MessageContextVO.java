package com.winteree.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Title: MessageContextVO</p>
 * <p>Description: 消息内容传递对象</p>
 *
 * @author RenFei
 * @date : 2020-09-23 21:39
 */
@Data
@ApiModel("消息内容传递对象")
public class MessageContextVO extends MessageVO {
    @ApiModelProperty("消息正文")
    private String content;
    @ApiModelProperty("消息扩展链接")
    private String extendedLinks;
}
