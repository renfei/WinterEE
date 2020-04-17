package com.winteree.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * <p>Title: SmsServiceTest</p>
 * <p>Description: 短信服务测试</p>
 *
 * @author RenFei
 * @date : 2020-04-17 23:20
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Test SmsService")
@TestPropertySource(locations = "classpath:bootstrap.yml")
public class SmsServiceTest {
    private final SmsService aliyunSmsServiceImpl;

    public SmsServiceTest(SmsService aliyunSmsServiceImpl) {
        this.aliyunSmsServiceImpl = aliyunSmsServiceImpl;
    }

    @Test
    public void aliyunSmsServiceTest(){}
}
