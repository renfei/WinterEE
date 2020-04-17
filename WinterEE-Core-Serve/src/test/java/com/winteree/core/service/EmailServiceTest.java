package com.winteree.core.service;

import com.winteree.api.entity.Email;
import com.winteree.api.entity.EmailConfiguration;
import net.renfei.sdk.utils.Builder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeUtility;
import java.io.IOException;

/**
 * <p>Title: EmailServiceTest</p>
 * <p>Description: 邮件测试</p>
 *
 * @author RenFei
 * @date : 2020-04-17 14:48
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Test EmailService")
@TestPropertySource(locations = "classpath:bootstrap.yml")
public class EmailServiceTest {
    @javax.annotation.Resource
    private ResourceLoader resourceLoader;
    @Autowired
    private EmailService emailService;

    @Test
    public void sendTest() throws IOException {

        Resource resource = resourceLoader.getResource("classpath:EmailTest.txt");
        MultipartFile[] multipartFiles = new MultipartFile[]{
                new MockMultipartFile(MimeUtility.encodeWord(resource.getFilename()),resource.getInputStream())
        };
        Assertions.assertDoesNotThrow(() -> {
            Builder.of(Email::new)
                    .with(Email::setTo, "i@renfei.net")
                    .with(Email::setSubject, "Unit Test")
                    .with(Email::setText, "<h1>Unit Test</h1><p>这是来自单元测试的邮件</p>")
                    .build().checkMail();
        });
        Assertions.assertDoesNotThrow(() -> {
            emailService.send(Builder.of(Email::new)
                    .with(Email::setTo, "i@renfei.net")
                    .with(Email::setSubject, "Unit Test")
                    .with(Email::setText, "这是来自单元测试的邮件")
                    .with(Email::setMultipartFiles, multipartFiles)
                    .build());
        });
        EmailConfiguration emailConfiguration = Builder.of(EmailConfiguration::new)
                .with(EmailConfiguration::setHost, "smtp.163.com")
                .with(EmailConfiguration::setPort, 25)
                .with(EmailConfiguration::setUsername, "t_e_s_t_e_r@163.com")
                .with(EmailConfiguration::setPassword, "OHTLBKEQFTJIYXOC")
                .build();
        Assertions.assertDoesNotThrow(() -> {
            emailService.send(emailConfiguration, Builder.of(Email::new)
                    .with(Email::setTo, "i@renfei.net")
                    .with(Email::setSubject, "Unit Test")
                    .with(Email::setText, "<h1>Unit Test</h1><p>这是来自单元测试的邮件</p>")
                    .with(Email::setMultipartFiles, multipartFiles)
                    .build());
        });
    }
}
