package com.github.vincemann.smartlogger.service.jpa;

import com.github.vincemann.smartlogger.model.EagerSingleLogChild;
import com.github.vincemann.smartlogger.repo.EagerSingleLogChildRepository;
import com.github.vincemann.smartlogger.service.EagerSingleLogChildService;
import com.github.vincemann.springrapid.core.service.JPACrudService;
import com.github.vincemann.springrapid.core.slicing.ServiceComponent;
import org.springframework.stereotype.Service;


@Service
@ServiceComponent
public class JpaEagerSingleLogChildService extends JPACrudService<EagerSingleLogChild,Long, EagerSingleLogChildRepository> implements EagerSingleLogChildService {

}
