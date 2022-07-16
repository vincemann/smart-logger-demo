package com.github.vincemann.smartlogger.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.vincemann.smartlogger.ShortToStringProperty;
import com.github.vincemann.springrapid.autobidir.model.parent.annotation.BiDirParentEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "log_children3")
public class LogChild3 extends LogIdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "log_entity_id")
    @JsonBackReference
    @BiDirParentEntity
    @ShortToStringProperty
    private LogEntity logEntity;

    private String s1deProperty;

    @ShortToStringProperty
    private String name;



    public LogChild3(String name, String s1deProperty) {
        this.name = name;
        this.s1deProperty = s1deProperty;
    }



    @Builder
    public LogChild3(Long id, String name, LogEntity logEntity, String s1deProperty) {
        setId(id);
        this.s1deProperty = s1deProperty;
        this.logEntity = logEntity;
        this.name = name;
    }

    @Override
    public String toString() {
        return "LogChild3{" +
                "name='" + name + '\'' +
                '}';
    }
}
