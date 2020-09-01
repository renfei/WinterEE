package com.winteree.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
@ApiModel(value = "角色数据传输对象", description = "角色数据传输对象")
public class RoleDTO {
    @ApiModelProperty(value = "自增ID")
    private Long id;
    @ApiModelProperty(value = "唯一编号")
    private String uuid;
    @ApiModelProperty(value = "租户唯一编号")
    private String tenantUuid;
    @ApiModelProperty(value = "租户名称")
    private String tenantName;
    @ApiModelProperty(value = "所属公司唯一编号")
    private String officeUuid;
    @ApiModelProperty(value = "所属公司名称")
    private String officeName;
    @ApiModelProperty(value = "角色名称")
    private String name;
    @ApiModelProperty(value = "角色英文名称")
    private String enname;
    @ApiModelProperty(value = "角色类型")
    private String roleType;
    @ApiModelProperty(value = "数据范围")
    private Integer dataScope;
    @ApiModelProperty(value = "是否启用")
    private Boolean useable;
    @ApiModelProperty(value = "创建人唯一编号")
    private String createBy;
    @ApiModelProperty(value = "创建时间")
    private String createByName;
    @ApiModelProperty(value = "唯一编号")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;
    @ApiModelProperty(value = "更新人唯一编号")
    private String updateBy;
    @ApiModelProperty(value = "更新人名称")
    private String updateByName;
    @ApiModelProperty(value = "更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updateTime;
    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "删除标记")
    private String delFlag;
    @ApiModelProperty(value = "授权的菜单标示列表")
    private List<String> menuUuid;
    @ApiModelProperty(value = "授权的菜单")
    private List<MenuVO> menuVos;
}
