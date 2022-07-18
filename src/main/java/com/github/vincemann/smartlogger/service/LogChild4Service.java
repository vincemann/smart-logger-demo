
package com.github.vincemann.smartlogger.service;
import com.github.vincemann.aoplog.api.annotation.LogInteraction;
import com.github.vincemann.smartlogger.model.LogChild4;
import com.github.vincemann.springrapid.core.service.CrudService;


@LogInteraction(disabled = true)
public interface LogChild4Service extends CrudService<LogChild4,Long> {
	
}
	