package com.winteree.core.task;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * <p>Title: UnitTestJob</p>
 * <p>Description: 单元测试使用的定时任务</p>
 *
 * @author RenFei
 * @date : 2020-07-17 21:32
 */
@Slf4j
public class UnitTestJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Runing com.winteree.core.task.UnitTestJob.executeInternal.");
    }
}
