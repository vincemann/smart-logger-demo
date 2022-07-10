
package com.github.vincemann.smartlogger.model;

import com.github.vincemann.springrapid.autobidir.model.parent.annotation.BiDirParentCollection;
import com.github.vincemann.springrapid.core.model.IdentifiableEntityImpl;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "log_child4")
public class LogChild4 extends IdentifiableEntityImpl<Long> {

    private String name;

    @ManyToMany(mappedBy = "logChildren4", fetch = FetchType.LAZY)
    @BiDirParentCollection(LogEntity.class)
    private Set<LogEntity> logEntities = new HashSet<>();


    @Builder
    public LogChild4(String name, Set<LogEntity> logEntities) {
        this.name = name;
        this.logEntities = logEntities;
    }
}
	