package com.github.vincemann.smartlogger.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.vincemann.smartlogger.ShortToStringProperty;
import com.github.vincemann.smartlogger.SmartLogger;
import com.github.vincemann.smartlogger.CallToString;
import com.github.vincemann.springrapid.autobidir.model.parent.annotation.BiDirParentEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static com.github.vincemann.smartlogger.config.LoggerConfig.USE_SMART_LOGGER;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "log_children2")
@CallToString
public class LogChild2 extends LogIdentifiableEntity {

    public static SmartLogger LOGGER;

    @ShortToStringProperty
    @ManyToOne
    @JoinColumn(name = "log_entity_id")
    @JsonBackReference
    @BiDirParentEntity
    private LogEntity logEntity;

    @ShortToStringProperty
    private String name;

    private String sideProperty;

    @ShortToStringProperty
    private String secondMainProperty;



    public LogChild2(String name) {
        this.name = name;
    }

    @Builder
    public LogChild2(Long id, String name, LogEntity logEntity,String secondMainProperty, String sideProperty) {
        setId(id);
        this.sideProperty = sideProperty;
        this.secondMainProperty = secondMainProperty;
        this.logEntity = logEntity;
        this.name = name;
    }

    @Override
    public String toString() {
        if (USE_SMART_LOGGER){
            return LOGGER.toString(this);
        }else {
            return "LogChild2{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
