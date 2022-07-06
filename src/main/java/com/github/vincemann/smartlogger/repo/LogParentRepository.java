package com.github.vincemann.smartlogger.repo;

import com.github.vincemann.smartlogger.model.LogParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogParentRepository extends JpaRepository<LogParent,Long> {
}
