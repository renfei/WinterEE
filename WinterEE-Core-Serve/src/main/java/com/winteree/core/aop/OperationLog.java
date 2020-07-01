package com.winteree.core.aop;

import com.winteree.api.entity.LogSubTypeEnum;

import java.lang.annotation.*;

/**
 * <p>Title: OperationLog</p>
 * <p>Description: 操作日志</p>
 *
 * @author RenFei
 * @date : 2020-04-21 21:35
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
public @interface OperationLog {
    String description() default "";

    LogSubTypeEnum type() default LogSubTypeEnum.SELECT;
}
