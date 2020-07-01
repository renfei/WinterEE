package com.winteree.uaa.service.impl;

import com.winteree.api.entity.LogSubTypeEnum;
import com.winteree.api.entity.LogTypeEnum;
import com.winteree.uaa.dao.LogDOMapper;
import com.winteree.uaa.dao.entity.LogDOWithBLOBs;
import com.winteree.uaa.service.LogService;
import net.renfei.sdk.utils.Builder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * <p>Title: LogServiceImpl</p>
 * <p>Description: 日志服务</p>
 *
 * @author RenFei
 * @date : 2020-04-14 12:58
 */
@Service
public class LogServiceImpl implements LogService {
    private final LogDOMapper logDOMapper;

    public LogServiceImpl(LogDOMapper logDOMapper) {
        this.logDOMapper = logDOMapper;
    }

    @Async
    @Override
    public void log(String accountUuid, LogSubTypeEnum logSubTypeEnum) {
        log(accountUuid, logSubTypeEnum, null);
    }

    @Async
    @Override
    public void log(String accountUuid, LogSubTypeEnum logSubTypeEnum, String logValue) {
        LogDOWithBLOBs logDO = Builder.of(LogDOWithBLOBs::new)
                .with(LogDOWithBLOBs::setUuid, UUID.randomUUID().toString())
                .with(LogDOWithBLOBs::setCreateTime, new Date())
                .with(LogDOWithBLOBs::setLogType, LogTypeEnum.SIGNING.getType())
                .with(LogDOWithBLOBs::setLogSubType, logSubTypeEnum.getType())
                .with(LogDOWithBLOBs::setAccountUuid, accountUuid)
                .build();
        logDOMapper.insertSelective(logDO);
    }
}
