package com.tiffin.foodDelivery.dtos.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseDTO implements Comparable<BaseDTO>, Serializable {

    private String id;

    @LastModifiedBy
    private String lastUpdatedBy;
    @LastModifiedDate
    private String lastUpdatedDate;

    @CreatedBy
    private String createdBy;
    @CreatedDate
    private String createdDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseDTO that = (BaseDTO) o;
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

    @Override
    public int compareTo(BaseDTO o) {
        if (this.id == null && o.id == null) {
            return 0; // Both are null, considered equal
        }
        if (this.id == null) {
            return -1; // Null is considered less than non-null
        }
        if (o.id == null) {
            return 1; // Non-null is considered greater than null
        }
        return this.id.compareTo(o.id); // Compare based on id's natural ordering
    }

}
