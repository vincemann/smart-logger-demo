package com.github.vincemann.smartlogger.aop;

import com.github.vincemann.aoplog.api.CustomLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomLoggerAopTestConfig {


    @Bean
    public SidePropertyOnlyChild2Logger sidePropertyOnlyChild2Logger(){
        return new SidePropertyOnlyChild2Logger();
    }

    @Bean
    public ShortChild2Logger shortChild2Logger(){
        return new ShortChild2Logger();
    }
}
