package com.winteree.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Email {
    private String to;
    private String from;
    private String cc;
    private String bcc;
    private String subject;
    private String text;
    @JsonIgnore
    private MultipartFile[] multipartFiles;
    private String tenantId;

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
