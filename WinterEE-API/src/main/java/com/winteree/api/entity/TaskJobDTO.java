package com.winteree.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>Title: TaskJobDTO</p>
 * <p>Description: 定时任务数据传输对象</p>
 *
 * @author RenFei
 * @date : 2020-07-17 20:45
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="定时任务数据传输对象", description="")
public class TaskJobDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务名称")
    private String jobName;

    @ApiModelProperty(value = "任务分组")
    private String jobGroup;

    @ApiModelProperty(value = "任务描述")
    private String description;

    @ApiModelProperty(value = "执行类")
    private String jobClassName;

    @ApiModelProperty(value = "执行时间表达式")
    private String cronExpression;

    @ApiModelProperty(value = "触发器名称")
    private String triggerName;

    @ApiModelProperty(value = "触发器状态")
    private String triggerState;

    @ApiModelProperty(value = "任务名称 用于修改")
    private String oldJobName;

    @ApiModelProperty(value = "任务分组 用于修改")
    private String oldJobGroup;
}
