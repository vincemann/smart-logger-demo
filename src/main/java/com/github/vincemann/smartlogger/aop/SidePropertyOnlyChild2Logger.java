package com.github.vincemann.smartlogger.aop;

import com.github.vincemann.aoplog.api.CustomLogger;
import com.github.vincemann.smartlogger.SmartLogger;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

// only logs sideProperty of child2
public class SidePropertyOnlyChild2Logger implements CustomLogger
{

    @Override
    public String toString(Object o) {
        return SmartLogger.builder()
                .excludedProperties(Sets.newHashSet("secondMainProperty", "name", "logEntity"))
                .build().toString(o);
    }
}
