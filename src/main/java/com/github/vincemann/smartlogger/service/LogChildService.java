package com.github.vincemann.smartlogger.service;

import com.github.vincemann.aoplog.api.LogInteraction;
import com.github.vincemann.smartlogger.model.LogChild;
import com.github.vincemann.springrapid.core.service.CrudService;

@LogInteraction(disabled = true)
public interface LogChildService extends CrudService<LogChild,Long> {


}
