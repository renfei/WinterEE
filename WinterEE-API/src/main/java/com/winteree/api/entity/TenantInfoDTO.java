package com.winteree.api.entity;

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
public class TenantInfoDTO {
    private String name;
    private String uuid;
    private String tenantUuid;
    private String administrators;
    private String contact;
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;
}
