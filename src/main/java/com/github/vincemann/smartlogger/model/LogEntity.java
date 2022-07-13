package com.github.vincemann.smartlogger.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.github.vincemann.smartlogger.MainStringProperty;
import com.github.vincemann.springrapid.autobidir.model.child.annotation.BiDirChildCollection;
import com.github.vincemann.springrapid.autobidir.model.child.annotation.BiDirChildEntity;
import com.github.vincemann.springrapid.autobidir.model.child.annotation.UniDirChildEntity;
import com.github.vincemann.springrapid.autobidir.model.parent.annotation.BiDirParentEntity;
import com.github.vincemann.smartlogger.SmartLogger;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static com.github.vincemann.smartlogger.config.DemoConfig.USE_LAZY_LOGGER;


@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "log_entity")
public class LogEntity extends LogIdentifiableEntity {

    @MainStringProperty
    private String name;

    @Builder
    public LogEntity(String name, Set<LogChild> lazyChildren1, Set<LogChild2> lazyChildren2, Set<LogChild3> eagerChildren, LogParent lazyParent) {
        this.name = name;
        if (lazyChildren1!=null)
            this.lazyChildren1 = lazyChildren1;
        if (lazyChildren2!=null)
            this.lazyChildren2 = lazyChildren2;
        if (eagerChildren != null)
            this.eagerChildren = eagerChildren;
        this.lazyParent = lazyParent;
    }


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "logEntity",fetch = FetchType.LAZY)
    @BiDirChildCollection(LogChild.class)
    @JsonManagedReference
    private Set<LogChild> lazyChildren1 = new HashSet<>();

    @MainStringProperty
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "logEntity",fetch = FetchType.LAZY)
    @BiDirChildCollection(LogChild2.class)
    @JsonManagedReference
    private Set<LogChild2> lazyChildren2 = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "LOG_ENTITY_SCHOOL",
            joinColumns = {@JoinColumn(name = "LOG_ENTITY_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "LOG_CHILD4_ID", referencedColumnName = "ID")})
//    @NotNull
    @BiDirChildCollection(LogChild4.class)
    private Set<LogChild4> logChildren4 = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "logEntity",fetch = FetchType.EAGER)
    @BiDirChildCollection(LogChild3.class)
    @JsonManagedReference
    private Set<LogChild3> eagerChildren = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @BiDirChildEntity
    @JoinColumn(name = "eager_child_id",referencedColumnName = "id")
    private EagerSingleLogChild eagerChild;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @BiDirChildEntity
    @JoinColumn(name = "lazy_child_id",referencedColumnName = "id")
    private LazySingleLogChild lazyChild;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "log_parent_id")
    @BiDirParentEntity
    private LogParent lazyParent;

    @Override
    public String toString() {
        if (USE_LAZY_LOGGER){
            SmartLogger logger =  SmartLogger.builder()
                    .logShortOnAlreadySeen(true)
                    .build();

            return logger.toString(this);
        }else {
            return "LogEntity{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }


}
