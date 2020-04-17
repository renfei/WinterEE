package com.winteree.core.service;

import com.winteree.api.entity.LogDTO;
import com.winteree.api.entity.LogSubTypeEnum;
import com.winteree.api.entity.LogTypeEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * <p>Title: LogServiceTest</p>
 * <p>Description: 日志服务测试</p>
 *
 * @author RenFei
 * @date : 2020-04-15 23:17
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Test I18nMessageService")
@TestPropertySource(locations = "classpath:bootstrap.yml")
public class LogServiceTest {
    @Autowired
    private LogService logService;

    @Test
    @Rollback
    @Transactional
    public void logTest() {
        Assertions.assertEquals(logService.log(null).getCode(), 100);
        LogDTO logDTO = new LogDTO();
        Assertions.assertEquals(logService.log(logDTO).getCode(), 100);
        logDTO.setId(UUID.randomUUID().toString());
        logDTO.setDateTime(new Date());
        logDTO.setLogType(LogTypeEnum.ACCESS);
        Assertions.assertEquals(logService.log(logDTO).getCode(), 200);
        logDTO.setId(UUID.randomUUID().toString());
        logDTO.setDateTime(new Date());
        logDTO.setLogType(LogTypeEnum.ACCESS);
        logDTO.setLogSubType(LogSubTypeEnum.DEBUG);
        Assertions.assertEquals(logService.log(logDTO).getCode(), 200);
    }
}
