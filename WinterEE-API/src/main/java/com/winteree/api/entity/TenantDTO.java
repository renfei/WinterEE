package com.winteree.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * <p>Title: TenantDTO</p>
 * <p>Description: 租户传输类</p>
 *
 * @author RenFei
 * @date : 2020-04-26 21:28
 */
@Data
public class TenantDTO {
    private Long id;
    private String uuid;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expiryDate;
    private Integer status;
    private Date updateTime;
}
