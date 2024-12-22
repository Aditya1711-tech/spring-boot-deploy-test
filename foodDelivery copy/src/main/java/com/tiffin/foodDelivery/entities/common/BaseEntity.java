package com.tiffin.foodDelivery.entities.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public abstract class BaseEntity implements Comparable<BaseEntity>, Serializable {

    @Id
    private String id;

    @LastModifiedBy
    private String lastUpdatedBy;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime lastUpdatedDate;

    @CreatedDate
    private LocalDateTime createdDate;

    @Override
    public int compareTo(BaseEntity other) {
        if (this.id == null && other.getId() == null) {
            return 0; // Both are null, considered equal
        }
        if (this.id == null) {
            return -1; // Null is considered less than non-null
        }
        if (other.getId() == null) {
            return 1; // Non-null is considered greater than null
        }
        return this.id.compareTo(other.getId()); // Compare based on id's natural ordering
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + "]";
    }
}
