package com.winteree.uaa.service;

import com.winteree.api.entity.LogSubTypeEnum;
import com.winteree.api.entity.LogTypeEnum;
import com.winteree.uaa.dao.LogDOMapper;
import com.winteree.uaa.dao.entity.LogDO;
import com.winteree.uaa.dao.entity.LogDOWithBLOBs;
import net.renfei.sdk.utils.Builder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * <p>Title: LogService</p>
 * <p>Description: 日志服务</p>
 *
 * @author RenFei
 * @date : 2020-04-14 12:58
 */
@Service
public class LogService {
    private final LogDOMapper logDOMapper;

    public LogService(LogDOMapper logDOMapper) {
        this.logDOMapper = logDOMapper;
    }

    @Async
    public void log(String accountId, LogSubTypeEnum logSubTypeEnum) {
        log(accountId, logSubTypeEnum, null);
    }

    @Async
    public void log(String accountId, LogSubTypeEnum logSubTypeEnum, String logValue) {
        LogDOWithBLOBs logDO = Builder.of(LogDOWithBLOBs::new)
                .with(LogDOWithBLOBs::setId, UUID.randomUUID().toString())
                .with(LogDOWithBLOBs::setDateTime, new Date())
                .with(LogDOWithBLOBs::setLogType, LogTypeEnum.SIGNING.getType())
                .with(LogDOWithBLOBs::setLogSubType, logSubTypeEnum.getType())
                .with(LogDOWithBLOBs::setAccountId, accountId)
                .build();
        logDOMapper.insertSelective(logDO);
    }
}
