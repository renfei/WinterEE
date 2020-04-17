package com.winteree.core.service;

import com.winteree.api.entity.LogDTO;
import net.renfei.sdk.entity.APIResult;

/**
 * <p>Title: LogService</p>
 * <p>Description: 日志服务</p>
 *
 * @author RenFei
 * @date : 2020-04-17 13:01
 */
public interface LogService {
    APIResult log(LogDTO logDTO);
}
