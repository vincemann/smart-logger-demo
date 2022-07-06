package com.github.vincemann.smartlogger.service.jpa;

import com.github.vincemann.smartlogger.model.LogChild2;
import com.github.vincemann.smartlogger.repo.LogChild2Repository;
import com.github.vincemann.smartlogger.service.LogChild2Service;
import com.github.vincemann.springrapid.core.service.JPACrudService;
import com.github.vincemann.springrapid.core.slicing.ServiceComponent;
import org.springframework.stereotype.Service;

@Service
@ServiceComponent
public class JpaLogChild2Service extends JPACrudService<LogChild2,Long, LogChild2Repository> implements LogChild2Service {
}
