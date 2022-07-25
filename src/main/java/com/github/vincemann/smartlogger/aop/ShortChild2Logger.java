package com.github.vincemann.smartlogger.aop;

import com.github.vincemann.aoplog.api.CustomLogger;
import com.github.vincemann.smartlogger.SmartLogger;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;


public class ShortChild2Logger implements CustomLogger {


    @Override
    public String toString(Object o) {
        return SmartLogger.builder()
                .logShortForm(true)
                .build().toString(o);
    }

}
