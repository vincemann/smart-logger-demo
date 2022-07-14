package com.github.vincemann.smartlogger.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.vincemann.springrapid.autobidir.model.child.annotation.UniDirChildCollection;
import com.github.vincemann.springrapid.autobidir.model.parent.annotation.BiDirParentEntity;
import com.github.vincemann.smartlogger.SmartLogger;
import lombok.*;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static com.github.vincemann.smartlogger.config.DemoConfig.USE_SMART_LOGGER;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "log_child2_id")
    @UniDirChildCollection(LogChild2.class)
    private Set<LogChild2> logChild2Members = new HashSet<>();


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
        if (USE_SMART_LOGGER){
            SmartLogger logger = SmartLogger.builder()
                    .logShortForm(true)
                    .build();

            return logger.toString(this);
        }else {
            return "LogChild{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
