package com.tiffin.foodDelivery.entities.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public abstract class NameWithDeletedEntity extends BaseEntity {

    private String name;

    private boolean isDeleted = false;

    public NameWithDeletedEntity(String name) {
        this();
        this.name = name;
    }

    public NameWithDeletedEntity(String name, String id) {
        this(name);
        this.setId(id);
    }
}
