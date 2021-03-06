package com.github.vincemann.smartlogger.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import com.github.vincemann.smartlogger.ExtraShortToString;
import com.github.vincemann.smartlogger.ShortToString;
import com.github.vincemann.springrapid.autobidir.model.child.annotation.BiDirChildCollection;
import com.github.vincemann.springrapid.autobidir.model.child.annotation.BiDirChildEntity;
import com.github.vincemann.springrapid.autobidir.model.parent.annotation.BiDirParentEntity;
import com.github.vincemann.smartlogger.SmartLogger;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static com.github.vincemann.smartlogger.config.LoggerConfig.USE_SMART_LOGGER;


@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "log_entity")
public class LogEntity extends LogIdentifiableEntity {

    @ShortToString
    private String name;

    @Builder
    public LogEntity(String name, Set<LogChild> lazyChildren1, Set<LogChild2> lazyChildren2, Set<LogChild3> logChildren3, LogParent lazyParent) {
        this.name = name;
        if (lazyChildren1!=null)
            this.lazyChildren1 = lazyChildren1;
        if (lazyChildren2!=null)
            this.lazyChildren2 = lazyChildren2;
        if (logChildren3 != null)
            this.logChildren3 = logChildren3;
        this.lazyParent = lazyParent;
    }


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "logEntity",fetch = FetchType.LAZY)
    @BiDirChildCollection(LogChild.class)
    @JsonManagedReference
    private Set<LogChild> lazyChildren1 = new HashSet<>();

    @ShortToString
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
    @ExtraShortToString
    private Set<LogChild4> logChildren4 = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "logEntity",fetch = FetchType.EAGER)
    @BiDirChildCollection(LogChild3.class)
    @JsonManagedReference
    @ShortToString
    private Set<LogChild3> logChildren3 = new HashSet<>();

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
        if (USE_SMART_LOGGER){
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
