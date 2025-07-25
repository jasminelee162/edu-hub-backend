package com.project.framework.aspectj;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.project.framework.config.SystemConfig;
import com.project.framework.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @description: 请求日志
 * @author shaozhujie
 * 
 */
@Slf4j
@Aspect
@Component
public class WebLogAspect {

    /**
     * 以 controller 包下定义的所有请求为切入点
     */
    @Pointcut("execution(public * com.ape.*.controller..*.*(..))")
    public void webLog() {
    }

    /**
     * 环绕通知
     */
    @Around("webLog()")
    public Object handle(ProceedingJoinPoint joinPoint) throws Throwable {
        String logTemplate = "请求响应正常->URL:{}, Time:{}ms, Method:{}, IP:{}, Class:{}.{}, Params:{}, Response: {}";
        ServletRequestAttributes servletContainer = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletContainer.getRequest();
        String ipAddr = RequestUtils.getRemoteHost(request);
        long begin = System.currentTimeMillis();
        String respParam = getParameterInfo(joinPoint);
        Object result = joinPoint.proceed();
        if (SystemConfig.isRequestLog()) {
            // 判断是否是资源类型（文件下载）
            String responseLog;
            if (result instanceof org.springframework.core.io.Resource) {
                responseLog = "【文件资源响应，已跳过打印】";
            } else {
                try {
                    responseLog = JSON.toJSONString(result);
                } catch (Exception e) {
                    responseLog = "【响应内容序列化失败】";
                }
            }

            log.info(logTemplate,
                    request.getRequestURL(),
                    (System.currentTimeMillis() - begin),
                    request.getMethod(),
                    ipAddr,
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    respParam,
                    responseLog);

        }
        return result;
    }

    /**
     * @description: 异常通知:在方法抛出异常退出时执行的通知。
     * @param: joinPoint
    ex
     * @return:
     * @author shaozhujie
     * 
     */
    @AfterThrowing(value = "webLog()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
        if (SystemConfig.isRequestLog()) {
            ServletRequestAttributes servletContainer = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletContainer.getRequest();
            String logTemplate = "请求响应异常->URL:{}, Method:{}, IP:{}, Params:{}, Class:{}.{}, ExInfo: {}";
            log.error(logTemplate, request.getRequestURL(), request.getMethod(), request.getRemoteAddr(), getParameterInfo(joinPoint), joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), ex.getMessage());
        }
    }


    private String getParameterInfo(JoinPoint joinPoint) {
        String parameter = "";
        try {
            Object[] args = joinPoint.getArgs();
            Object[] arguments = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof ServletRequest || args[i] instanceof ServletResponse || args[i] instanceof MultipartFile) {
                    continue;
                }
                arguments[i] = args[i];
            }

            if (arguments != null) {
                try {
                    parameter = JSONObject.toJSONString(arguments);
                } catch (Exception e) {
                    parameter = arguments.toString();
                }
            }
        } catch (Exception e) {
            parameter = "参数异常";
        }
        return parameter;
    }

}

