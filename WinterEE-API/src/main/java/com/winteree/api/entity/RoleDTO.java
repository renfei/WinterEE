package com.winteree.api.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>Title: RoleDTO</p>
 * <p>Description: 角色数据传输对象</p>
 *
 * @author RenFei
 * @date : 2020-06-15 11:31
 */
@Data
public class RoleDTO {
    private Long id;
    private String uuid;
    private String tenantUuid;
    private String tenantName;
    private String officeUuid;
    private String officeName;
    private String name;
    private String enname;
    private String roleType;
    private Integer dataScope;
    private Boolean useable;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private String remarks;
    private String delFlag;
    private List<String> menuUuid;
}
