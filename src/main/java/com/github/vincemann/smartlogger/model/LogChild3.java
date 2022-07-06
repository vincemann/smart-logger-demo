package com.github.vincemann.smartlogger.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private LogEntity logEntity;
    private String name;



    public LogChild3(String name) {
        this.name = name;
    }

    @Builder
    public LogChild3(Long id, String name, LogEntity logEntity) {
        setId(id);
        this.logEntity = logEntity;
        this.name = name;
    }

    @Override
    public String toString() {
        return "LogChild{" +
                "name='" + name + '\'' +
                '}';
    }
}
