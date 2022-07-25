package com.github.vincemann.smartlogger.service.jpa;

import com.github.vincemann.aoplog.Severity;
import com.github.vincemann.aoplog.api.annotation.ConfigureCustomLoggers;
import com.github.vincemann.aoplog.api.annotation.LogInteraction;
import com.github.vincemann.aoplog.api.annotation.CustomLogger;
import com.github.vincemann.smartlogger.model.LogChild2;
import com.github.vincemann.smartlogger.repo.LogChild2Repository;
import com.github.vincemann.smartlogger.service.LogChild2Service;
import com.github.vincemann.springrapid.core.service.JPACrudService;
import com.github.vincemann.springrapid.core.slicing.ServiceComponent;
import org.springframework.stereotype.Service;

@Service
@ServiceComponent
public class JpaLogChild2Service extends JPACrudService<LogChild2, Long, LogChild2Repository> implements LogChild2Service {

    public static String NEW_SIDE_PROPERTY = "newSidePr0p";

    @ConfigureCustomLoggers(loggers =
            {
                    @CustomLogger(key = "arg2", beanname = "shortChild2Logger"),
                    @CustomLogger(key = "ret", beanname = "sidePropertyOnlyChild2Logger")
            }
    )
    @LogInteraction(value = Severity.INFO)
    @Override
    public LogChild2 testAop(String s1, LogChild2 logChild2) {
        logChild2.setSideProperty(NEW_SIDE_PROPERTY);
        return logChild2;
    }
}
