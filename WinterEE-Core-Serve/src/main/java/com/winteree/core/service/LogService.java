package com.winteree.core.service;

import com.winteree.api.entity.LogDTO;
import com.winteree.api.exception.FailureException;

import java.util.List;

/**
 * <p>Title: LogService</p>
 * <p>Description: 日志服务</p>
 *
 * @author RenFei
 * @date : 2020-04-17 20:01
 */
public interface LogService {
    /**
     * 记录日志
     *
     * @param logDTO 日志实体
     * @return
     * @throws FailureException 执行失败异常
     */
    void log(LogDTO logDTO) throws FailureException;

    /**
     * 获取系统日志
     *
     * @param page      页数
     * @param rows      每页行数
     * @param logType   每页行数
     * @param subType   每页行数
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     * @throws FailureException 执行失败异常
     */
    List<LogDTO> getLogList(int page, int rows, String logType, String subType, String startDate, String endDate) throws FailureException;
}
