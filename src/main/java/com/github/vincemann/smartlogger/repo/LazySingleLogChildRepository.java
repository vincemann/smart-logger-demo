package com.github.vincemann.smartlogger.repo;

import com.github.vincemann.smartlogger.model.LazySingleLogChild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LazySingleLogChildRepository extends JpaRepository<LazySingleLogChild,Long> {
}
