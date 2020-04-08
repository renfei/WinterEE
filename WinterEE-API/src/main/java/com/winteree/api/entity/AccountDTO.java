package com.winteree.api.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author RenFei
 */
@Data
public class AccountDTO {
    private String uuid;
    private Date createTime;
    private String userName;
    private String email;
    private String phone;
    private String passwd;
    private Integer userStatus;
    private Date lockTime;
}
