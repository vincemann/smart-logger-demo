package com.github.vincemann.smartlogger.config;

import com.github.vincemann.smartlogger.SmartLogger;
import com.github.vincemann.smartlogger.aop.ShortChild2Logger;
import com.github.vincemann.smartlogger.aop.SidePropertyOnlyChild2Logger;
import com.google.common.collect.Sets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfig {

    public static Boolean USE_SMART_LOGGER = Boolean.FALSE;

    @Bean
    public SidePropertyOnlyChild2Logger sidePropertyOnlyChild2Logger(){
        return new SidePropertyOnlyChild2Logger();
    }

    @Bean
    public ShortChild2Logger shortChild2Logger(){
        return new ShortChild2Logger();
    }


}
