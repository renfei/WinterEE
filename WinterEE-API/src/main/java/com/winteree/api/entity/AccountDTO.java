package com.winteree.api.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author RenFei
 */
@Data
public class AccountDTO {
    private String uuid;
    private String tenantUuid;
    private Date createTime;
    private String userName;
    private String email;
    private String phone;
    private String passwd;
    private Integer userStatus;
    private Date lockTime;
    private List<String> authorities;
}
