package com.github.vincemann.smartlogger.repo;

import com.github.vincemann.smartlogger.model.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogEntityRepository extends JpaRepository<LogEntity,Long> {
}
