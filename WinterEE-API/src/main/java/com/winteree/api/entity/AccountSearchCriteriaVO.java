package com.winteree.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>Title: AccountSearchCriteria</p>
 * <p>Description: 账户搜索条件</p>
 *
 * @author RenFei
 * @date : 2020-06-22 20:05
 */
@Data
@ApiModel(value = "账户查询参数对象", description = "此对象用于封装账户查询参数")
public class AccountSearchCriteriaVO {
    @ApiModelProperty(value = "所属租户唯一编号", example = "BC21F895-63DA-4E94-9D9E-D4CD2DCFB189")
    private String tenantuuid;
    @ApiModelProperty(value = "组织架构的编号", example = "ed9ee135-029d-42d9-8a1c-b9a43a8c55ea")
    private String orgUuid;
    @ApiModelProperty(value = "组织架构类型", example = "公司：1，部门：2")
    private Integer orgType;
    @ApiModelProperty(value = "账户唯一编号", example = "9369919A-F95E-44CF-AB0A-6BCD1D933403")
    private String accountUuid;
    @ApiModelProperty(value = "用户名", example = "admin")
    private String userName;
    @ApiModelProperty(value = "用户手机", example = "130010000000")
    private String phone;
    @ApiModelProperty(value = "用户邮箱", example = "i@renfei.net")
    private String email;
    @ApiModelProperty(value = "查询分页的页码", example = "1")
    private Integer pages;
    @ApiModelProperty(value = "查询分页的每页容量", example = "10")
    private Integer rows;
}
