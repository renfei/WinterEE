package com.winteree.core.task;

import com.winteree.core.service.CmsService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * <p>Title: TaskServiceImpl</p>
 * <p>Description: 更新文章页面评级服务</p>
 *
 * @author RenFei
 * @date : 2020-07-17 20:02
 */
@Slf4j
public class UpdatePostPageRankJob extends QuartzJobBean {
    @Autowired
    private CmsService cmsService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("== Start:UpdatePostPageRankJob ====");
        cmsService.updatePageRank();
        log.info("== End:UpdatePostPageRankJob ====");
    }
}
