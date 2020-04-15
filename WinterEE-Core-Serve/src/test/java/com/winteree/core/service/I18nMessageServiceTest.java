package com.winteree.core.service;

import com.winteree.core.CoreApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * <p>Title: I18nMessageServiceTest</p>
 * <p>Description: 国际化语言测试</p>
 *
 * @author RenFei
 * @date : 2020-04-15 21:24
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Test I18nMessageService")
@TestPropertySource(locations = "classpath:bootstrap.yml")
public class I18nMessageServiceTest {
    @Autowired
    private I18nMessageService i18nMessageService;

    @Test
    public void getMessageTest() {
        String message = "test.testmessage";
        Assertions.assertEquals(i18nMessageService.getMessage("zh-cn", message, "默认测试消息"), "测试消息");
        Assertions.assertEquals(i18nMessageService.getMessage("en-us", message, "默认测试消息"), "Test message");
        Assertions.assertEquals(i18nMessageService.getMessage("ja-jp", message, "默认测试消息"), "テストメッセージ");
        Assertions.assertEquals(i18nMessageService.getMessage("tester", message, "默认测试消息"), "默认测试消息");
        Assertions.assertEquals(i18nMessageService.getMessage("", message, "默认测试消息"), "默认测试消息");
        Assertions.assertEquals(i18nMessageService.getMessage(null, message, "默认测试消息"), "默认测试消息");
    }
}
