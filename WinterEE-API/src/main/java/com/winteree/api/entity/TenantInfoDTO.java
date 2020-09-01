package com.winteree.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>Title: TenantInfoDTO</p>
 * <p>Description: 租户信息</p>
 *
 * @author RenFei
 * @date : 2020-06-10 13:02
 */
@Data
@ApiModel(value = "租户信息", description = "租户信息")
public class TenantInfoDTO {
    @ApiModelProperty(value = "租户名称")
    private String name;
    @ApiModelProperty(value = "唯一编号")
    private String uuid;
    @ApiModelProperty(value = "租户唯一编号")
    private String tenantUuid;
    @ApiModelProperty(value = "管理人员")
    private String administrators;
    @ApiModelProperty(value = "联系方式")
    private String contact;
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;
    @ApiModelProperty(value = "纬度")
    private BigDecimal latitude;
}
