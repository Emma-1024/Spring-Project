package com.myspringboot.aspect;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

    @Pointcut("@annotation(com.myspringboot.annotation.Log)")
    public void logPointcut(){
        // this method doesn't be called
    }

    @Before("logPointcut()")
    public void logMethodCallBefore(){
        System.out.println("Be called before");
    }

    @After("logPointcut()")
    public void logMethodCallAfter(){
        System.out.println("Be called after");
    }


}
