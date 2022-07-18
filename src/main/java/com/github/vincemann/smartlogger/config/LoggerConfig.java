package com.github.vincemann.smartlogger.config;

import com.github.vincemann.smartlogger.SmartLogger;
import com.google.common.collect.Sets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfig {

    public static Boolean USE_SMART_LOGGER = Boolean.FALSE;


    // use short form and also ignore name
    @Bean
    public SmartLogger shortChild2Logger(){
        return SmartLogger.builder()
                .logShortForm(true)
                .excludedProperties(Sets.newHashSet("name"))
                .build();
    }

    // only side property is logged
    @Bean
    public SmartLogger sidePropertyOnlyChild2Logger(){
        return SmartLogger.builder()
                .excludedProperties(Sets.newHashSet("name","logEntity","secondMainProperty"))
                .build();
    }


}
