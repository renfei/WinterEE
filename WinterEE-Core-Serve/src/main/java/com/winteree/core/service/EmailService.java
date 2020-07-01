package com.winteree.core.service;

import com.winteree.api.entity.Email;
import com.winteree.api.entity.EmailConfiguration;

/**
 * <p>Title: EmailService</p>
 * <p>Description: 电子邮件服务</p>
 *
 * @author RenFei
 * @date : 2020-04-17 12:50
 */
public interface EmailService {
    void send(EmailConfiguration emailConfiguration, Email email);

    /**
     * 发送电子邮件
     *
     * @param email 电子邮件实体
     */
    void send(Email email);
}
