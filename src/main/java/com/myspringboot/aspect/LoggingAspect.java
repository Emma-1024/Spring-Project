package com.myspringboot.aspect;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@Component
@Aspect
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);


    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restController() {}

    @Pointcut("@annotation(com.myspringboot.annotation.Log)")
    public void logPointcut(){
        // this method doesn't be called
    }

    @Before("logPointcut()")
    public void logMethodCallBefore(){
        System.out.println("Be called before");
    }

    @Before("restController()")
    public void logRequest(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpRequest = attributes.getRequest();
        HttpServletResponse httpResponse = attributes.getResponse();
        logger.info("Response Status Code: {}", httpResponse.getStatus());
        logger.info("Request URL: {}", httpRequest.getRequestURL());
        logger.info("Request method: {}", httpRequest.getMethod());
        logger.info("Request headers: {}", Collections.list(httpRequest.getHeaderNames()));
        logger.info("Request payload: {}", getPayload(joinPoint));
    }

    private String getPayload(JoinPoint joinPoint){
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<joinPoint.getArgs().length;i++){
            String parameterName = signature.getParameterNames()[i];
            builder.append(parameterName);
            builder.append(":");
            builder.append(joinPoint.getArgs()[i].toString());
            builder.append(",");
        }
        return builder.toString();
    }

    @After("logPointcut()")
    public void logMethodCallAfter(){
        System.out.println("Be called after");
    }

}
