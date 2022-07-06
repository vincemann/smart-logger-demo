package com.github.vincemann.smartlogger.service;

import com.github.vincemann.smartlogger.model.LogEntity;
import com.github.vincemann.springrapid.core.service.CrudService;
import com.github.vincemann.springrapid.core.service.exception.BadEntityException;

import java.util.Optional;

public interface LogEntityService extends CrudService<LogEntity,Long> {

    public Optional<LogEntity> findByIdAndLoadCol1(Long id) throws BadEntityException;

    public Optional<LogEntity> findByIdAndLoadCol1AndCol2(Long id) throws BadEntityException;

}

