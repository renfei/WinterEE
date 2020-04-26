package com.winteree.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winteree.api.entity.LogDTO;
import com.winteree.api.entity.LogSubTypeEnum;
import com.winteree.api.entity.LogTypeEnum;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.LogDOMapper;
import com.winteree.core.dao.entity.LogDOExample;
import com.winteree.core.dao.entity.LogDOWithBLOBs;
import com.winteree.core.service.AccountService;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.LogService;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.Builder;
import net.renfei.sdk.utils.DateUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    protected LogServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                             AccountService accountService,
                             LogDOMapper logDOMapper) {
        super(accountService, wintereeCoreConfig);
        this.logDOMapper = logDOMapper;
    }


    @Async
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

    /**
     * 获取系统日志
     *
     * @param page    页数
     * @param rows    每页行数
     * @param logType 每页行数
     * @param subType 每页行数
     * @return
     */
    @Override
    public APIResult<List<LogDTO>> getLogList(int page, int rows, String logType, String subType, String startDate, String endDate) {
        List<LogDTO> logDTOS = new ArrayList<>();
        LogDOExample logDOExample = new LogDOExample();
        LogDOExample.Criteria criteria = logDOExample.createCriteria();
        logDOExample.setOrderByClause("create_time DESC");
        if (!BeanUtils.isEmpty(logType) && !"ALL".equals(logType)) {
            criteria.andLogTypeEqualTo(logType);
        }
        if (!BeanUtils.isEmpty(subType) && !"ALL".equals(subType)) {
            criteria.andLogSubTypeEqualTo(subType);
        }
        try {
            if (!BeanUtils.isEmpty(startDate)) {
                Date startD = DateUtils.parseDate(startDate);
                criteria.andCreateTimeGreaterThan(startD);
            }
            if (!BeanUtils.isEmpty(endDate)) {
                Date endD = DateUtils.parseDate(endDate);
                criteria.andCreateTimeLessThan(endD);
            }

            if (!wintereeCoreConfig.getRootAccount().equals(getSignedUser().getUuid())) {
                // 不是超级管理查询租户自己的日志
                criteria.andTenantUuidEqualTo(getSignedUser().getTenantUuid());
            }
            Page pages = PageHelper.startPage(page, rows);
            List<LogDOWithBLOBs> logDOWithBLOBs = logDOMapper.selectByExampleWithBLOBs(logDOExample);
            if (BeanUtils.isEmpty(logDOExample)) {
                return APIResult.builder()
                        .code(StateCode.OK)
                        .data(logDTOS)
                        .build();
            }
            for (LogDOWithBLOBs log : logDOWithBLOBs
            ) {
                LogDTO logDTO = convert(log);
                logDTO.setTotal(pages.getTotal());
                logDTOS.add(logDTO);
            }
            return APIResult.builder()
                    .code(StateCode.OK)
                    .data(logDTOS)
                    .build();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message("Failure")
                    .build();
        }
    }

    private LogDOWithBLOBs convert(LogDTO logDTO) {
        if (logDTO != null) {
            return Builder.of(LogDOWithBLOBs::new)
                    .with(LogDOWithBLOBs::setUuid, UUID.randomUUID().toString())
                    .with(LogDOWithBLOBs::setCreateTime, logDTO.getCreateTime())
                    .with(LogDOWithBLOBs::setLogType, logDTO.getLogType() == null ? null : logDTO.getLogType().getType())
                    .with(LogDOWithBLOBs::setLogSubType, logDTO.getLogSubType() == null ? null : logDTO.getLogSubType().getType())
                    .with(LogDOWithBLOBs::setTenantUuid, logDTO.getTenantUuid())
                    .with(LogDOWithBLOBs::setAccountUuid, logDTO.getAccountUuid())
                    .with(LogDOWithBLOBs::setClientUuid, logDTO.getClientUuid())
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

    private LogDTO convert(LogDOWithBLOBs logDOWithBLOBs) {
        if (logDOWithBLOBs != null) {
            return Builder.of(LogDTO::new)
                    .with(LogDTO::setUuid, UUID.randomUUID().toString())
                    .with(LogDTO::setCreateTime, logDOWithBLOBs.getCreateTime())
                    .with(LogDTO::setLogType, logDOWithBLOBs.getLogType() == null ? null : LogTypeEnum.valueOf(logDOWithBLOBs.getLogType()))
                    .with(LogDTO::setLogSubType, logDOWithBLOBs.getLogSubType() == null ? null : LogSubTypeEnum.valueOf(logDOWithBLOBs.getLogSubType()))
                    .with(LogDTO::setTenantUuid, logDOWithBLOBs.getTenantUuid())
                    .with(LogDTO::setAccountUuid, logDOWithBLOBs.getAccountUuid())
                    .with(LogDTO::setClientUuid, logDOWithBLOBs.getClientUuid())
                    .with(LogDTO::setClientIp, logDOWithBLOBs.getClientIp())
                    .with(LogDTO::setRequestUrl, logDOWithBLOBs.getRequestUrl())
                    .with(LogDTO::setRequestMethod, logDOWithBLOBs.getRequestMethod())
                    .with(LogDTO::setRequestHead, logDOWithBLOBs.getRequestHead())
                    .with(LogDTO::setRequestBody, logDOWithBLOBs.getRequestBody())
                    .with(LogDTO::setResponseHead, logDOWithBLOBs.getResponseHead())
                    .with(LogDTO::setResponseBody, logDOWithBLOBs.getResponseBody())
                    .with(LogDTO::setStatusCode, logDOWithBLOBs.getStatusCode())
                    .with(LogDTO::setLogValue, logDOWithBLOBs.getLogvalue())
                    .build();
        } else {
            return null;
        }
    }
}
