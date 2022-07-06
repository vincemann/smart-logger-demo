package com.github.vincemann.smartlogger.service.jpa;

import com.github.vincemann.smartlogger.model.LogChild;
import com.github.vincemann.smartlogger.repo.LogChildRepository;
import com.github.vincemann.smartlogger.service.LogChildService;
import com.github.vincemann.springrapid.core.service.JPACrudService;
import com.github.vincemann.springrapid.core.slicing.ServiceComponent;
import org.springframework.stereotype.Service;


@Service
@ServiceComponent
//@DisableAutoBiDir
public class JpaLogChildService extends JPACrudService<LogChild,Long, LogChildRepository> implements LogChildService {

}