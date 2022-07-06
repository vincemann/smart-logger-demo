package com.github.vincemann.smartlogger.model;

import com.github.vincemann.smartlogger.IdAware;
import com.github.vincemann.springrapid.core.model.IdentifiableEntity;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass
@NoArgsConstructor
public class LogIdentifiableEntity implements IdentifiableEntity<Long>, IdAware<Long> {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @javax.persistence.Id
    private Long id;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id=id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogIdentifiableEntity other = (LogIdentifiableEntity) o;
        //added null check here, otherwise entities with null ids are considered equal
        //and cut down to one entity in a set for example
        return id != null &&
                id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
