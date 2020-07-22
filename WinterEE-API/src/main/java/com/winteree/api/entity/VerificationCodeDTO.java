package com.winteree.api.entity;

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
public class VerificationCodeDTO {
    private String uuid;
    private String accountUuid;
    private String phone;
    private String email;
    private String verificationCode;
    private Date deadDate;
    private Boolean usable;
    private Boolean sended;
    private String contentText;
    private ValidationType validationType;
    private Date createTime;
    private Date updateTime;
}
