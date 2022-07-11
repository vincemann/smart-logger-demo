package com.github.vincemann.smartlogger.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.vincemann.springrapid.autobidir.model.parent.annotation.BiDirParentEntity;
import com.github.vincemann.smartlogger.SmartLogger;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static com.github.vincemann.smartlogger.config.DemoConfig.USE_LAZY_LOGGER;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "log_children")
public class LogChild extends LogIdentifiableEntity {

    private String name;
    @ManyToOne
    @JoinColumn(name = "log_entity_id")
    @JsonBackReference
    @BiDirParentEntity
    private LogEntity logEntity;


    public LogChild(String name) {
        this.name = name;
    }

    @Builder
    public LogChild(Long id, String name, LogEntity logEntity) {
        setId(id);
        this.logEntity = logEntity;
        this.name = name;
    }


    @Override
    public String toString() {
        if (USE_LAZY_LOGGER){
            SmartLogger logger = SmartLogger.builder()
                    .ignoreLazyException(Boolean.TRUE)
                    .ignoreEntities(Boolean.FALSE)
                    .onlyLogLoaded(Boolean.FALSE)
                    .build();

            return logger.toString(this);
        }else {
            return "LogChild{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
