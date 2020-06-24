package com.winteree.api.entity;

import lombok.Data;

/**
 * <p>Title: AccountSearchCriteria</p>
 * <p>Description: 账户搜索条件</p>
 *
 * @author RenFei
 * @date : 2020-06-22 20:05
 */
@Data
public class AccountSearchCriteriaVO {
    private String tenantuuid;
    private String orgUuid;
    private Integer orgType;
    private String accountUuid;
    private String userName;
    private String phone;
    private String email;
    private Integer pages;
    private Integer rows;
}
