package com.github.vincemann.smartlogger.service;

import com.github.vincemann.smartlogger.model.LogChild2;
import com.github.vincemann.springrapid.core.service.CrudService;

public interface LogChild2Service extends CrudService<LogChild2,Long> {

    public LogChild2 testAop(String s1, LogChild2 logChild2);

}
