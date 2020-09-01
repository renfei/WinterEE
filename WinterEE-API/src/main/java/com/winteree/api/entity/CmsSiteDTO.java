package com.winteree.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>Title: CmsSiteDTO</p>
 * <p>Description: CMS系统站点数据传输类</p>
 *
 * @author RenFei
 * @date : 2020-06-26 23:09
 */
@Data
@ApiModel(value = "CMS系统站点数据传输对象", description = "CMS系统站点数据传输对象")
public class CmsSiteDTO {
    @ApiModelProperty(value = "唯一编号")
    private String uuid;
    @ApiModelProperty(value = "所属租户唯一编号")
    private String tenantUuid;
    @ApiModelProperty(value = "租户名称")
    private String tenantName;
    @ApiModelProperty(value = "所属公司唯一编号")
    private String officeUuid;
    @ApiModelProperty(value = "公司名称")
    private String officeName;
    @ApiModelProperty(value = "所属部门唯一编号")
    private String departmentUuid;
    @ApiModelProperty(value = "部门名称")
    private String departmentName;
    @ApiModelProperty(value = "站点名称")
    private String siteName;
    @ApiModelProperty(value = "站点域名")
    private String siteDomain;
    @ApiModelProperty(value = "站点关键字")
    private String siteKeyword;
    @ApiModelProperty(value = "站点简介")
    private String siteDescription;
    @ApiModelProperty(value = "ICP备案号")
    private String icpNo;
    @ApiModelProperty(value = "全局评论开关")
    private Boolean isComment;
    @ApiModelProperty(value = "公安备案号")
    private String gonganNo;
    @ApiModelProperty(value = "统计代码")
    private String analysisCode;
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;
    @ApiModelProperty(value = "创建人唯一编号")
    private String createBy;
    @ApiModelProperty(value = "创建人用户名")
    private String createByName;
    @ApiModelProperty(value = "更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updateTime;
    @ApiModelProperty(value = "更新人唯一编号")
    private String updateBy;
    @ApiModelProperty(value = "更新人用户名")
    private String updateByName;
    @ApiModelProperty(value = "是否启用")
    private Boolean siteEnable;
}
