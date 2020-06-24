package com.winteree.api.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>Title: OrganizationVO</p>
 * <p>Description: 组织机构</p>
 *
 * @author RenFei
 * @date : 2020-06-12 14:55
 */
@Data
public class OrganizationVO {
    private Boolean isTenant;
    private Long id;
    private String uuid;
    private String tenantUuid;
    private String parentUuid;
    private Integer orgType;
    private String name;
    private String address;
    private String zipCode;
    private String master;
    private String phone;
    private String fax;
    private String email;
    private String primaryPerson;
    private String deputyPerson;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private String remarks;
    private String delFlag;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private List<OrganizationVO> children;
}
