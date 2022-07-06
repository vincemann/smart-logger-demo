package com.github.vincemann.smartlogger.service.jpa;

import com.github.vincemann.smartlogger.model.LogParent;
import com.github.vincemann.smartlogger.repo.LogParentRepository;
import com.github.vincemann.smartlogger.service.LogParentService;
import com.github.vincemann.springrapid.core.service.JPACrudService;
import com.github.vincemann.springrapid.core.slicing.ServiceComponent;
import org.springframework.stereotype.Service;

@Service
@ServiceComponent
public class JpaLogParentService extends JPACrudService<LogParent, Long, LogParentRepository> implements LogParentService {

}
