package com.github.vincemann.smartlogger.repo;

import com.github.vincemann.smartlogger.model.EagerSingleLogChild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EagerSingleLogChildRepository extends JpaRepository<EagerSingleLogChild,Long> {
}
