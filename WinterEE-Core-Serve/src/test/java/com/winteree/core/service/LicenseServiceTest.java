package com.winteree.core.service;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * <p>Title: LicenseServiceTest</p>
 * <p>Description: 收取服务测试</p>
 *
 * @author RenFei
 * @date : 2020-08-05 21:40
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Test LicenseService")
@TestPropertySource(locations = "classpath:bootstrap.yml")
public class LicenseServiceTest {
    @Autowired
    private LicenseService licenseService;

    @Test
    public void getLicense() {
        try {
            System.out.println(JSON.toJSONString(licenseService.getLicense()));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
