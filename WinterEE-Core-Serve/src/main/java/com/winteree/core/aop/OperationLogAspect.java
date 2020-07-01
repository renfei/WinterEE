package com.winteree.core.aop;

import com.winteree.api.entity.LogDTO;
import com.winteree.api.entity.LogTypeEnum;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.entity.AccountDTO;
import com.winteree.core.service.AccountService;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.LogService;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.utils.Builder;
import net.renfei.sdk.utils.IpUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Aspect
@Component
public class OperationLogAspect extends BaseService {
    private final AccountService accountService;
    private final LogService logService;

    protected OperationLogAspect(AccountService accountService,
                                 WintereeCoreConfig wintereeCoreConfig,
                                 LogService logService) {
        super(wintereeCoreConfig);
        this.accountService = accountService;
        this.logService = logService;
    }

    @Pointcut("@annotation(com.winteree.core.aop.OperationLog)")
    public void operationLog() {
    }

    /**
     * 前置通知  用于拦截Controller层记录用户的操作
     *
     * @param joinPoint
     */
    @Before("operationLog()")
    public void doBefore(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        AccountDTO accountDTO = getSignedUser(accountService);
        String ip = IpUtils.getIpAddress(request);
        try {
            OperationLog operationLog = getControllerMethodDescription(joinPoint);
            // 保存数据库
            logService.log(Builder.of(LogDTO::new)
                    .with(LogDTO::setUuid, UUID.randomUUID().toString())
                    .with(LogDTO::setCreateTime, new Date())
                    .with(LogDTO::setLogValue, operationLog.description())
                    .with(LogDTO::setLogType, LogTypeEnum.OPERATION)
                    .with(LogDTO::setLogSubType, operationLog.type())
                    .with(LogDTO::setAccountUuid, accountDTO.getUuid())
                    .with(LogDTO::setClientIp, ip)
                    .build());
        } catch (Exception e) {
            // 记录本地异常日志
            log.error("异常信息：{}", e.getMessage());
        }
    }

    /**
     * 获取注解
     *
     * @param joinPoint
     * @return
     * @throws Exception
     */
    public static OperationLog getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();//目标方法名
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        OperationLog description = null;
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(OperationLog.class);
                    break;
                }
            }
        }
        return description;
    }
}
