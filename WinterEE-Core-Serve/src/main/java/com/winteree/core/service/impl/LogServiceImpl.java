package com.winteree.core.service.impl;

import com.winteree.api.entity.LogDTO;
import com.winteree.core.dao.LogDOMapper;
import com.winteree.core.dao.entity.LogDOWithBLOBs;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.LogService;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import net.renfei.sdk.utils.Builder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * <p>Title: LogService</p>
 * <p>Description: 日志服务</p>
 *
 * @author RenFei
 * @date : 2020-04-13 13:15
 */
@Slf4j
@Service
public class LogServiceImpl extends BaseService implements LogService {
    private final LogDOMapper logDOMapper;

    public LogServiceImpl(LogDOMapper logDOMapper) {
        this.logDOMapper = logDOMapper;
    }

    @Override
    public APIResult log(LogDTO logDTO) {
        LogDOWithBLOBs logDOWithBLOBs = convert(logDTO);
        if (logDOWithBLOBs != null) {
            try {
                logDOMapper.insertSelective(logDOWithBLOBs);
                return APIResult.builder()
                        .code(StateCode.OK)
                        .build();
            } catch (Exception ex) {
                log.error(ex.getMessage());
                return APIResult.builder()
                        .code(StateCode.Failure)
                        .build();
            }
        } else {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .build();
        }
    }

    private LogDOWithBLOBs convert(LogDTO logDTO) {
        if (logDTO != null) {
            return Builder.of(LogDOWithBLOBs::new)
                    .with(LogDOWithBLOBs::setId, UUID.randomUUID().toString())
                    .with(LogDOWithBLOBs::setDateTime, logDTO.getDateTime())
                    .with(LogDOWithBLOBs::setLogType, logDTO.getLogType() == null ? null : logDTO.getLogType().getType())
                    .with(LogDOWithBLOBs::setLogSubType, logDTO.getLogSubType() == null ? null : logDTO.getLogSubType().getType())
                    .with(LogDOWithBLOBs::setTenantId, logDTO.getTenantId())
                    .with(LogDOWithBLOBs::setAccountId, logDTO.getAccountId())
                    .with(LogDOWithBLOBs::setClientId, logDTO.getClientId())
                    .with(LogDOWithBLOBs::setClientIp, logDTO.getClientIp())
                    .with(LogDOWithBLOBs::setRequestUrl, logDTO.getRequestUrl())
                    .with(LogDOWithBLOBs::setRequestMethod, logDTO.getRequestMethod())
                    .with(LogDOWithBLOBs::setRequestHead, logDTO.getRequestHead())
                    .with(LogDOWithBLOBs::setRequestBody, logDTO.getRequestBody())
                    .with(LogDOWithBLOBs::setResponseHead, logDTO.getResponseHead())
                    .with(LogDOWithBLOBs::setResponseBody, logDTO.getResponseBody())
                    .with(LogDOWithBLOBs::setStatusCode, logDTO.getStatusCode())
                    .with(LogDOWithBLOBs::setLogvalue, logDTO.getLogValue())
                    .build();
        } else {
            return null;
        }
    }
}
