/*
 * Copyright (C)2023, emma Wu
 * All rights reserved.
 */
package com.myspringboot.aspect;

import com.google.gson.Gson;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restController() {}

    @Pointcut("@annotation(com.myspringboot.annotation.Log)")
    public void logPointcut() {
        // this method doesn't be called
    }

    @Before("logPointcut()")
    public void logMethodCallBefore() {
        System.out.println("Be called before");
    }

    @Before("restController()")
    public void logRequest(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpRequest = attributes.getRequest();
        HttpServletResponse httpResponse = attributes.getResponse();
        String args = getArgs(joinPoint);
        // TODO
        // Due to privacy concerns, payload info should not be printed. In this case, just for the convenience of
        // development.
        logger.info(String.format(
                "%s, %d, %s " + (args != "" ? ", args: " : "") + "%s",
                httpRequest.getMethod(),
                httpResponse.getStatus(),
                httpRequest.getRequestURL(),
                args));
    }

    private String getArgs(JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();
        StringBuilder builder = new StringBuilder();
        Object[] args = joinPoint.getArgs();
        int len = args.length;
        if (len == 0) return "";
        Gson gson = new Gson();
        for (int i = 0; i < len; i++) {
            String parameterName = signature.getParameterNames()[i];
            builder.append(parameterName);
            builder.append(":");
            builder.append(gson.toJson(args[i]).toString());
            builder.append(i == len - 1 ? "" : ",");
        }
        return builder.toString();
    }

    @After("logPointcut()")
    public void logMethodCallAfter() {
        System.out.println("Be called after");
    }
}
