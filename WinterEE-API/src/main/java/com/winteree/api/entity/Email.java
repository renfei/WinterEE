package com.winteree.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>Title: Email</p>
 * <p>Description: 电子邮件实体类</p>
 *
 * @author RenFei
 * @date : 2020-04-17 12:51
 */
@Data
@ApiModel(value = "电子邮件实体类", description = "电子邮件实体类")
public class Email {
    @ApiModelProperty(value = "收件人")
    private String to;
    @ApiModelProperty(value = "发件人")
    private String from;
    @ApiModelProperty(value = "抄送")
    private String cc;
    @ApiModelProperty(value = "秘密抄送")
    private String bcc;
    @ApiModelProperty(value = "主题")
    private String subject;
    @ApiModelProperty(value = "内容")
    private String text;
    @JsonIgnore
    @ApiModelProperty(value = "附件列表")
    private MultipartFile[] multipartFiles;
    @ApiModelProperty(value = "租户唯一编号")
    private String tenantUuid;

    public void checkMail() {
        if (StringUtils.isEmpty(this.to)) {
            throw new RuntimeException("邮件收信人不能为空");
        }
        if (StringUtils.isEmpty(this.subject)) {
            throw new RuntimeException("邮件主题不能为空");
        }
        if (StringUtils.isEmpty(this.text)) {
            throw new RuntimeException("邮件内容不能为空");
        }
    }
}
