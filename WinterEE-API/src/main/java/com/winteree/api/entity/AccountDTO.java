package com.winteree.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author RenFei
 */
@Data
@ApiModel(value = "账户对象", description = "账户数据传输对象")
public class AccountDTO {
    @ApiModelProperty(value = "账户唯一编号", example = "9369919A-F95E-44CF-AB0A-6BCD1D933403")
    private String uuid;
    @ApiModelProperty(value = "所属租户唯一编号", example = "BC21F895-63DA-4E94-9D9E-D4CD2DCFB189")
    private String tenantUuid;
    @ApiModelProperty(value = "所属公司唯一编号", example = "ed9ee135-029d-42d9-8a1c-b9a43a8c55ea")
    private String officeUuid;
    @ApiModelProperty(value = "所属公司名称", example = "XXX公司")
    private String officeName;
    @ApiModelProperty(value = "所属部门唯一编号", example = "1106dc4a-4a15-434e-b78d-282a3bcffc71")
    private String departmentUuid;
    @ApiModelProperty(value = "所属部门名称", example = "XXX部")
    private String departmentName;
    @ApiModelProperty(value = "账户创建时间", example = "2020-08-21 12:32:51")
    private Date createTime;
    @ApiModelProperty(value = "用户名", example = "admin")
    private String userName;
    @ApiModelProperty(value = "用户邮箱", example = "i@renfei.net")
    private String email;
    @ApiModelProperty(value = "用户手机", example = "130010000000")
    private String phone;
    @ApiModelProperty(value = "账户状态", example = "1")
    private Integer userStatus;
    @ApiModelProperty(value = "锁定截止时间", example = "2020-08-21 12:32:51")
    private Date lockTime;
    @ApiModelProperty(value = "账户权限列表")
    private List<String> authorities;
    @ApiModelProperty(value = "账户角色列表")
    private List<String> roles;
    @ApiModelProperty(value = "密码")
    private String passwd;
}
