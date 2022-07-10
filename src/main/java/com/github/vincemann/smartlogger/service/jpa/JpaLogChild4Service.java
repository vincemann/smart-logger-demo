
package com.github.vincemann.smartlogger.service.jpa;

import com.github.vincemann.springrapid.core.service.JPACrudService;
import com.github.vincemann.springrapid.core.slicing.ServiceComponent;
import com.github.vincemann.smartlogger.model.LogChild4;
import com.github.vincemann.smartlogger.repo.LogChild4Repository;
import com.github.vincemann.smartlogger.service.LogChild4Service;
import org.springframework.stereotype.Service;


@Service
@ServiceComponent
public class JpaLogChild4Service extends JPACrudService<LogChild4,Long, LogChild4Repository> implements LogChild4Service {
	
}
	