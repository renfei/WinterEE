package com.winteree.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>Title: VerificationCodeDTO</p>
 * <p>Description: 验证码数据传输对象</p>
 *
 * @author RenFei
 * @date : 2020-07-21 22:03
 */
@Data
@ApiModel(value = "验证码数据传输对象", description = "验证码数据传输对象")
public class VerificationCodeDTO {
    @ApiModelProperty(value = "唯一编号")
    private String uuid;
    @ApiModelProperty(value = "账户唯一编号")
    private String accountUuid;
    @ApiModelProperty(value = "电话号码")
    private String phone;
    @ApiModelProperty(value = "电子邮箱")
    private String email;
    @ApiModelProperty(value = "验证码")
    private String verificationCode;
    @ApiModelProperty(value = "过期时间")
    private Date deadDate;
    @ApiModelProperty(value = "是否已经被使用")
    private Boolean usable;
    @ApiModelProperty(value = "是否已经发送")
    private Boolean sended;
    @ApiModelProperty(value = "内容")
    private String contentText;
    @ApiModelProperty(value = "验证码的类型")
    private ValidationType validationType;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
