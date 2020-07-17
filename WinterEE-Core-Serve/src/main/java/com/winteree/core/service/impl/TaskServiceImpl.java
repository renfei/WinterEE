package com.winteree.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winteree.api.entity.ListData;
import com.winteree.api.entity.TaskJobDTO;
import com.winteree.core.dao.QrtzCronTriggersDOMapper;
import com.winteree.core.dao.QrtzJobDetailsDOMapper;
import com.winteree.core.dao.QrtzTriggersDOMapper;
import com.winteree.core.dao.entity.*;
import com.winteree.core.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.Builder;
import net.renfei.sdk.utils.ListUtils;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Title: TaskServiceImpl</p>
 * <p>Description: 定时任务管理</p>
 *
 * @author RenFei
 * @date : 2020-07-17 20:39
 */
@Slf4j
@Service
public class TaskServiceImpl implements TaskService {
    private final Scheduler scheduler;
    private final QrtzJobDetailsDOMapper qrtzJobDetailsDOMapper;
    private final QrtzTriggersDOMapper qrtzTriggersDOMapper;
    private final QrtzCronTriggersDOMapper qrtzCronTriggersDOMapper;

    public TaskServiceImpl(Scheduler scheduler,
                           QrtzJobDetailsDOMapper qrtzJobDetailsDOMapper,
                           QrtzTriggersDOMapper qrtzTriggersDOMapper,
                           QrtzCronTriggersDOMapper qrtzCronTriggersDOMapper) {
        this.scheduler = scheduler;
        this.qrtzJobDetailsDOMapper = qrtzJobDetailsDOMapper;
        this.qrtzTriggersDOMapper = qrtzTriggersDOMapper;
        this.qrtzCronTriggersDOMapper = qrtzCronTriggersDOMapper;
    }

    /**
     * 获取定时任务列表
     *
     * @param pages 页码
     * @param rows  行数
     * @return
     */
    @Override
    public ListData<TaskJobDTO> getTaskList(int pages, int rows) {
        ListData<TaskJobDTO> taskJobDTOListData = new ListData<>();
        QrtzJobDetailsDOExample qrtzJobDetailsDOExample = new QrtzJobDetailsDOExample();
        Page page = PageHelper.startPage(pages, rows);
        List<QrtzJobDetailsDO> qrtzJobDetailsDOList = qrtzJobDetailsDOMapper.selectByExampleWithBLOBs(qrtzJobDetailsDOExample);
        taskJobDTOListData.setTotal(page.getTotal());
        if (BeanUtils.isEmpty(qrtzJobDetailsDOList)) {
            return taskJobDTOListData;
        }
        QrtzTriggersDOExample qrtzTriggersDOExample = new QrtzTriggersDOExample();
        QrtzCronTriggersDOExample qrtzCronTriggersDOExample = new QrtzCronTriggersDOExample();
        List<TaskJobDTO> taskJobDTOList = new ArrayList<>();
        qrtzJobDetailsDOList.forEach(qrtzJobDetailsDO -> {
            qrtzTriggersDOExample.createCriteria()
                    .andJobNameEqualTo(qrtzJobDetailsDO.getJobName())
                    .andJobGroupEqualTo(qrtzJobDetailsDO.getJobGroup());
            QrtzTriggersDO qrtzTriggersDO = ListUtils.getOne(qrtzTriggersDOMapper.selectByExampleWithBLOBs(qrtzTriggersDOExample));
            QrtzCronTriggersDO qrtzCronTriggersDO = null;
            if (qrtzTriggersDO != null) {
                qrtzCronTriggersDOExample.createCriteria()
                        .andTriggerGroupEqualTo(qrtzTriggersDO.getTriggerGroup())
                        .andTriggerNameEqualTo(qrtzTriggersDO.getTriggerName());
                qrtzCronTriggersDO = ListUtils.getOne(qrtzCronTriggersDOMapper.selectByExample(qrtzCronTriggersDOExample));
            }
            TaskJobDTO taskJobDTO = Builder.of(TaskJobDTO::new)
                    .with(TaskJobDTO::setJobName, qrtzJobDetailsDO.getJobName())
                    .with(TaskJobDTO::setJobGroup, qrtzJobDetailsDO.getJobGroup())
                    .with(TaskJobDTO::setDescription, qrtzJobDetailsDO.getDescription())
                    .with(TaskJobDTO::setJobClassName, qrtzJobDetailsDO.getJobClassName())
                    .with(TaskJobDTO::setTriggerName, qrtzTriggersDO == null ? "" : qrtzTriggersDO.getTriggerName())
                    .with(TaskJobDTO::setTriggerState, qrtzTriggersDO == null ? "" : qrtzTriggersDO.getTriggerState())
                    .with(TaskJobDTO::setCronExpression, qrtzCronTriggersDO == null ? "" : qrtzCronTriggersDO.getCronExpression())
                    .build();
            taskJobDTOList.add(taskJobDTO);
        });
        taskJobDTOListData.setData(taskJobDTOList);
        return taskJobDTOListData;
    }

    /**
     * 保存定时任务
     *
     * @param taskJobDTO 定时任务数据传输对象
     * @return
     */
    @Override
    public boolean saveJob(TaskJobDTO taskJobDTO) {
        try {
            //如果是修改，先删除旧的任务
            if (taskJobDTO.getOldJobGroup() != null && !"".equals(taskJobDTO.getOldJobGroup())) {
                JobKey key = new JobKey(taskJobDTO.getOldJobName(), taskJobDTO.getOldJobGroup());
                scheduler.deleteJob(key);
            }
            //构建job信息
            Class cls = Class.forName(taskJobDTO.getJobClassName());
            cls.newInstance();
            JobDetail job = JobBuilder.newJob(cls).withIdentity(taskJobDTO.getJobName(),
                    taskJobDTO.getJobGroup())
                    .withDescription(taskJobDTO.getDescription()).build();
            // 触发时间点
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(taskJobDTO.getCronExpression().trim());
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger" + taskJobDTO.getJobName(), taskJobDTO.getJobGroup())
                    .startNow().withSchedule(cronScheduleBuilder).build();
            //交由Scheduler安排触发
            scheduler.scheduleJob(job, trigger);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 立即触发一个任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     * @return
     */
    @Override
    public boolean triggerJob(String jobName, String jobGroup) {
        JobKey key = new JobKey(jobName, jobGroup);
        try {
            scheduler.triggerJob(key);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * 暂停一个任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     * @return
     */
    @Override
    public boolean pauseJob(String jobName, String jobGroup) {
        JobKey key = new JobKey(jobName, jobGroup);
        try {
            scheduler.pauseJob(key);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * 恢复一个任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     * @return
     */
    @Override
    public boolean resumeJob(String jobName, String jobGroup) {
        JobKey key = new JobKey(jobName, jobGroup);
        try {
            scheduler.resumeJob(key);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * 移除一个任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     * @return
     */
    @Override
    public boolean removeJob(String jobName, String jobGroup) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * 修改某个任务的执行时间
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     * @param time     执行时间
     * @return
     * @throws SchedulerException
     */
    @Override
    public boolean modifyJob(String jobName, String jobGroup, String time) throws SchedulerException {
        Date date = null;
        TriggerKey triggerKey = new TriggerKey(jobName, jobGroup);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String oldTime = cronTrigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(time)) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
                    .withSchedule(cronScheduleBuilder).build();
            date = scheduler.rescheduleJob(triggerKey, trigger);
        }
        return date != null;
    }
}
