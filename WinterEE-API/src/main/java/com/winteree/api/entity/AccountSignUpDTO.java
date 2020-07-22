package com.winteree.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>Title: AccountSignUpDTO</p>
 * <p>Description: 账户注册数据传输对象</p>
 *
 * @author RenFei
 * @date : 2020-07-21 21:41
 */
@Data
@ApiModel(value = "账户注册数据传输对象", description = "账户注册数据传输对象")
public class AccountSignUpDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户名/手机号/邮箱
     */
    @ApiModelProperty(value = "用户名/手机号/邮箱")
    private String userName;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * 验证码
     * 使用手机号/邮箱注册的时候需要验证码
     */
    @ApiModelProperty(value = "验证码，使用手机号/邮箱注册的时候需要验证码")
    private String verCode;
    /**
     * 加密秘钥的UUID
     */
    @ApiModelProperty(value = "加密秘钥的UUID")
    private String keyId;
    /**
     * 客户端ID
     */
    @ApiModelProperty(value = "客户端ID")
    private String clientId;
    /**
     * 姓氏
     */
    @ApiModelProperty(value = "姓氏")
    private String lastName;
    /**
     * 名字
     */
    @ApiModelProperty(value = "名字")
    private String firstName;
}
