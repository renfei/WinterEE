package com.winteree.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Title: PasswordResetDAT</p>
 * <p>Description: 密码重置数据传输对象</p>
 *
 * @author RenFei
 * @date : 2020-06-24 09:59
 */
@Data
@ApiModel(value = "密码重置数据传输对象", description = "密码重置数据传输对象")
public class PasswordResetDAT {
    @ApiModelProperty(value = "账户唯一编号")
    String accountUuid;
    @ApiModelProperty(value = "新密码")
    String newPassword;
    @ApiModelProperty(value = "语言")
    String language;
    @ApiModelProperty(value = "加密Key的唯一编码")
    String keyid;
}
