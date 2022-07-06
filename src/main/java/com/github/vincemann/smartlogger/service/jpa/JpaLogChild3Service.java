package com.github.vincemann.smartlogger.service.jpa;

import com.github.vincemann.smartlogger.model.LogChild3;
import com.github.vincemann.smartlogger.repo.LogChild3Repository;
import com.github.vincemann.smartlogger.service.LogChild3Service;
import com.github.vincemann.springrapid.core.service.JPACrudService;
import com.github.vincemann.springrapid.core.slicing.ServiceComponent;
import org.springframework.stereotype.Service;

@Service
@ServiceComponent
public class JpaLogChild3Service extends JPACrudService<LogChild3,Long, LogChild3Repository>implements LogChild3Service {


}
