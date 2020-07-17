package com.winteree.core.service;

import com.winteree.api.entity.ListData;
import com.winteree.api.entity.TaskJobDTO;
import org.quartz.SchedulerException;

/**
 * <p>Title: TaskService</p>
 * <p>Description: 定时任务管理</p>
 *
 * @author RenFei
 * @date : 2020-07-17 20:38
 */
public interface TaskService {

    /**
     * 获取定时任务列表
     *
     * @param pages 页码
     * @param rows  行数
     * @return
     */
    ListData<TaskJobDTO> getTaskList(int pages, int rows);

    /**
     * 保存定时任务
     *
     * @param taskJobDTO 定时任务数据传输对象
     * @return
     */
    boolean saveJob(TaskJobDTO taskJobDTO);

    /**
     * 立即触发一个任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     * @return
     */
    boolean triggerJob(String jobName, String jobGroup);

    /**
     * 暂停一个任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     * @return
     */
    boolean pauseJob(String jobName, String jobGroup);

    /**
     * 恢复一个任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     * @return
     */
    boolean resumeJob(String jobName, String jobGroup);

    /**
     * 移除一个任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     * @return
     */
    boolean removeJob(String jobName, String jobGroup);

    /**
     * 修改某个任务的执行时间
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     * @param time     执行时间
     * @return
     * @throws SchedulerException
     */
    boolean modifyJob(String jobName, String jobGroup, String time) throws SchedulerException;
}
