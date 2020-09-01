package com.winteree.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "组织机构", description = "组织机构")
public class OrganizationVO {
    @ApiModelProperty(value = "是否是租户")
    private Boolean isTenant;
    @ApiModelProperty(value = "自增ID")
    private Long id;
    @ApiModelProperty(value = "唯一编号")
    private String uuid;
    @ApiModelProperty(value = "租户唯一编号")
    private String tenantUuid;
    @ApiModelProperty(value = "父级唯一编号")
    private String parentUuid;
    @ApiModelProperty(value = "组织类型")
    private Integer orgType;
    @ApiModelProperty(value = "组织名称")
    private String name;
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "邮编")
    private String zipCode;
    @ApiModelProperty(value = "负责人")
    private String master;
    @ApiModelProperty(value = "手机")
    private String phone;
    @ApiModelProperty(value = "传真")
    private String fax;
    @ApiModelProperty(value = "电邮")
    private String email;
    @ApiModelProperty(value = "主要负责人")
    private String primaryPerson;
    @ApiModelProperty(value = "副级负责人")
    private String deputyPerson;
    @ApiModelProperty(value = "创建人唯一编码")
    private String createBy;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新人唯一编号")
    private String updateBy;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "删除标记")
    private String delFlag;
    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;
    @ApiModelProperty(value = "纬度")
    private BigDecimal latitude;
    @ApiModelProperty(value = "子级组织")
    private List<OrganizationVO> children;
}
