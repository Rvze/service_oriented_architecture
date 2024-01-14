package com.example.spring_service.configuration;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class GlobalAspect {

    @Pointcut("execution(public * com.example.spring_service.configuration.GlobalExceptionHandler.*(..)) && args(e)")
    public void callAtExceptionHandle(Throwable e) {
    }


    @Before(value = "callAtExceptionHandle(e)", argNames = "e")
    public void beforeExceptionHandlerHandleException(Throwable e) {
        e.printStackTrace();
    }
}
