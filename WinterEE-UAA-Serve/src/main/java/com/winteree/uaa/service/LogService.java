package com.winteree.uaa.service;

import com.winteree.api.entity.LogSubTypeEnum;

/**
 * <p>Title: LogService</p>
 * <p>Description: 日志服务</p>
 *
 * @author RenFei
 * @date : 2020-04-18 21:31
 */
public interface LogService {
    void log(String accountId, LogSubTypeEnum logSubTypeEnum);
    void log(String accountId, LogSubTypeEnum logSubTypeEnum, String logValue);
}
