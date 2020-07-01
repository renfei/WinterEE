package com.winteree.api.entity;

import lombok.Data;

/**
 * <p>Title: PasswordResetDAT</p>
 * <p>Description: 密码重置数据传输对象</p>
 *
 * @author RenFei
 * @date : 2020-06-24 09:59
 */
@Data
public class PasswordResetDAT {
    String accountUuid;
    String newPassword;
    String language;
    String keyid;
}
