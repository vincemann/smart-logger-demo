package com.github.vincemann.smartlogger.repo;

import com.github.vincemann.smartlogger.model.LogChild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogChildRepository extends JpaRepository<LogChild,Long> {
}
