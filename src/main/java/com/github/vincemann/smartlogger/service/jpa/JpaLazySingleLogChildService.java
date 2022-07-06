package com.github.vincemann.smartlogger.service.jpa;

import com.github.vincemann.smartlogger.model.LazySingleLogChild;
import com.github.vincemann.smartlogger.repo.LazySingleLogChildRepository;
import com.github.vincemann.smartlogger.service.LazySingleLogChildService;
import com.github.vincemann.springrapid.core.service.JPACrudService;
import com.github.vincemann.springrapid.core.slicing.ServiceComponent;
import org.springframework.stereotype.Service;


@Service
@ServiceComponent
public class JpaLazySingleLogChildService extends JPACrudService<LazySingleLogChild,Long, LazySingleLogChildRepository> implements LazySingleLogChildService {

}