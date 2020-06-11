package com.winteree.core.service.impl;

import com.winteree.api.entity.*;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.EmailService;
import com.winteree.core.service.LogService;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.utils.Builder;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeUtility;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

/**
 * <p>Title: EmailService</p>
 * <p>Description: 电子邮件服务</p>
 *
 * @author RenFei
 * @date : 2020-04-17 20:48
 */
@Slf4j
@Service
public class EmailServiceImpl extends BaseService implements EmailService {
    private final LogService logService;
    private final JavaMailSenderImpl mailSender;

    protected EmailServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                               LogService logService,
                               JavaMailSenderImpl mailSender) {
        super(wintereeCoreConfig);
        this.logService = logService;
        this.mailSender = mailSender;
    }


    private JavaMailSenderImpl initJavaMailSender(EmailConfiguration emailConfiguration) {
        JavaMailSenderImpl javaMailSender = Builder.of(JavaMailSenderImpl::new)
                .with(JavaMailSenderImpl::setHost, emailConfiguration.getHost())
                .with(JavaMailSenderImpl::setPort, emailConfiguration.getPort())
                .with(JavaMailSenderImpl::setUsername, emailConfiguration.getUsername())
                .with(JavaMailSenderImpl::setPassword, emailConfiguration.getPassword())
                .with(JavaMailSenderImpl::setProtocol, emailConfiguration.getProtocol())
                .with(JavaMailSenderImpl::setDefaultEncoding, emailConfiguration.getDefaultEncoding().name())
                .build();

        if (emailConfiguration.getProperties() != null) {
            Properties properties = new Properties();
            emailConfiguration.getProperties().forEach(properties::setProperty);
            javaMailSender.setJavaMailProperties(properties);
        }
        if (StringUtils.isEmpty(javaMailSender.getJavaMailProperties().getProperty("from"))) {
            javaMailSender.getJavaMailProperties().setProperty("from", emailConfiguration.getUsername());
        }
        return javaMailSender;
    }

    @Override
    public void send(EmailConfiguration emailConfiguration, Email email) {
        if (wintereeCoreConfig.getEnableEmail()) {
            send(emailConfiguration == null ? mailSender : initJavaMailSender(emailConfiguration), email);
        } else {
            log.error("Send Email {}->{} Fail,EnableEmail is false! Subject:{}",
                    email.getFrom(), email.getTo(), email.getSubject());
            LogDTO logDTO = Builder.of(LogDTO::new)
                    .with(LogDTO::setUuid, UUID.randomUUID().toString())
                    .with(LogDTO::setCreateTime, new Date())
                    .with(LogDTO::setTenantUuid, email.getTenantUuid())
                    .with(LogDTO::setLogType, LogTypeEnum.SYSTEM)
                    .with(LogDTO::setLogSubType, LogSubTypeEnum.ERROR)
                    .with(LogDTO::setLogValue, String.format("Send Email %s->%s Fail,EnableEmail is false! Subject:%s", email.getFrom(), email.getTo(), email.getSubject()))
                    .build();
            logService.log(logDTO);
        }
    }

    @Override
    public void send(Email email) {
        if (wintereeCoreConfig.getEnableEmail()) {
            send(mailSender, email);
        } else {
            log.error("Send Email {}->{} Fail,EnableEmail is false! Subject:{}",
                    email.getFrom(), email.getTo(), email.getSubject());
            LogDTO logDTO = Builder.of(LogDTO::new)
                    .with(LogDTO::setUuid, UUID.randomUUID().toString())
                    .with(LogDTO::setCreateTime, new Date())
                    .with(LogDTO::setTenantUuid, email.getTenantUuid())
                    .with(LogDTO::setLogType, LogTypeEnum.SYSTEM)
                    .with(LogDTO::setLogSubType, LogSubTypeEnum.ERROR)
                    .with(LogDTO::setLogValue, String.format("Send Email %s->%s Fail,EnableEmail is false! Subject:%s", email.getFrom(), email.getTo(), email.getSubject()))
                    .build();
            logService.log(logDTO);
        }
    }

    private void send(JavaMailSenderImpl mailSender, Email email) {
        try {
            //true表示支持复杂类型
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
            messageHelper.setFrom(email.getFrom() == null ? mailSender.getJavaMailProperties().getProperty("from") : email.getFrom());
            messageHelper.setTo(email.getTo().split(","));
            messageHelper.setSubject(email.getSubject());
            messageHelper.setText(email.getText(), true);
            if (!StringUtils.isEmpty(email.getCc())) {
                messageHelper.setCc(email.getCc().split(","));
            }
            if (!StringUtils.isEmpty(email.getBcc())) {
                messageHelper.setCc(email.getBcc().split(","));
            }
            if (email.getMultipartFiles() != null) {
                for (MultipartFile multipartFile : email.getMultipartFiles()) {
                    if (multipartFile == null || multipartFile.getOriginalFilename() == null) {
                        continue;
                    }
                    messageHelper.addAttachment(MimeUtility.encodeWord(multipartFile.getName() == null ? multipartFile.getOriginalFilename() : multipartFile.getName()), multipartFile);
                }
            }
            mailSender.send(messageHelper.getMimeMessage());
            log.info("Send Email {}->{} Fail, Subject:{}, Content:{}",
                    email.getFrom(), email.getTo(), email.getSubject(), email.getText());
            LogDTO logDTO = Builder.of(LogDTO::new)
                    .with(LogDTO::setUuid, UUID.randomUUID().toString())
                    .with(LogDTO::setCreateTime, new Date())
                    .with(LogDTO::setLogType, LogTypeEnum.SYSTEM)
                    .with(LogDTO::setLogSubType, LogSubTypeEnum.EMAIL)
                    .with(LogDTO::setTenantUuid, email.getTenantUuid())
                    .with(LogDTO::setLogValue, String.format("Send Email %s->%s Success, Subject:%s, Content:%s",
                            email.getFrom(), email.getTo(), email.getSubject(), email.getText()))
                    .build();
            logService.log(logDTO);
        } catch (Exception e) {
            log.error("Send Email {}->{} Fail, Subject:{}, Content:{} | Exception:{}",
                    email.getFrom(), email.getTo(), email.getSubject(), email.getText(), e.getMessage());
            LogDTO logDTO = Builder.of(LogDTO::new)
                    .with(LogDTO::setUuid, UUID.randomUUID().toString())
                    .with(LogDTO::setCreateTime, new Date())
                    .with(LogDTO::setLogType, LogTypeEnum.SYSTEM)
                    .with(LogDTO::setLogSubType, LogSubTypeEnum.ERROR)
                    .with(LogDTO::setTenantUuid, email.getTenantUuid())
                    .with(LogDTO::setLogValue, String.format("Send Email %s->%s Fail, Subject:%s, Content:%s | Exception:%s",
                            email.getFrom(), email.getTo(), email.getSubject(), email.getText(), e.getMessage()))
                    .build();
            logService.log(logDTO);
            throw new RuntimeException(e);
        }
    }
}
